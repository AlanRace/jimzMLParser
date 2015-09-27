package com.alanmrace.jimzmlparser.parser;

import com.alanmrace.jimzmlparser.exceptions.CVParamAccessionNotFoundException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.alanmrace.jimzmlparser.mzML.*;
import com.alanmrace.jimzmlparser.obo.OBO;
import com.alanmrace.jimzmlparser.obo.OBOTerm;
import imzMLConverter.InvalidMzML;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class MzMLHeaderHandler extends DefaultHandler {
	
	private static Logger logger = Logger.getLogger(MzMLHeaderHandler.class.getName());
	
        protected Locator locator;
        
	protected OBO obo;
	protected MzML mzML;
	
	protected CVList cvList;
	protected FileDescription fileDescription;
	protected SourceFileList sourceFileList;
	protected ReferenceableParamGroupList referenceableParamGroupList;
	protected SampleList sampleList;
	protected SoftwareList softwareList;
	protected ScanSettingsList scanSettingsList;
	protected ScanSettings currentScanSettings;
	protected SourceFileRefList currentSourceFileRefList;
	protected TargetList currentTargetList;
	protected InstrumentConfigurationList instrumentConfigurationList;	
	protected InstrumentConfiguration currentInstrumentConfiguration;
	protected ComponentList currentComponentList;
	protected DataProcessingList dataProcessingList;
	protected DataProcessing currentDataProcessing;
	protected Run run;
	protected SpectrumList spectrumList;
	protected Spectrum currentSpectrum;
	protected ScanList currentScanList;
	protected Scan currentScan;
	protected ScanWindowList currentScanWindowList;
	protected PrecursorList currentPrecursorList;
	protected Precursor currentPrecursor;
	protected SelectedIonList currentSelectedIonList;
	protected ProductList currentProductList;
	protected Product currentProduct;
	protected BinaryDataArrayList currentBinaryDataArrayList;
	protected BinaryDataArray currentBinaryDataArray;
	protected ChromatogramList chromatogramList;
	protected Chromatogram currentChromatogram;
	
	protected MzMLContent currentContent;
	
	// Flags for tags that share the same sub-tags
	protected boolean processingSpectrum;
	protected boolean processingChromatogram;
	
	protected boolean processingPrecursor;
	protected boolean processingProduct;
        
        protected boolean processingOffset;
        protected StringBuffer offsetData;
	protected String previousOffsetIDRef;
        protected String currentOffsetIDRef;
        protected long previousOffset = -1;
        
        protected DataStorage dataStorage;
	
        public MzMLHeaderHandler(OBO obo) {
            this.obo = obo;
		
            processingSpectrum = false;
            processingChromatogram = false;
            processingPrecursor = false;
            processingProduct = false;
            
            // Create a string buffer for storing the character offsets stored in indexed mzML
            offsetData = new StringBuffer();
        }
        
        @Override
        public void setDocumentLocator(Locator locator) {
            this.locator = locator;
        }
        
	public MzMLHeaderHandler(OBO obo, File mzMLFile) {
		this(obo);
                
                this.dataStorage = new MzMLSpectrumDataStorage(mzMLFile);
	}
	
	public static MzML parsemzMLHeader(String filename) {
            OBO obo = new OBO("imagingMS.obo");
				
            // Parse mzML
            MzMLHeaderHandler handler = new MzMLHeaderHandler(obo, new File(filename));
        		
            SAXParserFactory spf = SAXParserFactory.newInstance();
            try {
                //get a new instance of parser
                SAXParser sp = spf.newSAXParser();

                File file = new File(filename);

                //parse the file and also register this class for call backs
                sp.parse(file, handler);

            }catch(SAXException se) {
                    se.printStackTrace();
            }catch(ParserConfigurationException pce) {
                    pce.printStackTrace();
            }catch (IOException ie) {
                    ie.printStackTrace();
            }

            handler.getmzML().setOBO(obo);

            return handler.getmzML();
	}
	
        @Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		
//            System.out.println(locator.)
            
		// Most common attribute at the start to reduce the number of comparisons needed
		if(qName.equals("cvParam")) {
			if(currentContent != null) {			
				String accession = attributes.getValue("accession");
				
				OBOTerm term = obo.getTerm(accession);
				
				if(term == null)
					System.err.println("CV Param with accession '" + attributes.getValue("accession") + "' not found in any OBO.");
				
				try {
					CVParam.CVParamType paramType = CVParam.getCVParamType(accession);
					CVParam cvParam;
					
					String value = attributes.getValue("value");
					OBOTerm units = obo.getTerm(attributes.getValue("unitAccession"));
					
					try {
						if(paramType.equals(CVParam.CVParamType.String))
							cvParam = new StringCVParam(term, value, units); 
						else if(paramType.equals(CVParam.CVParamType.Empty))
							cvParam = new EmptyCVParam(term);
						else if(paramType.equals(CVParam.CVParamType.Long)) {
							 cvParam = new LongCVParam(term, Long.parseLong(value), units);
									
//							currentContent.addLongCVParam(cvParam);
						} else if(paramType.equals(CVParam.CVParamType.Double)) {
							cvParam = new DoubleCVParam(term, Double.parseDouble(value), units);
							
//							currentContent.addDoubleCVParam(cvParam);
						} else {
							cvParam = new StringCVParam(term, attributes.getValue("value"), obo.getTerm(attributes.getValue("unitAccession")));
						}
					} catch (NumberFormatException nfe) {
						cvParam = new StringCVParam(term, attributes.getValue("value"), obo.getTerm(attributes.getValue("unitAccession")));
					}
					
					currentContent.addCVParam(cvParam);
				} catch (CVParamAccessionNotFoundException notFound) {
					System.err.println("Couldn't find " + term + " in OBO. Changing to UserParam instead.");
					
					UserParam userParam = new UserParam(attributes.getValue("accession"), attributes.getValue("value"), obo.getTerm(attributes.getValue("unitAccession")));
					currentContent.addUserParam(userParam);
				}
			} else {
				throw new InvalidMzML("<cvParam> tag without a parent.");
			}
		} else if(qName.equals("referenceableParamGroupRef")) {
			ReferenceableParamGroupRef ref = new ReferenceableParamGroupRef(referenceableParamGroupList.getReferenceableParamGroup(attributes.getValue("ref")));

			if(currentContent != null)
				currentContent.addReferenceableParamGroupRef(ref);
		} else if(qName.equals("userParam")) {
			if(currentContent != null) {
				UserParam userParam = new UserParam(attributes.getValue("name"));
				
				String type = attributes.getValue("type");
				if(type != null)
					userParam.setType(type);
				String value = attributes.getValue("value");
				if(value != null)
					userParam.setValue(value);
				
				userParam.setUnits(obo.getTerm(attributes.getValue("unitAccession")));
				
				currentContent.addUserParam(userParam);
			}
		} else if(qName.equalsIgnoreCase("mzML")) {
			mzML = new MzML(attributes.getValue("version"));
			
			mzML.setOBO(obo);
			
			// Add optional attributes
			if(attributes.getValue("accession") != null)
				mzML.setAccession(attributes.getValue("accession"));
			
			if(attributes.getValue("id") != null)
				mzML.setID(attributes.getValue("id"));
		} else if(qName.equals("cvList")) {
			cvList = new CVList(Integer.parseInt(attributes.getValue("count")));
			
			try {
				mzML.setCVList(cvList);
			} catch (NullPointerException ex) {
				throw new InvalidMzML("<mzML> tag not defined prior to defining <cvList> tag.");
			}
		} else if(qName.equals("cv")) {
			CV cv = new CV(attributes.getValue("URI"), attributes.getValue("fullName"), attributes.getValue("id"));
			
			if(attributes.getValue("version") != null)
				cv.setVersion(attributes.getValue("version"));
			
			try {
				cvList.addCV(cv);
			} catch (NullPointerException ex) {
				throw new InvalidMzML("<cvList> tag not defined prior to defining <cv> tag.");
			}
		} else if(qName.equals("fileDescription")) {
			fileDescription = new FileDescription();
			
			mzML.setFileDescription(fileDescription);
		} else if(qName.equals("fileContent")) {
			FileContent fc = new FileContent();
			
			try {
				fileDescription.setFileContent(fc);
			} catch (NullPointerException ex) {
				throw new InvalidMzML("<fileDescription> tag not defined prior to defining <fileContent> tag.");
			}
			
			currentContent = fc;
		} else if(qName.equals("sourceFileList")) {
			sourceFileList = new SourceFileList(Integer.parseInt(attributes.getValue("count")));
			
			try {
				fileDescription.setSourceFileList(sourceFileList);
			} catch (NullPointerException ex) {
				throw new InvalidMzML("<fileDescription> tag not defined prior to defining <sourceFileList> tag.");
			}
		} else if(qName.equals("sourceFile")) {
			SourceFile sf = new SourceFile(attributes.getValue("id"), attributes.getValue("location"), attributes.getValue("name"));
			
			try {
				sourceFileList.addSourceFile(sf);
			} catch (NullPointerException ex) {
				throw new InvalidMzML("<sourceFileList> tag not defined prior to defining <sourceFile> tag.");
			}
			
			currentContent = sf;
		} else if(qName.equals("contact")) {
			Contact contact = new Contact();
			
			try {
				fileDescription.addContact(contact);
			} catch (NullPointerException ex) {
				throw new InvalidMzML("<fileDescription> tag not defined prior to defining <contact> tag.");
			}
			
			currentContent = contact;
		} else if(qName.equals("referenceableParamGroupList")) {
			referenceableParamGroupList = new ReferenceableParamGroupList(Integer.parseInt(attributes.getValue("count")));
			
			try {
				mzML.setReferenceableParamGroupList(referenceableParamGroupList);
			} catch (NullPointerException ex) {
				throw new InvalidMzML("<mzML> tag not defined prior to defining <referenceableParamGroupList> tag.");
			}
		} else if(qName.equals("referenceableParamGroup")) {
			ReferenceableParamGroup rpg = new ReferenceableParamGroup(attributes.getValue("id"));
			
			currentContent = rpg;
			
			try {
				referenceableParamGroupList.addReferenceableParamGroup(rpg);
			} catch (NullPointerException ex) {
				throw new InvalidMzML("<referenceableParamGroupList> tag not defined prior to defining <referenceableParamGroup> tag.");
			}
		} else if(qName.equals("sampleList")) {
			sampleList = new SampleList(Integer.parseInt(attributes.getValue("count")));
			
			try {
				mzML.setSampleList(sampleList);
			} catch (NullPointerException ex) {
				throw new InvalidMzML("<mzML> tag not defined prior to defining <sampleList> tag.");
			}
		} else if(qName.equals("sample")) {
			Sample sample = new Sample(attributes.getValue("id"));
			
			if(attributes.getValue("name") != null)
				sample.setName(attributes.getValue("name"));
			
			try {
				sampleList.addSample(sample);
			} catch (NullPointerException ex) {
				throw new InvalidMzML("<sampleList> tag not defined prior to defining <sample> tag.");
			}
			
			currentContent = sample;
		} else if(qName.equals("softwareList")) {
			softwareList = new SoftwareList(Integer.parseInt(attributes.getValue("count")));
			
			try {
				mzML.setSoftwareList(softwareList);
			} catch (NullPointerException ex) {
				throw new InvalidMzML("<mzML> tag not defined prior to defining <softwareList> tag.");
			}
		} else if(qName.equals("software")) {
			Software sw = new Software(attributes.getValue("id"), attributes.getValue("version"));
			
			try {
				softwareList.addSoftware(sw);
			} catch (NullPointerException ex) {
				throw new InvalidMzML("<softwareList> tag not defined prior to defining <software> tag.");
			}
			currentContent = sw;
		} else if(qName.equals("scanSettingsList")) {
			scanSettingsList = new ScanSettingsList(Integer.parseInt(attributes.getValue("count")));
			
			try {
				mzML.setScanSettingsList(scanSettingsList);
			} catch (NullPointerException ex) {
				throw new InvalidMzML("<mzML> tag not defined prior to defining <scanSettingsList> tag.");
			}
		} else if(qName.equals("scanSettings")) {
			currentScanSettings = new ScanSettings(attributes.getValue("id"));
			
			try {
				scanSettingsList.addScanSettings(currentScanSettings);
			} catch (NullPointerException ex) {
				throw new InvalidMzML("<scanSettingsList> tag not defined prior to defining <scanSettings> tag.");
			}
			
			currentContent = currentScanSettings;
		} else if(qName.equals("sourceFileRefList")) {
			currentSourceFileRefList = new SourceFileRefList(Integer.parseInt(attributes.getValue("count")));
			
			try {
				currentScanSettings.setSourceFileRefList(currentSourceFileRefList);
			} catch(NullPointerException ex) {
				throw new InvalidMzML("<scanSettings> tag not defined prior to defining <sourceFileRefList> tag.");
			}
		} else if(qName.equals("sourceFileRef")) {
			String ref = attributes.getValue("ref");
			
			SourceFile sourceFile = null;
			
			try {
				sourceFile = sourceFileList.getSourceFile(ref);
			} catch (NullPointerException ex) {
				throw new InvalidMzML("<sourceFileList> tag not defined prior to defining <sourceFile> tag.");
			}
			
			if(sourceFile != null)
				currentSourceFileRefList.addSourceFileRef(new SourceFileRef(sourceFile));
			else
				throw new InvalidMzML("Can't find sourceFileRef '" + ref + "' in sourceFileList");
		} else if(qName.equals("targetList")) {
			currentTargetList = new TargetList(Integer.parseInt(attributes.getValue("count")));
			
			try {
				currentScanSettings.setTargetList(currentTargetList);
			} catch (NullPointerException ex) {
				throw new InvalidMzML("<scanSettings> tag not defined prior to defining <targetList> tag.");
			}
		} else if(qName.equals("target")) {
			Target target = new Target();
			
			try {
				currentTargetList.addTarget(target);
			} catch (NullPointerException ex) {
				throw new InvalidMzML("<targetList> tag not defined prior to defining <target> tag.");
			}
			
			currentContent = target;
		} else if(qName.equals("instrumentConfigurationList")) {
			instrumentConfigurationList = new InstrumentConfigurationList(Integer.parseInt(attributes.getValue("count")));
			
			try {
				mzML.setInstrumentConfigurationList(instrumentConfigurationList);
			} catch (NullPointerException ex) {
				throw new InvalidMzML("<mzML> tag not defined prior to defining <instrumentConfigurationList> tag.");
			}
		} else if(qName.equals("instrumentConfiguration")) {
			currentInstrumentConfiguration = new InstrumentConfiguration(attributes.getValue("id"));
			
			String scanSettingsRef = attributes.getValue("scanSettingsRef");
			
			if(scanSettingsRef != null) {
				ScanSettings scanSettings = scanSettingsList.getScanSettings(scanSettingsRef);
				
				if(scanSettings != null)
					currentInstrumentConfiguration.setScanSettingsRef(scanSettings);
				else
					throw new InvalidMzML("Can't find scanSettingsRef '" + scanSettingsRef + "' on instrumentConfiguration '" + currentInstrumentConfiguration.getID() + "'");
			}
			
			try {
				instrumentConfigurationList.addInstrumentConfiguration(currentInstrumentConfiguration);
			} catch (NullPointerException ex) {
				throw new InvalidMzML("<instrumentConfigurationList> tag not defined prior to defining <instrumentConfiguration> tag.");
			}
				
			currentContent = currentInstrumentConfiguration;
		} else if(qName.equals("componentList")) {
			currentComponentList = new ComponentList();
			
			try {
				currentInstrumentConfiguration.setComponentList(currentComponentList);
			} catch (NullPointerException ex) {
				throw new InvalidMzML("<instrumentConfiguration> tag not defined prior to defining <componentList> tag.");
			}
		} else if(qName.equals("source")) {
			Source source = new Source(Integer.parseInt(attributes.getValue("order")));
			
			try {
				currentComponentList.addSource(source);
			} catch (NullPointerException ex) {
				throw new InvalidMzML("<componentList> tag not defined prior to defining <source> tag.");
			}
			
			currentContent = source;
		} else if(qName.equals("analyzer")) {
			Analyser analyser = new Analyser(Integer.parseInt(attributes.getValue("order")));
			
			try {
				currentComponentList.addAnalyser(analyser);
			} catch (NullPointerException ex) {
				throw new InvalidMzML("<componentList> tag not defined prior to defining <analyser> tag.");
			}
			
			currentContent = analyser;
		} else if(qName.equals("detector")) {
			Detector detector = new Detector(Integer.parseInt(attributes.getValue("order")));
			
			try {
				currentComponentList.addDetector(detector);
			} catch (NullPointerException ex) {
				throw new InvalidMzML("<componentList> tag not defined prior to defining <detector> tag.");
			}
			
			currentContent = detector;
		} else if(qName.equals("softwareRef")) {
			String softwareRef = attributes.getValue("ref");
			
			Software software = null;
			
			try {
				 software = softwareList.getSoftware(softwareRef);
			} catch (NullPointerException ex) {
				throw new InvalidMzML("<softwareList> tag not defined prior to defining <softwareRef> tag.");
			}
			
			if(currentInstrumentConfiguration == null)
				throw new InvalidMzML("<instrumentConfiguration> tag not defined prior to defining <softwareRef> tag.");
			
			if(software != null)
				currentInstrumentConfiguration.setSoftwareRef(new SoftwareRef(software));
			else {
				logger.warning("Invalid mzML file - could not find softwareRef '" + softwareRef + "'. Attempting to continue...");
				
				
				// TODO: Reinstate these checks
				//throw new InvalidMzML("Can't find softwareRef '" + softwareRef + "' in instrumentConfiguration '" + currentInstrumentConfiguration.getID() + "'");
			}
		} else if(qName.equals("dataProcessingList")) {
			dataProcessingList = new DataProcessingList(Integer.parseInt(attributes.getValue("count")));
			
			try {
				mzML.setDataProcessingList(dataProcessingList);
			} catch (NullPointerException ex) {
				throw new InvalidMzML("<mzML> tag not defined prior to defining <dataProcessingList> tag.");
			}
		} else if(qName.equals("dataProcessing")) {
			DataProcessing dp = new DataProcessing(attributes.getValue("id"));
			
			try {
				dataProcessingList.addDataProcessing(dp);
			} catch (NullPointerException ex) {
				throw new InvalidMzML("<dataProcessingList> tag not defined prior to defining <dataProcessing> tag.");
			}
				
			currentDataProcessing = dp;
			currentContent = dp;
		} else if(qName.equals("processingMethod")) {
			String softwareRef = attributes.getValue("softwareRef");
			
			Software software = null;
			
			try {
				software = softwareList.getSoftware(softwareRef);
			} catch (NullPointerException ex) {
				throw new InvalidMzML("<softwareList> tag not defined prior to defining <processingMethod> tag.");
			}
			
			if(software != null) {
				ProcessingMethod pm = new ProcessingMethod(Integer.parseInt(attributes.getValue("order")), software);
			
				try {
					currentDataProcessing.addProcessingMethod(pm);
				} catch (NullPointerException ex) {
					throw new InvalidMzML("<dataProcessing> tag not defined prior to defining <processingMethod> tag.");
				}
				
				currentContent = pm;
			} else {
				logger.warning("Invalid mzML file - could not find softwareRef '" + softwareRef + "'. Attempting to continue...");
				
				// TODO: reininstate these checks
				//throw new InvalidMzML("Can't find softwareRef '" + softwareRef + "'");
			}
		} else if(qName.equals("run")) {
			String instrumentConfigurationRef = attributes.getValue("defaultInstrumentConfigurationRef");
			
			InstrumentConfiguration instrumentConfiguration = null;
			
			try {
				instrumentConfiguration = instrumentConfigurationList.getInstrumentConfiguration(instrumentConfigurationRef);
			} catch (NullPointerException ex) {
				throw new InvalidMzML("<instrumentConfigurationList> tag not defined prior to defining <run> tag.");
			}
			
			if(instrumentConfiguration != null) {
				run = new Run(attributes.getValue("id"), instrumentConfiguration);
			} else {
				// TODO: Workaround only in place because of ABSciex converter bug where 
				// the defaultInstrumentConfigurationRef is auto-incremented in every raster
				// line file but the instrumentConfiguration id remains as 'instrumentConfiguration1'					
				if(currentInstrumentConfiguration != null) {
					logger.warning("Invalid mzML file - could not find instrumentConfigurationRef '" + instrumentConfigurationRef + "'. Attempting to continue...");
					
					run = new Run(attributes.getValue("id"), currentInstrumentConfiguration);
				} else
					throw new InvalidMzML("Can't find instrumentConfigurationRef '" + instrumentConfigurationRef + "'");
			}
			
			String defaultSourceFileRef = attributes.getValue("defaultSourceFileRef");
				
			if(defaultSourceFileRef != null) {
				SourceFile sourceFile = null;
				
				try {
					sourceFile = sourceFileList.getSourceFile(defaultSourceFileRef);
				} catch (NullPointerException ex) {
					throw new InvalidMzML("<sourceFileList> tag not defined prior to defining <run> tag.");
				}
					
				if(sourceFile != null)
					run.setDefaultSourceFileRef(sourceFile);
				else
					throw new InvalidMzML("Can't find defaultSourceFileRef '" + defaultSourceFileRef + "'.");
			}
			
			String sampleRef = attributes.getValue("sampleRef");
				
			if(sampleRef != null) {
				Sample sample = null;
				
				try {
					sample = sampleList.getSample(sampleRef);
				} catch (NullPointerException ex) {
					throw new InvalidMzML("<sampleList> tag not defined prior to defining <run> tag.");
				}
				
				if(sample != null)
					run.setSampleRef(sample);
				else
					throw new InvalidMzML("Can't find sampleRef '" + sampleRef + "'.");
			}
				
			String startTimeStamp = attributes.getValue("startTimeStamp");
			
			if(startTimeStamp != null) {
				Date parsed = new Date();
				
				try {
				    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
				    parsed = format.parse(startTimeStamp);
				    
				    run.setStartTimeStamp(parsed);
				} catch(ParseException pe) {
				    //throw new IllegalArgumentException();
				}
			}
			
			try {
				mzML.setRun(run);
			} catch (NullPointerException ex) {
				throw new InvalidMzML("<mzML> tag not defined prior to defining <run> tag.");
			}
			
			currentContent = run;
		} else if(qName.equals("spectrumList")) {
			String defaultDataProcessingRef = attributes.getValue("defaultDataProcessingRef");
			
			if(defaultDataProcessingRef != null) {
				DataProcessing dataProcessing = null;
				
				try {
					dataProcessing = dataProcessingList.getDataProcessing(defaultDataProcessingRef);
				} catch (NullPointerException ex) {
					throw new InvalidMzML("<dataProcessingList> tag not defined prior to defining <spectrumList> tag.");
				}
			
				if(dataProcessing != null)
					spectrumList = new SpectrumList(Integer.parseInt(attributes.getValue("count")), dataProcessing);
				else
					throw new InvalidMzML("Can't find defaultDataProcessingRef '" + defaultDataProcessingRef + "'.");
			} else {
				// msconvert doesn't include default data processing so try and fix it
				throw new InvalidMzML("No defaultProcessingRef attribute in spectrumList.");
				
				//spectrumList = new SpectrumList(Integer.parseInt(attributes.getValue("count")), dataProcessingList.getDataProcessing(0));
			}
			
			try {
				run.setSpectrumList(spectrumList);
			} catch (NullPointerException ex) {
				throw new InvalidMzML("<run> tag not defined prior to defining <spectrumList> tag.");
			}
		} else if(qName.equals("spectrum")) {
			currentSpectrum = new Spectrum(attributes.getValue("id"), Integer.parseInt(attributes.getValue("defaultArrayLength")), Integer.parseInt(attributes.getValue("index")));
			
			String dataProcessingRef = attributes.getValue("dataProcessingRef");
			
			if(dataProcessingRef != null) {
				DataProcessing dataProcessing = null;
				
				try {
					dataProcessing = dataProcessingList.getDataProcessing(dataProcessingRef);
				} catch (NullPointerException ex) {
					throw new InvalidMzML("<dataProcessingList> tag not defined prior to defining <spectrum> tag.");
				}
				
				if(dataProcessing != null)
					currentSpectrum.setDataProcessingRef(dataProcessing);
				else
					throw new InvalidMzML("Can't find dataProcessingRef '" + dataProcessingRef + "' referenced in spectrum '" + currentSpectrum.getID() + "'");
			}
			
			String sourceFileRef = attributes.getValue("sourceFileRef");
			
			if(sourceFileRef != null) {
				SourceFile sourceFile = null;
				
				try {
					sourceFile = sourceFileList.getSourceFile(sourceFileRef);
				} catch (NullPointerException ex) {
					throw new InvalidMzML("<sourceFileList> tag not defined prior to defining <spectrum> tag.");
				}
				
				if(sourceFile != null)
					currentSpectrum.setSourceFileRef(sourceFile);
				else
					throw new InvalidMzML("Can't find sourceFileRef '" + sourceFileRef + "' referenced in spectrum '" + currentSpectrum.getID() + "'");
			}
			
			if(attributes.getValue("spotID") != null)
				currentSpectrum.setSpotID(attributes.getValue("spotID"));
			
			processingSpectrum = true;
			
			try {
				spectrumList.addSpectrum(currentSpectrum);
			} catch (NullPointerException ex) {
				throw new InvalidMzML("<spectrumList> tag not defined prior to defining <spectrum> tag.");
			}			
			
			currentContent = currentSpectrum;
		} else if(qName.equals("scanList")) {
			currentScanList = new ScanList(Integer.parseInt(attributes.getValue("count")));
			
			try {
				currentSpectrum.setScanList(currentScanList);
			} catch (NullPointerException ex) {
				throw new InvalidMzML("<spectrum> tag not defined prior to defining <scanList> tag.");
			}
			
			currentContent = currentScanList;
		} else if(qName.equals("scan")) {
			currentScan = new Scan();
			
			if(attributes.getValue("externalSpectrumID") != null)
				currentScan.setExternalSpectrumID(attributes.getValue("externalSpectrumID"));
			
			String instrumentConfigurationRef = attributes.getValue("instrumentConfigurationRef");
			
			if(instrumentConfigurationRef != null) {
				InstrumentConfiguration instrumentConfiguration = null;
				
				try {
					instrumentConfiguration = instrumentConfigurationList.getInstrumentConfiguration(instrumentConfigurationRef);
				} catch (NullPointerException ex) {
					throw new InvalidMzML("<instrumentConfigurationList> tag not defined prior to defining <scan> tag.");
				}
				
				if(instrumentConfiguration != null)
					currentScan.setInstrumentConfigurationRef(instrumentConfiguration);
				else {
					// TODO: Workaround only in place because of ABSciex converter bug where 
					// the defaultInstrumentConfigurationRef is auto-incremented in every raster
					// line file but the instrumentConfiguration id remains as 'instrumentConfiguration1'					
					if(currentInstrumentConfiguration != null)
						currentScan.setInstrumentConfigurationRef(currentInstrumentConfiguration);
					else					
						throw new InvalidMzML("Can't find instrumentConfigurationRef '" + instrumentConfigurationRef + "' referenced in scan.");
				}
			} else {
				InstrumentConfiguration defaultIC = run.getDefaultInstrumentConfiguration();
				
				if(defaultIC != null)
					currentScan.setInstrumentConfigurationRef(defaultIC);			
			}
			
			String sourceFileRef = attributes.getValue("sourceFileRef");
			
			if(sourceFileRef != null) {
				SourceFile sourceFile = null;
				
				try {
					sourceFile = sourceFileList.getSourceFile(sourceFileRef);
				} catch (NullPointerException ex) {
					throw new InvalidMzML("<sourceFileList> tag not defined prior to defining <scan> tag.");
				}
				
				if(sourceFile != null)
					currentScan.setSourceFileRef(sourceFile);
				else
					throw new InvalidMzML("Can't find sourceFileRef '" + sourceFileRef + "' referenced in scan.");
			}				
			
			if(attributes.getValue("spectrumRef") != null)
				currentScan.setSpectrumRef(attributes.getValue("spectrumRef"));
			
			try {
				currentScanList.addScan(currentScan);
			} catch (NullPointerException ex) {
				throw new InvalidMzML("<scanList> tag not defined prior to defining <scan> tag.");
			}
			
			currentContent = currentScan;
		} else if(qName.equals("scanWindowList")) {
			currentScanWindowList = new ScanWindowList(Integer.parseInt(attributes.getValue("count")));
			
			try {
				currentScan.setScanWindowList(currentScanWindowList);
			} catch (NullPointerException ex) {
				throw new InvalidMzML("<scan> tag not defined prior to defining <scanWindowList> tag.");
			}
		} else if(qName.equals("scanWindow")) {
			ScanWindow scanWindow = new ScanWindow();
			
			try {
				currentScanWindowList.addScanWindow(scanWindow);
			} catch (NullPointerException ex) {
				throw new InvalidMzML("<scanWindowList> tag not defined prior to defining <scanWindow> tag.");
			}
			
			currentContent = scanWindow;
		} else if(qName.equals("precursorList")) {
			currentPrecursorList = new PrecursorList(Integer.parseInt(attributes.getValue("count")));
			
			try {
				currentSpectrum.setPrecursorList(currentPrecursorList);
			} catch (NullPointerException ex) {
				throw new InvalidMzML("<spectrum> tag not defined prior to defining <precursorList> tag.");
			}
		} else if(qName.equals("precursor")) {
			processingPrecursor = true;
			currentPrecursor = new Precursor();
			
			if(attributes.getValue("externalSpectrumID") != null)
				currentPrecursor.setExternalSpectrumID(attributes.getValue("externalSpectrumID"));
			
			String sourceFileRef = attributes.getValue("sourceFileRef");
			
			if(sourceFileRef != null) {
				SourceFile sourceFile = null; 
				
				try {
					sourceFile = sourceFileList.getSourceFile(sourceFileRef);
				} catch (NullPointerException ex) {
					throw new InvalidMzML("<sourceFileList> tag not defined prior to defining <precursor> tag.");
				}
				
				if(sourceFile != null)
					currentPrecursor.setSourceFileRef(sourceFile);
				else
					throw new InvalidMzML("Can't find sourceFileRef '" + sourceFileRef + "' referenced in precursor.");
			}
			
			if(attributes.getValue("spectrumRef") != null)
				currentPrecursor.setSpectrumRef(attributes.getValue("spectrumRef"));
			
			if(processingSpectrum) {
				try {
					currentPrecursorList.addPrecursor(currentPrecursor);
				} catch (NullPointerException ex) {
					throw new InvalidMzML("<precursorList> tag not defined prior to defining <precursor> tag.");
				}
			} else if(processingChromatogram) {
				try {
					currentChromatogram.setPrecursor(currentPrecursor);
				} catch (NullPointerException ex) {
					throw new InvalidMzML("<chromatogram> tag not defined prior to defining <precursor> tag.");
				}
			}
		} else if(qName.equals("isolationWindow")) {
			IsolationWindow isolationWindow = new IsolationWindow();
			
			if(processingPrecursor) {
				try {
					currentPrecursor.setIsolationWindow(isolationWindow);
				} catch (NullPointerException ex) {
					throw new InvalidMzML("<precursor> tag not defined prior to defining <isolationWindow> tag.");
				}
			} else if(processingProduct) {
				try {
					currentProduct.setIsolationWindow(isolationWindow);
				} catch (NullPointerException ex) {
					throw new InvalidMzML("<product> tag not defined prior to defining <isolationWindow> tag.");
				}
			}
			
			currentContent = isolationWindow;
		} else if(qName.equals("selectedIonList")) {
			currentSelectedIonList = new SelectedIonList(Integer.parseInt(attributes.getValue("count")));
			
			try {
				currentPrecursor.setSelectedIonList(currentSelectedIonList);
			} catch (NullPointerException ex) {
				throw new InvalidMzML("<precursor> tag not defined prior to defining <selectedIonList> tag.");
			}
		} else if(qName.equals("selectedIon")) {
			SelectedIon selectedIon = new SelectedIon();
			
			try {
				currentSelectedIonList.addSelectedIon(selectedIon);
			} catch (NullPointerException ex) {
				throw new InvalidMzML("<selectedIonList> tag not defined prior to defining <selectedIon> tag.");
			}
			
			currentContent = selectedIon;
		} else if(qName.equals("activation")) {
			Activation activation = new Activation();
			
			try {
				currentPrecursor.setActivation(activation);
			} catch (NullPointerException ex) {
				throw new InvalidMzML("<precursor> tag not defined prior to defining <activation> tag.");
			}
			
			currentContent = activation;
		} else if(qName.equals("productList")) {
			currentProductList = new ProductList(Integer.parseInt(attributes.getValue("count")));
			
			try {
				currentSpectrum.setProductList(currentProductList);
			} catch (NullPointerException ex) {
				throw new InvalidMzML("<spectrum> tag not defined prior to defining <productList> tag.");
			}
		} else if(qName.equals("product")) {
			processingProduct = true;
			currentProduct = new Product();
			
			if(processingSpectrum) {
				try {
					currentProductList.addProduct(currentProduct);
				} catch (NullPointerException ex) {
					throw new InvalidMzML("<productList> tag not defined prior to defining <product> tag.");
				}
			} else if(processingChromatogram) {
				try {
					currentChromatogram.setProduct(currentProduct);
				} catch (NullPointerException ex) {
					throw new InvalidMzML("<chromatogram> tag not defined prior to defining <product> tag.");
				}
			}
		} else if(qName.equals("binaryDataArrayList")) {
			currentBinaryDataArrayList = new BinaryDataArrayList(Integer.parseInt(attributes.getValue("count")));
			
			if(processingSpectrum) {
				try {
					currentSpectrum.setBinaryDataArrayList(currentBinaryDataArrayList);
				} catch (NullPointerException ex) {
					throw new InvalidMzML("<spectrum> tag not defined prior to defining <binaryDataArrayList> tag.");
				}
			} else if(processingChromatogram) {
				try {
					currentChromatogram.setBinaryDataArrayList(currentBinaryDataArrayList);
				} catch (NullPointerException ex) {
					throw new InvalidMzML("<chromatogram> tag not defined prior to defining <binaryDataArrayList> tag.");
				}
			}
		} else if(qName.equals("binaryDataArray")) {
			currentBinaryDataArray = new BinaryDataArray(Integer.parseInt(attributes.getValue("encodedLength")));
			
			if(attributes.getValue("arrayLength") != null)
				currentBinaryDataArray.setArrayLength(Integer.parseInt(attributes.getValue("arrayLength")));
			
			String dataProcessingRef = attributes.getValue("dataProcessingRef");
			
			if(dataProcessingRef != null) {
				DataProcessing dataProcessing = null;
				
				try {
					dataProcessing = dataProcessingList.getDataProcessing(dataProcessingRef);
				} catch (NullPointerException ex) {
					throw new InvalidMzML("<dataProcessingList> tag not defined prior to defining <binaryDataArray> tag.");
				}
				
				if(dataProcessing != null)
					currentBinaryDataArray.setDataProcessingRef(dataProcessing);
				else
					throw new InvalidMzML("Can't find dataProcessingRef '" + dataProcessingRef + "' referenced by binaryDataArray.");
			}
			
			try {
				currentBinaryDataArrayList.addBinaryDataArray(currentBinaryDataArray);
			} catch (NullPointerException ex) {
				throw new InvalidMzML("<binaryDataArrayList> tag not defined prior to defining <binaryDataArray> tag.");
			}
			
			currentContent = currentBinaryDataArray;
		} else if(qName.equals("binary")) {
			// Ignore binary data for the header
		} else if(qName.equals("chromatogramList")) {
			String defaultDataProcessingRef = attributes.getValue("defaultDataProcessingRef");
			
			if(defaultDataProcessingRef != null) {
				DataProcessing dataProcessing = null;
				
				try {
					dataProcessing = dataProcessingList.getDataProcessing(defaultDataProcessingRef);
				} catch (NullPointerException ex) {
					throw new InvalidMzML("<dataProcessingList> tag not defined prior to defining <chromatogramList> tag.");
				}
						
				if(dataProcessing != null)
					chromatogramList = new ChromatogramList(Integer.parseInt(attributes.getValue("count")), dataProcessing);
				else
					throw new InvalidMzML("Can't find defaultDataProcessingRef '" + defaultDataProcessingRef + "' referenced by chromatogramList.");
			} else {
				// msconvert doesn't include default data processing so try and fix it				
				throw new InvalidMzML("No defaultProcessingRef attribute in chromatogramList.");
				
				//chromatogramList = new ChromatogramList(Integer.parseInt(attributes.getValue("count")), dataProcessingList.getDataProcessing(0));
			}
			
			try {
				run.setChromatogramList(chromatogramList);
			} catch (NullPointerException ex) {
				throw new InvalidMzML("<run> tag not defined prior to defining <chromatogramList> tag.");
			}
		} else if(qName.equals("chromatogram")) {
			processingChromatogram = true;
			currentChromatogram = new Chromatogram(attributes.getValue("id"), Integer.parseInt(attributes.getValue("defaultArrayLength")), Integer.parseInt(attributes.getValue("index")));
			
			String dataProcessingRef = attributes.getValue("dataProcessingRef");
			
			if(dataProcessingRef != null) {
				DataProcessing dataProcessing = null;
				
				try {
					dataProcessing = dataProcessingList.getDataProcessing(dataProcessingRef);
				} catch (NullPointerException ex) {
					throw new InvalidMzML("<dataProcessingList> tag not defined prior to defining <chromatogram> tag.");
				}
				
				if(dataProcessing != null)
					currentChromatogram.setDataProcessingRef(dataProcessing);
				else
					throw new InvalidMzML("Can't find dataProcessingRef '" + dataProcessingRef + "' referenced by chromatogram '" + currentChromatogram.getID() + "'.");
			}
			
			try {
				chromatogramList.addChromatogram(currentChromatogram);
			} catch (NullPointerException ex) {
				throw new InvalidMzML("<chromatogramList> tag not defined prior to defining <chromatogram> tag.");
			}
			
			currentContent = currentChromatogram;
                } else if(qName.equals("offset") || qName.equals("indexListOffset")) {
                    if(qName.equals("offset")) {
			previousOffsetIDRef = currentOffsetIDRef;
			
                        this.currentOffsetIDRef = attributes.getValue("idRef");
		    }
                    
                    offsetData.setLength(0);
                    processingOffset = true;
		} else if(qName.equals("indexedmzML") || qName.equals("indexList") || qName.equals("index") || qName.equals("indexListOffset")) {
		} else {
			logger.info("No processing for tag <" + qName + ">");
		}
	}

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            if(processingOffset) {
                offsetData.append(ch, start, length);
            }
	}
        
        @Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
            switch (qName) {
                case "spectrum":
                    processingSpectrum = false;
                    break;
                case "chromatogram":
                    processingChromatogram = false;
                    break;
                case "precursor":
                    processingPrecursor = false;
                    break;
                case "product":
                    processingProduct = false;
                    break;
                case "binaryDataArrayList":
                    currentBinaryDataArrayList.updatemzAndIntensityArray();
                    break;
                case "offset":
		case "indexListOffset":
                    //System.out.println("[" + offsetData.toString() + "] " + spectrumList.getSpectrum(currentOffsetIDRef));
                    
//		    System.out.println("OffsetData: " + offsetData);
		    
		    long offset = Long.parseLong(offsetData.toString());
		    
		    Spectrum spectrum = spectrumList.getSpectrum(previousOffsetIDRef);
		    
                    if(previousOffset != -1) {
			DataLocation dataLocation = new DataLocation(dataStorage, previousOffset, (int)(offset-previousOffset));
			
//			System.out.println(previousOffsetIDRef + " " + dataLocation);
			
			spectrum.setDataLocation(dataLocation);
                    }
                    
		    previousOffset = offset;
                    processingOffset = false;
                    break;
            }
	}
	
