package com.alanmrace.jimzmlparser.event;

import com.alanmrace.jimzmlparser.mzml.CVParam;
import com.alanmrace.jimzmlparser.mzml.MzMLContentWithParams;

/**
 * Event describing that a CV parameter has been removed from a specific MzML tag.
 * 
 * @author alan.race
 */
public class CVParamRemovedEvent extends CVParamAddRemoveEvent {
    
    /**
     * Create an event describing that a CV parameter has been removed from a specific 
     * MzML tag.
     * 
     * @param parent Tag that the CV parameter was removed from (source of the event)
     * @param param CV parameter that was removed
     */
    public CVParamRemovedEvent(MzMLContentWithParams parent, CVParam param) {
        super(parent, param);
    }    
}
