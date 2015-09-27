package com.alanmrace.jimzmlparser.mzML;

import com.alanmrace.jimzmlparser.exceptions.CVParamAccessionNotFoundException;
import com.alanmrace.jimzmlparser.obo.OBOTerm;

public class EmptyCVParam extends CVParam {

	public EmptyCVParam(OBOTerm term) throws CVParamAccessionNotFoundException {
		this.term = term;
	}
	
	
	@Override
	public String getValueAsString() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public double getValueAsDouble() {
		// TODO Auto-generated method stub
		return Double.NaN;
	}

        @Override
        public int getValueAsInteger(){
            return Integer.MIN_VALUE;
        }
        
	@Override
	public long getValueAsLong() {
		// TODO Auto-generated method stub
		return Long.MIN_VALUE;
	}

	@Override
	public void setValueAsString(String newValue) {
		throw new RuntimeException("Cannot change the value of an empty CV param");
	}

}
