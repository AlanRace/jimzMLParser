package com.alanmrace.jimzmlparser.imzml;

import com.alanmrace.jimzmlparser.exceptions.ImzMLParseException;
import com.alanmrace.jimzmlparser.exceptions.ImzMLWriteException;
import com.alanmrace.jimzmlparser.mzml.BinaryDataArray;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import com.alanmrace.jimzmlparser.mzml.CVParam;
import com.alanmrace.jimzmlparser.mzml.FileContent;
import com.alanmrace.jimzmlparser.mzml.MzML;
import com.alanmrace.jimzmlparser.mzml.Scan;
import com.alanmrace.jimzmlparser.mzml.ScanSettings;
import com.alanmrace.jimzmlparser.mzml.ScanSettingsList;
import com.alanmrace.jimzmlparser.mzml.Software;
import com.alanmrace.jimzmlparser.mzml.Spectrum;
import com.alanmrace.jimzmlparser.mzml.SpectrumList;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alan Race
 */
public class ImzML extends MzML implements MassSpectrometryImagingData {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(ImzML.class.getName());

    private int width;
    private int height;
    private int depth;

    private File ibdFile;

    private double[] fullmzList;
    private double[][] ticImage;

    private Spectrum[][][] spectrumGrid;

    private static double minMZ = Double.MAX_VALUE;
    private static double maxMZ = Double.MIN_VALUE;

    private int dimensionality = -1;

    // REMOVED - zero filling code because it caused more issues. Alternative 
    // (and faster) work around is to go to mzML and then to imzML.
    // Default to no zero indexing, however if we find a 0, then turn it on
//	private boolean zeroIndexing = false;
    public ImzML(String version) {
        super(version);
    }

    public ImzML(ImzML imzML) {
        super(imzML);
    }

    public ImzML(MzML mzML) {
        super(mzML);
    }
    
