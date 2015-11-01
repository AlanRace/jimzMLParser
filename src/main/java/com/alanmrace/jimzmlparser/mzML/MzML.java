package com.alanmrace.jimzmlparser.mzML;

//import com.alanmrace.jimzmlparser.exceptions.ImzMLConversionException;
import com.alanmrace.jimzmlparser.exceptions.ImzMLWriteException;
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
import java.util.logging.Level;
import java.util.logging.Logger;

public class MzML extends MzMLContent implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static String namespace = "http://psi.hupo.org/ms/mzml";
    public static String xsi = "http://www.w3.org/2001/XMLSchema-instance";
    public static String schemaLocation = "http://psi.hupo.org/ms/mzml http://psidev.info/files/ms/mzML/xsd/mzML1.1.0_idx.xsd";

    public static String currentVersion = "1.1.0";

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

//	@JsonIgnore
    public void setOBO(OBO obo) {
	this.obo = obo;
    }

//	@JsonIgnore
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

//	public void addReferenceableParamGroup(ReferenceableParamGroup rpg) {
//		referenceableParamGroupList.add(rpg);
//	}
//	
//	public int getReferenceableParamGroupCount() {
//		return referenceableParamGroupList.size();
//	}
//	
//	public ReferenceableParamGroup getReferenceableParamGroup(int index) {
//		return referenceableParamGroupList.getReferenceableParamGroup(index);
//	}
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

//	public ReferenceableParamGroup getReferenceableParamGroup(String id) {
//		return referenceableParamGroupList.getReferenceableParamGroup(id);
//	}
//	
//	public void removeReferenceableParamGroup(int index) {
//		referenceableParamGroupList.remove(index);
//	}
//	
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

//	public void addSoftware(Software sw) {
//		softwareList.add(sw);
//	}
//	
//	public int getSoftwareCount() {
//		return softwareList.size();
//	}
//	
//	public Software getSoftware(int index) {
//		return softwareList.get(index);
//	}
//	
//	public void removeSoftware(int index) {
//		softwareList.remove(index);
//	}
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

//	public void addInstrumentConfiguration(InstrumentConfiguration ic) {
//		instrumentConfigurationList.add(ic);
//	}
//	
//	public int getInstrumentConfigurationCount() {
//		return instrumentConfigurationList.size();
//	}
//	
//	public InstrumentConfiguration getInstrumentConfiguration(int index) {
//		return instrumentConfigurationList.get(index);
//	}
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

//	public void addDataProcessing(DataProcessing dp) {
//		dataProcessingList.add(dp);
//	}
//	
//	public int getDataProcessingCount() {
//		return dataProcessingList.size();
//	}
//	
//	public DataProcessing getDataProcessing(int index) {
//		return dataProcessingList.get(index);
//	}
//	
//	public void removeDataProcessing(int index) {
//		dataProcessingList.remove(index);
//	}
    public void setRun(Run run) {
	run.setParent(this);

	this.run = run;
    }

    public Run getRun() {
	return run;
    }

