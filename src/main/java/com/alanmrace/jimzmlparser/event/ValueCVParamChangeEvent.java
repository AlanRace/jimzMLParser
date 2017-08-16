/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alanmrace.jimzmlparser.event;

import com.alanmrace.jimzmlparser.mzml.CVParam;

/**
 *
 * @author alan.race
 */
public class ValueCVParamChangeEvent<T> extends CVParamChangeEvent {
    T oldValue;
    T newValue;
    
    public ValueCVParamChangeEvent(CVParam source, T oldValue, T newValue) {
        super(source);
        
        this.oldValue = oldValue;
        this.newValue = newValue;
    }
}
