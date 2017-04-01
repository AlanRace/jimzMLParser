package com.alanmrace.jimzmlparser.mzml;

import java.util.Collection;

/**
 * Interface for any class which describes a tag within an MzML file.
 * 
 * @author Alan Race
 */
public interface MzMLTag {

    /**
     * Get the name of the tag, as it appears in the MzML file.
     * 
     * @return tag name
     */
    public String getTagName();
    
    /**
     * Add all child tags, and their children (repeated recursively) to the supplied 
     * collection. 
     * 
     * @param children Collection to add the children to
     */
    public void addChildrenToCollection(Collection<MzMLTag> children);
}
