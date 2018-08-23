package com.alanmrace.jimzmlparser.mzml;

/**
 * Class describing a mass detector.
 * 
 * @author Alan Race
 */
public class Detector extends Component {

    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Accession: Detector type (MS:1000026).
     */
    public static final String detectorTypeID = "MS:1000026";
    
    /**
     * Accession: Detector attribute (MS:1000481).
     */
    public static final String detectorAttributeID = "MS:1000481";
    
    /**
     * Accession: Detector acquisition mode (MS:1000027).
     */
    public static final String detectorAcquisitionModeID = "MS:1000027";

    /**
     * Create empty Detector, does nothing.
     */
    public Detector() {
    }

    /**
     * Copy constructor.
     *
     * @param detector Old Detector to copy
     * @param rpgList New ReferenceableParamGroupList to match references to
     */
    public Detector(Detector detector, ReferenceableParamGroupList rpgList) {
        super(detector, rpgList);
    }

    @Override
    public String getTagName() {
        return "detector";
    }
}
