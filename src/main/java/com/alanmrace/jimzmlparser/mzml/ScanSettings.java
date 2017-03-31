package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.util.XMLHelper;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class ScanSettings extends MzMLContent implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static String lineScanDirectionID = "IMS:1000049";
    public static String lineScanDirectionLeftRightID = "IMS:1000491";
    public static String lineScanDirectionRightLeftID = "IMS:1000490";
    public static String lineScanDirectionTopDownID = "IMS:1000493";
    public static String lineScanDirectionBottomUpID = "IMS:1000492";

    public static String scanDirectionID = "IMS:1000040";
    public static String scanDirectionTopDownID = "IMS:1000401";
    public static String scanDirectionBottomUpID = "IMS:1000400";
    public static String scanDirectionLeftRightID = "IMS:1000402";
    public static String scanDirectionRightLeftID = "IMS:1000403";

    public static String scanPatternID = "IMS:1000041";
    public static String scanPatternFlybackID = "IMS:1000413";
    public static String scanPatternMeanderingID = "IMS:1000410";
    public static String scanPatternRandomAccessID = "IMS:1000412";

    public static String scanTypeID = "IMS:1000048";
    public static String scanTypeVerticalID = "IMS:1000481";

    public static String imageID = "IMS:1000004";

    public static String maxCountPixelXID = "IMS:1000042";
    public static String maxCountPixelYID = "IMS:1000043";
    public static String maxDimensionXID = "IMS:1000044";
    public static String maxDimensionYID = "IMS:1000045";

    public static String pixelAreaID = "IMS:1000046";

    // Attributes
    private String id;	// Required

    // Sub-elements
    private SourceFileRefList sourceFileRefList;
    private TargetList targetList;

//	public ScanSettings() {
//		super();
//		
//		id = "ss" + scanSettingsID++;
//	}
    public ScanSettings(String id) {
        this.id = id;
    }

    public ScanSettings(ScanSettings scanSettings, ReferenceableParamGroupList rpgList, SourceFileList sourceFileList) {
        super(scanSettings, rpgList);

        this.id = scanSettings.id;

        if (scanSettings.sourceFileRefList != null && sourceFileList != null) {
            sourceFileRefList = new SourceFileRefList(scanSettings.sourceFileRefList, sourceFileList);
        }

        if (scanSettings.targetList != null) {
            targetList = new TargetList(scanSettings.targetList, rpgList);
        }
    }

    @Override
    public ArrayList<OBOTermInclusion> getListOfRequiredCVParams() {
        ArrayList<OBOTermInclusion> required = new ArrayList<OBOTermInclusion>();
        required.add(new OBOTermInclusion(lineScanDirectionID, true, true, false));
        required.add(new OBOTermInclusion(scanDirectionID, true, true, false));
        required.add(new OBOTermInclusion(scanPatternID, true, true, false));
        required.add(new OBOTermInclusion(scanTypeID, true, true, false));

        return required;
    }

    @Override
    public ArrayList<OBOTermInclusion> getListOfOptionalCVParams() {
        ArrayList<OBOTermInclusion> optional = new ArrayList<OBOTermInclusion>();
        optional.add(new OBOTermInclusion(imageID, false, true, false));

        return optional;
    }

    public String getID() {
        return id;
    }

    public void setSourceFileRefList(SourceFileRefList sourceFileRefList) {
        sourceFileRefList.setParent(this);

        this.sourceFileRefList = sourceFileRefList;
    }

    public void setTargetList(TargetList targetList) {
        targetList.setParent(this);

        this.targetList = targetList;
    }

//	public void setmaximumXY(OBO obo, int x, int y) {
//		addCvParam(new OBOTermValue(obo.getTerm(maxCountPixelXID), ""+x));
//		addCvParam(new OBOTermValue(obo.getTerm(maxCountPixelYID), ""+y));
//	}
    public CVParam getLineScanDirection() {
        return getCVParamOrChild(lineScanDirectionID);
    }

    public CVParam getScanDirection() {
        return getCVParamOrChild(scanDirectionID);
    }

    public CVParam getScanPattern() {
        return getCVParamOrChild(scanPatternID);
    }

    public CVParam getScanType() {
        return getCVParamOrChild(scanTypeID);
    }

    @Override
    public void outputXML(BufferedWriter output, int indent) throws IOException {
        MzMLContent.indent(output, indent);
        output.write("<scanSettings");
        output.write(" id=\"" + XMLHelper.ensureSafeXML(id) + "\"");
        output.write(">\n");

        super.outputXML(output, indent + 1);

        if (sourceFileRefList != null && sourceFileRefList.size() > 0) {
            sourceFileRefList.outputXML(output, indent + 1);
        }

        if (targetList != null && targetList.size() > 0) {
            targetList.outputXML(output, indent + 1);
        }

        MzMLContent.indent(output, indent);
        output.write("</scanSettings>\n");
    }

    @Override
    public String toString() {
        return "scanSettings: " + id;
    }

    @Override
    public String getTagName() {
        return "scanSettings";
    }
    
    @Override
    public void addChildrenToCollection(Collection<MzMLTag> children) {
        if(sourceFileRefList != null)
            children.add(sourceFileRefList);
        if(targetList != null)
            children.add(targetList);
        
        super.addChildrenToCollection(children);
    }
}
