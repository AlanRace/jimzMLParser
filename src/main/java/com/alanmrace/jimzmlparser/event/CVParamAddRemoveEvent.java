package com.alanmrace.jimzmlparser.event;

import com.alanmrace.jimzmlparser.mzml.CVParam;
import com.alanmrace.jimzmlparser.mzml.MzMLContentWithParams;

/**
 * Event describing that a CV parameter has been added to or removed from a specific MzML tag.
 * 
 * @author alan.race
 */
public abstract class CVParamAddRemoveEvent extends ChildAddRemoveEvent {
    private final CVParam param;
    
    /**
     * Abstract constructor, pass through source to parent and store CV parameter 
     * which has been added or removed.
     * 
     * @param source MzML tag which is the source of the event.
     * @param param CV parameter that has been added or removed
     */
    public CVParamAddRemoveEvent(MzMLContentWithParams source, CVParam param) {
        super(source);
        
        this.param = param;
    }
    
    /**
     * Returns the CV parameter that was either added or removed.
     * 
     * @return CV parameter
     */
    public CVParam getCVParam() {
        return param;
    }
}
