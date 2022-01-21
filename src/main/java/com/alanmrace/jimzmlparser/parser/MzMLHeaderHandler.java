package com.alanmrace.jimzmlparser.parser;

import com.alanmrace.jimzmlparser.data.MzMLSpectrumDataStorage;
import com.alanmrace.jimzmlparser.data.DataLocation;
import com.alanmrace.jimzmlparser.data.DataStorage;
import com.alanmrace.jimzmlparser.exceptions.CVParamAccessionNotFoundIssue;
import com.alanmrace.jimzmlparser.exceptions.FatalParseIssue;
import com.alanmrace.jimzmlparser.exceptions.FatalRuntimeParseException;
import com.alanmrace.jimzmlparser.exceptions.InvalidFormatIssue;
import com.alanmrace.jimzmlparser.exceptions.MissingReferenceIssue;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.alanmrace.jimzmlparser.mzml.*;
import com.alanmrace.jimzmlparser.obo.OBO;
import com.alanmrace.jimzmlparser.obo.OBOTerm;
import com.alanmrace.jimzmlparser.exceptions.InvalidMzMLIssue;
import com.alanmrace.jimzmlparser.exceptions.Issue;
import com.alanmrace.jimzmlparser.exceptions.MzMLParseException;
import com.alanmrace.jimzmlparser.exceptions.NonFatalParseException;
import com.alanmrace.jimzmlparser.exceptions.NonFatalParseIssue;
import com.alanmrace.jimzmlparser.exceptions.ObsoleteTermUsed;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.channels.Channels;
import java.util.logging.Level;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * SAX parser for mzML files, only focusing on metadata.
 *
 * <p>
 * All metadata are parsed, however the data is ignored. This allows a lower
 * memory usage for loading metadata, while also allowing code reuse for both
 * MzML and ImzML files.
 *
 * @author Alan Race
 */
public class MzMLHeaderHandler extends DefaultHandler {

    /**
     * Name of the XML attribute where the CV parameter accession number is stored.
     */
    static final String ACCESSION_ATTRIBUTE_NAME = "accession";
    /**
     * Name of the XML attribute where the CV parameter value is stored.
     */
    static final String VALUE_ATTRIBUTE_NAME = "value";
    /**
     * Name of the XML attribute where the unit CV accession number is stored.
     */
    private static final String UNIT_ACCESSION_ATTRIBUTE_NAME = "unitAccession";
    /**
     * Name of the XML attribute where the ID of a tag is stored.
     */
    static final String ID_ATTRIBUTE_NAME = "id";
    /**
     * Name of the XML attribute where the count of a list is stored.
     */
    private static final String COUNT_ATTRIBUTE_NAME = "count";

    /**
     * Class logger.
     */
    private static final Logger LOGGER = Logger.getLogger(MzMLHeaderHandler.class.getName());

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
    ScanSettings currentScanSettings;

    /**
     * Current SourceFileRefList created each time a
     * {@literal <sourceFileRefList>} tag is encountered.
     */
    private SourceFileRefList currentSourceFileRefList;

    /**
     * Current TargetList created each time a {@literal <targetList>} tag is
     * encountered.
     */
    private TargetList currentTargetList;

    /**
     * InstrumentConfigurationList associated with the MzML that is currently
     * being parsed.
     */
    protected InstrumentConfigurationList instrumentConfigurationList;

    /**
     * Current InstrumentConfiguration created each time a
     * {@literal <instrumentConfiguration>} tag is encountered.
     */
    private InstrumentConfiguration currentInstrumentConfiguration;

    /**
     * Current ComponentList (can consist of Source, Analyzer and Detector)
     * created each time a {@literal <instrumentConfiguration>} tag is
     * encountered.
     */
    private ComponentList currentComponentList;

    /**
     * DataProcessingList associated with the MzML that is currently being
     * parsed.
     */
    private DataProcessingList dataProcessingList;

    /**
     * Current DataProcessing created each time a {@literal <dataProcessing>}
     * tag is encountered.
     */
    private DataProcessing currentDataProcessing;

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
    private Spectrum currentSpectrum;

    /**
     * Current ScanList created each time a {@literal <scanList>} tag is
     * encountered.
     */
    private ScanList currentScanList;

    /**
     * Current Scan created each time a {@literal <scan>} tag is encountered.
     */
    Scan currentScan;

    /**
     * Current ScanWindowList created each time a {@literal <scanWindowList>}
     * tag is encountered.
     */
    private ScanWindowList currentScanWindowList;

    /**
     * Current PrecursorList created each time a {@literal <precursorList>} tag
     * is encountered.
     */
    private PrecursorList currentPrecursorList;

    /**
     * Current Precursor created each time a {@literal <precursor>} tag is
     * encountered.
     */
    private Precursor currentPrecursor;

    /**
     * Current SelectedIonList created each time a {@literal <selectedIonList>}
     * tag is encountered.
     */
    private SelectedIonList currentSelectedIonList;

    /**
     * Current ProductList created each time a {@literal <productList>} tag is
     * encountered.
     */
    private ProductList currentProductList;

    /**
     * Current Product created each time a {@literal <product>} tag is
     * encountered.
     */
    private Product currentProduct;

    /**
     * Current BinaryDataArrayList created each time a
     * {@literal <binaryDataArrayList>} tag is encountered.
     */
    private BinaryDataArrayList currentBinaryDataArrayList;

    /**
     * Current BinaryDataArray created each time a {@literal <binaryDataArray>}
     * tag is encountered.
     */
    BinaryDataArray currentBinaryDataArray;

    /**
     * ChromatogramList associated with the current MzML being parsed.
     */
    private ChromatogramList chromatogramList;

    /**
     * Current Chromatogram created each time a {@literal <chromatogram>} tag is
     * encountered.
     */
    private Chromatogram currentChromatogram;

    /**
     * Current tag.
     */
    //protected MzMLContent currentContent;

    protected Stack<MzMLContent> contentStack = new Stack<MzMLContent>();

    // Flags for tags that share the same sub-tags
    /**
     * True after {@literal <spectrum>} tag and before the end tag has been
     * encountered.
     */
    private boolean processingSpectrum;

    /**
     * True after {@literal <chromatogram>} tag and before the end tag has been
     * encountered.
     */
    private boolean processingChromatogram;

    /**
     * True after {@literal <precursor>} tag and before the end tag has been
     * encountered.
     */
    private boolean processingPrecursor;

    /**
     * True after {@literal <product>} tag and before the end tag has been
     * encountered.
     */
    private boolean processingProduct;

    /**
     * True after {@literal <offset>} tag and before the end tag has been
     * encountered.
     */
    private boolean processingOffset;

    /**
     * StringBuffer containing the contents between the {@literal <offset>} and
     * {@literal </offset>} tags.
     */
    private StringBuffer offsetData;

    private String previousOffsetIDRef;
    private String currentOffsetIDRef;

    private long previousOffset = -1;

    DataStorage dataStorage;
    private boolean openDataStorage = true;

    int numberOfSpectra = 0;

    private List<ParserListener> listeners;

    /**
     * Set up a SAX parser for MzML metadata with the specified ontology
     * dictionary.
     *
     * @param obo Ontology database
     */
    MzMLHeaderHandler(OBO obo) {
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

    /**
     * Set whether the data file should be opened and remain open after parsing is complete.
     *
     * @param openDataStorage true if data file should be opened, false if it should be closed.
     */
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
        
        LOGGER.log(Level.FINER, "Registered listener {0}: {1}", new Object[] {listener, Arrays.toString(listeners.toArray())});
    }

