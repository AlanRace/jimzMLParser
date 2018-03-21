package com.alanmrace.jimzmlparser.parser;

import com.alanmrace.jimzmlparser.data.MzMLSpectrumDataStorage;
import com.alanmrace.jimzmlparser.data.DataLocation;
import com.alanmrace.jimzmlparser.data.DataStorage;
import com.alanmrace.jimzmlparser.exceptions.CVParamAccessionNotFoundException;
import com.alanmrace.jimzmlparser.exceptions.FatalParseException;
import com.alanmrace.jimzmlparser.exceptions.InvalidFormatIssue;
import com.alanmrace.jimzmlparser.exceptions.MissingReferenceIssue;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.alanmrace.jimzmlparser.mzml.*;
import com.alanmrace.jimzmlparser.obo.OBO;
import com.alanmrace.jimzmlparser.obo.OBOTerm;
import com.alanmrace.jimzmlparser.exceptions.InvalidMzML;
import com.alanmrace.jimzmlparser.exceptions.Issue;
import com.alanmrace.jimzmlparser.exceptions.MzMLParseException;
import com.alanmrace.jimzmlparser.exceptions.NonFatalParseException;
import com.alanmrace.jimzmlparser.exceptions.ObsoleteTermUsed;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.channels.Channels;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * SAX parser for mzML files, only focusing on metadata.
 *
 * <p>All metadata are parsed, however the data is ignored. This allows a lower
 * memory usage for loading metadata, while also allowing code reuse for both
 * MzML and ImzML files.
 *
 * @author Alan Race
 */
public class MzMLHeaderHandler extends DefaultHandler {

    /**
     * Class logger.
     */
    private static final Logger logger = Logger.getLogger(MzMLHeaderHandler.class.getName());

    /**
     * SAX document locator. TODO: Why is it used?
     */
    protected Locator locator;

    /**
     * OBO ontology used to match and check cvParam tags.
     */
    protected OBO obo;

    /**
     * MzML currently being built by the SAX parser.
     */
    protected MzML mzML;

    /**
     * CVList associated with the MzML that is being parsed.
     */
    protected CVList cvList;

    /**
     * FileDescription associated with the MzML that is being parsed.
     */
    protected FileDescription fileDescription;

    /**
     * SourceFileList associated with the MzML that is being parsed.
     */
    protected SourceFileList sourceFileList;

    /**
     * ReferenceableParamGroupList associated with the MzML that is being
     * parsed.
     */
    protected ReferenceableParamGroupList referenceableParamGroupList;

    /**
     * SampleList associated with the MzML that is being parsed.
     */
    protected SampleList sampleList;

    /**
     * SoftwareList associated with the MzML that is being parsed.
     */
    protected SoftwareList softwareList;

    /**
     * ScanSettingsList associated with the MzML that is being parsed.
     */
    protected ScanSettingsList scanSettingsList;

    /**
     * Current ScanSettings created each time a {@literal <scanSettings>} tag is
     * encountered.
     */
    protected ScanSettings currentScanSettings;

    /**
     * Current SourceFileRefList created each time a
     * {@literal <sourceFileRefList>} tag is encountered.
     */
    protected SourceFileRefList currentSourceFileRefList;

    /**
     * Current TargetList created each time a {@literal <targetList>} tag is
     * encountered.
     */
    protected TargetList currentTargetList;

    /**
     * InstrumentConfigurationList associated with the MzML that is currently
     * being parsed.
     */
    protected InstrumentConfigurationList instrumentConfigurationList;

    /**
     * Current InstrumentConfiguration created each time a
     * {@literal <instrumentConfiguration>} tag is encountered.
     */
    protected InstrumentConfiguration currentInstrumentConfiguration;

    /**
     * Current ComponentList (can consist of Source, Analyzer and Detector)
     * created each time a {@literal <instrumentConfiguration>} tag is
     * encountered.
     */
    protected ComponentList currentComponentList;

    /**
     * DataProcessingList associated with the MzML that is currently being
     * parsed.
     */
    protected DataProcessingList dataProcessingList;

    /**
     * Current DataProcessing created each time a {@literal <dataProcessing>}
     * tag is encountered.
     */
    protected DataProcessing currentDataProcessing;

    /**
     * Run associated with the MzML that is currently being parsed.
     */
    protected Run run;

    /**
     * SpectrumList associated with the MzML that is currently being parsed.
     */
    protected SpectrumList spectrumList;

    /**
     * Current Spectrum created each time a {@literal <spectrum>} tag is
     * encountered.
     */
    protected Spectrum currentSpectrum;

    /**
     * Current ScanList created each time a {@literal <scanList>} tag is
     * encountered.
     */
    protected ScanList currentScanList;

    /**
     * Current Scan created each time a {@literal <scan>} tag is encountered.
     */
    protected Scan currentScan;

    /**
     * Current ScanWindowList created each time a {@literal <scanWindowList>}
     * tag is encountered.
     */
    protected ScanWindowList currentScanWindowList;

    /**
     * Current PrecursorList created each time a {@literal <precursorList>} tag
     * is encountered.
     */
    protected PrecursorList currentPrecursorList;

    /**
     * Current Precursor created each time a {@literal <precursor>} tag is
     * encountered.
     */
    protected Precursor currentPrecursor;

    /**
     * Current SelectedIonList created each time a {@literal <selectedIonList>}
     * tag is encountered.
     */
    protected SelectedIonList currentSelectedIonList;

    /**
     * Current ProductList created each time a {@literal <productList>} tag is
     * encountered.
     */
    protected ProductList currentProductList;

    /**
     * Current Product created each time a {@literal <product>} tag is
     * encountered.
     */
    protected Product currentProduct;

    /**
     * Current BinaryDataArrayList created each time a
     * {@literal <binaryDataArrayList>} tag is encountered.
     */
    protected BinaryDataArrayList currentBinaryDataArrayList;

    /**
     * Current BinaryDataArray created each time a {@literal <binaryDataArray>}
     * tag is encountered.
     */
    protected BinaryDataArray currentBinaryDataArray;

    /**
     * ChromatogramList associated with the current MzML being parsed.
     */
    protected ChromatogramList chromatogramList;

    /**
     * Current Chromatogram created each time a {@literal <chromatogram>} tag is
     * encountered.
     */
    protected Chromatogram currentChromatogram;

    /**
     * Current tag.
     */
    protected MzMLContentWithChildren currentContent;

    // Flags for tags that share the same sub-tags
    /**
     * True after {@literal <spectrum>} tag and before the end tag has been
     * encountered.
     */
    protected boolean processingSpectrum;

    /**
     * True after {@literal <chromatogram>} tag and before the end tag has been
     * encountered.
     */
    protected boolean processingChromatogram;

    /**
     * True after {@literal <precursor>} tag and before the end tag has been
     * encountered.
     */
    protected boolean processingPrecursor;

    /**
     * True after {@literal <product>} tag and before the end tag has been
     * encountered.
     */
    protected boolean processingProduct;

    /**
     * True after {@literal <offset>} tag and before the end tag has been
     * encountered.
     */
    protected boolean processingOffset;

    /**
     * StringBuffer containing the contents between the {@literal <offset>} and
     * {@literal </offset>} tags.
     */
    protected StringBuffer offsetData;

    protected String previousOffsetIDRef;
    protected String currentOffsetIDRef;

    protected long previousOffset = -1;

    protected DataStorage dataStorage;
    protected boolean openDataStorage = true;

    protected int numberOfSpectra = 0;

    protected List<ParserListener> listeners;

