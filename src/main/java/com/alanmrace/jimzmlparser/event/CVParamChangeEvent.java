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
public class CVParamChangeEvent extends MzMLEvent {
    
    public CVParamChangeEvent(CVParam param) {
        super(param);
    }
    
}
