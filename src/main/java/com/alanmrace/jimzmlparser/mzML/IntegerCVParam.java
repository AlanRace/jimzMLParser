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
public class IntegerCVParam extends CVParam {
    protected int value;
	
	public IntegerCVParam(OBOTerm term, int value, OBOTerm units) throws CVParamAccessionNotFoundException {
		this(term, value);

		this.units = units;
	}
	
	public IntegerCVParam(OBOTerm term, int value) throws CVParamAccessionNotFoundException {
		if(term == null)
			throw(new CVParamAccessionNotFoundException("" + value));
		
		this.term = term;
		this.value = value;
	}
	
	public IntegerCVParam(IntegerCVParam cvParam) {
		this.term = cvParam.term;
		this.value = cvParam.value;
		this.units = cvParam.units;
	}
	
	public double getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	@Override
	public String getValueAsString() {
		return "" + getValue();
	}

	@Override
	public double getValueAsDouble() {
		return value;
	}
        
        @Override
        public int getValueAsInteger(){
            return value;
        }

	@Override
	public long getValueAsLong() {
		return value;
	}

	@Override
	public void setValueAsString(String newValue) {
		value = Integer.parseInt(newValue);
	}
}
