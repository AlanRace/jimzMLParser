package com.alanmrace.jimzmlparser.mzML;

//import imzMLConverter.OBO;
//import imzMLConverter.OBOTermValue;
import com.alanmrace.jimzmlparser.exceptions.InvalidXPathException;
import com.alanmrace.jimzmlparser.exceptions.UnfollowableXPathException;
import com.alanmrace.jimzmlparser.util.XMLHelper;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class Scan extends MzMLContent implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static String scanAttributeID = "MS:1000503";
    public static String scanDirectionID = "MS:1000018";
    public static String scanLawID = "MS:1000019";
    public static String positionXID = "IMS:1000050";
    public static String positionYID = "IMS:1000051";
    public static String positionZID = "IMS:1000052";

    public static String elutionTimeID = "MS:1000826";
    public static String scanStartTimeID = "MS:1000016";
    public static String ionMobilityDriftTimeID = "MS:1002476";

    private String externalSpectrumID;
    private InstrumentConfiguration instrumentConfigurationRef;
    private SourceFile sourceFileRef;
    private String spectrumRef;

    private ScanWindowList scanWindowList;

    public Scan() {
        super();
    }

    public Scan(Scan scan, ReferenceableParamGroupList rpgList, InstrumentConfigurationList icList, SourceFileList sourceFileList) {
        super(scan, rpgList);

        this.externalSpectrumID = scan.externalSpectrumID;
        this.spectrumRef = scan.spectrumRef;

        if (scan.instrumentConfigurationRef != null && icList != null) {
            for (InstrumentConfiguration ic : icList) {
                if (scan.instrumentConfigurationRef.getID().equals(ic.getID())) {
                    this.instrumentConfigurationRef = ic;

                    break;
                }
            }
        }

        if (scan.sourceFileRef != null && sourceFileList != null) {
            for (SourceFile sourceFile : sourceFileList) {
                if (scan.sourceFileRef.getID().equals(sourceFile.getID())) {
                    this.sourceFileRef = sourceFile;

                    break;
                }
            }
        }

        if (scan.scanWindowList != null) {
            scanWindowList = new ScanWindowList(scan.scanWindowList, rpgList);
        }
    }

    public ArrayList<OBOTermInclusion> getListOfRequiredCVParams() {
        ArrayList<OBOTermInclusion> required = new ArrayList<OBOTermInclusion>();
        required.add(new OBOTermInclusion(positionXID, true, false, true));
        required.add(new OBOTermInclusion(positionYID, true, false, true));

        return required;
    }

    public ArrayList<OBOTermInclusion> getListOfOptionalCVParams() {
        ArrayList<OBOTermInclusion> optional = new ArrayList<OBOTermInclusion>();
        optional.add(new OBOTermInclusion(positionZID, true, false, true));
        optional.add(new OBOTermInclusion(scanAttributeID, false, true, false));
        optional.add(new OBOTermInclusion(scanDirectionID, false, true, false));
        optional.add(new OBOTermInclusion(scanLawID, true, true, false));

        return optional;
    }

    public void setExternalSpectrumID(String externalSpectrumID) {
        this.externalSpectrumID = externalSpectrumID;
    }

    public String getExternalSpectrumID() {
        return externalSpectrumID;
    }

    public void setInstrumentConfigurationRef(InstrumentConfiguration instrumentConfigurationRef) {
        this.instrumentConfigurationRef = instrumentConfigurationRef;
    }

    public InstrumentConfiguration getInstrumentConfigurationRef() {
        return instrumentConfigurationRef;
    }

    public void setSourceFileRef(SourceFile sourceFileRef) {
        this.sourceFileRef = sourceFileRef;
    }

    public SourceFile getSourceFileRef() {
        return sourceFileRef;
    }

    public void setSpectrumRef(String spectrumRef) {
        this.spectrumRef = spectrumRef;
    }

    public void setScanWindowList(ScanWindowList scanWindowList) {
        scanWindowList.setParent(this);

        this.scanWindowList = scanWindowList;
    }

    public ScanWindowList getScanWindowList() {
        return scanWindowList;
    }

    public InstrumentConfiguration getInstrumentConfiguration() {
        return instrumentConfigurationRef;
    }

    public SourceFile getSourceFile() {
        return sourceFileRef;
    }

