package com.alanmrace.jimzmlparser.mzml;

/**
 * Class describing {@literal <target>} tag, one item in an inclusion (or target) list.
 * 
 * @author Alan Race
 */
public class Target extends MzMLContentWithParams {

    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Create empty Target.
     */
    public Target() {
        super();
    }

    /**
     * Copy constructor.
     * 
     * @param target Old Target to copy
     * @param rpgList New ReferenceableParamGroupList to match references to
     */
    public Target(Target target, ReferenceableParamGroupList rpgList) {
        super(target, rpgList);
    }

    @Override
    public String getTagName() {
        return "target";
    }
}
