/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alanmrace.jimzmlparser.event;

import com.alanmrace.jimzmlparser.mzml.CVParam;
import com.alanmrace.jimzmlparser.mzml.MzMLContentWithParams;

/**
 *
 * @author alan.race
 */
public class CVParamAddRemoveEvent extends ChildAddRemoveEvent {
    CVParam param;
    
    public CVParamAddRemoveEvent(MzMLContentWithParams parent, CVParam param) {
        super(parent);
        
        this.param = param;
    }
    
    
    public CVParam getCVParam() {
        return param;
    }
}
