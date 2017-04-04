/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
