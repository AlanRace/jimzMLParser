package com.alanmrace.jimzmlparser.mzml;

import java.util.Collection;

/**
 * Class describing a {@literal <scanSettings>} tag.
 * 
 * @author Alan Race
 */
public class ScanSettings extends MzMLContentWithParams implements ReferenceableTag {

    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Accession: line scan direction (IMS:1000049).
     */
    public static final String LINE_SCAN_DIRECTION_ID = "IMS:1000049";

    /**
     * Accession: line scan direction left-right (IMS:1000491).
     */
    public static final String LINE_SCAN_DIRECTION_LEFT_RIGHT_ID = "IMS:1000491";

    /**
     * Accession: line scan direction right-left (IMS:1000490).
     */
    public static final String LINE_SCAN_DIRECTION_RIGHT_LEFT_ID = "IMS:1000490";

    /**
     * Accession: line scan direction top-down (IMS:1000493).
     */
    public static final String LINE_SCAN_DIRECTION_TOP_DOWN_ID = "IMS:1000493";

    /**
     * Accession: line scan direction bottom-up (IMS:1000492).
     */
    public static final String LINE_SCAN_DIRECTION_BOTTOM_UP_ID = "IMS:1000492";

    /**
     * Accession: scan direction (IMS:1000040).
     */
    public static final String SCAN_DIRECTION_ID = "IMS:1000040";

    /**
     * Accession: scan direction top-down (IMS:1000401).
     */
    public static final String SCAN_DIRECTION_TOP_DOWN_ID = "IMS:1000401";

    /**
     * Accession: scan direction bottom-up (IMS:1000400).
     */
    public static final String SCAN_DIRECTION_BOTTOM_UP_ID = "IMS:1000400";

    /**
     * Accession: scan direction left-right (IMS:1000402).
     */
    public static final String SCAN_DIRECTION_LEFT_RIGHT_ID = "IMS:1000402";

    /**
     * Accession: scan direction right-left (IMS:1000403).
     */
    public static final String SCAN_DIRECTION_RIGHT_LEFT_ID = "IMS:1000403";

    /**
     * Accession: scan pattern (IMS:1000041).
     */
    public static final String SCAN_PATTERN_ID = "IMS:1000041";

    /**
     * Accession: scan pattern flyback (IMS:1000413).
     */
    public static final String SCAN_PATTERN_FLYBACK_ID = "IMS:1000413";

    /**
     * Accession: scan pattern meandering (IMS:1000410).
     */
    public static final String SCAN_PATTERN_MEANDERING_ID = "IMS:1000410";

    /**
     * Accession: scan pattern random access (IMS:1000412).
     */
    public static final String SCAN_PATTERN_RANDOM_ACCESS_ID = "IMS:1000412";

    /**
     * Accession: scan type (IMS:1000048).
     */
    public static final String SCAN_TYPE_ID = "IMS:1000048";
    
    /**
     * Accession: scan type horizontal (IMS:1000480).
     */
    public static final String SCAN_TYPE_HORIZONTAL_ID = "IMS:1000480";

    /**
     * Accession: scan type vertical (IMS:1000481).
     */
    public static final String SCAN_TYPE_VERTICAL_ID = "IMS:1000481";

    /**
     * Accession: image (IMS:1000004).
     */
    public static final String IMAGE_ID = "IMS:1000004";

    /**
     * Accession: max count pixel x (IMS:1000042).
     */
    public static final String MAX_COUNT_PIXEL_X_ID = "IMS:1000042";

    /**
     * Accession: max count pixel y (IMS:1000043).
     */
    public static final String MAX_COUNT_PIXEL_Y_ID = "IMS:1000043";

    /**
     * Accession: max dimension x (IMS:1000044).
     */
    public static final String MAX_DIMENSION_X_ID = "IMS:1000044";

    /**
     * Accession: max dimension y (IMS:1000045).
     */
    public static final String MAX_DIMENSION_Y_ID = "IMS:1000045";

    /**
     * Accession: pixel area (IMS:1000046).
     */
    public static final String PIXEL_AREA_ID = "IMS:1000046";

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
     * TargetList [Required].
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
    public String getID() {
        return id;
    }

    /**
     * Set SourceFileList.
     * 
     * @param sourceFileRefList SourceFileList
     */
    public void setSourceFileRefList(SourceFileRefList sourceFileRefList) {
        sourceFileRefList.setParent(this);

        this.sourceFileRefList = sourceFileRefList;
    }

    /**
     * Set TargetList.
     * 
     * @param targetList TargetList
     */
    public void setTargetList(TargetList targetList) {
        targetList.setParent(this);

        this.targetList = targetList;
    }

    /**
     * Get CVParam or child CVParam of line scan direction, or null if one is not found.
     * 
     * @return CVParam if found, null otherwise
     * @see ScanSettings#LINE_SCAN_DIRECTION_ID
     * @see ScanSettings#LINE_SCAN_DIRECTION_LEFT_RIGHT_ID
     * @see ScanSettings#LINE_SCAN_DIRECTION_RIGHT_LEFT_ID
     * @see ScanSettings#LINE_SCAN_DIRECTION_TOP_DOWN_ID
     * @see ScanSettings#LINE_SCAN_DIRECTION_BOTTOM_UP_ID
     */
    public CVParam getLineScanDirection() {
        return getCVParamOrChild(LINE_SCAN_DIRECTION_ID);
    }

    /**
     * Get CVParam or child CVParam of scan direction, or null if one is not found.
     * 
     * @return CVParam if found, null otherwise
     * @see ScanSettings#SCAN_DIRECTION_ID
     * @see ScanSettings#SCAN_DIRECTION_LEFT_RIGHT_ID
     * @see ScanSettings#SCAN_DIRECTION_RIGHT_LEFT_ID
     * @see ScanSettings#SCAN_DIRECTION_TOP_DOWN_ID
     * @see ScanSettings#SCAN_DIRECTION_BOTTOM_UP_ID
     */
    public CVParam getScanDirection() {
        return getCVParamOrChild(SCAN_DIRECTION_ID);
    }

    /**
     * Get CVParam or child CVParam of scan pattern, or null if one is not found.
     * 
     * @return CVParam if found, null otherwise
     * @see ScanSettings#SCAN_PATTERN_ID
     * @see ScanSettings#SCAN_PATTERN_FLYBACK_ID
     * @see ScanSettings#SCAN_PATTERN_MEANDERING_ID
     * @see ScanSettings#SCAN_PATTERN_RANDOM_ACCESS_ID
     */
    public CVParam getScanPattern() {
        return getCVParamOrChild(SCAN_PATTERN_ID);
    }

    /**
     * Get CVParam or child CVParam of scan type, or null if one is not found.
     * 
     * @return CVParam if found, null otherwise
     * @see ScanSettings#SCAN_TYPE_ID
     * @see ScanSettings#SCAN_TYPE_VERTICAL_ID
     */
    public CVParam getScanType() {
        return getCVParamOrChild(SCAN_TYPE_ID);
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

    @Override
    public void setID(String id) {
        this.id = id;
    }
}
