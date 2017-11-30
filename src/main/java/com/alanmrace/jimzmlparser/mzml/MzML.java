package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.exceptions.InvalidXPathException;
import com.alanmrace.jimzmlparser.exceptions.UnfollowableXPathException;
import java.io.IOException;
import java.io.Serializable;

import com.alanmrace.jimzmlparser.obo.OBO;
import com.alanmrace.jimzmlparser.data.DataLocation;
import com.alanmrace.jimzmlparser.data.DataStorage;
import com.alanmrace.jimzmlparser.util.XMLHelper;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.Collection;

/**
 * Class capturing {@literal <mzML>} tag within an MzML file.
 * 
 * @author Alan Race
 */
public class MzML extends MzMLContentWithParams implements Serializable {

    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Default XML namespace for an mzML file.
     */
    public static final String NAMESPACE = "http://psi.hupo.org/ms/mzml";

    /**
     * Default XML schema instance (XSI) for an mzML file.
     */
    public static final String XSI = "http://www.w3.org/2001/XMLSchema-instance";

    /**
     * Default XML schema location for an mzML file.
     */
    public static final String SCHEMA_LOCATION = "http://psi.hupo.org/ms/mzml http://psidev.info/files/ms/mzML/xsd/mzML1.1.0.xsd";
    
    /**
     * Default XML schema location for an indexed mzML file.
     */
    public static final String IDX_SCHEMA_LOCATION = "http://psi.hupo.org/ms/mzml http://psidev.info/files/ms/mzML/xsd/mzML1.1.2_idx.xsd";

    /**
     * Current mzML version.
     */
    public static String currentVersion = "1.1.0";

    /**
     * Storage for accessing mzML data.
     */
    protected DataStorage dataStorage;

    // Attributes

    /**
     * Accession attribute from the mzML tag [Optional].
     */
    private String accession;

    /**
     * ID attribute from the mzML tag [Optional].
     */
    private String id;		

    /**
     * Version attribute from the mzML tag [Required].
     */
    private String version;		

    // Sub-elements
    // Required

    /**
     * CVList containing the included ontologies for this mzML [Required].
     */
    private CVList cvList;

    /**
     * FileDescription describing the mzML file [Required].
     */
    private FileDescription fileDescription;
    
    /**
     * ReferenceableParamGroupList for mzML file [Optional].
     */
    private ReferenceableParamGroupList referenceableParamGroupList;

    /**
     * SampleList for the mzML file [Optional].
     */
    private SampleList sampleList;
    
    /**
     * SoftwareList for the mzML file [Required].
     */
    private SoftwareList softwareList;

    /**
     * ScanSettingsList for the mzML file [Optional].
     */
    private ScanSettingsList scanSettingsList;

    /**
     * InstrumentConfigurationList for the mzML file [Required].
     */
    private InstrumentConfigurationList instrumentConfigurationList;

    /**
     * DataProcessingList for the mzML file [Required].
     */
    private DataProcessingList dataProcessingList;

    /**
     * Run for the mzML file [Required].
     */
    private Run run;

    /**
     * Ontology dictionary used to link cvParams to.
     */
    private OBO obo;
    
    /**
     * Constructor with the minimal required information (mzML version).
     * 
     * @param version mzML version
     */
    public MzML(String version) {
        this.version = version;
    }

    /**
     * Copy constructor.
     * 
     * @param mzML Old MzML to copy
     */
    public MzML(MzML mzML) {
        this.accession = mzML.accession;
        this.id = mzML.id;
        this.version = mzML.version;

        this.obo = mzML.obo;

        if (mzML.referenceableParamGroupList != null) {
            referenceableParamGroupList = new ReferenceableParamGroupList(mzML.referenceableParamGroupList);
        }

        cvList = new CVList(mzML.cvList);
        fileDescription = new FileDescription(mzML.fileDescription, referenceableParamGroupList);

        if (mzML.sampleList != null) {
            sampleList = new SampleList(mzML.sampleList, referenceableParamGroupList);
        }

        softwareList = new SoftwareList(mzML.softwareList, referenceableParamGroupList);

        if (mzML.scanSettingsList != null) {
            scanSettingsList = new ScanSettingsList(mzML.scanSettingsList, referenceableParamGroupList, fileDescription.getSourceFileList());
        }

        instrumentConfigurationList = new InstrumentConfigurationList(mzML.instrumentConfigurationList, referenceableParamGroupList, scanSettingsList, softwareList);
        dataProcessingList = new DataProcessingList(mzML.dataProcessingList, referenceableParamGroupList, softwareList);
        run = new Run(mzML.run, referenceableParamGroupList, instrumentConfigurationList,
                fileDescription.getSourceFileList(), sampleList, dataProcessingList);
    }

