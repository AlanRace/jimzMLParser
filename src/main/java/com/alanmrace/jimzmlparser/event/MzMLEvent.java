/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alanmrace.jimzmlparser.event;

import com.alanmrace.jimzmlparser.mzml.MzMLContent;

/**
 *
 * @author alan.race
 */
public abstract class MzMLEvent {
    MzMLContent source;
    
    boolean notifyParents = true;
        
    public MzMLEvent(MzMLContent source) {
        this.source = source;
    }
    
    public void notifyParents(boolean notifyParents) {
        this.notifyParents = notifyParents;
    }
    
    public boolean notifyParents() {
        return notifyParents;
    }
    
    public MzMLContent getSource() {
        return source;
    }
}
