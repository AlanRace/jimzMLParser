/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alanmrace.jimzmlparser.mzML;

import java.util.Collection;

/**
 *
 * @author Alan
 */
public interface MzMLTag {
    public String getTagName();
    
    public void addChildrenToCollection(Collection<MzMLTag> children);
}