//	public int getSpectrumCount() {
//		return spectrumCount;
//	}
	
	public MzML getmzML() {
		return mzML;
	}

	public static void main(String[] args) {
		String filename = "/home/alan/workspace/imzMLConverter/RianRasterLine.mzML";
		File mzMLFile = new File(filename);
		File temporaryBinaryFile = new File(filename + ".tmp");
		
		OBO obo = new OBO("imagingMS.obo");
		MzMLHandler headerHandler;
		try {
			headerHandler = new MzMLHandler(obo, temporaryBinaryFile);
				
			SAXParserFactory sspf = SAXParserFactory.newInstance();
			try {
			
				//get a new instance of parser
				SAXParser sp = sspf.newSAXParser();
				
				//parse the file and also register this class for call backs
				sp.parse(mzMLFile, headerHandler);			
			}catch(SAXException se) {
				se.printStackTrace();
			}catch(ParserConfigurationException pce) {
				pce.printStackTrace();
			}catch (IOException ie) {
				ie.printStackTrace();
			}
			
			try {
				String encoding = "ISO-8859-1";
				
				OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream("RianTest.mzML"), encoding);
				BufferedWriter output = new BufferedWriter(out);
				
				//writer = new FileWriter(imzMLFilename + ".imzML");
	//			System.out.println(out.getEncoding() + " - " + xo.getFormat().getEncoding());
	//			xo.output(new Document(mzMLElement), out);
				
				output.write("<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>\n");
				headerHandler.getmzML().outputXML(output, 0);
				
				output.flush();
				output.close();
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			//temporaryBinaryFile.delete();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
