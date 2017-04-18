package com.alanmrace.jimzmlparser.mzml;

/**
 * Class describing {@literal <referenceableParamGroupRef>} tag.
 * 
 * @author Alan Race
 */
public class ReferenceableParamGroupRef extends MzMLReference<ReferenceableParamGroup> {

    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Create {@literal <referenceableParamGroupRef>} tag from ReferenceableParamGroup.
     * 
     * @param ref ReferenceableParamGroup
     */
    public ReferenceableParamGroupRef(ReferenceableParamGroup ref) {
        super(ref);
    }

    @Override
    public String getTagName() {
        return "referenceableParamGroupRef";
    }
}
