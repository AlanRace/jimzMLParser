package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.exceptions.InvalidXPathException;
import com.alanmrace.jimzmlparser.exceptions.UnfollowableXPathException;
import com.alanmrace.jimzmlparser.listener.ReferenceListener;
import com.alanmrace.jimzmlparser.util.XMLHelper;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import java.util.Collection;
import com.alanmrace.jimzmlparser.writer.MzMLWritable;

public class Run extends MzMLContentWithParams implements ReferenceableTag {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static String runAttributeID = "MS:1000857";

    private InstrumentConfiguration defaultInstrumentConfigurationRef;	// Required
    private SourceFile defaultSourceFileRef;							// Optional
    private String id;													// Required
    private Sample sampleRef;											// Optional
    private Date startTimeStamp;										// Optional

    private ReferenceListener<DataProcessing> dataProcessingListener;
    
    private SpectrumList spectrumList;
    private ChromatogramList chromatogramList;

    public Run(String id, InstrumentConfiguration defaultInstrumentConfigurationRef) {
        this.id = id;
        this.defaultInstrumentConfigurationRef = defaultInstrumentConfigurationRef;
    }

    public Run(Run run, ReferenceableParamGroupList rpgList, InstrumentConfigurationList icList,
            SourceFileList sourceFileList, SampleList sampleList, DataProcessingList dpList) {
        super(run, rpgList);

        this.id = run.id;

        if (run.startTimeStamp != null) {
            this.startTimeStamp = new Date(run.startTimeStamp.getTime());
        }

        if (run.defaultInstrumentConfigurationRef != null && icList != null) {
            for (InstrumentConfiguration ic : icList) {
                if (run.defaultInstrumentConfigurationRef.getID().equals(ic.getID())) {
                    defaultInstrumentConfigurationRef = ic;

                    break;
                }
            }
        }

        if (run.defaultSourceFileRef != null && sourceFileList != null) {
            for (SourceFile sourceFile : sourceFileList) {
                if (run.defaultSourceFileRef.getID().equals(sourceFile.getID())) {
                    defaultSourceFileRef = sourceFile;

                    break;
                }
            }
        }

        if (run.sampleRef != null && sampleList != null) {
            for (Sample sample : sampleList) {
                if (run.sampleRef.getID().equals(sample.getID())) {
                    sampleRef = sample;

                    break;
                }
            }
        }

        if (run.spectrumList != null) {
            spectrumList = new SpectrumList(run.spectrumList, rpgList, dpList, sourceFileList, icList);
        }
        if (run.chromatogramList != null) {
            chromatogramList = new ChromatogramList(run.chromatogramList, rpgList, dpList, sourceFileList);
        }
    }

    protected void setDataProcessingListener(ReferenceListener<DataProcessing> dataProcessingListener) {
        this.dataProcessingListener = dataProcessingListener;
        
        if(spectrumList != null)
            spectrumList.setDataProcessingListener(dataProcessingListener);
        
        if(chromatogramList != null)
            chromatogramList.setDataProcessingListener(dataProcessingListener);
    }
    
    @Override
    public ArrayList<OBOTermInclusion> getListOfOptionalCVParams() {
        ArrayList<OBOTermInclusion> optional = new ArrayList<OBOTermInclusion>();
        optional.add(new OBOTermInclusion(runAttributeID, true, true, false));

        return optional;
    }

    public void setDefaultSourceFileRef(SourceFile defaultSourceFileRef) {
        this.defaultSourceFileRef = defaultSourceFileRef;
    }

    public SourceFile getDefaultSourceFileRef() {
        return defaultSourceFileRef;
    }
    
    @Override
    public String getID() {
        return id;
    }

    public void setSampleRef(Sample sampleRef) {
        this.sampleRef = sampleRef;
    }

    public void setStartTimeStamp(Date startTimeStamp) {
        this.startTimeStamp = startTimeStamp;
    }

    public void setSpectrumList(SpectrumList spectrumList) {
        spectrumList.setParent(this);

        this.spectrumList = spectrumList;
        this.spectrumList.setDataProcessingListener(dataProcessingListener);
    }

    public InstrumentConfiguration getDefaultInstrumentConfiguration() {
        return defaultInstrumentConfigurationRef;
    }

    public SpectrumList getSpectrumList() {
        return spectrumList;
    }

    public void setChromatogramList(ChromatogramList chromatogramList) {
        chromatogramList.setParent(this);

        this.chromatogramList = chromatogramList;
        this.chromatogramList.setDataProcessingListener(dataProcessingListener);
    }

    public ChromatogramList getChromatogramList() {
        return chromatogramList;
    }

