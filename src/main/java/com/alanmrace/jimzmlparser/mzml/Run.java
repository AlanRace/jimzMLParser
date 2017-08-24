package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.exceptions.InvalidXPathException;
import com.alanmrace.jimzmlparser.exceptions.UnfollowableXPathException;
import com.alanmrace.jimzmlparser.util.XMLHelper;
import java.util.Calendar;

import java.util.Collection;
import javax.xml.bind.DatatypeConverter;

/**
 * Class describing a {@literal <run>} tag.
 * 
 * @author Alan Race
 */
public class Run extends MzMLContentWithParams implements ReferenceableTag {

    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Accession: run attribute (MS:1000857).
     */
    public static String runAttributeID = "MS:1000857";

    /**
     * Default InstrumentConfiguration to describe how data were acquired [Required].
     */
    private InstrumentConfiguration defaultInstrumentConfigurationRef;

    /**
     * Default SourceFile describing where original data came from [Optional].
     */
    private SourceFile defaultSourceFileRef;

    /**
     * Unique ID for the run [Required].
     */
    private String id;													// Required

    /**
     * Reference for the Sample analysed [Optional].
     */
    private Sample sampleRef;											// Optional

    /**
     * Start time of the run.
     */
    private Calendar startTimeStamp;										// Optional

    /**
     * DataProcessingList to be passed to SpectrumList and ChromatogramList to ensure
     * that the list is kept up to date when defaults are changed.
     */
    private ReferenceList<DataProcessing> dataProcessingList;
    
    /**
     * SpectrumList containing all Spectrum instances that describe the entire run.
     */
    private SpectrumList spectrumList;

    /**
     * ChromatogramList containing all Chromatogram instances that describe the entire run.
     */
    private ChromatogramList chromatogramList;

    /**
     * Create a Run instance with specified unique ID and default InstrumentConfiguration.
     * 
     * @param id Unique ID for the run
     * @param defaultInstrumentConfigurationRef Default InstrumentConfiguration
     */
    public Run(String id, InstrumentConfiguration defaultInstrumentConfigurationRef) {
        this.id = id;
        this.defaultInstrumentConfigurationRef = defaultInstrumentConfigurationRef;
    }