    @Override
    public synchronized double[] getFullmzList() {
        logger.entering(ImzML.class.getName(), "getFullmzList");

        if (fullmzList == null) {
            Software imzMLConverter = this.getSoftwareList().getSoftware("imzMLConverter");

            logger.log(Level.FINE, "Software found: {0}", imzMLConverter);

            if (imzMLConverter != null) {
                CVParam offsetParam = imzMLConverter.getCVParam(BinaryDataArray.externalOffsetID);
                CVParam encodedLengthParam = imzMLConverter.getCVParam(BinaryDataArray.externalEncodedLengthID);

                logger.log(Level.FINE, "Found CVParams: {0}, {1}", new Object[]{offsetParam, encodedLengthParam});

                if (offsetParam != null) {
                    try {
                        byte[] fullmzListBytes = dataStorage.getData(offsetParam.getValueAsLong(), encodedLengthParam.getValueAsInteger());

                        logger.log(Level.FINE, "Read in {0} bytes", fullmzListBytes.length);

                        int numBytesPerDouble = Double.SIZE / Byte.SIZE;
                        // The below is only available in Java 1.8
                        //Double.BYTES
                        
                        fullmzList = new double[fullmzListBytes.length / numBytesPerDouble];

                        ByteBuffer buffer = ByteBuffer.wrap(fullmzListBytes);
                        DoubleBuffer doubleBuffer = buffer.asDoubleBuffer();

                        if(fullmzList.length > 0) {
                            logger.log(Level.FINE, "First double is {0}", doubleBuffer.get(0));

                            for (int i = 0; i < fullmzList.length; i++) {
                                fullmzList[i] = doubleBuffer.get(i);
                            }

                            logger.log(Level.FINE, "First double in array is {0}", fullmzList[0]);
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(ImzML.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

        return fullmzList;
    }

    @Override
    public int getSpatialDimensionality() {
        int spatialDimensionality = 0; 
        spatialDimensionality += (getWidth() > 1) ? 1 : 0;
        spatialDimensionality += (getHeight() > 1) ? 1 : 0;
        spatialDimensionality += (getDepth() > 1) ? 1 : 0;

        return spatialDimensionality;
    }

    @Override
    public int getDimensionality() {
        // Determine the dimensionality of the data
        if (dimensionality <= 0) {
            dimensionality = 1; // At least 1 dimension from m/z 
            dimensionality += getSpatialDimensionality();

            // Check if we have MS/MS or mobility by looking to see if we have two or more spectra with same x, y location
            Spectrum spectrum1 = getRun().getSpectrumList().getSpectrum(0);
            Spectrum spectrum2 = getRun().getSpectrumList().getSpectrum(1);

            if (spectrum1.getPixelLocation().equals(spectrum2.getPixelLocation())) {
                dimensionality++;
            }
        }

        return dimensionality;
    }

    @Override
    public int getNumberOfSpectraPerPixel() {
        Spectrum firstSpectrum = getRun().getSpectrumList().getSpectrum(0);
        PixelLocation location = firstSpectrum.getPixelLocation();
        
        int numberOfSpectraPerPixel = 1;
        
        int numSpectra = getRun().getSpectrumList().size();
        SpectrumList spectrumList = getRun().getSpectrumList();
        
        for(int i = 1; i < numSpectra; i++) {
            if(spectrumList.getSpectrum(i).getPixelLocation().equals(location))
                numberOfSpectraPerPixel++;
        }
        
        return numberOfSpectraPerPixel;
    }
    
    @Override
    public synchronized Spectrum getSpectrum(int x, int y) {
        return getSpectrum(x, y, 1);
    }

    @Override
    public synchronized Spectrum getSpectrum(int x, int y, int z) {
        if (spectrumGrid == null) {
            spectrumGrid = new Spectrum[getWidth()][getHeight()][getDepth()];

            for (Spectrum spectrum : getRun().getSpectrumList()) {
                for (Scan curScan : spectrum.getScanList()) {
                    int curX = curScan.getCVParam(Scan.positionXID).getValueAsInteger();
                    int curY = curScan.getCVParam(Scan.positionYID).getValueAsInteger();
                    int curZ = 1;

                    CVParam zPosCVParam = curScan.getCVParam(Scan.positionZID);

                    if (zPosCVParam != null) {
                        curZ = zPosCVParam.getValueAsInteger();
                    }

                    if (curX - 1 < 0 || curX - 1 > spectrumGrid.length || curY - 1 < 0 || curY - 1 > spectrumGrid[0].length || curZ - 1 < 0 || curZ - 1 > spectrumGrid[0][0].length) {
                        return null;
                    }

                    spectrumGrid[curX - 1][curY - 1][curZ - 1] = spectrum;
                }
            }
        }

        if (spectrumGrid.length >= 1 && (x - 1) < spectrumGrid.length && x >= 1 &&
                (y - 1) < spectrumGrid[0].length && y >= 1 &&
                (z - 1) < spectrumGrid[0][0].length && z >= 1) {
            return spectrumGrid[x - 1][y - 1][z - 1];
        }
        
        return null;
    }

    @Override
    public int getWidth() {
        if (width != 0) {
            return width;
        }

        // Check scan settings first
        ScanSettingsList scanSettingsList = getScanSettingsList();

        if (getScanSettingsList() != null) {
            for (ScanSettings scanSettings : scanSettingsList) {
                CVParam maxCountPixelX = scanSettings.getCVParam(ScanSettings.maxCountPixelXID);

                if (maxCountPixelX != null) {
                    width = maxCountPixelX.getValueAsInteger();
                }
            }
        }

        // TODO: Nothing in scan settings, so should look at all spectra
        return width;
    }

    @Override
    public int getHeight() {
        if (height != 0) {
            return height;
        }

        // Check scan settings first
        ScanSettingsList scanSettingsList = getScanSettingsList();

        if (getScanSettingsList() != null) {
            for (ScanSettings scanSettings : scanSettingsList) {
                CVParam maxCountPixelY = scanSettings.getCVParam(ScanSettings.maxCountPixelYID);

                if (maxCountPixelY != null) {
                    height = maxCountPixelY.getValueAsInteger();
                }
            }
        }

        // TODO: Nothing in scan settings, so should look at all spectra
        return height;
    }

    @Override
    public int getDepth() {
        if (depth != 0) {
            return depth;
        }

        depth = 1;

        SpectrumList spectrumList = getRun().getSpectrumList();

        if (spectrumList != null) {
            for (Spectrum spectrum : spectrumList) {
                //for(ScanSettings scanSettings : spectrum.getScanList().getScan(0)) {
                CVParam maxCountPixelZ = spectrum.getScanList().getScan(0).getCVParam(Scan.positionZID);

                if (maxCountPixelZ != null) {
                    int curDepth = maxCountPixelZ.getValueAsInteger();

                    if (curDepth > depth) {
                        depth = curDepth;
                    }
                }
                //}
            }
        }

        // TODO: Nothing in scan settings, so should look at all spectra
        return depth;
    }

    @Override
    public double getMinimumDetectedmz() {
        if (minMZ != Double.MAX_VALUE) {
            return minMZ;
        }

        for (Spectrum spectrum : getRun().getSpectrumList()) {
            CVParam minDetectedMZ = spectrum.getCVParam(Spectrum.lowestObservedmzID);

            if (minDetectedMZ == null) {
                break;
            }

            double spectrumMinMZ = minDetectedMZ.getValueAsDouble();

            if (minMZ > spectrumMinMZ) {
                minMZ = spectrumMinMZ;
            }
        }

        if (minMZ == Double.MAX_VALUE) {
            return Double.NaN;
        }

        return minMZ;
    }

    @Override
    public double getMaximumDetectedmz() {
        if (maxMZ != Double.MIN_VALUE) {
            return maxMZ;
        }

        for (Spectrum spectrum : getRun().getSpectrumList()) {
            CVParam maxDetectedMZ = spectrum.getCVParam(Spectrum.highestObservedmzID);

            if (maxDetectedMZ == null) {
                break;
            }

            double spectrumMaxMZ = maxDetectedMZ.getValueAsDouble();

            if (maxMZ < spectrumMaxMZ) {
                maxMZ = spectrumMaxMZ;
            }

        }

        if (maxMZ == Double.MIN_VALUE) {
            return Double.NaN;
        }

        return maxMZ;
    }

    public File getIBDFile() {
        return ibdFile;
    }

//	public void generateSpectralData() throws CVParamAccessionNotFoundException {
//		try {
//			RandomAccessFile raf = new RandomAccessFile(ibdFile, "r");
//			
//			double[] mzArray = null;
//			
//			for(Spectrum spectrum : getRun().getSpectrumList()) {
//				// If the data is processed then we need to get the new m/z list
//				if(isProcessed() || mzArray == null)
//					mzArray = spectrum.getmzArray(raf);
//				
//				if(mzArray == null)
//					continue;
//				
//				double[] intensityArray = spectrum.getIntensityArray(raf);
//				
//				// Calculate the total ion current 
//				double totalIonCurrent = 0;
//				double basePeakMZ = 0;
//				double basePeakIntensity = 0;
//				
//				for(int i = 0; i < intensityArray.length; i++) {
//					totalIonCurrent += intensityArray[i];
//					
//					if(intensityArray[i] > basePeakIntensity) {
//						basePeakIntensity = intensityArray[i];
//						basePeakMZ = mzArray[i];
//					}
//				}
//				
//				spectrum.removeCVParam(Spectrum.totalIonCurrentID);
//				spectrum.addCVParam(new DoubleCVParam(getOBO().getTerm(Spectrum.totalIonCurrentID), totalIonCurrent));
//				
//				spectrum.removeCVParam(Spectrum.basePeakMZID);
//				spectrum.addCVParam(new DoubleCVParam(getOBO().getTerm(Spectrum.basePeakMZID), basePeakMZ));
//				
//				spectrum.removeCVParam(Spectrum.basePeakIntensityID);
//				spectrum.addCVParam(new DoubleCVParam(getOBO().getTerm(Spectrum.basePeakIntensityID), basePeakIntensity));
//				
//				double minMZ = Double.MAX_VALUE;
//				double maxMZ = Double.MIN_VALUE;
//				
//				for(int i = 0; i < mzArray.length; i++) {
//					if(mzArray[i] > maxMZ)
//						maxMZ = mzArray[i];
//					if(mzArray[i] < minMZ)
//						minMZ = mzArray[i];
//				}
//				
//				spectrum.removeCVParam(Spectrum.lowestObservedmzID);
//				spectrum.addCVParam(new DoubleCVParam(getOBO().getTerm(Spectrum.lowestObservedmzID), minMZ));
//				
//				spectrum.removeCVParam(Spectrum.highestObservedmzID);
//				spectrum.addCVParam(new DoubleCVParam(getOBO().getTerm(Spectrum.highestObservedmzID), maxMZ));
//			}
//				
//			raf.close();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
    @Override
    public boolean isProcessed() {
        return getFileDescription().getFileContent().getCVParam(FileContent.binaryTypeProcessedID) != null;
    }

    @Override
    public boolean isContinuous() {
        return getFileDescription().getFileContent().getCVParam(FileContent.binaryTypeContinuousID) != null;
    }

    public static double[] getBinnedmzList(double minMZ, double maxMZ, double binSize) {
        // Round the min m/z down to the next lowest bin, and the max m/z up to the next bin
        double minMZRounded = minMZ - (minMZ % binSize);
        double maxMZRounded = maxMZ + (binSize - (maxMZ % binSize));

        int numBins = (int) Math.ceil((maxMZRounded - minMZRounded) / binSize);
        double[] mzs = new double[numBins];

        for (int i = 0; i < numBins; i++) {
            mzs[i] = minMZRounded + (i * binSize);
        }

        return mzs;
    }

//	public double[][][] generateDatacube(double minMZ, double maxMZ, double binSize) {
//		double[] mzs = getBinnedmzList(minMZ, maxMZ, binSize);
//		double[][][] datacube = new double[getHeight()][getWidth()][mzs.length];
//		
//		try {
//			RandomAccessFile raf = new RandomAccessFile(ibdFile, "r");
//			
//			double[] mzArray = null;
//			
//			for(Spectrum spectrum : getRun().getSpectrumList()) {
//				// If the data is processed then we need to get the new m/z list
//				if(isProcessed() || mzArray == null)
//					mzArray = spectrum.getmzArray(raf);
//				
//				if(mzArray == null)
//					continue;
//				
//				double[] intensityArray = spectrum.getIntensityArray(raf);
//				
//				int x = spectrum.getScanList().getScan(0).getCVParam(Scan.positionXID).getValueAsInteger() - 1;
//				int y = spectrum.getScanList().getScan(0).getCVParam(Scan.positionYID).getValueAsInteger() - 1;				
//				
//				for(int i = 0; i < mzArray.length; i++) {
//					int index = (int)Math.floor((mzArray[i] - mzs[0]) / binSize);
//					
//					if(index >= 0 && index < mzs.length)
//						datacube[y][x][index] += intensityArray[i];
//				}
//			}
//			
//			raf.close();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		return datacube;
//	}
//	
    @Override
    public double[][] generateTICImage() {
        if(ticImage == null) {
            ticImage = new double[getHeight()][getWidth()];

//		RandomAccessFile raf = null;
            for (Spectrum spectrum : getRun().getSpectrumList()) {
                int x = spectrum.getScanList().getScan(0).getCVParam(Scan.positionXID).getValueAsInteger() - 1;
                int y = spectrum.getScanList().getScan(0).getCVParam(Scan.positionYID).getValueAsInteger() - 1;

                try {
                    double tic = spectrum.getCVParam(Spectrum.totalIonCurrentID).getValueAsDouble();
    //				System.out.println(spectrum.getID());
    //				System.out.println(image.length + " - " + y);
    //				System.out.println("x -> " + image[y].length + " - " + x);
                    ticImage[y][x] = tic;
                } catch (NullPointerException ex) {
                    try {
    //					if(raf == null)
    //						raf = new RandomAccessFile(ibdFile, "r");

                        double[] intensityArray = spectrum.getIntensityArray();

                        try {
                            for (int i = 0; i < intensityArray.length; i++) {
                                ticImage[y][x] += intensityArray[i];
                            }
                        } catch (NullPointerException exception) {
                            // Do nothing - no data
                        }
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        Logger.getLogger(ImzML.class.getName()).log(Level.SEVERE, null, e);
                    } catch (IOException ex1) {
                        Logger.getLogger(ImzML.class.getName()).log(Level.SEVERE, null, ex1);
                    }
                }
            }

//		if(raf != null) {
//			try {
//				raf.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
        }
        
        return ticImage;
    }
    
//	
//	public double[][] generateBasePeakMZImage() {
//		double[][] image = new double[getHeight()][getWidth()];
//
//		RandomAccessFile raf = null;
//		
//		for(Spectrum spectrum : getRun().getSpectrumList()) {
//			int x = spectrum.getScanList().getScan(0).getCVParam(Scan.positionXID).getValueAsInteger() - 1;
//			int y = spectrum.getScanList().getScan(0).getCVParam(Scan.positionYID).getValueAsInteger() - 1;				
//			
//			try {
//				double tic = spectrum.getCVParam(Spectrum.basePeakMZID).getValueAsDouble();
//				
//				image[y][x] = tic;
//			} catch(NullPointerException ex) {
//				try {
//					if(raf == null)
//						raf = new RandomAccessFile(ibdFile, "r");
//					
//					double[] mzArray = spectrum.getmzArray(raf);
//					double[] intensityArray = spectrum.getIntensityArray(raf);
//					
//					double maxIntensity = 0;					
//					
//					try {
//						for(int i = 0; i < intensityArray.length; i++) {
//							if(intensityArray[i] > maxIntensity) {
//								image[y][x] = mzArray[i];
//								
//								maxIntensity = intensityArray[i];
//							}
//						}
//					} catch(NullPointerException exception) {
//						// Do nothing - no data
//					}
//				}catch (FileNotFoundException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}
//		
//		if(raf != null) {
//			try {
//				raf.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		
//		return image;
//	}
//	
//	public double[][] generateBasePeakIntensityImage() {
//		double[][] image = new double[getHeight()][getWidth()];
//
//		RandomAccessFile raf = null;
//		
//		for(Spectrum spectrum : getRun().getSpectrumList()) {
//			int x = spectrum.getScanList().getScan(0).getCVParam(Scan.positionXID).getValueAsInteger() - 1;
//			int y = spectrum.getScanList().getScan(0).getCVParam(Scan.positionYID).getValueAsInteger() - 1;				
//			
//			try {
//				double tic = spectrum.getCVParam(Spectrum.basePeakIntensityID).getValueAsDouble();
//				
//				image[y][x] = tic;
//			} catch(NullPointerException ex) {
//				try {
//					if(raf == null)
//						raf = new RandomAccessFile(ibdFile, "r");
//					
//					double[] intensityArray = spectrum.getIntensityArray(raf);				
//					
//					double maxIntensity = 0;
//					
//					try {
//						for(int i = 0; i < intensityArray.length; i++) {
//							if(intensityArray[i] > maxIntensity) {
//								image[y][x] = intensityArray[i];
//								
//								maxIntensity = intensityArray[i];
//							}
//						}
//					} catch(NullPointerException exception) {
//						// Do nothing - no data
//					}
//				}catch (FileNotFoundException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}
//		
//		if(raf != null) {
//			try {
//				raf.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		
//		return image;
//	}
//	
//	public double[][] generatemzImage(double mz, double width) {
//		double[][] image = new double[getHeight()][getWidth()];
//		
//		try {
//			RandomAccessFile raf = new RandomAccessFile(ibdFile, "r");
//			
//			double lower = mz - width;
//			double upper = mz + width;
//			
//			double[] mzArray = null;
//			
//			for(Spectrum spectrum : getRun().getSpectrumList()) {
//				// If the data is processed then we need to get the new m/z list
//				if(isProcessed() || mzArray == null)
//					mzArray = spectrum.getmzArray(raf);
//				
//				if(mzArray == null)
//					continue;
//				
//				ArrayList<Integer> indicies = new ArrayList<Integer>(); 
//				
//				int x = spectrum.getScanList().getScan(0).getCVParam(Scan.positionXID).getValueAsInteger() - 1;
//				int y = spectrum.getScanList().getScan(0).getCVParam(Scan.positionYID).getValueAsInteger() - 1;				
//				
//				for(int i = 0; i < mzArray.length; i++) {
//				//	System.out.println(mzArray[i] + " >= " + lower + " && " + mzArray[i] + " <= " + upper);
//					if(mzArray[i] >= lower && mzArray[i] <= upper)
//						indicies.add(i);
//				}
//				//System.out.println("x = " + x + ", y = " + y + ", " + mzArray.length + ", " + indicies.size());
//				if(indicies.size() > 0) {
//					double[] intensityArray = spectrum.getIntensityArray(raf);
//					
//					for(int i = 0; i < indicies.size(); i++)
//						image[y][x] += intensityArray[indicies.get(i)];
//				}					
//			}
//			
//			raf.close();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		return image;
//	}

    public void setibdFile(File ibdFile) {
        this.ibdFile = ibdFile;
    }

    public void write() throws ImzMLWriteException {
        if (ibdFile == null) {
            throw new ImzMLWriteException("No ibd file, can't write imzML file.");
        }

        write(ibdFile.getAbsolutePath().substring(0, ibdFile.getAbsolutePath().length() - ".ibd".length()) + ".imzML");
    }

    @Override
    public void write(String filename) throws ImzMLWriteException {
        String encoding = "ISO-8859-1";

        try {
            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(filename), encoding);
            BufferedWriter output = new BufferedWriter(out);

            output.write("<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>\n");
            outputXML(output, 0);

            output.flush();

            output.close();
            out.close();
        } catch (IOException e1) {
            throw new ImzMLWriteException("Error writing imzML file " + filename + ". " + e1.getLocalizedMessage());
        }
    }
    
    public static String calculateChecksum(String filename, String algorithm) throws ImzMLParseException {
        // Open the .ibd data stream
        DataInputStream dataStream = null;
        byte[] hash;

        try {
            dataStream = new DataInputStream(new FileInputStream(filename));
        } catch (FileNotFoundException e2) {
            throw new ImzMLParseException("Could not open file " + filename, e2);
        }

        try {
            byte[] buffer = new byte[1024 * 1024];
            int bytesRead;

            MessageDigest md = MessageDigest.getInstance(algorithm); //"SHA-1");

            do {
                bytesRead = dataStream.read(buffer);

                if (bytesRead > 0) {
                    md.update(buffer, 0, bytesRead);
                }
            } while (bytesRead > 0);

            dataStream.close();

            hash = md.digest();
        } catch (NoSuchAlgorithmException e) {
            try {
                dataStream.close();
            } catch (IOException e1) {
                throw new ImzMLParseException("Failed to close ibd file after trying to generate " + algorithm + " hash", e1);
            }

            throw new ImzMLParseException("Generation of " + algorithm + " hash failed. No " + algorithm + " algorithm. " + e.getLocalizedMessage(), e);
        } catch (IOException e) {
            throw new ImzMLParseException("Failed generating " + algorithm + " hash. Failed to read data from " + filename + e.getMessage(), e);
        }

        try {
            dataStream.close();
        } catch (IOException e) {
            throw new ImzMLParseException("Failed to close ibd file after generating " + algorithm + " hash", e);
        }

        return byteArrayToHexString(hash);
    }
    
    public static String calculateSHA1(String filename) throws ImzMLParseException {
        return calculateChecksum(filename, "SHA-1");
    }

    public static String calculateMD5(String filename) throws ImzMLParseException {
        return calculateChecksum(filename, "MD5");
    }
    
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];

        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }

        return data;
    }

    public static String byteArrayToHexString(byte[] byteArray) {
        StringBuilder sb = new StringBuilder(2 * byteArray.length);

        byte[] Hexhars = {
            '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b',
            'c', 'd', 'e', 'f'
        };

        for (int i = 0; i < byteArray.length; i++) {

            int v = byteArray[i] & 0xff;

            sb.append((char) Hexhars[v >> 4]);
            sb.append((char) Hexhars[v & 0xf]);
        }

        return sb.toString();
    }
    
    public static UUID byteArrayToUuid(byte[] bytes) {
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        long firstLong = bb.getLong();
        long secondLong = bb.getLong();
        return new UUID(firstLong, secondLong);
    }

    public static byte[] uuidToByteArray(UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }
}
