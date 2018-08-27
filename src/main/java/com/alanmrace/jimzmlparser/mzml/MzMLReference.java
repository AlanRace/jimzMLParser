package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.util.XMLHelper;

/**
 * MzMLTag which describes a reference to another MzMLTag.
 * 
 * @author Alan Race
 * @param <T> MzMLTag the reference is to
 */
public abstract class MzMLReference<T extends Enum<T> & ReferenceableTag & MzMLTag> extends MzMLContent {

    /**
     * Referenced MzMLTag.
     */
    private final T reference;
    
    /**
     * Create a reference to a specific MzMLTag.
     * 
     * @param reference Tag to reference
     */
    public MzMLReference(T reference) {
        this.reference = reference;
    }
        
    /**
     * Returns the tag which is being referenced.
     * 
     * @return MzMLTag the reference points to
     */
    public T getReference() {
        return reference;
    }
        
    @Override
    public String getXMLAttributeText() {
        return "ref=\"" + XMLHelper.ensureSafeXML(reference.getID()) + "\"";
    }
    
    @Override
    public String toString() {
        return getTagName() + ": " + reference.getID();
    }
}
