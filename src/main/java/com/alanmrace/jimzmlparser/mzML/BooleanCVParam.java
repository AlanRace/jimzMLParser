/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alanmrace.jimzmlparser.mzML;

import com.alanmrace.jimzmlparser.exceptions.CVParamAccessionNotFoundException;
import com.alanmrace.jimzmlparser.obo.OBOTerm;

/**
 *
 * @author Alan
 */
public class BooleanCVParam extends CVParam {

    protected boolean value;
    
    public BooleanCVParam(OBOTerm term, boolean value, OBOTerm units) throws CVParamAccessionNotFoundException {
        this(term, value);

        this.units = units;
    }

    public BooleanCVParam(OBOTerm term, boolean value) throws CVParamAccessionNotFoundException {
        if (term == null) {
            throw (new CVParamAccessionNotFoundException("" + value));
        }

        this.term = term;
        this.value = value;
    }

    public BooleanCVParam(BooleanCVParam cvParam) {
        this.term = cvParam.term;
        this.value = cvParam.value;
        this.units = cvParam.units;
    }

    @Override
    public String getValueAsString() {
        return "" + value;
    }

    @Override
    public double getValueAsDouble() {
        return value ? 1.0 : 0.0;
    }

    @Override
    public int getValueAsInteger() {
        return value ? 1 : 0;
    }

    @Override
    public long getValueAsLong() {
        return value ? 1L : 0L;
    }

    @Override
    public void setValueAsString(String newValue) {
        value = Boolean.parseBoolean(newValue);
    }
    
}
