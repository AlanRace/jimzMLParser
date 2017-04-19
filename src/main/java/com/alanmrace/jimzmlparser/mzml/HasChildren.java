package com.alanmrace.jimzmlparser.mzml;

import java.util.Collection;

/**
 * Interface for MzMLTags which have children.
 * 
 * @author Alan Race
 */
public interface HasChildren {
    
    /**
     * Add all child tags, and their children (repeated recursively) to the supplied 
     * collection. 
     * 
     * @param children Collection to add the children to
     */
    public void addChildrenToCollection(Collection<MzMLTag> children);
}
