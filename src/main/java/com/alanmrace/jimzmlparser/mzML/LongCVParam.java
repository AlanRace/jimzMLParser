package com.alanmrace.jimzmlparser.mzML;

import com.alanmrace.jimzmlparser.exceptions.CVParamAccessionNotFoundException;
import com.alanmrace.jimzmlparser.obo.OBOTerm;

public class LongCVParam extends CVParam {

	protected long value;
	
	public LongCVParam(OBOTerm term, long value, OBOTerm units) throws CVParamAccessionNotFoundException {
		this(term, value);

		this.units = units;
	}
	
	public LongCVParam(OBOTerm term, long value) throws CVParamAccessionNotFoundException {
		if(term == null)
			throw(new CVParamAccessionNotFoundException("" + value));
		
		this.term = term;
		this.value = value;
	}
	
	public LongCVParam(LongCVParam cvParam) {
		this.term = cvParam.term;
		this.value = cvParam.value;
		this.units = cvParam.units;
	}
	
	public long getValue() {
		return value;
	}
	
	public void setValue(long value) {
		this.value = value;
	}
	
	@Override
	public String getValueAsString() {
		return "" + getValueAsLong();
	}

	@Override
	public double getValueAsDouble() {
		return value;
	}
        
        @Override
        public int getValueAsInteger(){
            return (int)value;
        }

	@Override
	public long getValueAsLong() {
		return value;
	}

	@Override
	public void setValueAsString(String newValue) {
		value = Long.parseLong(newValue);
	}

}
