/*
 * 
 */
package com.alanmrace.jimzmlparser.mzml;

import java.util.Collection;

/**
 *
 * @author Alan
 */
public interface MzMLTag {
    public String getTagName();
    
    public void addChildrenToCollection(Collection<MzMLTag> children);
}