//	public void addScanWindow(ScanWindow sw) {
//		scanWindowList.add(sw);
//	}
//	public void setXYPosition(OBO obo, int x, int y) {
//		addCvParam(new OBOTermValue(obo.getTerm(positionXID), ""+x));
//		addCvParam(new OBOTermValue(obo.getTerm(positionYID), ""+y));
//	}
//	
//	public int getXPosition() {
//		ArrayList<OBOTermValue> xPos = getChildrenOf(positionXID);
//		
//		if(xPos.size() == 0)
//			return -1;
//		
//		return Integer.parseInt(xPos.get(0).getValue());
//	}
//	
//	public int getYPosition() {
//		ArrayList<OBOTermValue> yPos = getChildrenOf(positionYID);
//		
//		if(yPos.size() == 0)
//			return -1;
//		
//		return Integer.parseInt(yPos.get(0).getValue());
//	}
//	
//	public double getScanStartTime() {
//		ArrayList<OBOTermValue> scanStartTime = this.getChildrenOf(scanStartTimeID);
//		
//		if(scanStartTime.size() > 0)
//			return Double.parseDouble(scanStartTime.get(0).getValue());
//		
//		return -1;
//	}
    
    @Override
    protected void addTagSpecificElementsAtXPathToCollection(Collection<MzMLContent> elements, String fullXPath, String currentXPath) throws InvalidXPathException {
        if (currentXPath.startsWith("/scanWindowList")) {
            if (scanWindowList == null) {
                throw new UnfollowableXPathException("No scanWindowList exists, so cannot go to " + fullXPath, fullXPath, currentXPath);
            }

            scanWindowList.addElementsAtXPathToCollection(elements, fullXPath, currentXPath);
        }
    }

    @Override
    public void outputXML(BufferedWriter output, int indent) throws IOException {
        MzMLContent.indent(output, indent);
        output.write("<scan");
        if (externalSpectrumID != null) {
            output.write(" externalSpectrumID=\"" + XMLHelper.ensureSafeXML(externalSpectrumID) + "\"");
        }
        if (instrumentConfigurationRef != null) {
            output.write(" instrumentConfigurationRef=\"" + XMLHelper.ensureSafeXML(instrumentConfigurationRef.getID()) + "\"");
        }
        if (sourceFileRef != null) {
            output.write(" sourceFileRef=\"" + XMLHelper.ensureSafeXML(sourceFileRef.getID()) + "\"");
        }
        if (spectrumRef != null) {
            output.write(" spectrumRef=\"" + XMLHelper.ensureSafeXML(spectrumRef) + "\"");
        }
        output.write(">\n");

        super.outputXML(output, indent + 1);

        if (scanWindowList != null && scanWindowList.size() > 0) {
            scanWindowList.outputXML(output, indent + 1);
        }

        MzMLContent.indent(output, indent);
        output.write("</scan>\n");
    }

    @Override
    public String toString() {
        return "scan: "
                + ((externalSpectrumID != null && !externalSpectrumID.isEmpty()) ? (" externalSpectrumID=\"" + externalSpectrumID + "\"") : "")
                + ((instrumentConfigurationRef != null) ? (" instrumentConfigurationRef=\"" + instrumentConfigurationRef.getID() + "\"") : "")
                + ((sourceFileRef != null) ? (" sourceFileRef=\"" + sourceFileRef.getID() + "\"") : "")
                + ((spectrumRef != null && !spectrumRef.isEmpty()) ? (" spectrumRef=\"" + spectrumRef + "\"") : "");
    }

    @Override
    public String getTagName() {
        return "scan";
    }
    
    @Override
    public void addChildrenToCollection(Collection<MzMLTag> children) {
        if(scanWindowList != null)
            children.add(scanWindowList);
        
        super.addChildrenToCollection(children);
    }
}
