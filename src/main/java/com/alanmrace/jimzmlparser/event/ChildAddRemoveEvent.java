/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alanmrace.jimzmlparser.event;

import com.alanmrace.jimzmlparser.mzml.MzMLContentWithParams;

/**
 *
 * @author alan.race
 */
public abstract class ChildAddRemoveEvent extends MzMLEvent {
    
    public ChildAddRemoveEvent(MzMLContentWithParams source) {
        super(source);
    }
    
    @Override
    public MzMLContentWithParams getSource() {
        return (MzMLContentWithParams) source;
    }
}
