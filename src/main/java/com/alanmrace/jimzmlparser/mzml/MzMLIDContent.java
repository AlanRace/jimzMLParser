package com.alanmrace.jimzmlparser.mzml;

/**
 * Adds unique identifier to MzMLContent, which can be used in lists.
 * 
 * @author Alan Race
 * 
 * @see MzMLContentList
 */
public abstract class MzMLIDContent extends MzMLContent implements ReferenceableTag {
    
    /**
     * A unique identifier for the MzMLContent.
     */
    protected String id;
    
    @Override
    public String getID() {
        return id;
    }
    
    @Override
    public void setID(String id) {
        this.id = id;
    }
}
