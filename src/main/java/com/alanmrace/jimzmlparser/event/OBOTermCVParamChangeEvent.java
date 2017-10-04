package com.alanmrace.jimzmlparser.event;

import com.alanmrace.jimzmlparser.mzml.CVParam;
import com.alanmrace.jimzmlparser.obo.OBOTerm;

/**
 * Event triggered when the ontology term of a CV parameter is changed.
 * 
 * @author alan.race
 */
public class OBOTermCVParamChangeEvent extends CVParamChangeEvent {
    
    private final OBOTerm oldValue;
    private final OBOTerm newValue;
    
    /**
     * Create event describing a change to the ontology term of a CV parameter.
     *
     * @param source Source of the event
     * @param oldValue Previous ontology term
     * @param newValue New ontology term
     */
    public OBOTermCVParamChangeEvent(CVParam source, OBOTerm oldValue, OBOTerm newValue) {
        super(source);
        
        this.oldValue = oldValue;
        this.newValue = newValue;
    }
    
    /**
     * Get the previous ontology term value prior to the event.
     *  
     * @return Previous ontology term
     */
    public OBOTerm getOldValue() {
        return oldValue;
    }
    
    /**
     * Get the new ontology term value after to the event.
     *  
     * @return New ontology term
     */
    public OBOTerm getNewValue() {
        return newValue;
    }
}