    @Override
    protected void addTagSpecificElementsAtXPathToCollection(Collection<MzMLTag> elements, String fullXPath, String currentXPath) throws InvalidXPathException {
        if (currentXPath.startsWith("/spectrumList")) {
            if (spectrumList == null) {
                throw new UnfollowableXPathException("No spectrumList exists, so cannot go to " + fullXPath, fullXPath, currentXPath);
            }

            spectrumList.addElementsAtXPathToCollection(elements, fullXPath, currentXPath);
        } else if (currentXPath.startsWith("/chromatogramList")) {
            if (chromatogramList == null) {
                throw new UnfollowableXPathException("No chromatogramList exists, so cannot go to " + fullXPath, fullXPath, currentXPath);
            }

            chromatogramList.addElementsAtXPathToCollection(elements, fullXPath, currentXPath);
        }
    }

//	public void setDefaultInstrumentConfiguration(InstrumentConfiguration defaultInstrumentConfigurationRef) {
//		this.defaultInstrumentConfigurationRef = defaultInstrumentConfigurationRef;
//	}
//	
//	public InstrumentConfiguration getDefaultInstrumentConfiguration() {
//		return defaultInstrumentConfigurationRef;
//	}
//	public void addSpectrum(Spectrum spectrum) {
//		spectrumList.add(spectrum);
//	}
//	
//	public int getSpectrumCount() {
//		return spectrumList.size();
//	}
//	
//	public Spectrum getSpectrum(int index) {
//		return spectrumList.get(index);
//	}
//	// This method looks at the scan start times and if the differences between them are over 1.5 times larger 
//	// than the smallest difference then it is assumed that missing pixels feature 
//	public void replaceMissingSpectra(OBO obo, int numberOfSpectraToExpect, int experimentsPerPixel) {
//		double minStartTimeDifference = Double.MAX_VALUE;
//		
////		// Determine the minimum difference
////		for(int i = experimentsPerPixel; i < spectrumList.size(); i+=experimentsPerPixel) {
////			double curStartTime = spectrumList.get(i).getScanList().getScanStartTime();
////			double prevStartTime = spectrumList.get(i-experimentsPerPixel).getScanList().getScanStartTime();
////			
////			if(curStartTime == -1 || prevStartTime == -1) {
////				minStartTimeDifference = -1;
////				break;
////			}
////			
////			double difference = curStartTime - prevStartTime;
////			
////			if(difference <= 0) {
////				System.out.println("Problematic difference " + difference + " at spectrum number " + i + " with curStartTime = " + curStartTime + ", prevStartTime = " + prevStartTime);
////			}
////			
////			if(difference < minStartTimeDifference)
////				minStartTimeDifference = difference;
////		}
////		System.out.println("Determined minimum start time difference = " + minStartTimeDifference);
////		// Replace spectra as necessary
////		if(minStartTimeDifference > 0) {
////			for(int i = experimentsPerPixel; i < spectrumList.size() ; i+=experimentsPerPixel) {
////				double curStartTime = spectrumList.get(i).getScanList().getScanStartTime();
////				double prevStartTime = spectrumList.get(i-experimentsPerPixel).getScanList().getScanStartTime();		
////				double difference = curStartTime - prevStartTime;
////				long missingSpectra = Math.round(difference/minStartTimeDifference) - 1;
////				
//////				System.out.println(difference + " / " + minStartTimeDifference + " = " + missingSpectra);
////				
////				// Check if there are spectra to replace
////				if(missingSpectra > 0) {
////					System.out.println("Adding " + missingSpectra + " empty spectra at location " + i);
////					
////					for(int j = 0; j < missingSpectra; j++) {
////						for(int k = 0; k < experimentsPerPixel; k++) {
////							Spectrum spectrum = new Spectrum();
////							
////							Scan scan = new Scan();
////							scan.addCvParam(new OBOTermValue(obo.getTerm(Scan.scanStartTimeID), "" + (prevStartTime + ((k + 1) * (difference / experimentsPerPixel)))));
////							
////							spectrum.getScanList().addScan(scan);
////							
////							BinaryDataArray mzCount = new BinaryDataArray();
////							mzCount.addCvParam(new OBOTermValue(obo.getTerm(BinaryDataArray.mzArrayID), ""));
////							mzCount.addCvParam(new OBOTermValue(obo.getTerm(BinaryDataArray.externalArrayLengthID), "0"));
////							mzCount.addCvParam(new OBOTermValue(obo.getTerm(BinaryDataArray.externalEncodedLengthID), "0"));
////							mzCount.addCvParam(new OBOTermValue(obo.getTerm(BinaryDataArray.externalOffsetID), "0"));
////							spectrum.addBinaryDataArray(mzCount);
////							
////							BinaryDataArray intensityCount = new BinaryDataArray();
////							intensityCount.addCvParam(new OBOTermValue(obo.getTerm(BinaryDataArray.intensityArrayID), ""));
////							intensityCount.addCvParam(new OBOTermValue(obo.getTerm(BinaryDataArray.externalArrayLengthID), "0"));
////							intensityCount.addCvParam(new OBOTermValue(obo.getTerm(BinaryDataArray.externalEncodedLengthID), "0"));
////							intensityCount.addCvParam(new OBOTermValue(obo.getTerm(BinaryDataArray.externalOffsetID), "0"));
////							spectrum.addBinaryDataArray(intensityCount);
////							
////							// Add the spectrum to the spectrumList
////							spectrumList.add(i++, spectrum);
////						}
////					}
////				}
////			}
////		}
//		
//		// Replace missing spectra at the end
//		for(int i = spectrumList.size(); i < numberOfSpectraToExpect; i++) {
//			System.out.println("Adding empty spectra at (end) location " + i);
//
//			Spectrum spectrum = new Spectrum();
//				
//			BinaryDataArray mzCount = new BinaryDataArray();
//			mzCount.addCvParam(new OBOTermValue(obo.getTerm(BinaryDataArray.mzArrayID), ""));
//			mzCount.addCvParam(new OBOTermValue(obo.getTerm(BinaryDataArray.externalArrayLengthID), "0"));
//			mzCount.addCvParam(new OBOTermValue(obo.getTerm(BinaryDataArray.externalEncodedLengthID), "0"));
//			mzCount.addCvParam(new OBOTermValue(obo.getTerm(BinaryDataArray.externalOffsetID), "0"));
//			spectrum.addBinaryDataArray(mzCount);
//				
//			BinaryDataArray intensityCount = new BinaryDataArray();
//			intensityCount.addCvParam(new OBOTermValue(obo.getTerm(BinaryDataArray.intensityArrayID), ""));
//			intensityCount.addCvParam(new OBOTermValue(obo.getTerm(BinaryDataArray.externalArrayLengthID), "0"));
//			intensityCount.addCvParam(new OBOTermValue(obo.getTerm(BinaryDataArray.externalEncodedLengthID), "0"));
//			intensityCount.addCvParam(new OBOTermValue(obo.getTerm(BinaryDataArray.externalOffsetID), "0"));
//			spectrum.addBinaryDataArray(intensityCount);
//				
//			// Add the spectrum to the spectrumList
//			spectrumList.add(i, spectrum);
//		}
//	}
//	public void setDefaultDataProcessing(DataProcessing defaultDataProcessingRef) {
//		this.defaultDataProcessingRef = defaultDataProcessingRef;
//	}
//	public void addChromatogram(Chromatogram chromatogram) {
//		chromatogramList.add(chromatogram);
//	}
//	
//	public int getChromatogramCount() {
//		return chromatogramList.size();
//	}
//	
//	public Chromatogram getChromatogram(int index) {
//		return chromatogramList.get(index);
//	}
    @Override
    public void outputXML(MzMLWritable output, int indent) throws IOException {
        MzMLContent.indent(output, indent);
        output.write("<run");
        output.write(" defaultInstrumentConfigurationRef=\"" + XMLHelper.ensureSafeXML(defaultInstrumentConfigurationRef.getID()) + "\"");
        if (defaultSourceFileRef != null) {
            output.write(" defaultSourceFileRef=\"" + XMLHelper.ensureSafeXML(defaultSourceFileRef.getID()) + "\"");
        }
        output.write(" id=\"" + XMLHelper.ensureSafeXML(id) + "\"");
        if (sampleRef != null) {
            output.write(" sampleRef=\"" + XMLHelper.ensureSafeXML(sampleRef.getID()) + "\"");
        }
        if (startTimeStamp != null) {
            SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-DD'T'hh:mm:ss");
            
            output.write(" startTimeStamp=\"" + XMLHelper.ensureSafeXML(format.format(startTimeStamp)) + "\"");
        }
        output.write(">\n");

        super.outputXMLContent(output, indent + 1);

        MzMLContent.indent(output, indent);
        output.write("</run>\n");
    }

    @Override
    public String toString() {
        return "run: defaultInstrumentConfigurationRef=\"" + defaultInstrumentConfigurationRef.getID() + "\""
                + ((defaultSourceFileRef != null) ? (" defaultSourceFileRef=\"" + defaultSourceFileRef.getID() + "\"") : "")
                + " id=\"" + id + "\""
                + ((sampleRef != null) ? (" sampleRef=\"" + sampleRef.getID() + "\"") : "")
                + ((startTimeStamp != null) ? (" startTimeStamp=\"" + startTimeStamp.toString() + "\"") : "");
    }

    @Override
    public String getTagName() {
        return "run";
    }
    
    @Override
    public void addChildrenToCollection(Collection<MzMLTag> children) {
        super.addChildrenToCollection(children);
        
        if(spectrumList != null)
            children.add(spectrumList);
        if(chromatogramList != null)
            children.add(chromatogramList);
    }

    @Override
    public void setID(String id) {
        this.id = id;
    }
}
