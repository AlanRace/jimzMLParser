/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alanmrace.jimzmlparser.event;

import com.alanmrace.jimzmlparser.mzml.CVParam;
import com.alanmrace.jimzmlparser.obo.OBOTerm;

/**
 *
 * @author alan.race
 */
public class OBOTermCVParamChangeEvent extends CVParamChangeEvent {
    
    OBOTerm oldValue;
    OBOTerm newValue;
    
    public OBOTermCVParamChangeEvent(CVParam source, OBOTerm oldValue, OBOTerm newValue) {
        super(source);
        
        this.oldValue = oldValue;
        this.newValue = newValue;
    }
    
}