    /**
     * Set up a SAX parser for MzML metadata with the specified ontology
     * dictionary.
     *
     * @param obo Ontology database
     */
    protected MzMLHeaderHandler(OBO obo) {
        this.obo = obo;

        processingSpectrum = false;
        processingChromatogram = false;
        processingPrecursor = false;
        processingProduct = false;

        // Create a string buffer for storing the character offsets stored in indexed mzML
        offsetData = new StringBuffer();

        listeners = new LinkedList<ParserListener>();
    }

    @Override
    public void setDocumentLocator(Locator locator) {
        this.locator = locator;
    }

    public void setOpenDataStorage(boolean openDataStorage) {
        this.openDataStorage = openDataStorage;
    }

    public MzMLHeaderHandler(OBO obo, File mzMLFile) throws FileNotFoundException {
        this(obo, mzMLFile, true);
    }

    public MzMLHeaderHandler(OBO obo, File mzMLFile, boolean openDataFile) throws FileNotFoundException {
        this(obo);

        if (openDataFile) {
            this.dataStorage = new MzMLSpectrumDataStorage(mzMLFile);
        }
    }

    public void registerParserListener(ParserListener listener) {
        this.listeners.add(listener);
    }

    protected void notifyParserListeners(Issue issue) {
        for (ParserListener listener : listeners) {
            listener.issueFound(issue);
        }
    }


    public static MzML parsemzMLHeader(String filename) throws FatalParseException {
        return parsemzMLHeader(filename, true);
    }
    
    public static MzML parsemzMLHeader(String filename, boolean openDataFile) throws FatalParseException {
        return parsemzMLHeader(filename, openDataFile, null);
    }
    
