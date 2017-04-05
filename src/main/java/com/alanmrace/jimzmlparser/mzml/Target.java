package com.alanmrace.jimzmlparser.mzml;

import java.io.Serializable;

public class Target extends MzMLContentWithParams implements Serializable {

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
