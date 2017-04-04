/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alanmrace.jimzmlparser.mzml;

import java.util.Collection;

/**
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
