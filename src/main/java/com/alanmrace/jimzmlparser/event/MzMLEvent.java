package com.alanmrace.jimzmlparser.event;

import com.alanmrace.jimzmlparser.mzml.MzMLContent;

/**
 * Event class describing a change that has occurred to a specific MzML element.
 * 
 * @author alan.race
 */
public abstract class MzMLEvent {
    /**
     * MzML tag which triggered the event.
     */
    protected final MzMLContent source;
    
    /**
     * Whether any parent tags should be notified of this event type.
     */
    private boolean notifyParents = true;
        
    /**
     * Create an event which has been triggered by the specified MzML tag.
     * 
     * @param source Source of the event
     */
    public MzMLEvent(MzMLContent source) {
        this.source = source;
    }
    
    /**
     * Set whether any parents of the source element should be notified of this 
     * event.
     * 
     * @param notifyParents Notify parents?
     */
    public void notifyParents(boolean notifyParents) {
        this.notifyParents = notifyParents;
    }
    
    /**
     * Whether parents of the source element should be notified of this event.
     * 
     * @return Notify parents?
     */
    public boolean notifyParents() {
        return notifyParents;
    }
    
    /**
     * Returns the MzML tag which is the source of the event.
     * 
     * @return Source of the event
     */
    public MzMLContent getSource() {
        return source;
    }
}