    protected void notifyParserListeners(Issue issue) {
        LOGGER.log(Level.FINER, "Notifying {0} listeners about the issue {1}", new Object[] {Arrays.toString(listeners.toArray()), issue});
        
        for (ParserListener listener : listeners) {
            listener.issueFound(issue);
        }
    }

    public static MzML parsemzMLHeader(String filename) throws MzMLParseException {
        return parsemzMLHeader(filename, true);
    }

    public static MzML parsemzMLHeader(String filename, boolean openDataFile) throws MzMLParseException {
        return parsemzMLHeader(filename, openDataFile, null);
    }

    public static MzML parsemzMLHeader(String filename, ParserListener listener) throws MzMLParseException {
        return parsemzMLHeader(filename, true, listener);
    }

    public static MzML parsemzMLHeader(String filename, boolean openDataFile, ParserListener listener) throws MzMLParseException {
        OBO obo = OBO.getOBO();

        RandomAccessFile raf = null;
        InputStream is = null;
        MzMLHeaderHandler handler;

        try {
            // Parse mzML
            handler = new MzMLHeaderHandler(obo, new File(filename), openDataFile);
            handler.setOpenDataStorage(openDataFile);

            if (listener != null) {
                handler.registerParserListener(listener);
            }

            SAXParserFactory spf = SAXParserFactory.newInstance();

            // TODO: INDEXED RAF when reading!!!
            raf = new RandomAccessFile(filename, "r");
            is = Channels.newInputStream(raf.getChannel());

            //get a new instance of parser
            SAXParser sp = spf.newSAXParser();

            //parse the file and also register this class for call backs
            sp.parse(is, handler);

            handler.getmzML().setOBO(obo);

        } catch (FatalRuntimeParseException runtimeException) {
            throw new MzMLParseException(runtimeException.getIssue(), runtimeException);
        } catch (SAXException ex) {
            LOGGER.log(Level.SEVERE, null, ex);

            throw new MzMLParseException(new InvalidMzMLIssue("SAXException: " + ex, ex.getLocalizedMessage()), ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MzMLHeaderHandler.class.getName()).log(Level.SEVERE, null, ex);

            throw new MzMLParseException(new FatalParseIssue("File not found: " + filename, ex.getLocalizedMessage()), ex);
        } catch (IOException ex) {
            Logger.getLogger(MzMLHeaderHandler.class.getName()).log(Level.SEVERE, null, ex);

            throw new MzMLParseException(new FatalParseIssue("IOException: " + ex, ex.getLocalizedMessage()), ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(MzMLHeaderHandler.class.getName()).log(Level.SEVERE, null, ex);

            throw new MzMLParseException(new FatalParseIssue("ParserConfigurationException: " + ex, ex.getLocalizedMessage()), ex);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                    Logger.getLogger(MzMLHeaderHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (raf != null) {
                try {
                    raf.close();
                } catch (IOException ex) {
                    Logger.getLogger(MzMLHeaderHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return handler.getmzML();
    }

    protected int getCountAttribute(Attributes attributes) {
        String countString = attributes.getValue(COUNT_ATTRIBUTE_NAME);
        int count;

        if (countString == null) {
            count = 0;
        } else {
            count = Integer.parseInt(countString);
        }

        return count;
    }

    protected void startCVParam(Attributes attributes) {
        if (contentStack.peek() != null) {
            String accession = attributes.getValue(ACCESSION_ATTRIBUTE_NAME);

            OBOTerm term = obo.getTerm(accession);

            // If the term does not exist within any OBO, convert to UserParam for the sake of continuing parsing
            // Notify listeners of the fact that an issue occured and we attempted to resolve it
            if (term == null) {
                UserParam userParam = new UserParam(attributes.getValue(ACCESSION_ATTRIBUTE_NAME), attributes.getValue(VALUE_ATTRIBUTE_NAME), obo.getTerm(attributes.getValue(UNIT_ACCESSION_ATTRIBUTE_NAME)));
                ((MzMLContentWithParams) contentStack.peek()).addUserParam(userParam);

                CVParamAccessionNotFoundIssue notFound = new CVParamAccessionNotFoundIssue(attributes.getValue(ACCESSION_ATTRIBUTE_NAME), userParam);

                notFound.setIssueLocation(contentStack.peek());

                notifyParserListeners(notFound);
            } else {
                if (term.isObsolete()) {
                    ObsoleteTermUsed obsoleteIssue = new ObsoleteTermUsed(term);
                    obsoleteIssue.setIssueLocation(contentStack.peek());

                    notifyParserListeners(obsoleteIssue);
                }

                try {
                    CVParam.CVParamType paramType = CVParam.getCVParamType(term);
                    CVParam cvParam;

                    String value = attributes.getValue(VALUE_ATTRIBUTE_NAME);
                    OBOTerm units = obo.getTerm(attributes.getValue(UNIT_ACCESSION_ATTRIBUTE_NAME));

                    try {
                        switch (paramType) {
                            case STRING:
                                cvParam = new StringCVParam(term, value, units);
                                break;
                            case EMPTY:
                                cvParam = new EmptyCVParam(term, units);

                                if (value != null && !value.isEmpty()) {
                                    InvalidFormatIssue formatIssue = new InvalidFormatIssue(term, attributes.getValue(VALUE_ATTRIBUTE_NAME));
                                    formatIssue.setIssueLocation(contentStack.peek());

                                    notifyParserListeners(formatIssue);
                                }
                                break;
                            case LONG:
                                cvParam = new LongCVParam(term, Long.parseLong(value), units);
                                break;
                            case DOUBLE:
                                cvParam = new DoubleCVParam(term, Double.parseDouble(value), units);
                                break;
                            case BOOLEAN:
                                cvParam = new BooleanCVParam(term, Boolean.parseBoolean(value), units);
                                break;
                            case INTEGER:
                                cvParam = new IntegerCVParam(term, Integer.parseInt(value), units);
                                break;
                            default:
                                cvParam = new StringCVParam(term, attributes.getValue(VALUE_ATTRIBUTE_NAME), obo.getTerm(attributes.getValue(UNIT_ACCESSION_ATTRIBUTE_NAME)));

                                InvalidFormatIssue formatIssue = new InvalidFormatIssue(term, paramType);
                                formatIssue.fixAttemptedByChangingType((StringCVParam) cvParam);
                                formatIssue.setIssueLocation(contentStack.peek());
                                notifyParserListeners(formatIssue);

                                break;
                        }
                    } catch (NumberFormatException nfe) {
                        cvParam = new StringCVParam(term, attributes.getValue(VALUE_ATTRIBUTE_NAME), obo.getTerm(attributes.getValue(UNIT_ACCESSION_ATTRIBUTE_NAME)));

                        InvalidFormatIssue formatIssue = new InvalidFormatIssue(term, attributes.getValue(VALUE_ATTRIBUTE_NAME));
                        formatIssue.fixAttemptedByChangingType((StringCVParam) cvParam);
                        formatIssue.setIssueLocation(contentStack.peek());

                        notifyParserListeners(formatIssue);
                    }

                    if (contentStack.peek() instanceof MzMLContentWithParams) {
                        ((MzMLContentWithParams) contentStack.peek()).addCVParam(cvParam);
                    } else {
                        throw new RuntimeException("Failure to add CVParam to " + contentStack.peek());
                    }
                } catch (NonFatalParseException ex) {
                    NonFatalParseIssue issue = ex.getIssue();

                    issue.setIssueLocation(contentStack.peek());

                    notifyParserListeners(issue);

                    LOGGER.log(Level.FINE, issue.getIssueMessage(), ex);
                }
            }
        } else {
            throw new FatalRuntimeParseException(new InvalidMzMLIssue("<cvParam> tag without a parent."));
        }
    }

    protected void startReferenceableParamGroupRef(Attributes attributes) {
        boolean foundReference = false;

        if (referenceableParamGroupList != null) {
            ReferenceableParamGroup group = referenceableParamGroupList.getReferenceableParamGroup(attributes.getValue("ref"));

            if (group != null) {
                ReferenceableParamGroupRef ref = new ReferenceableParamGroupRef(group);

                if (contentStack.peek() != null) {
                    foundReference = true;

                    ((MzMLContentWithParams) contentStack.peek()).addReferenceableParamGroupRef(ref);
                }
            }
        }

        if (!foundReference) {
            MissingReferenceIssue missingRefIssue = new MissingReferenceIssue(attributes.getValue("ref"), "referenceableParamGroupRef", "ref");
            missingRefIssue.setIssueLocation(contentStack.peek());
            missingRefIssue.fixAttemptedByRemovingReference();

            notifyParserListeners(missingRefIssue);
        }
    }

    protected void startRun(Attributes attributes) {
        String instrumentConfigurationRef = attributes.getValue("defaultInstrumentConfigurationRef");

        InstrumentConfiguration instrumentConfiguration = null;

        if (instrumentConfigurationRef != null && !instrumentConfigurationRef.isEmpty()) {
            try {
                instrumentConfiguration = instrumentConfigurationList.getInstrumentConfiguration(instrumentConfigurationRef);
            } catch (NullPointerException ex) {
                throw new FatalRuntimeParseException(new InvalidMzMLIssue("<instrumentConfigurationList> tag not defined prior to defining <run> tag.", ex.getLocalizedMessage()), ex);
            }
        }

        if (instrumentConfiguration != null) {
            run = new Run(attributes.getValue(ID_ATTRIBUTE_NAME), instrumentConfiguration);
        } else {
            MissingReferenceIssue missingRefIssue = new MissingReferenceIssue(instrumentConfigurationRef, "run", "defaultInstrumentConfigurationRef");
            missingRefIssue.setIssueLocation(contentStack.peek());

            // TODO: Workaround only in place because of ABSciex converter bug where 
            // the defaultInstrumentConfigurationRef is auto-incremented in every raster
            // line file but the instrumentConfiguration id remains as 'instrumentConfiguration1'					
            if (currentInstrumentConfiguration != null) {
                missingRefIssue.fixAttemptedByChangingReference(currentInstrumentConfiguration);

                run = new Run(attributes.getValue(ID_ATTRIBUTE_NAME), currentInstrumentConfiguration);
            } else {
                missingRefIssue.fixAttemptedByRemovingReference();

                run = new Run(attributes.getValue(ID_ATTRIBUTE_NAME), null);
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
                missingRefIssue.setIssueLocation(contentStack.peek());
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
                missingRefIssue.setIssueLocation(contentStack.peek());
                missingRefIssue.fixAttemptedByRemovingReference();

                notifyParserListeners(missingRefIssue);
            }
        }

        String startTimeStamp = attributes.getValue("startTimeStamp");

        if (startTimeStamp != null) {
            try {
                // This should handle the datetime, assuming it is formatted correctly
                SimpleDateFormat xmlDateTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

                Date date = xmlDateTime.parse(startTimeStamp);
                Calendar dateTime = xmlDateTime.getCalendar();
                dateTime.setTime(date);

                run.setStartTimeStamp(dateTime);
            } catch (ParseException ex) {
                // Inform validator that invalid timestamp used
                InvalidFormatIssue formatIssue = new InvalidFormatIssue("startTimeStamp", "yyyy-MM-dd'T'HH:mm:ss", startTimeStamp);
                formatIssue.setIssueLocation(contentStack.peek());

                notifyParserListeners(formatIssue);

                try {
                    SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
                    Date parsed = format.parse(startTimeStamp);

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(parsed);

                    run.setStartTimeStamp(calendar);
                } catch (ParseException pe) {
                    InvalidFormatIssue secondFormatIssue = new InvalidFormatIssue("startTimeStamp", "EEE MMM dd HH:mm:ss zzz yyyy", startTimeStamp);
                    secondFormatIssue.setIssueLocation(contentStack.peek());

                    notifyParserListeners(secondFormatIssue);
                }
            }
        }

        mzML.setRun(run);

        contentStack.push(run);
    }

    protected void startInstrumentConfiguration(Attributes attributes) {
        currentInstrumentConfiguration = new InstrumentConfiguration(attributes.getValue(ID_ATTRIBUTE_NAME));

        String scanSettingsRef = attributes.getValue("scanSettingsRef");

        if (scanSettingsRef != null) {
            ScanSettings scanSettings = scanSettingsList.getScanSettings(scanSettingsRef);

            if (scanSettings != null) {
                currentInstrumentConfiguration.setScanSettingsRef(scanSettings);
            } else {
                MissingReferenceIssue missingRefIssue = new MissingReferenceIssue(scanSettingsRef, "instrumentConfiguration", "scanSettingsRef");
                missingRefIssue.setIssueLocation(contentStack.peek());
                missingRefIssue.fixAttemptedByRemovingReference();

                notifyParserListeners(missingRefIssue);
            }
        }

        try {
            instrumentConfigurationList.addInstrumentConfiguration(currentInstrumentConfiguration);
        } catch (NullPointerException ex) {
            throw new FatalRuntimeParseException(new InvalidMzMLIssue("<instrumentConfigurationList> tag not defined prior to defining <instrumentConfiguration> tag.", ex.getLocalizedMessage()), ex);
        }

        contentStack.push(currentInstrumentConfiguration);
    }

    protected void startScan(Attributes attributes) {
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
                throw new FatalRuntimeParseException(new InvalidMzMLIssue("<instrumentConfigurationList> tag not defined prior to defining <scan> tag.", ex.getLocalizedMessage()), ex);
            }

            if (instrumentConfiguration != null) {
                currentScan.setInstrumentConfigurationRef(instrumentConfiguration);
            } else {
                MissingReferenceIssue refIssue = new MissingReferenceIssue(instrumentConfigurationRef, "scan", "instrumentConfigurationRef");
                refIssue.setIssueLocation(contentStack.peek());

                // TODO: Workaround only in place because of ABSciex converter bug where 
                // the defaultInstrumentConfigurationRef is auto-incremented in every raster
                // line file but the instrumentConfiguration id remains as 'instrumentConfiguration1'					
                if (currentInstrumentConfiguration != null) {
                    currentScan.setInstrumentConfigurationRef(currentInstrumentConfiguration);
                    refIssue.fixAttemptedByChangingReference(currentInstrumentConfiguration);
                } else {
                    refIssue.fixAttemptedByRemovingReference();
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
                refIssue.setIssueLocation(contentStack.peek());
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
            throw new FatalRuntimeParseException(new InvalidMzMLIssue("<scanList> tag not defined prior to defining <scan> tag.", ex.getLocalizedMessage()), ex);
        }

        contentStack.push(currentScan);
    }

    protected void startPrecursor(Attributes attributes) {
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
                refIssue.setIssueLocation(contentStack.peek());
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
                throw new FatalRuntimeParseException(new InvalidMzMLIssue("<precursorList> tag not defined prior to defining <precursor> tag.", ex.getLocalizedMessage()), ex);
            }
        } else if (processingChromatogram) {
            try {
                currentChromatogram.setPrecursor(currentPrecursor);
            } catch (NullPointerException ex) {
                throw new FatalRuntimeParseException(new InvalidMzMLIssue("<chromatogram> tag not defined prior to defining <precursor> tag.", ex.getLocalizedMessage()), ex);
            }
        }

        contentStack.push(currentPrecursor);
    }

    protected void startProcessingMethod(Attributes attributes) {
        String softwareRef = attributes.getValue("softwareRef");

        boolean referenceFound = false;
        Software software = null;

        if (softwareList != null) {
            software = softwareList.getSoftware(softwareRef);

            if (software != null) {
                referenceFound = true;
            } else {
                software = softwareList.get(0);
            }
        }

        ProcessingMethod pm = new ProcessingMethod(software);
        currentDataProcessing.addProcessingMethod(pm);

        contentStack.push(pm);

        if (!referenceFound && software != null) {
            MissingReferenceIssue missingRefIssue = new MissingReferenceIssue(softwareRef, "processingMethod", "softwareRef");
            missingRefIssue.setIssueLocation(contentStack.peek());
            missingRefIssue.fixAttemptedByChangingReference(software);

            notifyParserListeners(missingRefIssue);
        } else if (!referenceFound) {
            MissingReferenceIssue missingRefIssue = new MissingReferenceIssue(softwareRef, "processingMethod", "softwareRef");
            missingRefIssue.setIssueLocation(contentStack.peek());
            missingRefIssue.fixAttemptedByRemovingReference();

            notifyParserListeners(missingRefIssue);
        }
    }

    protected void startBinaryDataArray(Attributes attributes) {
        currentBinaryDataArray = new BinaryDataArray(Integer.parseInt(attributes.getValue("encodedLength")));

        if (attributes.getValue("arrayLength") != null) {
            currentBinaryDataArray.setArrayLength(Integer.parseInt(attributes.getValue("arrayLength")));
        }

        String dataProcessingRef = attributes.getValue("dataProcessingRef");

        if (dataProcessingRef != null) {
            boolean foundRef = false;
            DataProcessing dataProcessing;

            if (dataProcessingList != null) {
                dataProcessing = dataProcessingList.getDataProcessing(dataProcessingRef);

                if (dataProcessing != null) {
                    foundRef = true;
                    currentBinaryDataArray.setDataProcessingRef(dataProcessing);
                }
            }

            if (!foundRef) {
                MissingReferenceIssue refIssue = new MissingReferenceIssue(dataProcessingRef, "binaryDataArray", "dataProcessingRef");
                refIssue.setIssueLocation(contentStack.peek());
                refIssue.fixAttemptedByRemovingReference();

                notifyParserListeners(refIssue);
            }
        }

        try {
            currentBinaryDataArrayList.addBinaryDataArray(currentBinaryDataArray);
        } catch (NullPointerException ex) {
            throw new FatalRuntimeParseException(new InvalidMzMLIssue("<binaryDataArrayList> tag not defined prior to defining <binaryDataArray> tag.", ex.getLocalizedMessage()), ex);
        }

        contentStack.push(currentBinaryDataArray);
    }

    protected void startSpectrumList(Attributes attributes) {
        String defaultDataProcessingRef = attributes.getValue("defaultDataProcessingRef");
        DataProcessing dataProcessing = null;

        boolean foundRef = false;

        if (defaultDataProcessingRef != null && dataProcessingList != null) {
            dataProcessing = dataProcessingList.getDataProcessing(defaultDataProcessingRef);

            if (dataProcessing != null) {
                numberOfSpectra = Integer.parseInt(attributes.getValue(COUNT_ATTRIBUTE_NAME));
                foundRef = true;
            }
        }

        if (!foundRef) {
            MissingReferenceIssue refIssue = new MissingReferenceIssue(defaultDataProcessingRef, "spectrumList", "defaultDataProcessingRef");
            refIssue.setIssueLocation(contentStack.peek());
            refIssue.fixAttemptedByRemovingReference();

            notifyParserListeners(refIssue);

            // msconvert doesn't include default data processing so try and fix it
            //throw new InvalidMzML("No defaultProcessingRef attribute in spectrumList.");
            //spectrumList = new SpectrumList(Integer.parseInt(attributes.getValue("count")), dataProcessingList.getDataProcessing(0));
        }

        spectrumList = new SpectrumList(numberOfSpectra, dataProcessing);

        if (run == null) {
            throw new FatalRuntimeParseException(new InvalidMzMLIssue("<run> tag not defined prior to defining <spectrumList> tag."));
        }

        run.setSpectrumList(spectrumList);

        contentStack.push(spectrumList);
    }

    protected void startSpectrum(Attributes attributes) {
        currentSpectrum = new Spectrum(attributes.getValue(ID_ATTRIBUTE_NAME), Integer.parseInt(attributes.getValue("defaultArrayLength")));

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
                refIssue.setIssueLocation(contentStack.peek());
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
                refIssue.setIssueLocation(contentStack.peek());
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
            throw new FatalRuntimeParseException(new InvalidMzMLIssue("<spectrumList> tag not defined prior to defining <spectrum> tag.", ex.getLocalizedMessage()), ex);
        }

        contentStack.push(currentSpectrum);
    }

    protected void startChromatogramList(Attributes attributes) {
        String defaultDataProcessingRef = attributes.getValue("defaultDataProcessingRef");

        if (defaultDataProcessingRef != null) {
            DataProcessing dataProcessing = null;

            try {
                dataProcessing = dataProcessingList.getDataProcessing(defaultDataProcessingRef);
            } catch (NullPointerException ex) {
                throw new FatalRuntimeParseException(new InvalidMzMLIssue("<dataProcessingList> tag not defined prior to defining <chromatogramList> tag.", ex.getLocalizedMessage()), ex);
            }

            if (dataProcessing != null) {
                chromatogramList = new ChromatogramList(Integer.parseInt(attributes.getValue(COUNT_ATTRIBUTE_NAME)), dataProcessing);
            } else {
                throw new FatalRuntimeParseException(new InvalidMzMLIssue("Can't find defaultDataProcessingRef '" + defaultDataProcessingRef + "' referenced by chromatogramList."));
            }
        } else {
            // msconvert doesn't include default data processing so try and fix it				
            throw new FatalRuntimeParseException(new InvalidMzMLIssue("No defaultProcessingRef attribute in chromatogramList."));
        }

        try {
            run.setChromatogramList(chromatogramList);

            contentStack.push(chromatogramList);
        } catch (NullPointerException ex) {
            throw new FatalRuntimeParseException(new InvalidMzMLIssue("<run> tag not defined prior to defining <chromatogramList> tag.", ex.getLocalizedMessage()), ex);
        }
    }

    protected void startChromatogram(Attributes attributes) {
        processingChromatogram = true;
        currentChromatogram = new Chromatogram(attributes.getValue(ID_ATTRIBUTE_NAME), Integer.parseInt(attributes.getValue("defaultArrayLength")));

        String dataProcessingRef = attributes.getValue("dataProcessingRef");

        if (dataProcessingRef != null) {
            DataProcessing dataProcessing = null;

            try {
                dataProcessing = dataProcessingList.getDataProcessing(dataProcessingRef);
            } catch (NullPointerException ex) {
                throw new FatalRuntimeParseException(new InvalidMzMLIssue("<dataProcessingList> tag not defined prior to defining <chromatogram> tag.", ex.getLocalizedMessage()), ex);
            }

            if (dataProcessing != null) {
                currentChromatogram.setDataProcessingRef(dataProcessing);
            } else {
                throw new FatalRuntimeParseException(new InvalidMzMLIssue("Can't find dataProcessingRef '" + dataProcessingRef + "' referenced by chromatogram '" + currentChromatogram.getID() + "'."));
            }
        } else {
            currentChromatogram.setDataProcessingRef(chromatogramList.getDefaultDataProcessingRef());
        }

        try {
            chromatogramList.addChromatogram(currentChromatogram);
        } catch (NullPointerException ex) {
            throw new FatalRuntimeParseException(new InvalidMzMLIssue("<chromatogramList> tag not defined prior to defining <chromatogram> tag."));
        }

        contentStack.push(currentChromatogram);
    }

    protected void startUserParam(Attributes attributes) {
        if (contentStack.peek() != null) {
            UserParam userParam = new UserParam(attributes.getValue("name"));

            String type = attributes.getValue("type");
            if (type != null) {
                userParam.setType(type);
            }
            String value = attributes.getValue(VALUE_ATTRIBUTE_NAME);
            if (value != null) {
                userParam.setValue(value);
            }

            userParam.setUnits(obo.getTerm(attributes.getValue(UNIT_ACCESSION_ATTRIBUTE_NAME)));
            ((MzMLContentWithParams) contentStack.peek()).addUserParam(userParam);
        }
    }

    protected void startMzML(Attributes attributes) {
        mzML = new MzML(attributes.getValue("version"));

        mzML.setDataStorage(dataStorage);
        mzML.setOBO(obo);

        // Add optional attributes
        if (attributes.getValue(ACCESSION_ATTRIBUTE_NAME) != null) {
            mzML.setAccession(attributes.getValue(ACCESSION_ATTRIBUTE_NAME));
        }

        if (attributes.getValue(ID_ATTRIBUTE_NAME) != null) {
            mzML.setID(attributes.getValue(ID_ATTRIBUTE_NAME));
        }

        contentStack.push(mzML);
    }

    protected void startCVList(Attributes attributes) {
        cvList = new CVList(Integer.parseInt(attributes.getValue(COUNT_ATTRIBUTE_NAME)));

        try {
            mzML.setCVList(cvList);
        } catch (NullPointerException ex) {
            throw new FatalRuntimeParseException(new InvalidMzMLIssue("<mzML> tag not defined prior to defining <cvList> tag.", ex.getLocalizedMessage()), ex);
        }

        contentStack.push(cvList);
    }

    protected void startCV(Attributes attributes) {
        OBO childOBO = OBO.getOBO().getOBOWithID(attributes.getValue(ID_ATTRIBUTE_NAME));

        if (childOBO != null) {
            CV cv = new CV(childOBO);

            cvList.add(cv);

            contentStack.push(cv);
        } else {
            contentStack.push(null);
            LOGGER.log(Level.SEVERE, "WEIRD ONTOLOGY FOUND! {0}", attributes.getValue(ID_ATTRIBUTE_NAME));
        }
    }

    protected void startFileContent() {
        FileContent fc = new FileContent();

        try {
            fileDescription.setFileContent(fc);
        } catch (NullPointerException ex) {
            throw new FatalRuntimeParseException(new InvalidMzMLIssue("<fileDescription> tag not defined prior to defining <fileContent> tag.", ex.getLocalizedMessage()), ex);
        }

        contentStack.push(fc);
    }

    protected void startSourceFileList(Attributes attributes) {
        sourceFileList = new SourceFileList(Integer.parseInt(attributes.getValue(COUNT_ATTRIBUTE_NAME)));

        try {
            fileDescription.setSourceFileList(sourceFileList);
        } catch (NullPointerException ex) {
            throw new FatalRuntimeParseException(new InvalidMzMLIssue("<fileDescription> tag not defined prior to defining <sourceFileList> tag.", ex.getLocalizedMessage()), ex);
        }

        contentStack.push(sourceFileList);
    }

    protected void startSourceFile(Attributes attributes) {
        SourceFile sf = new SourceFile(attributes.getValue(ID_ATTRIBUTE_NAME), attributes.getValue("location"), attributes.getValue("name"));

        try {
            sourceFileList.addSourceFile(sf);
        } catch (NullPointerException ex) {
            throw new FatalRuntimeParseException(new InvalidMzMLIssue("<sourceFileList> tag not defined prior to defining <sourceFile> tag.", ex.getLocalizedMessage()), ex);
        }

        contentStack.push(sf);
    }

    protected void startContact() {
        Contact contact = new Contact();

        try {
            fileDescription.addContact(contact);
        } catch (NullPointerException ex) {
            throw new FatalRuntimeParseException(new InvalidMzMLIssue("<fileDescription> tag not defined prior to defining <contact> tag.", ex.getLocalizedMessage()), ex);
        }

        contentStack.push(contact);
    }

    protected void startReferenceableParamGroupList(Attributes attributes) {
        referenceableParamGroupList = new ReferenceableParamGroupList(getCountAttribute(attributes));

        try {
            mzML.setReferenceableParamGroupList(referenceableParamGroupList);
        } catch (NullPointerException ex) {
            throw new FatalRuntimeParseException(new InvalidMzMLIssue("<mzML> tag not defined prior to defining <referenceableParamGroupList> tag.", ex.getLocalizedMessage()), ex);
        }

        contentStack.push(referenceableParamGroupList);
    }

    protected void startReferenceableParamGroup(Attributes attributes) {
        ReferenceableParamGroup rpg = new ReferenceableParamGroup(attributes.getValue(ID_ATTRIBUTE_NAME));
        contentStack.push(rpg);

        referenceableParamGroupList.addReferenceableParamGroup(rpg);
    }

    protected void startSampleList(Attributes attributes) {
        sampleList = new SampleList(getCountAttribute(attributes));

        try {
            mzML.setSampleList(sampleList);
        } catch (NullPointerException ex) {
            throw new FatalRuntimeParseException(new InvalidMzMLIssue("<mzML> tag not defined prior to defining <sampleList> tag.", ex.getLocalizedMessage()), ex);
        }

        contentStack.push(sampleList);
    }

    protected void startSample(Attributes attributes) {
        Sample sample = new Sample(attributes.getValue(ID_ATTRIBUTE_NAME));

        if (attributes.getValue("name") != null) {
            sample.setName(attributes.getValue("name"));
        }

        try {
            sampleList.addSample(sample);
        } catch (NullPointerException ex) {
            throw new FatalRuntimeParseException(new InvalidMzMLIssue("<sampleList> tag not defined prior to defining <sample> tag.", ex.getLocalizedMessage()), ex);
        }

        contentStack.push(sample);
    }

    protected void startSoftwareList(Attributes attributes) {
        softwareList = new SoftwareList(getCountAttribute(attributes));

        try {
            mzML.setSoftwareList(softwareList);
        } catch (NullPointerException ex) {
            throw new FatalRuntimeParseException(new InvalidMzMLIssue("<mzML> tag not defined prior to defining <softwareList> tag.", ex.getLocalizedMessage()), ex);
        }

        contentStack.push(softwareList);
    }

    protected void startSoftware(Attributes attributes) {
        Software sw = new Software(attributes.getValue(ID_ATTRIBUTE_NAME), attributes.getValue("version"));

        try {
            softwareList.addSoftware(sw);
        } catch (NullPointerException ex) {
            throw new FatalRuntimeParseException(new InvalidMzMLIssue("<softwareList> tag not defined prior to defining <software> tag.", ex.getLocalizedMessage()), ex);
        }
        contentStack.push(sw);
    }

    protected void startScanSettingsList(Attributes attributes) {
        scanSettingsList = new ScanSettingsList(Integer.parseInt(attributes.getValue(COUNT_ATTRIBUTE_NAME)));

        try {
            mzML.setScanSettingsList(scanSettingsList);
        } catch (NullPointerException ex) {
            throw new FatalRuntimeParseException(new InvalidMzMLIssue("<mzML> tag not defined prior to defining <scanSettingsList> tag.", ex.getLocalizedMessage()), ex);
        }

        contentStack.push(scanSettingsList);
    }

    protected void startScanSettings(Attributes attributes) {
        currentScanSettings = new ScanSettings(attributes.getValue(ID_ATTRIBUTE_NAME));

        try {
            scanSettingsList.addScanSettings(currentScanSettings);
        } catch (NullPointerException ex) {
            throw new FatalRuntimeParseException(new InvalidMzMLIssue("<scanSettingsList> tag not defined prior to defining <scanSettings> tag.", ex.getLocalizedMessage()), ex);
        }

        contentStack.push(currentScanSettings);
    }

    protected void startSourceFileRefList(Attributes attributes) {
        currentSourceFileRefList = new SourceFileRefList(Integer.parseInt(attributes.getValue(COUNT_ATTRIBUTE_NAME)));

        try {
            currentScanSettings.setSourceFileRefList(currentSourceFileRefList);
        } catch (NullPointerException ex) {
            throw new FatalRuntimeParseException(new InvalidMzMLIssue("<scanSettings> tag not defined prior to defining <sourceFileRefList> tag.", ex.getLocalizedMessage()), ex);
        }

        contentStack.push(currentSourceFileRefList);
    }

    protected void startSourceFileRef(Attributes attributes) {
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
            missingRefIssue.setIssueLocation(contentStack.peek());
            missingRefIssue.fixAttemptedByRemovingReference();

            notifyParserListeners(missingRefIssue);
        }
    }

    protected void startTargetList(Attributes attributes) {
        currentTargetList = new TargetList(Integer.parseInt(attributes.getValue(COUNT_ATTRIBUTE_NAME)));

        try {
            currentScanSettings.setTargetList(currentTargetList);
        } catch (NullPointerException ex) {
            throw new FatalRuntimeParseException(new InvalidMzMLIssue("<scanSettings> tag not defined prior to defining <targetList> tag.", ex.getLocalizedMessage()), ex);
        }

        contentStack.push(currentTargetList);
    }

    protected void startTarget() {
        Target target = new Target();

        try {
            currentTargetList.addTarget(target);
        } catch (NullPointerException ex) {
            throw new FatalRuntimeParseException(new InvalidMzMLIssue("<targetList> tag not defined prior to defining <target> tag.", ex.getLocalizedMessage()), ex);
        }

        contentStack.push(target);
    }

    protected void startInstrumentConfigurationList(Attributes attributes) {
        instrumentConfigurationList = new InstrumentConfigurationList(getCountAttribute(attributes));

        try {
            mzML.setInstrumentConfigurationList(instrumentConfigurationList);
        } catch (NullPointerException ex) {
            throw new FatalRuntimeParseException(new InvalidMzMLIssue("<mzML> tag not defined prior to defining <instrumentConfigurationList> tag.", ex.getLocalizedMessage()), ex);
        }

        contentStack.push(instrumentConfigurationList);
    }

    protected void startComponentList() {
        currentComponentList = new ComponentList();

        try {
            currentInstrumentConfiguration.setComponentList(currentComponentList);
        } catch (NullPointerException ex) {
            throw new FatalRuntimeParseException(new InvalidMzMLIssue("<instrumentConfiguration> tag not defined prior to defining <componentList> tag.", ex.getLocalizedMessage()), ex);
        }

        contentStack.push(currentComponentList);
    }

    protected void startSource() {
        Source source = new Source();

        try {
            currentComponentList.addSource(source);
        } catch (NullPointerException ex) {
            throw new FatalRuntimeParseException(new InvalidMzMLIssue("<componentList> tag not defined prior to defining <source> tag.", ex.getLocalizedMessage()), ex);
        }

        contentStack.push(source);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        // Most common attribute at the start to reduce the number of comparisons needed
        if ("cvParam".equals(qName)) {
            startCVParam(attributes);
        } else if ("referenceableParamGroupRef".equals(qName)) {
            startReferenceableParamGroupRef(attributes);
        } else if ("userParam".equals(qName)) {
            startUserParam(attributes);
        } else if ("mzML".equalsIgnoreCase(qName)) {
            startMzML(attributes);
        } else if ("cvList".equals(qName)) {
            startCVList(attributes);
        } else if ("cv".equals(qName)) {
            startCV(attributes);
        } else if ("fileDescription".equals(qName)) {
            fileDescription = new FileDescription();

            mzML.setFileDescription(fileDescription);

            contentStack.push(fileDescription);
        } else if ("fileContent".equals(qName)) {
            startFileContent();
        } else if ("sourceFileList".equals(qName)) {
            startSourceFileList(attributes);
        } else if ("sourceFile".equals(qName)) {
            startSourceFile(attributes);
        } else if ("contact".equals(qName)) {
            startContact();
        } else if ("referenceableParamGroupList".equals(qName)) {
            startReferenceableParamGroupList(attributes);
        } else if ("referenceableParamGroup".equals(qName)) {
            startReferenceableParamGroup(attributes);
        } else if ("sampleList".equals(qName)) {
            startSampleList(attributes);
        } else if ("sample".equals(qName)) {
            startSample(attributes);
        } else if ("softwareList".equals(qName)) {
            startSoftwareList(attributes);
        } else if ("software".equals(qName)) {
            startSoftware(attributes);
        } else if ("scanSettingsList".equals(qName)) {
            startScanSettingsList(attributes);
        } else if ("scanSettings".equals(qName)) {
            startScanSettings(attributes);
        } else if ("sourceFileRefList".equals(qName)) {
            startSourceFileRefList(attributes);
        } else if ("sourceFileRef".equals(qName)) {
            startSourceFileRef(attributes);
        } else if ("targetList".equals(qName)) {
            startTargetList(attributes);
        } else if ("target".equals(qName)) {
            startTarget();
        } else if ("instrumentConfigurationList".equals(qName)) {
            startInstrumentConfigurationList(attributes);
        } else if ("instrumentConfiguration".equals(qName)) {
            startInstrumentConfiguration(attributes);
        } else if ("componentList".equals(qName)) {
            startComponentList();
        } else if ("source".equals(qName)) {
            startSource();
        } else if ("analyzer".equals(qName)) {
            Analyser analyser = new Analyser();

            try {
                currentComponentList.addAnalyser(analyser);
            } catch (NullPointerException ex) {
                throw new FatalRuntimeParseException(new InvalidMzMLIssue("<componentList> tag not defined prior to defining <analyser> tag.", ex.getLocalizedMessage()), ex);
            }

            contentStack.push(analyser);
        } else if ("detector".equals(qName)) {
            Detector detector = new Detector();

            try {
                currentComponentList.addDetector(detector);
            } catch (NullPointerException ex) {
                throw new FatalRuntimeParseException(new InvalidMzMLIssue("<componentList> tag not defined prior to defining <detector> tag.", ex.getLocalizedMessage()), ex);
            }

            contentStack.push(detector);
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
                missingRefIssue.setIssueLocation(contentStack.peek());
                missingRefIssue.fixAttemptedByRemovingReference();

                notifyParserListeners(missingRefIssue);
            }
        } else if ("dataProcessingList".equals(qName)) {
            dataProcessingList = new DataProcessingList(getCountAttribute(attributes));

            try {
                mzML.setDataProcessingList(dataProcessingList);
            } catch (NullPointerException ex) {
                throw new FatalRuntimeParseException(new InvalidMzMLIssue("<mzML> tag not defined prior to defining <dataProcessingList> tag.", ex.getLocalizedMessage()), ex);
            }

            contentStack.push(dataProcessingList);
        } else if ("dataProcessing".equals(qName)) {
            DataProcessing dp = new DataProcessing(attributes.getValue(ID_ATTRIBUTE_NAME));

            try {
                dataProcessingList.addDataProcessing(dp);
            } catch (NullPointerException ex) {
                throw new FatalRuntimeParseException(new InvalidMzMLIssue("<dataProcessingList> tag not defined prior to defining <dataProcessing> tag.", ex.getLocalizedMessage()), ex);
            }

            currentDataProcessing = dp;
            contentStack.push(dp);
        } else if ("processingMethod".equals(qName)) {
            startProcessingMethod(attributes);
        } else if ("run".equals(qName)) {
            startRun(attributes);
        } else if ("spectrumList".equals(qName)) {
            startSpectrumList(attributes);
        } else if ("spectrum".equals(qName)) {
            startSpectrum(attributes);
        } else if ("scanList".equals(qName)) {
            currentScanList = new ScanList(Integer.parseInt(attributes.getValue(COUNT_ATTRIBUTE_NAME)));

            try {
                currentSpectrum.setScanList(currentScanList);
            } catch (NullPointerException ex) {
                throw new FatalRuntimeParseException(new InvalidMzMLIssue("<spectrum> tag not defined prior to defining <scanList> tag."));
            }

            contentStack.push(currentScanList);
        } else if ("scan".equals(qName)) {
            startScan(attributes);
        } else if ("scanWindowList".equals(qName)) {
            currentScanWindowList = new ScanWindowList(Integer.parseInt(attributes.getValue(COUNT_ATTRIBUTE_NAME)));

            try {
                currentScan.setScanWindowList(currentScanWindowList);
            } catch (NullPointerException ex) {
                throw new FatalRuntimeParseException(new InvalidMzMLIssue("<scan> tag not defined prior to defining <scanWindowList> tag.", ex.getLocalizedMessage()), ex);
            }

            contentStack.push(currentScanWindowList);
        } else if ("scanWindow".equals(qName)) {
            ScanWindow scanWindow = new ScanWindow();

            try {
                currentScanWindowList.addScanWindow(scanWindow);
            } catch (NullPointerException ex) {
                throw new FatalRuntimeParseException(new InvalidMzMLIssue("<scanWindowList> tag not defined prior to defining <scanWindow> tag.", ex.getLocalizedMessage()), ex);
            }

            contentStack.push(scanWindow);
        } else if ("precursorList".equals(qName)) {
            currentPrecursorList = new PrecursorList(Integer.parseInt(attributes.getValue(COUNT_ATTRIBUTE_NAME)));

            try {
                currentSpectrum.setPrecursorList(currentPrecursorList);
            } catch (NullPointerException ex) {
                throw new FatalRuntimeParseException(new InvalidMzMLIssue("<spectrum> tag not defined prior to defining <precursorList> tag.", ex.getLocalizedMessage()), ex);
            }

            contentStack.push(currentPrecursorList);
        } else if ("precursor".equals(qName)) {
            startPrecursor(attributes);
        } else if ("isolationWindow".equals(qName)) {
            IsolationWindow isolationWindow = new IsolationWindow();

            if (processingPrecursor) {
                try {
                    currentPrecursor.setIsolationWindow(isolationWindow);
                } catch (NullPointerException ex) {
                    throw new FatalRuntimeParseException(new InvalidMzMLIssue("<precursor> tag not defined prior to defining <isolationWindow> tag.", ex.getLocalizedMessage()), ex);
                }
            } else if (processingProduct) {
                try {
                    currentProduct.setIsolationWindow(isolationWindow);
                } catch (NullPointerException ex) {
                    throw new FatalRuntimeParseException(new InvalidMzMLIssue("<product> tag not defined prior to defining <isolationWindow> tag.", ex.getLocalizedMessage()), ex);
                }
            }

            contentStack.push(isolationWindow);
        } else if ("selectedIonList".equals(qName)) {
            currentSelectedIonList = new SelectedIonList(Integer.parseInt(attributes.getValue(COUNT_ATTRIBUTE_NAME)));

            try {
                currentPrecursor.setSelectedIonList(currentSelectedIonList);
            } catch (NullPointerException ex) {
                throw new FatalRuntimeParseException(new InvalidMzMLIssue("<precursor> tag not defined prior to defining <selectedIonList> tag.", ex.getLocalizedMessage()), ex);
            }

            contentStack.push(currentSelectedIonList);
        } else if ("selectedIon".equals(qName)) {
            SelectedIon selectedIon = new SelectedIon();

            try {
                currentSelectedIonList.addSelectedIon(selectedIon);
            } catch (NullPointerException ex) {
                throw new FatalRuntimeParseException(new InvalidMzMLIssue("<selectedIonList> tag not defined prior to defining <selectedIon> tag.", ex.getLocalizedMessage()), ex);
            }

            contentStack.push(selectedIon);
        } else if ("activation".equals(qName)) {
            Activation activation = new Activation();

            try {
                currentPrecursor.setActivation(activation);
            } catch (NullPointerException ex) {
                throw new FatalRuntimeParseException(new InvalidMzMLIssue("<precursor> tag not defined prior to defining <activation> tag.", ex.getLocalizedMessage()), ex);
            }

            contentStack.push(activation);
        } else if ("productList".equals(qName)) {
            currentProductList = new ProductList(Integer.parseInt(attributes.getValue(COUNT_ATTRIBUTE_NAME)));

            try {
                currentSpectrum.setProductList(currentProductList);
            } catch (NullPointerException ex) {
                throw new FatalRuntimeParseException(new InvalidMzMLIssue("<spectrum> tag not defined prior to defining <productList> tag.", ex.getLocalizedMessage()), ex);
            }

            contentStack.push(currentProductList);
        } else if ("product".equals(qName)) {
            processingProduct = true;
            currentProduct = new Product();

            if (processingSpectrum) {
                try {
                    currentProductList.addProduct(currentProduct);
                } catch (NullPointerException ex) {
                    throw new FatalRuntimeParseException(new InvalidMzMLIssue("<productList> tag not defined prior to defining <product> tag.", ex.getLocalizedMessage()), ex);
                }
            } else if (processingChromatogram) {
                try {
                    currentChromatogram.setProduct(currentProduct);
                } catch (NullPointerException ex) {
                    throw new FatalRuntimeParseException(new InvalidMzMLIssue("<chromatogram> tag not defined prior to defining <product> tag.", ex.getLocalizedMessage()), ex);
                }
            }

            contentStack.push(currentProduct);
        } else if ("binaryDataArrayList".equals(qName)) {
            currentBinaryDataArrayList = new BinaryDataArrayList(Integer.parseInt(attributes.getValue(COUNT_ATTRIBUTE_NAME)));

            if (processingSpectrum) {
                try {
                    currentSpectrum.setBinaryDataArrayList(currentBinaryDataArrayList);
                } catch (NullPointerException ex) {
                    throw new FatalRuntimeParseException(new InvalidMzMLIssue("<spectrum> tag not defined prior to defining <binaryDataArrayList> tag.", ex.getLocalizedMessage()), ex);
                }
            } else if (processingChromatogram) {
                try {
                    currentChromatogram.setBinaryDataArrayList(currentBinaryDataArrayList);
                } catch (NullPointerException ex) {
                    throw new FatalRuntimeParseException(new InvalidMzMLIssue("<chromatogram> tag not defined prior to defining <binaryDataArrayList> tag.", ex.getLocalizedMessage()), ex);
                }
            }

            contentStack.push(currentBinaryDataArrayList);
        } else if ("binaryDataArray".equals(qName)) {
            startBinaryDataArray(attributes);
        } else if ("binary".equals(qName)) {
            // Ignore binary data for the header
        } else if ("chromatogramList".equals(qName)) {
            startChromatogramList(attributes);
        } else if ("chromatogram".equals(qName)) {
            startChromatogram(attributes);
        } else if ("offset".equals(qName) || "indexListOffset".equals(qName)) {
            previousOffsetIDRef = currentOffsetIDRef;

            if ("offset".equals(qName)) {
                this.currentOffsetIDRef = attributes.getValue("idRef");
            }

            offsetData.setLength(0);
            processingOffset = true;
        } else if ("index".equals(qName)) {
            if (attributes.getValue("name").equals("chromatogram")) {
                this.processingChromatogram = true;
            } else {
                this.processingSpectrum = true;
            }
        } else if ("indexedmzML".equals(qName) || "indexList".equals(qName) || "indexListOffset".equals(qName)) {
        } else {
            LOGGER.log(Level.FINEST, "No processing for tag <{0}>", qName);
        }

        //needToPop = stackSize != contentStack.size();

        //if(!needToPop)
        //    System.out.println(qName + ": doesn't need to pop! ");
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (processingOffset) {
            offsetData.append(ch, start, length);
        }
    }

    protected MzMLDataContainer getDataContainer() {
        MzMLDataContainer dataContainer;

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
            if (currentSpectrum.containsCVParam(cvParam)) {
                ReferenceableParamGroup bestGroup = currentSpectrum.findBestFittingRPG(referenceableParamGroupList);

                if (bestGroup == null) {
                    // TODO: Give it a better name
                    bestGroup = new ReferenceableParamGroup();

                    if (referenceableParamGroupList == null) {
                        referenceableParamGroupList = new ReferenceableParamGroupList(1);
                        mzML.setReferenceableParamGroupList(referenceableParamGroupList);
                    }

                    referenceableParamGroupList.add(bestGroup);

                    bestGroup.addCVParam(currentSpectrum.getCVParamOrChild("MS:1000294"));
                    bestGroup.addCVParam(currentSpectrum.getCVParamOrChild("MS:1000511")); // ms level
                    bestGroup.addCVParam(currentSpectrum.getCVParamOrChild(Spectrum.SCAN_POLARITY_ID));
                    bestGroup.addCVParam(currentSpectrum.getCVParamOrChild("MS:1000525")); // spectrum representation
                }

                currentSpectrum.replaceCVParamsWithRPG(bestGroup);
            }
        } else if ("scan".equals(qName)) {
            // Try and tidy up scan
            CVParam cvParam = currentScan.getCVParamOrChild("MS:1000616");
            if (currentScan.containsCVParam(cvParam)) {
                ReferenceableParamGroup bestGroup = currentScan.findBestFittingRPG(referenceableParamGroupList);

                if (bestGroup == null) {
                    // TODO: Give it a better name
                    bestGroup = new ReferenceableParamGroup();

                    if (referenceableParamGroupList == null) {
                        referenceableParamGroupList = new ReferenceableParamGroupList(1);
                        mzML.setReferenceableParamGroupList(referenceableParamGroupList);
                    }

                    referenceableParamGroupList.add(bestGroup);

                    bestGroup.addCVParam(currentScan.getCVParamOrChild("MS:1000512"));
                    bestGroup.addCVParam(currentScan.getCVParamOrChild("MS:1000616"));
                    bestGroup.addCVParam(currentScan.getCVParamOrChild("MS:1000927"));
                }

                currentScan.replaceCVParamsWithRPG(bestGroup);
            }
        } else if ("scanWindow".equals(qName)) {
            if (contentStack.peek() instanceof MzMLContentWithParams) {
                CVParam cvParam = ((MzMLContentWithParams) contentStack.peek()).getCVParamOrChild("MS:1000501");

                if (((MzMLContentWithParams) contentStack.peek()).containsCVParam(cvParam)) {
                    ReferenceableParamGroup bestGroup = ((MzMLContentWithParams) contentStack.peek()).findBestFittingRPG(referenceableParamGroupList);

                    if (bestGroup == null) {
                        // TODO: Give it a better name
                        bestGroup = new ReferenceableParamGroup();

                        if (referenceableParamGroupList == null) {
                            referenceableParamGroupList = new ReferenceableParamGroupList(1);
                            mzML.setReferenceableParamGroupList(referenceableParamGroupList);
                        }

                        referenceableParamGroupList.add(bestGroup);

                        bestGroup.addCVParam(((MzMLContentWithParams) contentStack.peek()).getCVParamOrChild("MS:1000501"));
                        bestGroup.addCVParam(((MzMLContentWithParams) contentStack.peek()).getCVParamOrChild("MS:1000500"));
                    }

                    ((MzMLContentWithParams) contentStack.peek()).replaceCVParamsWithRPG(bestGroup);
                }
            }
        } else if ("chromatogram".equals(qName)) {
            processingChromatogram = false;
        } else if ("precursor".equals(qName)) {
            processingPrecursor = false;
        } else if ("product".equals(qName)) {
            processingProduct = false;
        } else if ("offset".equals(qName) || "indexListOffset".equals(qName)) {
            long offset = getOffset();

            MzMLDataContainer dataContainer = getDataContainer();

            if (processingSpectrum && processingChromatogram) {
                processingSpectrum = false;
            }

            setDataContainer(dataContainer, offset);

            previousOffset = offset;
            processingOffset = false;
        } else if ("mzML".equals(qName) || "indexedmzML".equals(qName)) {
            // Go through spectra and chromatograms to convert the data storage if necessary
            if (spectrumList != null) {
                for (Spectrum spectrum : spectrumList) {
                    try {
                        spectrum.ensureLoadableData();
                    } catch (IOException ex) {
                        Logger.getLogger(MzMLHeaderHandler.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

        if(!qName.equals("indexedmzML") && !qName.equals("cvParam") && !qName.equals("userParam") && !qName.equals("softwareRef")
                && !qName.equals("binary") && !qName.equals("referenceableParamGroupRef") && !qName.equals("sourceFileRef")
                && !qName.equals("index") && !qName.equals("indexList") && !qName.equals("offset") && !qName.equals("indexListOffset")
                && !qName.equals("fileChecksum"))
            contentStack.pop();
    }

    public MzML getmzML() {
        return mzML;
    }
}
