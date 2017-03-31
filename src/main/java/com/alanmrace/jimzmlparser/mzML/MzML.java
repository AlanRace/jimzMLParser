package com.alanmrace.jimzmlparser.mzml;

//import com.alanmrace.jimzmlparser.exceptions.ImzMLConversionException;
import com.alanmrace.jimzmlparser.exceptions.ImzMLWriteException;
import com.alanmrace.jimzmlparser.exceptions.InvalidXPathException;
import com.alanmrace.jimzmlparser.exceptions.UnfollowableXPathException;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;

//import com.fasterxml.jackson.annotation.JsonIgnore;
import com.alanmrace.jimzmlparser.obo.OBO;
import com.alanmrace.jimzmlparser.parser.DataLocation;
import com.alanmrace.jimzmlparser.parser.DataStorage;
import com.alanmrace.jimzmlparser.util.XMLHelper;
import java.io.RandomAccessFile;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.Collection;

public class MzML extends MzMLContent implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static String namespace = "http://psi.hupo.org/ms/mzml";
    public static String xsi = "http://www.w3.org/2001/XMLSchema-instance";
    public static String schemaLocation = "http://psi.hupo.org/ms/mzml http://psidev.info/files/ms/mzML/xsd/mzML1.1.0_idx.xsd";

    public static String currentVersion = "1.1.0";

    protected DataStorage dataStorage;

    // Attributes
    private String accession;	// Optional
    private String id;			// Optional
    private String version;		// Required

    // Sub-elements
    // Required
    private CVList cvList;
    private FileDescription fileDescription;
    // Optional
    private ReferenceableParamGroupList referenceableParamGroupList;
    private SampleList sampleList;
    // Required
    private SoftwareList softwareList;
    // Optional
    private ScanSettingsList scanSettingsList;
    // Required
    private InstrumentConfigurationList instrumentConfigurationList;
    private DataProcessingList dataProcessingList;
    private Run run;

    private OBO obo;
    
    private boolean outputIndex = false;
    private RandomAccessFile raf;


    public MzML(String version) {
        this.version = version;
    }

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

    public void setDataStorage(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    public void setOBO(OBO obo) {
        this.obo = obo;
    }

    public OBO getOBO() {
        return obo;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public void setAccession(String accession) {
        this.accession = accession;
    }

    public String accession() {
        return accession;
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getID() {
        return id;
    }

    public void setCVList(CVList cvList) {
        cvList.setParent(this);

        this.cvList = cvList;
    }

    public CVList getCVList() {
        return cvList;
    }

    public void setFileDescription(FileDescription fd) {
        fd.setParent(this);

        this.fileDescription = fd;
    }

    public FileDescription getFileDescription() {
        return fileDescription;
    }

    public void setReferenceableParamGroupList(ReferenceableParamGroupList referenceableParamGroupList) {
        referenceableParamGroupList.setParent(this);

        this.referenceableParamGroupList = referenceableParamGroupList;
    }

    public ReferenceableParamGroupList getReferenceableParamGroupList() {
        if (referenceableParamGroupList == null) {
            referenceableParamGroupList = new ReferenceableParamGroupList(0);
        }

        return referenceableParamGroupList;
    }

    public void setSampleList(SampleList sampleList) {
        sampleList.setParent(this);

        this.sampleList = sampleList;
    }

    public SampleList getSampleList() {
        if (sampleList == null) {
            sampleList = new SampleList(0);
        }

        return sampleList;
    }

    public void setSoftwareList(SoftwareList softwareList) {
        softwareList.setParent(this);

        this.softwareList = softwareList;
    }

    public SoftwareList getSoftwareList() {
        if (softwareList == null) {
            softwareList = new SoftwareList(0);
        }

        return softwareList;
    }

    public void setScanSettingsList(ScanSettingsList scanSettingsList) {
        scanSettingsList.setParent(this);

        this.scanSettingsList = scanSettingsList;
    }

    public ScanSettingsList getScanSettingsList() {
        if (scanSettingsList == null) {
            scanSettingsList = new ScanSettingsList(0);
        }

        return scanSettingsList;
    }

    public void setInstrumentConfigurationList(InstrumentConfigurationList instrumentConfigurationList) {
        instrumentConfigurationList.setParent(this);

        this.instrumentConfigurationList = instrumentConfigurationList;
    }

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

    public void addElementsAtXPathToCollection(Collection<MzMLContent> elements, String xPath) throws InvalidXPathException {
        addElementsAtXPathToCollection(elements, xPath, xPath);
    }

    @Override
    protected void addTagSpecificElementsAtXPathToCollection(Collection<MzMLContent> elements, String fullXPath, String currentXPath) throws InvalidXPathException {
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
            if (scanSettingsList == null) {
                throw new UnfollowableXPathException("No scanSettingsList exists, so cannot go to " + fullXPath, fullXPath, currentXPath);
            }

            scanSettingsList.addElementsAtXPathToCollection(elements, fullXPath, currentXPath);
        } else if (currentXPath.startsWith("/instrumentConfigurationList")) {
            instrumentConfigurationList.addElementsAtXPathToCollection(elements, fullXPath, currentXPath);
        } else if (currentXPath.startsWith("/dataProcessingList")) {
            dataProcessingList.addElementsAtXPathToCollection(elements, fullXPath, currentXPath);
        } else if (currentXPath.startsWith("/run")) {
            run.addElementsAtXPathToCollection(elements, fullXPath, currentXPath);
        }
    }

    public void setDataProcessingList(DataProcessingList dataProcessingList) {
        dataProcessingList.setParent(this);

        this.dataProcessingList = dataProcessingList;
    }

    public DataProcessingList getDataProcessingList() {
        if (dataProcessingList == null) {
            dataProcessingList = new DataProcessingList(0);
        }

        return dataProcessingList;
    }

    public void setRun(Run run) {
        run.setParent(this);

        this.run = run;
    }

    public Run getRun() {
        return run;
    }

    public void write(String filename) throws ImzMLWriteException {
        try {
            String encoding = "ISO-8859-1";

            raf = new RandomAccessFile(filename, "rw");

            outputIndex = true;

            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(raf.getFD()), encoding);
            BufferedWriter output = new BufferedWriter(out);

            output.write("<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>\n");
            outputXML(output, 0);

            output.flush();
            output.close();
        } catch (IOException e1) {
            throw new ImzMLWriteException("Error writing mzML file " + filename + ". " + e1.getLocalizedMessage());
        }
    }

    @Override
    public void outputXML(BufferedWriter output, int indent) throws IOException {
        if (outputIndex) {
            MzMLContent.indent(output, indent);
            output.write("<indexedmzML");
            output.write("  xmlns=\"http://psi.hupo.org/ms/mzml\"");
            output.write("  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
            output.write("  xsi:schemaLocation=\"http://psi.hupo.org/ms/mzml http://psidev.info/files/ms/mzML/xsd/mzML1.1.2_idx.xsd\">\n");

            for (Spectrum spectrum : run.getSpectrumList()) {
                spectrum.setRAF(raf);
            }
        }

        MzMLContent.indent(output, indent);
        output.write("<mzML");
        // Set up namespaces
        output.write(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
        output.write(" xsi:schemaLocation=\"http://psi.hupo.org/ms/mzml http://psidev.info/files/ms/mzML/xsd/mzML1.1.0.xsd\"");
        output.write(" xmlns=\"http://psi.hupo.org/ms/mzml\"");
        // Attributes
        output.write(" version=\"" + XMLHelper.ensureSafeXML(version) + "\"");
        if (accession != null) {
            output.write(" accession=\"" + XMLHelper.ensureSafeXML(accession) + "\"");
        }
        if (id != null) {
            output.write(" id=\"" + XMLHelper.ensureSafeXML(id) + "\"");
        }
        output.write(">\n");

        // TODO: This shouldn't be the case ...there should always be a cvList
        if (cvList == null) {
            cvList = new CVList(0);
        }

        cvList.outputXML(output, indent + 1);

        // FileDescription
        fileDescription.outputXML(output, indent + 1);

        if (referenceableParamGroupList != null && referenceableParamGroupList.size() > 0) {
            referenceableParamGroupList.outputXML(output, indent + 1);
        }

        if (sampleList != null && sampleList.size() > 0) {
            sampleList.outputXML(output, indent + 1);
        }

        // SoftwareList
        softwareList.outputXML(output, indent + 1);

        // ScanSettingsList
        if (scanSettingsList != null && scanSettingsList.size() > 0) {
            scanSettingsList.outputXML(output, indent + 1);
        }

        // InstrumentConfigurationList
        instrumentConfigurationList.outputXML(output, indent + 1);

        // DataProcessingList
        dataProcessingList.outputXML(output, indent + 1);

        // Run
        run.outputXML(output, indent + 1);

        MzMLContent.indent(output, indent);
        output.write("</mzML>\n");

        if (outputIndex) {
            output.flush();
            long indexListOffset = raf.getFilePointer();

            MzMLContent.indent(output, indent + 1);
            output.write("<indexList count=\"1\">\n");
            MzMLContent.indent(output, indent + 2);
            output.write("<index name=\"spectrum\">\n");

            for (Spectrum spectrum : run.getSpectrumList()) {
                MzMLContent.indent(output, indent + 3);
                output.write("<offset idRef=\"" + spectrum.getID() + "\">" + spectrum.getmzMLLocation() + "</offset>\n");
            }

            MzMLContent.indent(output, indent + 2);
            output.write("</index>\n");
            MzMLContent.indent(output, indent + 1);
            output.write("</indexList>\n");

            output.write("<indexListOffset>" + indexListOffset + "</indexListOffset>\n");

            MzMLContent.indent(output, indent);
            output.write("</indexedmzML>\n");
        }
    }

    @Override
    public String toString() {
        return "mzML";
    }

    // Clean up by closing any open DataStorage
    public void close() {
        if (dataStorage != null) {
            try {
                dataStorage.close();
            } catch (IOException ex) {
                Logger.getLogger(MzML.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        SpectrumList spectrumList = getRun().getSpectrumList();

        if (spectrumList.size() > 0) {
            Spectrum spectrum = spectrumList.getSpectrum(0);
            //for(Spectrum spectrum : spectrumList) {
            closeDataStorage(spectrum.getDataLocation());

            BinaryDataArrayList bdal = spectrum.getBinaryDataArrayList();

            if (bdal.size() > 0) {
                BinaryDataArray bda = bdal.getBinaryDataArray(0);

                closeDataStorage(bda.getDataLocation());
            }
            //}
        }
    }

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
}
