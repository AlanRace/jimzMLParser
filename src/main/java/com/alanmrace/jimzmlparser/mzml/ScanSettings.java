package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.util.XMLHelper;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Class describing a {@literal <scanSettings>} tag.
 * 
 * @author Alan Race
 */
public class ScanSettings extends MzMLContent implements Serializable {

    /**
     * Serialisaiton version ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Accession: line scan direction (IMS:1000049).
     */
    public static String lineScanDirectionID = "IMS:1000049";

    /**
     * Accession: line scan direction left-right (IMS:1000491).
     */
    public static String lineScanDirectionLeftRightID = "IMS:1000491";

    /**
     * Accession: line scan direction right-left (IMS:1000490).
     */
    public static String lineScanDirectionRightLeftID = "IMS:1000490";

    /**
     * Accession: line scan direction top-down (IMS:1000493).
     */
    public static String lineScanDirectionTopDownID = "IMS:1000493";

    /**
     * Accession: line scan direction bottom-up (IMS:1000492).
     */
    public static String lineScanDirectionBottomUpID = "IMS:1000492";

    /**
     * Accession: scan direction (IMS:1000040).
     */
    public static String scanDirectionID = "IMS:1000040";

    /**
     * Accession: scan direction top-down (IMS:1000401).
     */
    public static String scanDirectionTopDownID = "IMS:1000401";

    /**
     * Accession: scan direction bottom-up (IMS:1000400).
     */
    public static String scanDirectionBottomUpID = "IMS:1000400";

    /**
     * Accession: scan direction left-right (IMS:1000402).
     */
    public static String scanDirectionLeftRightID = "IMS:1000402";

    /**
     * Accession: scan direction right-left (IMS:1000403).
     */
    public static String scanDirectionRightLeftID = "IMS:1000403";

    /**
     * Accession: scan pattern (IMS:1000041).
     */
    public static String scanPatternID = "IMS:1000041";

    /**
     * Accession: scan pattern flyback (IMS:1000413).
     */
    public static String scanPatternFlybackID = "IMS:1000413";

    /**
     * Accession: scan pattern meandering (IMS:1000410).
     */
    public static String scanPatternMeanderingID = "IMS:1000410";

    /**
     * Accession: scan pattern random access (IMS:1000412).
     */
    public static String scanPatternRandomAccessID = "IMS:1000412";

    /**
     * Accession: scan type (IMS:1000048).
     */
    public static String scanTypeID = "IMS:1000048";

    /**
     * Accession: scan type vertical (IMS:1000481).
     */
    public static String scanTypeVerticalID = "IMS:1000481";

    /**
     * Accession: image (IMS:1000004).
     */
    public static String imageID = "IMS:1000004";

    /**
     * Accession: max count pixel x (IMS:1000042).
     */
    public static String maxCountPixelXID = "IMS:1000042";

    /**
     * Accession: max count pixel y (IMS:1000043).
     */
    public static String maxCountPixelYID = "IMS:1000043";

    /**
     * Accession: max dimension x (IMS:1000044).
     */
    public static String maxDimensionXID = "IMS:1000044";

    /**
     * Accession: max dimension y (IMS:1000045).
     */
    public static String maxDimensionYID = "IMS:1000045";

    /**
     * Accession: pixel area (IMS:1000046).
     */
    public static String pixelAreaID = "IMS:1000046";

    // Attributes

    /**
     * Unique identifier [Required].
     */
    private String id;	// Required

    // Sub-elements

    /**
     * SourceFileRefList [Required].
     */
    private SourceFileRefList sourceFileRefList;
    
    /**
     * TargetList [Required]
     */
    private TargetList targetList;

    /**
     * Create ScanSettings with required attributes from XML tag.
     * 
     * @param id Unique identifier
     */
    public ScanSettings(String id) {
        this.id = id;
    }

    /**
     * Copy constructor, requiring new versions of lists to match old references to.
     * 
     * @param scanSettings Old ScanSettings to copy
     * @param rpgList           New ReferenableParamGroupList
     * @param sourceFileList    New SourceFileList
     */
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

    /**
     * Get unique identifier.
     * 
     * @return
     */
    public String getID() {
        return id;
    }

    /**
     * Set SourceFileList.
     * 
     * @param sourceFileRefList
     */
    public void setSourceFileRefList(SourceFileRefList sourceFileRefList) {
        sourceFileRefList.setParent(this);

        this.sourceFileRefList = sourceFileRefList;
    }

    /**
     * Set TargetList.
     * 
     * @param targetList
     */
    public void setTargetList(TargetList targetList) {
        targetList.setParent(this);

        this.targetList = targetList;
    }

    /**
     * Get CVParam or child CVParam of line scan direction, or null if one is not found.
     * 
     * @return
     * @see ScanSettings#lineScanDirectionID
     * @see ScanSettings#lineScanDirectionLeftRightID
     * @see ScanSettings#lineScanDirectionRightLeftID
     * @see ScanSettings#lineScanDirectionTopDownID
     * @see ScanSettings#lineScanDirectionBottomUpID
     */
    public CVParam getLineScanDirection() {
        return getCVParamOrChild(lineScanDirectionID);
    }

    /**
     * Get CVParam or child CVParam of scan direction, or null if one is not found.
     * 
     * @return
     * @see ScanSettings#scanDirectionID
     * @see ScanSettings#scanDirectionLeftRightID
     * @see ScanSettings#scanDirectionRightLeftID
     * @see ScanSettings#scanDirectionTopDownID
     * @see ScanSettings#scanDirectionBottomUpID
     */
    public CVParam getScanDirection() {
        return getCVParamOrChild(scanDirectionID);
    }

    /**
     * Get CVParam or child CVParam of scan pattern, or null if one is not found.
     * 
     * @return
     * @see ScanSettings#scanPatternID
     * @see ScanSettings#scanPatternFlybackID
     * @see ScanSettings#scanPatternMeanderingID
     * @see ScanSettings#scanPatternRandomAccessID
     */
    public CVParam getScanPattern() {
        return getCVParamOrChild(scanPatternID);
    }

    /**
     * Get CVParam or child CVParam of scan type, or null if one is not found.
     * 
     * @return
     * @see ScanSettings#scanTypeID
     * @see ScanSettings#scanTypeVerticalID
     */
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
