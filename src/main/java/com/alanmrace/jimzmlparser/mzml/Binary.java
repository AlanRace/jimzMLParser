package com.alanmrace.jimzmlparser.mzml;

/**
 * Binary tag.
 *
 * @author Alan Race
 */
public class Binary extends MzMLContent {

    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = 1L;
        
    @Override
    public String getTagName() {
        return "binary";
    }
}