    public static MzML parsemzMLHeader(String filename, boolean openDataFile, ParserListener listener) throws FatalParseException {
        try {
            //OBO obo = new OBO("imagingMS.obo");
            OBO obo = OBO.getOBO();

            // Parse mzML
            MzMLHeaderHandler handler = new MzMLHeaderHandler(obo, new File(filename), openDataFile);
            handler.setOpenDataStorage(openDataFile);
            
            if(listener != null)
                handler.registerParserListener(listener);

            SAXParserFactory spf = SAXParserFactory.newInstance();

            // TODO: INDEXED RAF when reading!!!
            RandomAccessFile raf = new RandomAccessFile(filename, "r");
            InputStream is = Channels.newInputStream(raf.getChannel());
            
            //get a new instance of parser
            SAXParser sp = spf.newSAXParser();

            //parse the file and also register this class for call backs
            sp.parse(is, handler);

            handler.getmzML().setOBO(obo);

            return handler.getmzML();
        } catch (SAXException ex) {
            logger.log(Level.SEVERE, null, ex);

            throw new MzMLParseException("SAXException: " + ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MzMLHeaderHandler.class.getName()).log(Level.SEVERE, null, ex);

            throw new MzMLParseException("File not found: " + filename);
        } catch (IOException ex) {
            Logger.getLogger(MzMLHeaderHandler.class.getName()).log(Level.SEVERE, null, ex);

            throw new MzMLParseException("IOException: " + ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(MzMLHeaderHandler.class.getName()).log(Level.SEVERE, null, ex);

            throw new MzMLParseException("ParserConfigurationException: " + ex);
        }
    }
    
    protected int getCountAttribute(Attributes attributes) {
        String countString = attributes.getValue("count");
        int count;

        if (countString == null) {
            count = 0;
        } else {
            count = Integer.parseInt(countString);
        }

        return count;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        // Most common attribute at the start to reduce the number of comparisons needed
        if ("cvParam".equals(qName)) {
            if (currentContent != null) {
                String accession = attributes.getValue("accession");

                OBOTerm term = obo.getTerm(accession);

                // If the term does not exist within any OBO, convert to UserParam for the sake of continuing parsing
                // Notify listeners of the fact that an issue occured and we attempted to resolve it
                if (term == null) {
                    UserParam userParam = new UserParam(attributes.getValue("accession"), attributes.getValue("value"), obo.getTerm(attributes.getValue("unitAccession")));
                    ((MzMLContentWithParams) currentContent).addUserParam(userParam);

                    CVParamAccessionNotFoundException notFound = new CVParamAccessionNotFoundException(attributes.getValue("accession"));
                    notFound.fixAttempted(userParam);
                    notFound.setIssueLocation(currentContent);

                    notifyParserListeners(notFound);
                } else {
                    if (term.isObsolete()) {
                        ObsoleteTermUsed obsoleteIssue = new ObsoleteTermUsed(term);
                        obsoleteIssue.setIssueLocation(currentContent);

                        notifyParserListeners(obsoleteIssue);
                    }

                    try {
                        CVParam.CVParamType paramType = CVParam.getCVParamType(term);
                        //System.out.println(term + " " + paramType);
                        //CVParam.CVParamType paramType = CVParam.getCVParamType(accession);
                        CVParam cvParam;

                        String value = attributes.getValue("value");
                        OBOTerm units = obo.getTerm(attributes.getValue("unitAccession"));

                        try {
                            switch (paramType) {
                                case String:
                                    cvParam = new StringCVParam(term, value, units);
                                    break;
                                case Empty:
                                    cvParam = new EmptyCVParam(term, units);
                                    
                                    if (value != null && !value.isEmpty()) {
                                        InvalidFormatIssue formatIssue = new InvalidFormatIssue(term, attributes.getValue("value"));
                                        formatIssue.setIssueLocation(currentContent);
                                        
                                        notifyParserListeners(formatIssue);
                                    }
                                    break;
                                case Long:
                                    cvParam = new LongCVParam(term, Long.parseLong(value), units);
                                    break;
                                case Double:
                                    cvParam = new DoubleCVParam(term, Double.parseDouble(value), units);
                                    break;
                                case Boolean:
                                    cvParam = new BooleanCVParam(term, Boolean.parseBoolean(value), units);
                                    break;
                                case Integer:
                                    cvParam = new IntegerCVParam(term, Integer.parseInt(value), units);
                                    break;
                                default:
                                    cvParam = new StringCVParam(term, attributes.getValue("value"), obo.getTerm(attributes.getValue("unitAccession")));
                                    
                                    InvalidFormatIssue formatIssue = new InvalidFormatIssue(term, paramType);
                                    formatIssue.fixAttemptedByChangingType((StringCVParam) cvParam);
                                    formatIssue.setIssueLocation(currentContent);
                                    notifyParserListeners(formatIssue);
                                    
                                    break;
                            }
                        } catch (NumberFormatException nfe) {
                            cvParam = new StringCVParam(term, attributes.getValue("value"), obo.getTerm(attributes.getValue("unitAccession")));

//                            notifyParserListeners(new NonFatalParseException("Failed value conversion " + nfe, nfe));                          
                            InvalidFormatIssue formatIssue = new InvalidFormatIssue(term, attributes.getValue("value"));
                            formatIssue.fixAttemptedByChangingType((StringCVParam) cvParam);
                            formatIssue.setIssueLocation(currentContent);

                            notifyParserListeners(formatIssue);
                        }

                        if (currentContent instanceof MzMLContentWithParams) {
                            ((MzMLContentWithParams) currentContent).addCVParam(cvParam);
                        } else {
                            throw new RuntimeException("Failure to add CVParam to " + currentContent);
                        }
                    } catch (NonFatalParseException ex) {
                        ex.setIssueLocation(currentContent);

                        notifyParserListeners(ex);

                        Logger.getLogger(MzMLHeaderHandler.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } else {
                throw new InvalidMzML("<cvParam> tag without a parent.");
            }
        } else if ("referenceableParamGroupRef".equals(qName)) {
            boolean foundReference = false;

            if (referenceableParamGroupList != null) {
                ReferenceableParamGroup group = referenceableParamGroupList.getReferenceableParamGroup(attributes.getValue("ref"));

                if (group != null) {
                    ReferenceableParamGroupRef ref = new ReferenceableParamGroupRef(group);

                    if (currentContent != null) {
                        foundReference = true;

                        ((MzMLContentWithParams) currentContent).addReferenceableParamGroupRef(ref);
                    }
                }
            }

            if (!foundReference) {
                MissingReferenceIssue missingRefIssue = new MissingReferenceIssue(attributes.getValue("ref"), "referenceableParamGroupRef", "ref");
                missingRefIssue.setIssueLocation(currentContent);
                missingRefIssue.fixAttemptedByRemovingReference();

                notifyParserListeners(missingRefIssue);
            }
        } else if ("userParam".equals(qName)) {
            if (currentContent != null) {
                UserParam userParam = new UserParam(attributes.getValue("name"));

                String type = attributes.getValue("type");
                if (type != null) {
                    userParam.setType(type);
                }
                String value = attributes.getValue("value");
                if (value != null) {
                    userParam.setValue(value);
                }

                userParam.setUnits(obo.getTerm(attributes.getValue("unitAccession")));

                ((MzMLContentWithParams) currentContent).addUserParam(userParam);
            }
        } else if ("mzML".equalsIgnoreCase(qName)) {
            mzML = new MzML(attributes.getValue("version"));

            mzML.setDataStorage(dataStorage);
            mzML.setOBO(obo);

            // Add optional attributes
            if (attributes.getValue("accession") != null) {
                mzML.setAccession(attributes.getValue("accession"));
            }

            if (attributes.getValue("id") != null) {
                mzML.setID(attributes.getValue("id"));
            }
        } else if ("cvList".equals(qName)) {
            cvList = new CVList(Integer.parseInt(attributes.getValue("count")));

            try {
                mzML.setCVList(cvList);
            } catch (NullPointerException ex) {
                throw new InvalidMzML("<mzML> tag not defined prior to defining <cvList> tag.", ex);
            }
        } else if ("cv".equals(qName)) {
            OBO obo = OBO.getOBO().getOBOWithID(attributes.getValue("id"));
            
            if(obo != null)
                cvList.add(new CV(obo));
            else
                System.out.println("WEIRD ONTOLOGY FOUND! " + attributes.getValue("id"));
            
//            String cvURI = attributes.getValue("URI");
//            
//            // In old versions a lower case attribute was used incorrectly, so 
//            // read this in
//            if(cvURI == null)
//                cvURI = attributes.getValue("uri");
//            
//            CV cv = new CV(cvURI, attributes.getValue("fullName"), attributes.getValue("id"));
//
//            if (attributes.getValue("version") != null) {
//                cv.setVersion(attributes.getValue("version"));
//            }
//
//            try {
//                cvList.add(cv);
//            } catch (NullPointerException ex) {
//                throw new InvalidMzML("<cvList> tag not defined prior to defining <cv> tag.", ex);
//            }
        } else if ("fileDescription".equals(qName)) {
            fileDescription = new FileDescription();

            mzML.setFileDescription(fileDescription);
        } else if ("fileContent".equals(qName)) {
            FileContent fc = new FileContent();

            try {
                fileDescription.setFileContent(fc);
            } catch (NullPointerException ex) {
                throw new InvalidMzML("<fileDescription> tag not defined prior to defining <fileContent> tag.", ex);
            }

            currentContent = fc;
        } else if ("sourceFileList".equals(qName)) {
            sourceFileList = new SourceFileList(Integer.parseInt(attributes.getValue("count")));

            try {
                fileDescription.setSourceFileList(sourceFileList);
            } catch (NullPointerException ex) {
                throw new InvalidMzML("<fileDescription> tag not defined prior to defining <sourceFileList> tag.", ex);
            }
        } else if ("sourceFile".equals(qName)) {
            SourceFile sf = new SourceFile(attributes.getValue("id"), attributes.getValue("location"), attributes.getValue("name"));

            try {
                sourceFileList.addSourceFile(sf);
            } catch (NullPointerException ex) {
                throw new InvalidMzML("<sourceFileList> tag not defined prior to defining <sourceFile> tag.", ex);
            }

            currentContent = sf;
        } else if ("contact".equals(qName)) {
            Contact contact = new Contact();

            try {
                fileDescription.addContact(contact);
            } catch (NullPointerException ex) {
                throw new InvalidMzML("<fileDescription> tag not defined prior to defining <contact> tag.", ex);
            }

            currentContent = contact;
        } else if ("referenceableParamGroupList".equals(qName)) {
            referenceableParamGroupList = new ReferenceableParamGroupList(getCountAttribute(attributes));

            try {
                mzML.setReferenceableParamGroupList(referenceableParamGroupList);
            } catch (NullPointerException ex) {
                throw new InvalidMzML("<mzML> tag not defined prior to defining <referenceableParamGroupList> tag.", ex);
            }
        } else if ("referenceableParamGroup".equals(qName)) {
            ReferenceableParamGroup rpg = new ReferenceableParamGroup(attributes.getValue("id"));
            currentContent = rpg;

            //try {
                referenceableParamGroupList.addReferenceableParamGroup(rpg);
            //} catch (NullPointerException ex) {
            //    throw new InvalidMzML("<referenceableParamGroupList> tag not defined prior to defining <referenceableParamGroup> tag.");
            //}
        } else if ("sampleList".equals(qName)) {
            sampleList = new SampleList(getCountAttribute(attributes));

            try {
                mzML.setSampleList(sampleList);
            } catch (NullPointerException ex) {
                throw new InvalidMzML("<mzML> tag not defined prior to defining <sampleList> tag.", ex);
            }
        } else if ("sample".equals(qName)) {
            Sample sample = new Sample(attributes.getValue("id"));

            if (attributes.getValue("name") != null) {
                sample.setName(attributes.getValue("name"));
            }

            try {
                sampleList.addSample(sample);
            } catch (NullPointerException ex) {
                throw new InvalidMzML("<sampleList> tag not defined prior to defining <sample> tag.", ex);
            }

            currentContent = sample;
        } else if ("softwareList".equals(qName)) {
            softwareList = new SoftwareList(getCountAttribute(attributes));

            try {
                mzML.setSoftwareList(softwareList);
            } catch (NullPointerException ex) {
                throw new InvalidMzML("<mzML> tag not defined prior to defining <softwareList> tag.", ex);
            }
        } else if ("software".equals(qName)) {
            Software sw = new Software(attributes.getValue("id"), attributes.getValue("version"));

            try {
                softwareList.addSoftware(sw);
            } catch (NullPointerException ex) {
                throw new InvalidMzML("<softwareList> tag not defined prior to defining <software> tag.", ex);
            }
            currentContent = sw;
        } else if ("scanSettingsList".equals(qName)) {
            scanSettingsList = new ScanSettingsList(Integer.parseInt(attributes.getValue("count")));

            try {
                mzML.setScanSettingsList(scanSettingsList);
            } catch (NullPointerException ex) {
                throw new InvalidMzML("<mzML> tag not defined prior to defining <scanSettingsList> tag.", ex);
            }
        } else if ("scanSettings".equals(qName)) {
            currentScanSettings = new ScanSettings(attributes.getValue("id"));

            try {
                scanSettingsList.addScanSettings(currentScanSettings);
            } catch (NullPointerException ex) {
                throw new InvalidMzML("<scanSettingsList> tag not defined prior to defining <scanSettings> tag.", ex);
            }

            currentContent = currentScanSettings;
        } else if ("sourceFileRefList".equals(qName)) {
            currentSourceFileRefList = new SourceFileRefList(Integer.parseInt(attributes.getValue("count")));

            try {
                currentScanSettings.setSourceFileRefList(currentSourceFileRefList);
            } catch (NullPointerException ex) {
                throw new InvalidMzML("<scanSettings> tag not defined prior to defining <sourceFileRefList> tag.", ex);
            }
        } else if ("sourceFileRef".equals(qName)) {
            String ref = attributes.getValue("ref");

            boolean foundReference = false;

            if (sourceFileList != null) {
                SourceFile sourceFile = sourceFileList.getSourceFile(ref);

                if (sourceFile != null) {
                    currentSourceFileRefList.addSourceFileRef(new SourceFileRef(sourceFile));

                    foundReference = true;
                }
            }

            if (!foundReference) {
                MissingReferenceIssue missingRefIssue = new MissingReferenceIssue(attributes.getValue("ref"), "sourceFileRef", "ref");
                missingRefIssue.setIssueLocation(currentContent);
                missingRefIssue.fixAttemptedByRemovingReference();

                notifyParserListeners(missingRefIssue);
            }
        } else if ("targetList".equals(qName)) {
            currentTargetList = new TargetList(Integer.parseInt(attributes.getValue("count")));

            try {
                currentScanSettings.setTargetList(currentTargetList);
            } catch (NullPointerException ex) {
                throw new InvalidMzML("<scanSettings> tag not defined prior to defining <targetList> tag.", ex);
            }
        } else if ("target".equals(qName)) {
            Target target = new Target();

            try {
                currentTargetList.addTarget(target);
            } catch (NullPointerException ex) {
                throw new InvalidMzML("<targetList> tag not defined prior to defining <target> tag.", ex);
            }

            currentContent = target;
        } else if ("instrumentConfigurationList".equals(qName)) {
            instrumentConfigurationList = new InstrumentConfigurationList(getCountAttribute(attributes));

            try {
                mzML.setInstrumentConfigurationList(instrumentConfigurationList);
            } catch (NullPointerException ex) {
                throw new InvalidMzML("<mzML> tag not defined prior to defining <instrumentConfigurationList> tag.", ex);
            }
        } else if ("instrumentConfiguration".equals(qName)) {
            currentInstrumentConfiguration = new InstrumentConfiguration(attributes.getValue("id"));

            String scanSettingsRef = attributes.getValue("scanSettingsRef");

            if (scanSettingsRef != null) {
                ScanSettings scanSettings = scanSettingsList.getScanSettings(scanSettingsRef);

                if (scanSettings != null) {
                    currentInstrumentConfiguration.setScanSettingsRef(scanSettings);
                } else {
                    //throw new InvalidMzML("Can't find scanSettingsRef '" + scanSettingsRef + "' on instrumentConfiguration '" + currentInstrumentConfiguration.getID() + "'");

                    MissingReferenceIssue missingRefIssue = new MissingReferenceIssue(scanSettingsRef, "instrumentConfiguration", "scanSettingsRef");
                    missingRefIssue.setIssueLocation(currentContent);
                    missingRefIssue.fixAttemptedByRemovingReference();

                    notifyParserListeners(missingRefIssue);
                }
            }

            try {
                instrumentConfigurationList.addInstrumentConfiguration(currentInstrumentConfiguration);
            } catch (NullPointerException ex) {
                throw new InvalidMzML("<instrumentConfigurationList> tag not defined prior to defining <instrumentConfiguration> tag.", ex);
            }

            currentContent = currentInstrumentConfiguration;
        } else if ("componentList".equals(qName)) {
            currentComponentList = new ComponentList();

            try {
                currentInstrumentConfiguration.setComponentList(currentComponentList);
            } catch (NullPointerException ex) {
                throw new InvalidMzML("<instrumentConfiguration> tag not defined prior to defining <componentList> tag.", ex);
            }
        } else if ("source".equals(qName)) {
            Source source = new Source();

            try {
                currentComponentList.addSource(source);
            } catch (NullPointerException ex) {
                throw new InvalidMzML("<componentList> tag not defined prior to defining <source> tag.", ex);
            }

            currentContent = source;
        } else if ("analyzer".equals(qName)) {
            Analyser analyser = new Analyser();

            try {
                currentComponentList.addAnalyser(analyser);
            } catch (NullPointerException ex) {
                throw new InvalidMzML("<componentList> tag not defined prior to defining <analyser> tag.", ex);
            }

            currentContent = analyser;
        } else if ("detector".equals(qName)) {
            Detector detector = new Detector();

            try {
                currentComponentList.addDetector(detector);
            } catch (NullPointerException ex) {
                throw new InvalidMzML("<componentList> tag not defined prior to defining <detector> tag.", ex);
            }

            currentContent = detector;
        } else if ("softwareRef".equals(qName)) {
            String softwareRef = attributes.getValue("ref");

            boolean foundReference = false;

            if (softwareList != null && currentInstrumentConfiguration != null) {
                Software software = softwareList.getSoftware(softwareRef);

                if (software != null) {
                    foundReference = true;

                    currentInstrumentConfiguration.setSoftwareRef(new SoftwareRef(software));
                }
            }

            if (!foundReference) {
                MissingReferenceIssue missingRefIssue = new MissingReferenceIssue(softwareRef, "softwareRef", "ref");
                missingRefIssue.setIssueLocation(currentContent);
                missingRefIssue.fixAttemptedByRemovingReference();

                notifyParserListeners(missingRefIssue);

                //logger.log(Level.WARNING, "Invalid mzML file - could not find softwareRef ''{0}''. Attempting to continue...", softwareRef);
                // TODO: Reinstate these checks
                //throw new InvalidMzML("Can't find softwareRef '" + softwareRef + "' in instrumentConfiguration '" + currentInstrumentConfiguration.getID() + "'");
            }
        } else if ("dataProcessingList".equals(qName)) {
            dataProcessingList = new DataProcessingList(getCountAttribute(attributes));

            try {
                mzML.setDataProcessingList(dataProcessingList);
            } catch (NullPointerException ex) {
                throw new InvalidMzML("<mzML> tag not defined prior to defining <dataProcessingList> tag.", ex);
            }
        } else if ("dataProcessing".equals(qName)) {
            DataProcessing dp = new DataProcessing(attributes.getValue("id"));

            try {
                dataProcessingList.addDataProcessing(dp);
            } catch (NullPointerException ex) {
                throw new InvalidMzML("<dataProcessingList> tag not defined prior to defining <dataProcessing> tag.", ex);
            }

            currentDataProcessing = dp;
            currentContent = dp;
        } else if ("processingMethod".equals(qName)) {
            String softwareRef = attributes.getValue("softwareRef");

            boolean referenceFound = false;
            Software software = null;

            if (softwareList != null) {
                software = softwareList.getSoftware(softwareRef);
                
                if(software != null)
                    referenceFound = true;
                else {
                    software = softwareList.get(0);
                }
            }

            ProcessingMethod pm = new ProcessingMethod(software);
            currentDataProcessing.addProcessingMethod(pm);

            currentContent = pm;

            if (!referenceFound && software != null) {
                MissingReferenceIssue missingRefIssue = new MissingReferenceIssue(softwareRef, "processingMethod", "softwareRef");
                missingRefIssue.setIssueLocation(currentContent);
                missingRefIssue.fixAttemptedByChangingReference(software);

                notifyParserListeners(missingRefIssue);
            } else if(!referenceFound) {
                MissingReferenceIssue missingRefIssue = new MissingReferenceIssue(softwareRef, "processingMethod", "softwareRef");
                missingRefIssue.setIssueLocation(currentContent);
                missingRefIssue.fixAttemptedByRemovingReference();

                notifyParserListeners(missingRefIssue);
            }
        } else if ("run".equals(qName)) {
            String instrumentConfigurationRef = attributes.getValue("defaultInstrumentConfigurationRef");

            InstrumentConfiguration instrumentConfiguration = null;

            if (instrumentConfigurationRef != null && !instrumentConfigurationRef.isEmpty()) {
                try {
                    instrumentConfiguration = instrumentConfigurationList.getInstrumentConfiguration(instrumentConfigurationRef);
                } catch (NullPointerException ex) {
                    throw new InvalidMzML("<instrumentConfigurationList> tag not defined prior to defining <run> tag.", ex);
                }
            }

            if (instrumentConfiguration != null) {
                run = new Run(attributes.getValue("id"), instrumentConfiguration);
            } else {
                MissingReferenceIssue missingRefIssue = new MissingReferenceIssue(instrumentConfigurationRef, "run", "defaultInstrumentConfigurationRef");
                missingRefIssue.setIssueLocation(currentContent);

                // TODO: Workaround only in place because of ABSciex converter bug where 
                // the defaultInstrumentConfigurationRef is auto-incremented in every raster
                // line file but the instrumentConfiguration id remains as 'instrumentConfiguration1'					
                if (currentInstrumentConfiguration != null) {
                    //logger.log(Level.WARNING, "Invalid mzML file - could not find instrumentConfigurationRef ''{0}''. Attempting to continue...", instrumentConfigurationRef);
                    missingRefIssue.fixAttemptedByChangingReference(currentInstrumentConfiguration);

                    run = new Run(attributes.getValue("id"), currentInstrumentConfiguration);
                } else {
                    //logger.log(Level.WARNING, "Invalid mzML file - could not find instrumentConfigurationRef ''{0}''. Attempting to continue...", instrumentConfigurationRef);
                    missingRefIssue.fixAttemptedByRemovingReference();

                    run = new Run(attributes.getValue("id"), null);
                    //throw new InvalidMzML("Can't find instrumentConfigurationRef '" + instrumentConfigurationRef + "'");
                }
                
                notifyParserListeners(missingRefIssue);
            }

            String defaultSourceFileRef = attributes.getValue("defaultSourceFileRef");

            if (defaultSourceFileRef != null) {
                boolean foundRef = false;

                if (sourceFileList != null) {
                    SourceFile sourceFile = sourceFileList.getSourceFile(defaultSourceFileRef);

                    if (sourceFile != null) {
                        run.setDefaultSourceFileRef(sourceFile);
                        foundRef = true;
                    }
                }

                if (!foundRef) {
                    MissingReferenceIssue missingRefIssue = new MissingReferenceIssue(defaultSourceFileRef, "run", "defaultSourceFileRef");
                    missingRefIssue.setIssueLocation(currentContent);
                    missingRefIssue.fixAttemptedByRemovingReference();

                    notifyParserListeners(missingRefIssue);
                }
            }

            String sampleRef = attributes.getValue("sampleRef");

            if (sampleRef != null) {
                boolean foundRef = false;

                if (sampleList != null) {
                    Sample sample = sampleList.getSample(sampleRef);

                    if (sample != null) {
                        foundRef = true;
                        run.setSampleRef(sample);
                    }
                }

                if (!foundRef) {
                    MissingReferenceIssue missingRefIssue = new MissingReferenceIssue(sampleRef, "run", "sampleRef");
                    missingRefIssue.setIssueLocation(currentContent);
                    missingRefIssue.fixAttemptedByRemovingReference();

                    notifyParserListeners(missingRefIssue);
                }
            }

            String startTimeStamp = attributes.getValue("startTimeStamp");

            if (startTimeStamp != null) {
                try {
                    // This should handle the datetime, assuming it is formatted correctly
                    //Calendar dateTime = DatatypeConverter.parseDateTime(startTimeStamp);
                    
                    SimpleDateFormat xmlDateTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    
                    Date date = xmlDateTime.parse(startTimeStamp);
                    Calendar dateTime = xmlDateTime.getCalendar();
                    dateTime.setTime(date);
                    
                    run.setStartTimeStamp(dateTime);
                } catch (ParseException ex) {
                    // Inform validator that invalid timestamp used
                    InvalidFormatIssue formatIssue = new InvalidFormatIssue("startTimeStamp", "yyyy-MM-dd'T'HH:mm:ss", startTimeStamp);
                    formatIssue.setIssueLocation(currentContent);

                    notifyParserListeners(formatIssue); 
                    
                    try {
                        SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz YYYY");
                        Date parsed = format.parse(startTimeStamp);

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(parsed);

                        run.setStartTimeStamp(calendar);
                    } catch (ParseException pe) {
                        //throw new IllegalArgumentException();
                        InvalidFormatIssue secondFormatIssue = new InvalidFormatIssue("startTimeStamp", "EEE MMM dd HH:mm:ss zzz YYYY", startTimeStamp);
                        secondFormatIssue.setIssueLocation(currentContent);

                        notifyParserListeners(secondFormatIssue); 
                    }
                } 
            }

//            try {
                mzML.setRun(run);
//            } catch (NullPointerException ex) {
//                throw new InvalidMzML("<mzML> tag not defined prior to defining <run> tag.");
//            }

            currentContent = run;
        } else if ("spectrumList".equals(qName)) {
            String defaultDataProcessingRef = attributes.getValue("defaultDataProcessingRef");
            DataProcessing dataProcessing = null;

            boolean foundRef = false;

            if (defaultDataProcessingRef != null && dataProcessingList != null) {
                dataProcessing = dataProcessingList.getDataProcessing(defaultDataProcessingRef);

                if (dataProcessing != null) {
                    numberOfSpectra = Integer.parseInt(attributes.getValue("count"));
                    foundRef = true;
                }
            }

            if (!foundRef) {
                MissingReferenceIssue refIssue = new MissingReferenceIssue(defaultDataProcessingRef, "spectrumList", "defaultDataProcessingRef");
                refIssue.setIssueLocation(currentContent);
                refIssue.fixAttemptedByRemovingReference();

                notifyParserListeners(refIssue);

                // msconvert doesn't include default data processing so try and fix it
                //throw new InvalidMzML("No defaultProcessingRef attribute in spectrumList.");
                //spectrumList = new SpectrumList(Integer.parseInt(attributes.getValue("count")), dataProcessingList.getDataProcessing(0));
            }

            spectrumList = new SpectrumList(numberOfSpectra, dataProcessing);

            if (run == null) {
                throw new InvalidMzML("<run> tag not defined prior to defining <spectrumList> tag.");
            }

            run.setSpectrumList(spectrumList);
        } else if ("spectrum".equals(qName)) {
            currentSpectrum = new Spectrum(attributes.getValue("id"), Integer.parseInt(attributes.getValue("defaultArrayLength")));

            String dataProcessingRef = attributes.getValue("dataProcessingRef");

            if (dataProcessingRef != null) {
                boolean foundRef = false;

                if (dataProcessingList != null) {
                    DataProcessing dataProcessing = dataProcessingList.getDataProcessing(dataProcessingRef);

                    if (dataProcessing != null) {
                        currentSpectrum.setDataProcessingRef(dataProcessing);
                        foundRef = true;
                    }
                }

                if (!foundRef) {
                    MissingReferenceIssue refIssue = new MissingReferenceIssue(dataProcessingRef, "spectrum", "dataProcessingRef");
                    refIssue.setIssueLocation(currentContent);
                    refIssue.fixAttemptedByRemovingReference();

                    notifyParserListeners(refIssue);
                }
            } else {
                // If a specific dataProcessingRef has not been defined, then assign the default.
                currentSpectrum.setDataProcessingRef(spectrumList.getDefaultDataProcessingRef());
            }

            String sourceFileRef = attributes.getValue("sourceFileRef");

            if (sourceFileRef != null) {
                boolean foundRef = false;

                if (sourceFileList != null) {
                    SourceFile sourceFile = sourceFileList.getSourceFile(sourceFileRef);

                    if (sourceFile != null) {
                        currentSpectrum.setSourceFileRef(sourceFile);
                        foundRef = true;
                    }
                }

                if (!foundRef) {
                    MissingReferenceIssue refIssue = new MissingReferenceIssue(sourceFileRef, "spectrum", "sourceFileRef");
                    refIssue.setIssueLocation(currentContent);
                    refIssue.fixAttemptedByRemovingReference();

                    notifyParserListeners(refIssue);
                }
            } else {
                currentSpectrum.setSourceFileRef(run.getDefaultSourceFileRef());
            }

            if (attributes.getValue("spotID") != null) {
                currentSpectrum.setSpotID(attributes.getValue("spotID"));
            }

            processingSpectrum = true;

            try {
                spectrumList.addSpectrum(currentSpectrum);
            } catch (NullPointerException ex) {
                throw new InvalidMzML("<spectrumList> tag not defined prior to defining <spectrum> tag.", ex);
            }

            currentContent = currentSpectrum;
        } else if ("scanList".equals(qName)) {
            currentScanList = new ScanList(Integer.parseInt(attributes.getValue("count")));

            try {
                currentSpectrum.setScanList(currentScanList);
            } catch (NullPointerException ex) {
                throw new InvalidMzML("<spectrum> tag not defined prior to defining <scanList> tag.");
            }

            currentContent = currentScanList;
        } else if ("scan".equals(qName)) {
            currentScan = new Scan();

            if (attributes.getValue("externalSpectrumID") != null) {
                currentScan.setExternalSpectrumID(attributes.getValue("externalSpectrumID"));
            }

            String instrumentConfigurationRef = attributes.getValue("instrumentConfigurationRef");

            if (instrumentConfigurationRef != null) {
                InstrumentConfiguration instrumentConfiguration = null;

                try {
                    instrumentConfiguration = instrumentConfigurationList.getInstrumentConfiguration(instrumentConfigurationRef);
                } catch (NullPointerException ex) {
                    throw new InvalidMzML("<instrumentConfigurationList> tag not defined prior to defining <scan> tag.", ex);
                }

                if (instrumentConfiguration != null) {
                    currentScan.setInstrumentConfigurationRef(instrumentConfiguration);
                } else {
                    MissingReferenceIssue refIssue = new MissingReferenceIssue(instrumentConfigurationRef, "scan", "instrumentConfigurationRef");
                    refIssue.setIssueLocation(currentContent);

                    // TODO: Workaround only in place because of ABSciex converter bug where 
                    // the defaultInstrumentConfigurationRef is auto-incremented in every raster
                    // line file but the instrumentConfiguration id remains as 'instrumentConfiguration1'					
                    if (currentInstrumentConfiguration != null) {
                        currentScan.setInstrumentConfigurationRef(currentInstrumentConfiguration);
                        refIssue.fixAttemptedByChangingReference(currentInstrumentConfiguration);
                    } else {
                        refIssue.fixAttemptedByRemovingReference();
                        //throw new InvalidMzML("Can't find instrumentConfigurationRef '" + instrumentConfigurationRef + "' referenced in scan.");
                    }

                    notifyParserListeners(refIssue);
                }
            } else {
                InstrumentConfiguration defaultIC = run.getDefaultInstrumentConfiguration();

                if (defaultIC != null) {
                    currentScan.setInstrumentConfigurationRef(defaultIC);
                }
            }

            String sourceFileRef = attributes.getValue("sourceFileRef");

            if (sourceFileRef != null) {
                boolean foundRef = false;

                if (sourceFileList != null) {
                    SourceFile sourceFile = sourceFileList.getSourceFile(sourceFileRef);

                    if (sourceFile != null) {
                        currentScan.setSourceFileRef(sourceFile);
                        foundRef = true;
                    }
                }

                if (!foundRef) {
                    MissingReferenceIssue refIssue = new MissingReferenceIssue(sourceFileRef, "scan", "sourceFileRef");
                    refIssue.setIssueLocation(currentContent);
                    refIssue.fixAttemptedByRemovingReference();

                    notifyParserListeners(refIssue);
                }
            }

            if (attributes.getValue("spectrumRef") != null) {
                currentScan.setSpectrumRef(attributes.getValue("spectrumRef"));
            }

            try {
                currentScanList.addScan(currentScan);
            } catch (NullPointerException ex) {
                throw new InvalidMzML("<scanList> tag not defined prior to defining <scan> tag.", ex);
            }

            currentContent = currentScan;
        } else if ("scanWindowList".equals(qName)) {
            currentScanWindowList = new ScanWindowList(Integer.parseInt(attributes.getValue("count")));

            try {
                currentScan.setScanWindowList(currentScanWindowList);
            } catch (NullPointerException ex) {
                throw new InvalidMzML("<scan> tag not defined prior to defining <scanWindowList> tag.", ex);
            }
        } else if ("scanWindow".equals(qName)) {
            ScanWindow scanWindow = new ScanWindow();

            try {
                currentScanWindowList.addScanWindow(scanWindow);
            } catch (NullPointerException ex) {
                throw new InvalidMzML("<scanWindowList> tag not defined prior to defining <scanWindow> tag.", ex);
            }

            currentContent = scanWindow;
        } else if ("precursorList".equals(qName)) {
            currentPrecursorList = new PrecursorList(Integer.parseInt(attributes.getValue("count")));

            try {
                currentSpectrum.setPrecursorList(currentPrecursorList);
            } catch (NullPointerException ex) {
                throw new InvalidMzML("<spectrum> tag not defined prior to defining <precursorList> tag.", ex);
            }
        } else if ("precursor".equals(qName)) {
            processingPrecursor = true;
            currentPrecursor = new Precursor();

            String sourceFileRef = attributes.getValue("sourceFileRef");

            if (sourceFileRef != null) {
                boolean foundRef = false;

                if (sourceFileList != null) {
                    SourceFile sourceFile = sourceFileList.getSourceFile(sourceFileRef);

                    if (sourceFile != null) {
                        foundRef = true;
                        currentPrecursor.setExternalSpectrum(sourceFile, attributes.getValue("externalSpectrumID"));
                    }
                }

                if (!foundRef) {
                    MissingReferenceIssue refIssue = new MissingReferenceIssue(sourceFileRef, "precursor", "sourceFileRef");
                    refIssue.setIssueLocation(currentContent);
                    refIssue.fixAttemptedByRemovingReference();

                    notifyParserListeners(refIssue);
                }
            }

            if (attributes.getValue("spectrumRef") != null) {
                currentPrecursor.setSpectrumRef(spectrumList.getSpectrum(attributes.getValue("spectrumRef")));
            }

            if (processingSpectrum) {
                try {
                    currentPrecursorList.addPrecursor(currentPrecursor);
                } catch (NullPointerException ex) {
                    throw new InvalidMzML("<precursorList> tag not defined prior to defining <precursor> tag.", ex);
                }
            } else if (processingChromatogram) {
                try {
                    currentChromatogram.setPrecursor(currentPrecursor);
                } catch (NullPointerException ex) {
                    throw new InvalidMzML("<chromatogram> tag not defined prior to defining <precursor> tag.", ex);
                }
            }
        } else if ("isolationWindow".equals(qName)) {
            IsolationWindow isolationWindow = new IsolationWindow();

            if (processingPrecursor) {
                try {
                    currentPrecursor.setIsolationWindow(isolationWindow);
                } catch (NullPointerException ex) {
                    throw new InvalidMzML("<precursor> tag not defined prior to defining <isolationWindow> tag.", ex);
                }
            } else if (processingProduct) {
                try {
                    currentProduct.setIsolationWindow(isolationWindow);
                } catch (NullPointerException ex) {
                    throw new InvalidMzML("<product> tag not defined prior to defining <isolationWindow> tag.", ex);
                }
            }

            currentContent = isolationWindow;
        } else if ("selectedIonList".equals(qName)) {
            currentSelectedIonList = new SelectedIonList(Integer.parseInt(attributes.getValue("count")));

            try {
                currentPrecursor.setSelectedIonList(currentSelectedIonList);
            } catch (NullPointerException ex) {
                throw new InvalidMzML("<precursor> tag not defined prior to defining <selectedIonList> tag.", ex);
            }
        } else if ("selectedIon".equals(qName)) {
            SelectedIon selectedIon = new SelectedIon();

            try {
                currentSelectedIonList.addSelectedIon(selectedIon);
            } catch (NullPointerException ex) {
                throw new InvalidMzML("<selectedIonList> tag not defined prior to defining <selectedIon> tag.", ex);
            }

            currentContent = selectedIon;
        } else if ("activation".equals(qName)) {
            Activation activation = new Activation();

            try {
                currentPrecursor.setActivation(activation);
            } catch (NullPointerException ex) {
                throw new InvalidMzML("<precursor> tag not defined prior to defining <activation> tag.", ex);
            }

            currentContent = activation;
        } else if ("productList".equals(qName)) {
            currentProductList = new ProductList(Integer.parseInt(attributes.getValue("count")));

            try {
                currentSpectrum.setProductList(currentProductList);
            } catch (NullPointerException ex) {
                throw new InvalidMzML("<spectrum> tag not defined prior to defining <productList> tag.", ex);
            }
        } else if ("product".equals(qName)) {
            processingProduct = true;
            currentProduct = new Product();

            if (processingSpectrum) {
                try {
                    currentProductList.addProduct(currentProduct);
                } catch (NullPointerException ex) {
                    throw new InvalidMzML("<productList> tag not defined prior to defining <product> tag.", ex);
                }
            } else if (processingChromatogram) {
                try {
                    currentChromatogram.setProduct(currentProduct);
                } catch (NullPointerException ex) {
                    throw new InvalidMzML("<chromatogram> tag not defined prior to defining <product> tag.", ex);
                }
            }
        } else if ("binaryDataArrayList".equals(qName)) {
            currentBinaryDataArrayList = new BinaryDataArrayList(Integer.parseInt(attributes.getValue("count")));

            if (processingSpectrum) {
                try {
                    currentSpectrum.setBinaryDataArrayList(currentBinaryDataArrayList);
                } catch (NullPointerException ex) {
                    throw new InvalidMzML("<spectrum> tag not defined prior to defining <binaryDataArrayList> tag.", ex);
                }
            } else if (processingChromatogram) {
                try {
                    currentChromatogram.setBinaryDataArrayList(currentBinaryDataArrayList);
                } catch (NullPointerException ex) {
                    throw new InvalidMzML("<chromatogram> tag not defined prior to defining <binaryDataArrayList> tag.", ex);
                }
            }
        } else if ("binaryDataArray".equals(qName)) {
            currentBinaryDataArray = new BinaryDataArray(Integer.parseInt(attributes.getValue("encodedLength")));

            if (attributes.getValue("arrayLength") != null) {
                currentBinaryDataArray.setArrayLength(Integer.parseInt(attributes.getValue("arrayLength")));
            }

            String dataProcessingRef = attributes.getValue("dataProcessingRef");

            if (dataProcessingRef != null) {
                boolean foundRef = false;
                DataProcessing dataProcessing = null;

                if (dataProcessingList != null) {
                    dataProcessing = dataProcessingList.getDataProcessing(dataProcessingRef);

                    if (dataProcessing != null) {
                        foundRef = true;
                        currentBinaryDataArray.setDataProcessingRef(dataProcessing);
                    }
                }

                if (!foundRef) {
                    MissingReferenceIssue refIssue = new MissingReferenceIssue(dataProcessingRef, "binaryDataArray", "dataProcessingRef");
                    refIssue.setIssueLocation(currentContent);
                    refIssue.fixAttemptedByRemovingReference();

                    notifyParserListeners(refIssue);
                }
            }

            try {
                currentBinaryDataArrayList.addBinaryDataArray(currentBinaryDataArray);
            } catch (NullPointerException ex) {
                throw new InvalidMzML("<binaryDataArrayList> tag not defined prior to defining <binaryDataArray> tag.", ex);
            }

            currentContent = currentBinaryDataArray;
        } else if ("binary".equals(qName)) {
            // Ignore binary data for the header
        } else if ("chromatogramList".equals(qName)) {
            String defaultDataProcessingRef = attributes.getValue("defaultDataProcessingRef");

            if (defaultDataProcessingRef != null) {
                DataProcessing dataProcessing = null;

                try {
                    dataProcessing = dataProcessingList.getDataProcessing(defaultDataProcessingRef);
                } catch (NullPointerException ex) {
                    throw new InvalidMzML("<dataProcessingList> tag not defined prior to defining <chromatogramList> tag.", ex);
                }

                if (dataProcessing != null) {
                    chromatogramList = new ChromatogramList(Integer.parseInt(attributes.getValue("count")), dataProcessing);
                } else {
                    throw new InvalidMzML("Can't find defaultDataProcessingRef '" + defaultDataProcessingRef + "' referenced by chromatogramList.");
                }
            } else {
                // msconvert doesn't include default data processing so try and fix it				
                throw new InvalidMzML("No defaultProcessingRef attribute in chromatogramList.");

                //chromatogramList = new ChromatogramList(Integer.parseInt(attributes.getValue("count")), dataProcessingList.getDataProcessing(0));
            }

            try {
                run.setChromatogramList(chromatogramList);
            } catch (NullPointerException ex) {
                throw new InvalidMzML("<run> tag not defined prior to defining <chromatogramList> tag.", ex);
            }
        } else if ("chromatogram".equals(qName)) {
            processingChromatogram = true;
            currentChromatogram = new Chromatogram(attributes.getValue("id"), Integer.parseInt(attributes.getValue("defaultArrayLength")));

            String dataProcessingRef = attributes.getValue("dataProcessingRef");

            if (dataProcessingRef != null) {
                DataProcessing dataProcessing = null;

                try {
                    dataProcessing = dataProcessingList.getDataProcessing(dataProcessingRef);
                } catch (NullPointerException ex) {
                    throw new InvalidMzML("<dataProcessingList> tag not defined prior to defining <chromatogram> tag.", ex);
                }

                if (dataProcessing != null) {
                    currentChromatogram.setDataProcessingRef(dataProcessing);
                } else {
                    throw new InvalidMzML("Can't find dataProcessingRef '" + dataProcessingRef + "' referenced by chromatogram '" + currentChromatogram.getID() + "'.");
                }
            } else {
                currentChromatogram.setDataProcessingRef(chromatogramList.getDefaultDataProcessingRef());
            }

            try {
                chromatogramList.addChromatogram(currentChromatogram);
            } catch (NullPointerException ex) {
                throw new InvalidMzML("<chromatogramList> tag not defined prior to defining <chromatogram> tag.");
            }

            currentContent = currentChromatogram;
        } else if ("offset".equals(qName) || "indexListOffset".equals(qName)) {
            previousOffsetIDRef = currentOffsetIDRef;

//            logger.log(Level.INFO, "Current qName: {0} - {1}", new String[] {qName, attributes.getValue("idRef")});
            if ("offset".equals(qName)) {
                this.currentOffsetIDRef = attributes.getValue("idRef");
            }

            offsetData.setLength(0);
            processingOffset = true;
        } else if ("index".equals(qName)) {
            if (attributes.getValue("name").equals("chromatogram")) {
                this.processingChromatogram = true;
//                this.processingSpectrum = false;
            } else {
                this.processingSpectrum = true;
//                this.processingChromatogram = false;
            }
        } else if ("indexedmzML".equals(qName) || "indexList".equals(qName) || "indexListOffset".equals(qName)) {
        } else {
            logger.log(Level.FINEST, "No processing for tag <{0}>", qName);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (processingOffset) {
            offsetData.append(ch, start, length);
        }
    }

    protected MzMLDataContainer getDataContainer() {
        MzMLDataContainer dataContainer = null;

        // There is probably a better way to do this - store a HashMap of IDs and 
        // locations, then at the end of the file, sort the HashMap by location
        // and then assign the DataLocation
        if (processingSpectrum) {
            dataContainer = spectrumList.getSpectrum(previousOffsetIDRef);

            if (dataContainer == null) {
                dataContainer = spectrumList.getSpectrum(spectrumList.size() - 1);
            }
        } else {
            dataContainer = chromatogramList.getChromatogram(previousOffsetIDRef);

            if (dataContainer == null) {
                dataContainer = chromatogramList.getChromatogram(chromatogramList.size() - 1);
            }
        }

        return dataContainer;
    }

    protected long getOffset() {
        return Long.parseLong(offsetData.toString());
    }

    protected void setDataContainer(MzMLDataContainer dataContainer, long offset) {
        if (previousOffset != -1 && openDataStorage && dataContainer != null) {
            DataLocation dataLocation = new DataLocation(dataStorage, previousOffset, (int) (offset - previousOffset));
            
            dataContainer.setDataLocation(dataLocation);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if ("spectrum".equals(qName)) {
            processingSpectrum = false;
            
            // Try and tidy up spectrum
            CVParam cvParam = currentSpectrum.getCVParamOrChild("MS:1000294");
            if(currentSpectrum.containsCVParam(cvParam)) {
//                System.out.println("Tidying up to be done.." + referenceableParamGroupList);
                
                ReferenceableParamGroup bestGroup = currentSpectrum.findBestFittingRPG(referenceableParamGroupList);
                
                if(bestGroup == null) {
//                    System.out.println("No group matches so far..");
                    // TODO: Give it a better name
                    bestGroup = new ReferenceableParamGroup();
                    
                    if(referenceableParamGroupList == null) {
                        referenceableParamGroupList = new ReferenceableParamGroupList(1);
                        mzML.setReferenceableParamGroupList(referenceableParamGroupList);
                    }
                    
                    referenceableParamGroupList.add(bestGroup);
                    
                    System.out.println("New group created: " + bestGroup);
                    
                    bestGroup.addCVParam(currentSpectrum.getCVParamOrChild("MS:1000294"));
                    bestGroup.addCVParam(currentSpectrum.getCVParamOrChild("MS:1000511")); // ms level
                    bestGroup.addCVParam(currentSpectrum.getCVParamOrChild(Spectrum.scanPolarityID));
                    bestGroup.addCVParam(currentSpectrum.getCVParamOrChild("MS:1000525")); // spectrum representation
                }
                
//                System.out.println("Replacing group..." + bestGroup);
                currentSpectrum.replaceCVParamsWithRPG(bestGroup);
            }
        } else if ("scan".equals(qName)) {            
            // Try and tidy up scan
            CVParam cvParam = currentScan.getCVParamOrChild("MS:1000616");
            if(currentScan.containsCVParam(cvParam)) {
                ReferenceableParamGroup bestGroup = currentScan.findBestFittingRPG(referenceableParamGroupList);
                
                if(bestGroup == null) {
//                    System.out.println("No group matches so far..");
                    // TODO: Give it a better name
                    bestGroup = new ReferenceableParamGroup();
                    
                    if(referenceableParamGroupList == null) {
                        referenceableParamGroupList = new ReferenceableParamGroupList(1);
                        mzML.setReferenceableParamGroupList(referenceableParamGroupList);
                    }
                    
                    referenceableParamGroupList.add(bestGroup);
                    
                    System.out.println("New group created: " + bestGroup);
                    
                    bestGroup.addCVParam(currentScan.getCVParamOrChild("MS:1000512"));
                    bestGroup.addCVParam(currentScan.getCVParamOrChild("MS:1000616"));
                    bestGroup.addCVParam(currentScan.getCVParamOrChild("MS:1000927"));
                }
                
                currentScan.replaceCVParamsWithRPG(bestGroup);
            }
        } else if("scanWindow".equals(qName)) {
            if (currentContent instanceof MzMLContentWithParams) {
                CVParam cvParam = ((MzMLContentWithParams) currentContent).getCVParamOrChild("MS:1000501");
                
                if(((MzMLContentWithParams) currentContent).containsCVParam(cvParam)) {
                    ReferenceableParamGroup bestGroup = ((MzMLContentWithParams) currentContent).findBestFittingRPG(referenceableParamGroupList);
                    
                    if(bestGroup == null) {
    //                    System.out.println("No group matches so far..");
                        // TODO: Give it a better name
                        bestGroup = new ReferenceableParamGroup();

                        if(referenceableParamGroupList == null) {
                            referenceableParamGroupList = new ReferenceableParamGroupList(1);
                            mzML.setReferenceableParamGroupList(referenceableParamGroupList);
                        }

                        referenceableParamGroupList.add(bestGroup);

                        System.out.println("New scanWindow group created: " + bestGroup);

                        bestGroup.addCVParam(((MzMLContentWithParams) currentContent).getCVParamOrChild("MS:1000501"));
                        bestGroup.addCVParam(((MzMLContentWithParams) currentContent).getCVParamOrChild("MS:1000500"));
                    }

                    ((MzMLContentWithParams) currentContent).replaceCVParamsWithRPG(bestGroup);
                }
            }
        } else if ("chromatogram".equals(qName)) {
            processingChromatogram = false;
        } else if ("precursor".equals(qName)) {
            processingPrecursor = false;
        } else if ("product".equals(qName)) {
            processingProduct = false;
//        } else if ("binaryDataArrayList".equals(qName)) {
//            currentBinaryDataArrayList.updatemzAndIntensityArray();
        } else if ("offset".equals(qName) || "indexListOffset".equals(qName)) {
            long offset = getOffset();

            MzMLDataContainer dataContainer = getDataContainer();

            if (processingSpectrum && processingChromatogram) {
                processingSpectrum = false;
            }
            
            setDataContainer(dataContainer, offset);

            previousOffset = offset;
            processingOffset = false;
//                case "mzML":
//                    for(Spectrum curSpectrum : spectrumList) {
//                        System.out.println(curSpectrum.getDataLocation());
//                        try {
//                            curSpectrum.getmzArray();
//                        } catch (IOException ex) {
//                            Logger.getLogger(MzMLHeaderHandler.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//                    }
//                    
//                    break;
        } else if("mzML".equals(qName) || "indexedmzML".equals(qName)) {
            // Go through spectra and chromatograms to convert the data storage if necessary
            if(spectrumList != null) {
                for(Spectrum spectrum : spectrumList) {
                    try {
                        spectrum.ensureLoadableData();
                    } catch (IOException ex) {
                        Logger.getLogger(MzMLHeaderHandler.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

//	public int getSpectrumCount() {
//		return spectrumCount;
//	}
    public MzML getmzML() {
        return mzML;
    }
}
