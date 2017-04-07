package com.alanmrace.jimzmlparser.mzml;

/**
 *
 * @author Alan Race
 */
public interface ReferenceableTag {
    
    /**
     * Get the unique identifier.
     * 
     * @return Unique identifier
     */
    public String getID();
    
    /**
     * Set the unique identifier.
     * 
     * @param id Unique ID
     */
    public void setID(String id);
}