    /**
     * Set the storage style for the data within the mzML file.
     * 
     * @param dataStorage dataStorage
     */
    public void setDataStorage(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    /**
     * Set the ontology dictionary to use for matching cvParams.
     * 
     * @param obo Ontology dictionary
     */
    public void setOBO(OBO obo) {
        this.obo = obo;
    }

    /**
     * Get the ontology dictionary used for this mzML file.
     * 
     * @return Ontology dictionary
     */
    public OBO getOBO() {
        return obo;
    }

    /**
     * Set the mzML version.
     * 
     * @param version mzML version
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Get the mzML version.
     * 
     * @return mzML version
     */
    public String getVersion() {
        return version;
    }

    /**
     * Set the accession for the mzML tag for when exporting to XML.
     * 
     * @param accession accession
     */
    public void setAccession(String accession) {
        this.accession = accession;
    }

    /**
     * Get the accession set within the mzML XML.
     * 
     * @return accession
     */
    public String accession() {
        return accession;
    }

    /**
     * Set the id attribute for the mzML tag for exporting to XML.
     * 
     * @param id id
     */
    public void setID(String id) {
        this.id = id;
    }

    /**
     * Get the id attribute set within the mzML tag in the XML file.
     * 
     * @return id
     */
    public String getID() {
        return id;
    }
    
    /**
     * Add a spectrum to the spectrumList in the run tag. Equivalent to calling
     * {@code mzML.getRun().getSpectrumList().add(spectrum);}.
     * 
     * @param spectrum Spectrum to add
     */
    public void addSpectrum(Spectrum spectrum) {
        getRun().getSpectrumList().add(spectrum);
    }

    /**
     * Returns the spectrum list associated with this MzML.
     * 
     * @return SpectrumList
     */
    public SpectrumList getSpectrumList() {
        return run.getSpectrumList();
    }
    
    /**
     * Returns the chromatogram list associated with this MzML.
     * 
     * @return ChromatogramList
     */
    public ChromatogramList getChromatogramList() {
        return run.getChromatogramList();
    }
    
    /**
     * Set the CVList for the mzML.
     * 
     * @param cvList cvList
     */
    public void setCVList(CVList cvList) {
        cvList.setParent(this);

        this.cvList = cvList;
    }

    /**
     * Get the CVList.
     * 
     * @return cvList
     */
    public CVList getCVList() {
        return cvList;
    }

    /**
     * Set the FileDescription for the mzML.
     * 
     * @param fileDescription fileDescription
     */
    public void setFileDescription(FileDescription fileDescription) {
        fileDescription.setParent(this);

        this.fileDescription = fileDescription;
    }

    /**
     * Get the FileDescription.
     * 
     * @return fileDescription
     */
    public FileDescription getFileDescription() {
        return fileDescription;
    }

    /**
     * Set the ReferenceableParamGroupList.
     * 
     * @param referenceableParamGroupList referenceableParamGroupList
     */
    public void setReferenceableParamGroupList(ReferenceableParamGroupList referenceableParamGroupList) {
        referenceableParamGroupList.setParent(this);

        this.referenceableParamGroupList = referenceableParamGroupList;
    }

    /**
     * Get the ReferenceableParamGroupList. If it does not exist, then create an 
     * empty list.
     * 
     * @return referenceableParamGroupList
     */
    public ReferenceableParamGroupList getReferenceableParamGroupList() {
        if (referenceableParamGroupList == null) {
            referenceableParamGroupList = new ReferenceableParamGroupList(0);
        }

        return referenceableParamGroupList;
    }

    /**
     * Set the SampleList.
     * 
     * @param sampleList sampleList
     */
    public void setSampleList(SampleList sampleList) {
        sampleList.setParent(this);

        this.sampleList = sampleList;
    }

    /**
     * Get the SampleList. If it does not exist, then create an empty list.
     * 
     * @return sampleList
     */
    public SampleList getSampleList() {
        if (sampleList == null) {
            sampleList = new SampleList(0);
            sampleList.setParent(this);
        }

        return sampleList;
    }

    /**
     * Set the SoftwareList.
     * 
     * @param softwareList softwareList
     */
    public void setSoftwareList(SoftwareList softwareList) {
        softwareList.setParent(this);

        this.softwareList = softwareList;
    }

    /**
     * Get the SoftwareList. If it does not exist, then create an empty list.
     * 
     * @return softwareList
     */
    public SoftwareList getSoftwareList() {
        if (softwareList == null) {
            softwareList = new SoftwareList(0);
        }

        return softwareList;
    }

    /**
     * Set ScanSettingsList.
     * 
     * @param scanSettingsList scanSettingsList
     */
    public void setScanSettingsList(ScanSettingsList scanSettingsList) {
        scanSettingsList.setParent(this);

        this.scanSettingsList = scanSettingsList;
    }

    /**
     * Get ScanSettingsList. If one does not exist, then create an empty list.
     * 
     * @return scanSettingsList
     */
    public ScanSettingsList getScanSettingsList() {
        if (scanSettingsList == null) {
            scanSettingsList = new ScanSettingsList(0);
        }

        return scanSettingsList;
    }

    /**
     * Set InstrumentConfigurationList.
     * 
     * @param instrumentConfigurationList instrumentConfigurationList
     */
    public void setInstrumentConfigurationList(InstrumentConfigurationList instrumentConfigurationList) {
        instrumentConfigurationList.setParent(this);

        this.instrumentConfigurationList = instrumentConfigurationList;
    }

    /**
     * Get InstrumentConfigurationList. If one does not exist, then create an 
     * empty list.
     * 
     * @return instrumentConfigurationList
     */
    public InstrumentConfigurationList getInstrumentConfigurationList() {
        if (instrumentConfigurationList == null) {
            instrumentConfigurationList = new InstrumentConfigurationList(0);
        }

        return instrumentConfigurationList;
    }

    @Override
    public String getTagName() {
        return "mzML";
    }
    
    @Override
    public void addChildrenToCollection(Collection<MzMLTag> children) {
        if(cvList != null)
            children.add(cvList);
        if(fileDescription != null)
            children.add(fileDescription);
        if(referenceableParamGroupList != null)
            children.add(referenceableParamGroupList);
        if(sampleList != null)
            children.add(sampleList);
        if(softwareList != null)
            children.add(softwareList);
        if(scanSettingsList != null)
            children.add(scanSettingsList);
        if(instrumentConfigurationList != null)
            children.add(instrumentConfigurationList);
        if(dataProcessingList != null)
            children.add(dataProcessingList);
        if(run != null)
            children.add(run);
        
        super.addChildrenToCollection(children);
    }

    @Override
    protected void addTagSpecificElementsAtXPathToCollection(Collection<MzMLTag> elements, String fullXPath, String currentXPath) throws InvalidXPathException {
        if (currentXPath.startsWith("/" + cvList.getTagName())) {
            cvList.addElementsAtXPathToCollection(elements, fullXPath, currentXPath);
        } else if (currentXPath.startsWith("/fileDescription")) {
            fileDescription.addElementsAtXPathToCollection(elements, fullXPath, currentXPath);
        } else if (currentXPath.startsWith("/referenceableParamGroupList")) {
            if (referenceableParamGroupList == null) {
                throw new UnfollowableXPathException("No referenceableParamGroupList exists, so cannot go to " + fullXPath, fullXPath, currentXPath);
            }

            referenceableParamGroupList.addElementsAtXPathToCollection(elements, fullXPath, currentXPath);
        } else if (currentXPath.startsWith("/sampleList")) {
            if (sampleList == null) {
                throw new UnfollowableXPathException("No sampleList exists, so cannot go to " + fullXPath, fullXPath, currentXPath);
            }

            sampleList.addElementsAtXPathToCollection(elements, fullXPath, currentXPath);
        } else if (currentXPath.startsWith("/softwareList")) {
            softwareList.addElementsAtXPathToCollection(elements, fullXPath, currentXPath);
        } else if (currentXPath.startsWith("/scanSettingsList")) {
//            if (scanSettingsList == null) {
//                throw new UnfollowableXPathException("No scanSettingsList exists, so cannot go to " + fullXPath, fullXPath, currentXPath);
//            }
            if (scanSettingsList != null)
                scanSettingsList.addElementsAtXPathToCollection(elements, fullXPath, currentXPath);
        } else if (currentXPath.startsWith("/instrumentConfigurationList")) {
            instrumentConfigurationList.addElementsAtXPathToCollection(elements, fullXPath, currentXPath);
        } else if (currentXPath.startsWith("/dataProcessingList")) {
            dataProcessingList.addElementsAtXPathToCollection(elements, fullXPath, currentXPath);
        } else if (currentXPath.startsWith("/run")) {
            run.addElementsAtXPathToCollection(elements, fullXPath, currentXPath);
        }
    }

    /**
     * Set DataProcessingList.
     * 
     * @param dataProcessingList dataProcessingList
     */
    public void setDataProcessingList(DataProcessingList dataProcessingList) {
        dataProcessingList.setParent(this);

        this.dataProcessingList = dataProcessingList;
        
        if(run != null)
            run.setDataProcessingList(dataProcessingList);
    }

    /**
     * Get DataProcessingList. If it does not exist, then create an empty list.
     * 
     * @return dataProcessingList
     */
    public DataProcessingList getDataProcessingList() {
        if (dataProcessingList == null) {
            dataProcessingList = new DataProcessingList(0);
        }

        return dataProcessingList;
    }

    /**
     * Set Run.
     * 
     * @param run run
     */
    public void setRun(Run run) {
        run.setParent(this);

        this.run = run;
        
        run.setDataProcessingList(dataProcessingList);
    }

    /**
     * Get Run.
     * 
     * @return run
     */
    public Run getRun() {
        return run;
    }

    @Override
    public String getXMLAttributeText() {
        String attributeText = "";
        
        // Set up namespaces
        attributeText += "xmlns:xsi=\"" + MzML.XSI + "\"";
        attributeText += " xsi:schemaLocation=\"" + MzML.SCHEMA_LOCATION + "\"";
        attributeText += " xmlns=\"" + MzML.NAMESPACE + "\"";
        // Attributes
        attributeText += " version=\"" + XMLHelper.ensureSafeXML(version) + "\"";
        if (accession != null) {
            attributeText += " accession=\"" + XMLHelper.ensureSafeXML(accession) + "\"";
        }
        if (id != null) {
            attributeText += " id=\"" + XMLHelper.ensureSafeXML(id) + "\"";
        }
        
        return attributeText;
    }
    
    @Override
    public String toString() {
        return "mzML";
    }

    // Clean up by closing any open DataStorage

    /**
     * Close the DataStorage (if it exists) for the data within the mzML file. 
     * This should be called when the MzML file is no longer required.
     */
    public void close() {
        if (dataStorage != null) {
            try {
                dataStorage.close();
            } catch (IOException ex) {
                Logger.getLogger(MzML.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        SpectrumList spectrumList = getRun().getSpectrumList();

        
        for(Spectrum spectrum : spectrumList) {
            closeDataStorage(spectrum.getDataLocation());

            for(BinaryDataArray bda : spectrum.getBinaryDataArrayList()) {
                closeDataStorage(bda.getDataLocation());
            }
        }
    }

    /**
     * Close the specified DataStorage if not null.
     * 
     * <p>TODO: Is this the best location for this?
     * 
     * @param dataLocation DataLocation to close
     */
    protected static void closeDataStorage(DataLocation dataLocation) {
        if (dataLocation != null) {
            DataStorage dataStorage = dataLocation.getDataStorage();

            if (dataStorage != null) {
                try {
                    dataStorage.close();
                } catch (IOException ex) {
                    Logger.getLogger(MzML.class.getName()).log(Level.SEVERE, "Failed to close DataStorage", ex);
                }
            }
        }
    }
    
    /**
     * Adds default MS parameters values to supplied mzML. 
     * 
     * @param mzML MzML to add parameters to.
     */
    protected static void createDefaults(MzML mzML) {
        CVList cvList = CVList.create();
        mzML.setCVList(cvList);
        
        FileDescription fd = FileDescription.create();
        mzML.setFileDescription(fd);
        
        SoftwareList softwareList = SoftwareList.create();
        mzML.setSoftwareList(softwareList);
        
        InstrumentConfigurationList icList = InstrumentConfigurationList.create();
        mzML.setInstrumentConfigurationList(icList);
        
        DataProcessingList dpList = DataProcessingList.create(softwareList.get(0));
        mzML.setDataProcessingList(dpList);
        
        Run run = new Run("run", icList.get(0));
        mzML.setRun(run);
        
        SpectrumList spectrumList = new SpectrumList(0, dpList.get(0));
        run.setSpectrumList(spectrumList);
    }
    
    /**
     * Create default valid MzML. Calls {@link MzML#createDefaults(com.alanmrace.jimzmlparser.mzml.MzML)}.
     *  
     * @return Default MzML instance
     */
    public static MzML create() {
        MzML mzML = new MzML(currentVersion);
        
        createDefaults(mzML);
        
        return mzML;
    }
}
