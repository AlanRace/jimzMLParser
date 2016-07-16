package com.alanmrace.jimzmlparser.mzML;

import com.alanmrace.jimzmlparser.imzML.PixelLocation;
import com.alanmrace.jimzmlparser.parser.DataLocation;
import com.alanmrace.jimzmlparser.parser.MzMLSpectrumDataStorage;
import com.alanmrace.jimzmlparser.util.XMLHelper;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class Spectrum extends MzMLDataContainer implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static final String scanPolarityID = "MS:1000465";
    public static final String spectrumTypeID = "MS:1000559";
    public static final String spectrumRepresentationID = "MS:1000525";
    public static final String spectrumAttributeID = "MS:1000499";

    public static final String totalIonCurrentID = "MS:1000285";
    public static final String basePeakMZID = "MS:1000504";
    public static final String basePeakIntensityID = "MS:1000505";

    public static final String lowestObservedmzID = "MS:1000528";
    public static final String highestObservedmzID = "MS:1000527";

    public static final String MS1Spectrum = "MS:1000579"; // EmptyCVParam
    public static final String positiveScanID = "MS:1000130";	// EmptyCVParam
    public static final String profileSpectrumID = "MS:1000128"; // EmptyCVParam

 //   private DataProcessing dataProcessingRef;
//	private int index;
    private SourceFile sourceFileRef;
    private String spotID;

    private ScanList scanList;
    private PrecursorList precursorList;
    private ProductList productList;
    
    private PixelLocation pixelLocation;

//	public Spectrum() {
//		super();
//		
//		id = "Spectrum="+spectrumID++;
//		
//		scanList = new ScanList();
//		binaryDataArray = new ArrayList<BinaryDataArray>();
//	}
//	
//
//	public Spectrum(String id, int defaultArrayLength) {
//		this();
//		
//		this.id = id;
//		this.defaultArrayLength = defaultArrayLength;
//	}
    public Spectrum(String id, int defaultArrayLength, int index) {
        super(id, defaultArrayLength, index);
//        this.id = id;
//        this.defaultArrayLength = defaultArrayLength;
//		this.index = index;
    }

    public Spectrum(Spectrum spectrum, ReferenceableParamGroupList rpgList, DataProcessingList dpList,
            SourceFileList sourceFileList, InstrumentConfigurationList icList) {
        super(spectrum, rpgList);

        this.id = spectrum.id;
        this.defaultArrayLength = spectrum.defaultArrayLength;
        this.spotID = spectrum.spotID;

        if (spectrum.dataProcessingRef != null && dpList != null) {
            for (DataProcessing dp : dpList) {
                if (spectrum.dataProcessingRef.getID().equals(dp.getID())) {
                    dataProcessingRef = dp;
                }
            }
        }

        if (spectrum.sourceFileRef != null && sourceFileList != null) {
            for (SourceFile sourceFile : sourceFileList) {
                if (spectrum.sourceFileRef.getID().equals(sourceFile.getID())) {
                    sourceFileRef = sourceFile;

                    break;
                }
            }
        }

        if (spectrum.scanList != null) {
            scanList = new ScanList(spectrum.scanList, rpgList, sourceFileList, icList);
        }
        if (spectrum.precursorList != null) {
            precursorList = new PrecursorList(spectrum.precursorList, rpgList, sourceFileList);
        }
        if (spectrum.productList != null) {
            productList = new ProductList(spectrum.productList, rpgList);
        }
        if (spectrum.binaryDataArrayList != null) {
            binaryDataArrayList = new BinaryDataArrayList(spectrum.binaryDataArrayList, rpgList, dpList);
        }
    }

    @Override
    public ArrayList<OBOTermInclusion> getListOfRequiredCVParams() {
        ArrayList<OBOTermInclusion> required = new ArrayList<OBOTermInclusion>();
        required.add(new OBOTermInclusion(spectrumTypeID, true, true, false));
        required.add(new OBOTermInclusion(spectrumRepresentationID, true, true, true));

        return required;
    }

    @Override
    public ArrayList<OBOTermInclusion> getListOfOptionalCVParams() {
        ArrayList<OBOTermInclusion> optional = new ArrayList<OBOTermInclusion>();
        optional.add(new OBOTermInclusion(scanPolarityID, true, true, false));
        optional.add(new OBOTermInclusion(spectrumAttributeID, false, true, false));

        return optional;
    }

    public void setSourceFileRef(SourceFile sourceFileRef) {
        this.sourceFileRef = sourceFileRef;
    }

    public void setSpotID(String spotID) {
        this.spotID = spotID;
    }    

    public ScanList getScanList() {
        return scanList;
    }

    public void setScanList(ScanList scanList) {
        scanList.setParent(this);

        this.scanList = scanList;
    }

    public void setPrecursorList(PrecursorList precursorList) {
        precursorList.setParent(this);

        this.precursorList = precursorList;
    }

    public PrecursorList getPrecursorList() {
        return precursorList;
    }

    public void setProductList(ProductList productList) {
        productList.setParent(this);

        this.productList = productList;
    }

    public ProductList getProductList() {
        return productList;
    }
    
    public PixelLocation getPixelLocation() {
        if(pixelLocation == null) {
            // 
            for(Scan scan : scanList) {
                CVParam xValue = scan.getCVParam(Scan.positionXID);
                CVParam yValue = scan.getCVParam(Scan.positionYID);
                CVParam zValue = scan.getCVParam(Scan.positionZID);
                
                if(xValue != null && yValue != null) {
                    int x = xValue.getValueAsInteger();
                    int y = yValue.getValueAsInteger();
                    
                    int z = (zValue != null)? zValue.getValueAsInteger() : 1;
                    
                    pixelLocation = new PixelLocation(x, y, z);
                    
                    break;
                }
            }
        }
        
        return pixelLocation;
    }

    public double[] getmzArray() throws IOException {
        return getmzArray(false);
    }

    public double[] getmzArray(boolean keepInMemory) throws IOException {
//	    System.out.println(dataLocation);

        if (binaryDataArrayList == null) {
            return null;
        }

        if (dataLocation != null && dataLocation.getDataStorage() instanceof MzMLSpectrumDataStorage) {
            convertMzMLDataStorageToBase64();
        }

        return binaryDataArrayList.getmzArray().getDataAsDouble(keepInMemory);
    }