    /**
     * Copy constructor, requiring new versions of lists to match old references
     * to.
     * 
     * @param run Run to copy
     * @param rpgList New ReferenceableParamGroupList 
     * @param icList New InstrumentConfigurationList
     * @param sourceFileList New SourceFileList
     * @param sampleList New SampleList
     * @param dpList New DataProcessingList
     */
    public Run(Run run, ReferenceableParamGroupList rpgList, InstrumentConfigurationList icList,
            SourceFileList sourceFileList, SampleList sampleList, DataProcessingList dpList) {
        super(run, rpgList);

        this.id = run.id;

        if (run.startTimeStamp != null) {
            this.startTimeStamp = (Calendar) run.startTimeStamp.clone();
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

    /**
     * Set the DataProcessingList. Required so that changing or adding a new Spectrum 
     * or Chromatogram to any list will keep the DataProcessingList up to date with 
     * all DataProcessing that exist.
     * 
     * @param dataProcessingList DataProcessingList
     */
    protected void setDataProcessingList(ReferenceList<DataProcessing> dataProcessingList) {
        this.dataProcessingList = dataProcessingList;
        
        if(spectrumList != null)
            spectrumList.setDataProcessingList(dataProcessingList);
        
        if(chromatogramList != null)
            chromatogramList.setDataProcessingList(dataProcessingList);
    }

    /**
     * Set the default SourceFile for the run.
     * 
     * @param defaultSourceFileRef Default SourceFile
     */
    public void setDefaultSourceFileRef(SourceFile defaultSourceFileRef) {
        this.defaultSourceFileRef = defaultSourceFileRef;
    }

    /**
     * Return the default SourceFile.
     * 
     * @return Default SourceFile
     */
    public SourceFile getDefaultSourceFileRef() {
        return defaultSourceFileRef;
    }
    
    @Override
    public String getID() {
        return id;
    }

    /**
     * Set the Sample analysed by the run.
     * 
     * @param sampleRef Sample
     */
    public void setSampleRef(Sample sampleRef) {
        this.sampleRef = sampleRef;
    }

    /**
     * Set the time that the run was started.
     * 
     * @param startTimeStamp Point in time when run started.
     */
    public void setStartTimeStamp(Calendar startTimeStamp) {
        this.startTimeStamp = startTimeStamp;
    }

    /**
     * Set the SpectrumList for the run. If a DataProcessingList has been set previously
     * then this then DataProcessingList of the SpectrumList will be updated to the 
     * one stored in the run.
     * 
     * <p>TODO: Consider removing this so that a SpectrumList is unique to the Run.
     * 
     * @param spectrumList SpectrumList
     */
    public void setSpectrumList(SpectrumList spectrumList) {
        spectrumList.setParent(this);

        this.spectrumList = spectrumList;
        this.spectrumList.setDataProcessingList(dataProcessingList);
    }

    /**
     * Returns the default InstrumentConfiguration for the run.
     * 
     * @return Default InstrumentConfiguration
     */
    public InstrumentConfiguration getDefaultInstrumentConfiguration() {
        return defaultInstrumentConfigurationRef;
    }

    /**
     * Returns the SpectrumList for the run.
     * 
     * @return SpectrumList
     */
    public SpectrumList getSpectrumList() {
        return spectrumList;
    }

    /**
     * Set the ChromatogramList for the run. If a DataProcessingList has been set previously
     * then this then DataProcessingList of the ChromatogramList will be updated to the 
     * one stored in the run.
     * 
     * <p>TODO: Consider removing this so that a ChromatogramList is unique to the Run.
     * 
     * @param chromatogramList ChromatogramList
     */
    public void setChromatogramList(ChromatogramList chromatogramList) {
        chromatogramList.setParent(this);

        this.chromatogramList = chromatogramList;
        this.chromatogramList.setDataProcessingList(dataProcessingList);
    }

    /**
     * Returns the ChromatogramList for the run.
     * 
     * @return ChromatogramList
     */
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
    public String getXMLAttributeText() {
        String attributes = "defaultInstrumentConfigurationRef=\"" + XMLHelper.ensureSafeXML(defaultInstrumentConfigurationRef.getID()) + "\"";
        
        if (defaultSourceFileRef != null) {
            attributes += " defaultSourceFileRef=\"" + XMLHelper.ensureSafeXML(defaultSourceFileRef.getID()) + "\"";
        }
        
        attributes += " id=\"" + XMLHelper.ensureSafeXML(id) + "\"";
        
        if (sampleRef != null) {
            attributes += " sampleRef=\"" + XMLHelper.ensureSafeXML(sampleRef.getID()) + "\"";
        }
        if (startTimeStamp != null) {
            attributes += " startTimeStamp=\"" + DatatypeConverter.printDateTime(startTimeStamp) + "\"";
        }        
        
        return attributes;
    }
    
//    @Override
//    public void outputXML(MzMLWritable output, int indent) throws IOException {
//        MzMLContent.indent(output, indent);
//        output.writeMetadata("<run");
//        output.writeMetadata(" defaultInstrumentConfigurationRef=\"" + XMLHelper.ensureSafeXML(defaultInstrumentConfigurationRef.getID()) + "\"");
//        if (defaultSourceFileRef != null) {
//            output.writeMetadata(" defaultSourceFileRef=\"" + XMLHelper.ensureSafeXML(defaultSourceFileRef.getID()) + "\"");
//        }
//        output.writeMetadata(" id=\"" + XMLHelper.ensureSafeXML(id) + "\"");
//        if (sampleRef != null) {
//            output.writeMetadata(" sampleRef=\"" + XMLHelper.ensureSafeXML(sampleRef.getID()) + "\"");
//        }
//        if (startTimeStamp != null) {
//            SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-DD'T'hh:mm:ss");
//            
//            output.writeMetadata(" startTimeStamp=\"" + XMLHelper.ensureSafeXML(format.format(startTimeStamp)) + "\"");
//        }
//        output.writeMetadata(">\n");
//
//        super.outputXMLContent(output, indent + 1);
//
//        MzMLContent.indent(output, indent);
//        output.writeMetadata("</run>\n");
//    }

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
