package com.alanmrace.jimzmlparser.mzml;

/**
 * Interface for (i)mzML tags which can be referenced at other locations within
 * the (i)mzML document. This adds the requirement of having an id attribute in
 * the XML.
 * 
 * @author Alan Race
 */
public interface ReferenceableTag {
    
    /**
     * Get the unique identifier.
     * 
     * @return Unique identifier
     */
    String getID();
    
    /**
     * Set the unique identifier.
     * 
     * @param id Unique ID
     */
    void setID(String id);
}
