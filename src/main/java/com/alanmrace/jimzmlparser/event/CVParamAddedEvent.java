package com.alanmrace.jimzmlparser.event;

import com.alanmrace.jimzmlparser.mzml.CVParam;
import com.alanmrace.jimzmlparser.mzml.MzMLContentWithParams;

/**
 * Event describing that a CV parameter has been added to a specific MzML tag.
 * 
 * @author alan.race
 */
public class CVParamAddedEvent extends CVParamAddRemoveEvent {
    
    /**
     * Create an event describing that a CV parameter has been added to a specific 
     * MzML tag.
     * 
     * @param parent Tag that the CV parameter was added to (source of the event)
     * @param param CV parameter that was added
     */
    public CVParamAddedEvent(MzMLContentWithParams parent, CVParam param) {
        super(parent, param);
    }
    
}
