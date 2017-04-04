package com.alanmrace.jimzmlparser.mzml;

public class ReferenceableParamGroupRef extends MzMLReference<ReferenceableParamGroup> { //, MutableTreeNode {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private ReferenceableParamGroup ref;

    public ReferenceableParamGroupRef(ReferenceableParamGroup ref) {
        super(ref);
    }

    @Override
    public String getTagName() {
        return "referenceableParamGroupRef";
    }
}
