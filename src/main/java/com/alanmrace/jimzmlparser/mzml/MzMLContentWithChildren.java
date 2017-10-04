package com.alanmrace.jimzmlparser.mzml;

/**
 * Abstract class implementing the basic functionality for a tag in MzML which has
 * child tags.
 *  
 * @author Alan Race
 */
public abstract class MzMLContentWithChildren extends MzMLContent implements HasChildren {

    /**
     * Returns a copy of a list of children of this MzMLTag. Changing this list
     * does not affect the imzML structure.
     * 
     * @return Copy of list of children.
     */
//    public abstract List<MzMLTag> getChildren();
}
