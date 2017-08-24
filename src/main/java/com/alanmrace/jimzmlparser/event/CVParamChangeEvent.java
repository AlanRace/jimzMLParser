package com.alanmrace.jimzmlparser.event;

import com.alanmrace.jimzmlparser.mzml.CVParam;

/**
 * Event describing that a change to a CV parameter has occurred. This could be
 * the change of the ontology term, a change to the value or a change to the units.
 * 
 * @author alan.race
 */
public class CVParamChangeEvent extends MzMLEvent {
    
    /**
     * Create an event describing that a change has occurred to a specific CV parameter.
     * 
     * @param param Parameter which has changed
     */
    public CVParamChangeEvent(CVParam param) {
        super(param);
    }
    
}