//	private double[] getDoubleData(RandomAccessFile ibdFile, BinaryDataArray bda) {
//		byte[] data = null;
//		
//		try {
//			data = bda.getBinary().getData(ibdFile, bda.isCompressed());
//		} catch (DataFormatException ex) {
//			try {
//				data = bda.getBinary().getData(ibdFile, false);
//			} catch (DataFormatException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//
//		int length = 0;
//		
//		if(bda.isDoublePrecision() || bda.isSigned64BitInteger())
//			length = data.length / 8;
//		else if(bda.isSinglePrecision() || bda.isSigned32BitInteger())
//			length = data.length / 4;
//		else if(bda.isSigned16BitInteger())
//			length = data.length / 2;
//		else if(bda.isSigned8BitInteger())
//			length = data.length;
//		
//		double[] convertedData = new double[length];
//		
//		ByteBuffer buffer = ByteBuffer.wrap(data);
//		buffer.order(ByteOrder.LITTLE_ENDIAN);
//		
//		// Convert 
//		if(bda.isDoublePrecision())
//			for(int j = 0; j < length; j++)
//				convertedData[j] = buffer.getDouble();
//		else if(bda.isSinglePrecision())
//			for(int j = 0; j < length; j++)
//				convertedData[j] = buffer.getFloat();
//		else if(bda.isSigned64BitInteger())
//			for(int j = 0; j < length; j++)
//				convertedData[j] = buffer.getLong();
//		else if(bda.isSigned32BitInteger())
//			for(int j = 0; j < length; j++)
//				convertedData[j] = buffer.getInt();
//		else if(bda.isSigned16BitInteger())
//			for(int j = 0; j < length; j++)
//				convertedData[j] = buffer.getShort();
//		else if(bda.isSigned8BitInteger())
//			for(int j = 0; j < length; j++)
//				convertedData[j] = buffer.get();
//		
//		return convertedData;
//	}
//	
//	public double[] getmzArray(RandomAccessFile ibdFile) {
////		for(BinaryDataArray bda : getBinaryDataArrayList()) {
////			if(bda.getCVParam(BinaryDataArray.mzArrayID) != null) {
////				return getDoubleData(ibdFile, bda);
////			}
////		}
//		BinaryDataArray mzArray = getBinaryDataArrayList().getmzArray();
//
//		if(mzArray != null)
//			return getDoubleData(ibdFile, mzArray);
//		
//		return null;
//	}
//	
//	public double[] getmzArray() {
//		return getmzArray(null);
//	}
//	
//	public double[] getIntensityArray() {
//		return getIntensityArray(null);
//	}
//	
//	public double[] getIntensityArray(RandomAccessFile ibdFile) {
////		for(BinaryDataArray bda : getBinaryDataArrayList()) {
////			if(bda.getCVParam(BinaryDataArray.intensityArrayID) != null) {
////				return getDoubleData(ibdFile, bda);
////			}
////		}
//		BinaryDataArray intensityArray = getBinaryDataArrayList().getIntensityArray();
//		
//		if(intensityArray != null)
//			return getDoubleData(ibdFile, intensityArray);
//		
//		return null;
//	}
//	public void addBinaryDataArray(BinaryDataArray bda) {
//		this.binaryDataArray.add(bda);
//	}
//	
//	public int getBinaryDataArrayCount() {
//		return binaryDataArray.size();
//	}
//	
//	public BinaryDataArray getBinaryDataArray(int index) {
//		return binaryDataArray.get(index);
//	}
//	public void setXYPosition(OBO obo, int x, int y) {
//		if(scanList.getScanCount() < 1)
//			scanList.addScan(new Scan());
//		
//		scanList.getScan(0).setXYPosition(obo, x, y);
//	}
//	
//	// TODO: Check all scans, rather than just the first one
//	public int getXPosition() {
//		return scanList.getScan(0).getXPosition();
//	}
//	
//	public int getYPosition() {
//		return scanList.getScan(0).getYPosition();
//	}
//	
//	public long outputDataFromTemp(OBO obo, DataOutputStream binaryDataStream, long offset) {
//		for(BinaryDataArray bda : binaryDataArray)
//			offset = bda.outputDataFromTemp(obo, binaryDataStream, offset);
//		
//		return offset;
//	}
//	
//	public long outputData(OBO obo, DataOutputStream binaryDataStream, long offset) {
//		for(BinaryDataArray bda : binaryDataArray)
//			offset = bda.outputData(obo, binaryDataStream, offset);
//		
//		return offset;
//	}
    public void outputXML(BufferedWriter output, int indent, int index) throws IOException {
        if(raf != null) {
            output.flush();
            this.setmzMLLocation(raf.getFilePointer());
        }
        
        MzMLContent.indent(output, indent);
        output.write("<spectrum");
        output.write(" defaultArrayLength=\"" + defaultArrayLength + "\"");
        output.write(" id=\"" + XMLHelper.ensureSafeXML(id) + "\"");
        output.write(" index=\"" + index + "\"");
        if (dataProcessingRef != null) {
            output.write(" dataProcessingRef=\"" + XMLHelper.ensureSafeXML(dataProcessingRef.getID()) + "\"");
        }
        if (sourceFileRef != null) {
            output.write(" sourceFileRef=\"" + XMLHelper.ensureSafeXML(sourceFileRef.getID()) + "\"");
        }
        if (spotID != null) {
            output.write(" spotID=\"" + XMLHelper.ensureSafeXML(spotID) + "\"");
        }
        output.write(">\n");

        super.outputXML(output, indent + 1);

        if (scanList != null && scanList.size() > 0) {
            scanList.outputXML(output, indent + 1);
        }

        if (precursorList != null && precursorList.size() > 0) {
            precursorList.outputXML(output, indent + 1);
        }

        if (productList != null && productList.size() > 0) {
            productList.outputXML(output, indent + 1);
        }

        if (binaryDataArrayList != null && binaryDataArrayList.size() > 0) {
            binaryDataArrayList.outputXML(output, indent + 1);
        }
//			MzMLContent.indent(output, indent+1);
//			output.write("<binaryDataArrayList");
//			output.write(" count=\"" + binaryDataArray.size() + "\"");
//			output.write(">\n");
//			
//			for(BinaryDataArray bda : binaryDataArray)
//				bda.outputXML(output, indent+2);
//			
//			MzMLContent.indent(output, indent+1);
//			output.write("</binaryDataArrayList>\n");
//		}

        MzMLContent.indent(output, indent);
        output.write("</spectrum>\n");
    }

    public String toString() {
        return "spectrum:"
                + " id=\"" + id + "\""
                + ((dataProcessingRef != null) ? (" dataProcessingRef=\"" + dataProcessingRef.getID() + "\"") : ""
                        + " defaultArrayLength=\"" + defaultArrayLength + "\""
                        + ((sourceFileRef != null) ? (" sourceFileRef=\"" + sourceFileRef.getID() + "\"") : "")
                        + ((spotID != null && !spotID.isEmpty()) ? (" spotID=\"" + spotID + "\"") : ""));
    }

    private int getAdditionalChildrenCount() {
        int additionalChildren = ((scanList != null) ? 1 : 0)
                + ((precursorList != null) ? 1 : 0)
                + ((productList != null) ? 1 : 0)
                + ((binaryDataArrayList != null) ? 1 : 0);

        return additionalChildren;
    }

