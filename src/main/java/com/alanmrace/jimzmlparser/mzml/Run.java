package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.exceptions.InvalidXPathException;
import com.alanmrace.jimzmlparser.exceptions.UnfollowableXPathException;
import com.alanmrace.jimzmlparser.util.XMLHelper;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.Collection;

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
    public static final String RUN_ATTRIBUTE_ID = "MS:1000857";

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
    private String id;						// Required

    /**
     * Reference for the Sample analysed [Optional].
     */
    private Sample sampleRef;					// Optional

    /**
     * Start time of the run.
     */
    private Calendar startTimeStamp;				// Optional

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
        this.chromatogramList = chromatogramList;
        
        if(chromatogramList != null) {
            chromatogramList.setParent(this);
       
            this.chromatogramList.setDataProcessingList(dataProcessingList);
        }
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
            SimpleDateFormat xmlDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            xmlDateTimeFormat.setTimeZone(startTimeStamp.getTimeZone());
            
            attributes += " startTimeStamp=\"" + xmlDateTimeFormat.format(startTimeStamp.getTime()) + "\"";
        }        
        
        return attributes;
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
