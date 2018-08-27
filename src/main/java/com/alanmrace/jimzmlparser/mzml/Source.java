package com.alanmrace.jimzmlparser.mzml;

/**
 * Class describing an ion source.
 * 
 * @author Alan Race
 */
public class Source extends Component {

    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Accession: Ionisation type (MS:1000008).
     */
    public static final String IONISATION_TYPE_ID = "MS:1000008";

    /**
     * Accession: Source attribute (MS:1000482).
     */
    public static final String SOURCE_ATTRIBUTE_ID = "MS:1000482";

    /**
     * Accession: Inlet type (MS:1000007).
     */
    public static final String INLET_TYPE_ID = "MS:1000007";

    /**
     * Accession: Sample stage (MS:1000002).
     */
    public static final String SAMPLE_STAGE_ID = "IMS:1000002";

    /**
     * Create empty Detector, does nothing.
     */
    public Source() {
    }

    /**
     * Copy constructor.
     *
     * @param source Old Source to copy
     * @param rpgList New ReferenceableParamGroupList to match references to
     */
    public Source(Source source, ReferenceableParamGroupList rpgList) {
        super(source, rpgList);
    }

    @Override
    public String getTagName() {
        return "source";
    }
}
