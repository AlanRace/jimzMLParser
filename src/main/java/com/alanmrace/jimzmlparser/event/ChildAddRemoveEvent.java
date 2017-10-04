package com.alanmrace.jimzmlparser.event;

import com.alanmrace.jimzmlparser.mzml.MzMLContentWithParams;

/**
 * Event describing that a child element has been added to or removed from a specific MzML tag.
 * 
 * @author alan.race
 */
public abstract class ChildAddRemoveEvent extends MzMLEvent {
    
    /**
     * Abstract constructor, pass through source to parent.
     * 
     * @param source MzML tag which is the source of the event.
     */
    public ChildAddRemoveEvent(MzMLContentWithParams source) {
        super(source);
    }
    
    @Override
    public MzMLContentWithParams getSource() {
        return (MzMLContentWithParams) source;
    }
}