//	@Override
//	public int getChildCount() {
//		return super.getChildCount() + getAdditionalChildrenCount();
//	}
//	
//	@Override
//	public Enumeration<TreeNode> children() {
//		Vector<TreeNode> children = new Vector<TreeNode>();		
//		Enumeration<TreeNode> superChildren = super.children();
//		
//		while(superChildren.hasMoreElements())
//			children.add(superChildren.nextElement());
//		
//		if(scanList != null)
//			children.add(scanList);
//		if(precursorList != null)
//			children.add(precursorList);
//		if(productList != null)
//			children.add(productList);
//		if(binaryDataArrayList != null)
//			children.add(binaryDataArrayList);
//		
//		return children.elements();
//	}
//	
//	@Override
//	public TreeNode getChildAt(int index) {
//		if(index < super.getChildCount()) {
//			return super.getChildAt(index);
//		} else if(index < getChildCount()) {			
//			int counter = super.getChildCount();
//			
//			if(scanList != null) {
//				if(counter == index)
//					return scanList;
//				
//				counter++;
//			}
//			if(precursorList != null) {
//				if(counter == index)
//					return precursorList;
//				
//				counter++;
//			}
//			if(productList != null) {
//				if(counter == index)
//					return productList;
//				
//				counter++;
//			}
//			if(binaryDataArrayList != null) {
//				if(counter == index)
//					return binaryDataArrayList;
//				
//				counter++;
//			}
//		}
//		
//		return null;
//	}
//	
//	@Override
//	public int getIndex(TreeNode childNode) {
//		int counter = super.getChildCount();
//			
//		if(childNode instanceof ScanList)
//			return counter;
//		
//		if(scanList != null)
//			counter++;
//		
//		if(childNode instanceof PrecursorList)
//			return counter;
//		
//		if(precursorList != null)
//			counter++;
//		
//		if(childNode instanceof ProductList)
//			return counter;
//		
//		if(productList != null)
//			counter++;
//		
//		if(childNode instanceof BinaryDataArrayList)
//			return counter;
//		
//		if(binaryDataArrayList != null)
//			counter++;
//		
//		return super.getIndex(childNode);
//	}
}