//	public Sample getSample(String sampleRef) {
//		for(Sample sample : sampleList)
//			if(sample.getID().equals(sampleRef))
//				return sample;
//		
//		return null;
//	}
//	
//	public Software getSoftware(String softwareRef) {
//		for(Software software : softwareList)
//			if(software.getID().equals(softwareRef))
//				return software;
//		
//		return null;
//	}
//	
//	public InstrumentConfiguration getInstrumentConfiguration(String instrumentConfigurationRef) {
//		for(InstrumentConfiguration ic : instrumentConfigurationList)
//			if(ic.getID().equals(instrumentConfigurationRef))
//				return ic;
//		
//		return null;
//	}
//
//	public DataProcessing getDataProcessing(String dataProcessingRef) {
//		return dataProcessingList.get(dataProcessingRef);
//	}
    public void write(String filename) throws ImzMLWriteException {
	try {
	    String encoding = "ISO-8859-1";

	    OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(filename), encoding);
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
    }

    // Need to include cvList, referenceableParamGroupList, etc..
    private int getAdditionalChildrenCount() {
	int additionalChildren = ((cvList != null) ? 1 : 0)
		+ ((fileDescription != null) ? 1 : 0)
		+ ((referenceableParamGroupList != null) ? 1 : 0)
		+ ((sampleList != null) ? 1 : 0)
		+ ((softwareList != null) ? 1 : 0)
		+ ((scanSettingsList != null) ? 1 : 0)
		+ ((instrumentConfigurationList != null) ? 1 : 0)
		+ ((dataProcessingList != null) ? 1 : 0)
		+ ((run != null) ? 1 : 0);

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
//		if(cvList != null)
//			children.add(cvList);
//		if(fileDescription != null)
//			children.add(fileDescription);
//		if(referenceableParamGroupList != null)
//			children.add(referenceableParamGroupList);
//		if(sampleList != null)
//			children.add(sampleList);
//		if(softwareList != null)
//			children.add(softwareList);
//		if(scanSettingsList != null)
//			children.add(scanSettingsList);
//		if(instrumentConfigurationList != null)
//			children.add(instrumentConfigurationList);
//		if(dataProcessingList != null)
//			children.add(dataProcessingList);
//		if(run != null)
//			children.add(run);
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
//			if(cvList != null) {
//				if(counter == index)
//					return cvList;
//				
//				counter++;
//			}
//			if(fileDescription != null) {
//				if(counter == index)
//					return fileDescription;
//				
//				counter++;
//			}
//			if(referenceableParamGroupList != null) {
//				if(counter == index)
//					return referenceableParamGroupList;
//				
//				counter++;
//			}
//			if(sampleList != null) {
//				if(counter == index)
//					return sampleList;
//				
//				counter++;
//			}
//			if(softwareList != null) {
//				if(counter == index)
//					return softwareList;
//				
//				counter++;
//			}
//			if(scanSettingsList != null) {
//				if(counter == index)
//					return scanSettingsList;
//				
//				counter++;
//			}
//			if(instrumentConfigurationList != null) {
//				if(counter == index)
//					return instrumentConfigurationList;
//				
//				counter++;
//			}
//			if(dataProcessingList != null) {
//				if(counter == index)
//					return dataProcessingList;
//				
//				counter++;
//			}
//			if(run != null) {
//				if(counter == index)
//					return run;
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
//		if(childNode instanceof CVParam)
//			return counter;
//		
//		if(cvList != null)
//			counter++;
//		
//		if(childNode instanceof FileDescription)
//			return counter;
//		
//		if(fileDescription != null)
//			counter++;
//		
//		if(childNode instanceof ReferenceableParamGroupList)
//			return counter;
//		
//		if(referenceableParamGroupList != null)
//			counter++;
//			
//		if(childNode instanceof SampleList)
//			return counter;
//		
//		if(sampleList != null)
//			counter++;
//		
//		if(childNode instanceof SoftwareList)
//			return counter;
//		
//		if(softwareList != null)
//			counter++;
//		
//		if(childNode instanceof ScanSettingsList)
//			return counter;
//		
//		if(scanSettingsList != null)
//			counter++;
//		
//		if(childNode instanceof InstrumentConfigurationList)
//			return counter;
//		
//		if(instrumentConfigurationList != null)
//			counter++;
//			
//		if(childNode instanceof DataProcessingList)
//			return counter;
//		
//		if(dataProcessingList != null)
//			counter++;
//		
//		if(childNode instanceof Run)
//			return counter;
//		
//		if(run != null)
//			counter++;
//
//		return super.getIndex(childNode);
//	}
    @Override
    public String toString() {
	return "mzML";
    }

    // Clean up by closing any open DataStorage
    public void close() {
	SpectrumList spectrumList = getRun().getSpectrumList();

	if (spectrumList.size() > 0) {
	    Spectrum spectrum = spectrumList.getSpectrum(0);
	    DataLocation dataLocation = spectrum.getDataLocation();

	    closeDataStorage(dataLocation);

	    BinaryDataArrayList bdal = spectrum.getBinaryDataArrayList();

	    if (bdal.size() > 0) {
		BinaryDataArray bda = bdal.getBinaryDataArray(0);
		dataLocation = bda.getDataLocation();

		closeDataStorage(dataLocation);
	    }
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
