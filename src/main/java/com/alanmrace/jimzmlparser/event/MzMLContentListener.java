package com.alanmrace.jimzmlparser.event;

/**
 * Listener class which monitors for any event occurring to the MzML structure.
 * 
 * @author Alan Race
 */
public interface MzMLContentListener {
    
    /**
     * Notification that an event has occurred.
     *
     * @param event MzMLEvent 
     */
    void eventOccured(MzMLEvent event);
}
