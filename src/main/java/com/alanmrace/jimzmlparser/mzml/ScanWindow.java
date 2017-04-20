package com.alanmrace.jimzmlparser.mzml;

import java.io.Serializable;

/**
 * Description of a scan window for a spectrum.
 *  
 * @author Alan Race
 */
public class ScanWindow extends MzMLContentWithParams implements Serializable {

    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Create empty ScanWindow.
     */
    public ScanWindow() {
        super();
    }

    /**
     * Copy constructor.
     * 
     * @param scanWindow ScanWindow to copy
     * @param rpgList New ReferenceableParamGroupList to match references to
     */
    public ScanWindow(ScanWindow scanWindow, ReferenceableParamGroupList rpgList) {
        super(scanWindow, rpgList);
    }

    @Override
    public String getTagName() {
        return "scanWindow";
    }

}
