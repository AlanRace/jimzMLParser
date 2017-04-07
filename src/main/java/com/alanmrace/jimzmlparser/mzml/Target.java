package com.alanmrace.jimzmlparser.mzml;

public class Target extends MzMLContentWithParams {

    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = 1L;

    public Target() {
        super();
    }

    public Target(Target target, ReferenceableParamGroupList rpgList) {
        super(target, rpgList);
    }

    @Override
    public String getTagName() {
        return "target";
    }
}
