package com.alanmrace.jimzmlparser.mzml;

import java.io.Serializable;

public class ScanWindow extends MzMLContentWithParams implements Serializable {

    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = 1L;

    public ScanWindow() {
        super();
    }

    public ScanWindow(ScanWindow scanWindow, ReferenceableParamGroupList rpgList) {
        super(scanWindow, rpgList);
    }

    @Override
    public String getTagName() {
        return "scanWindow";
    }

}
