package com.alanmrace.jimzmlparser.mzML;

import com.alanmrace.jimzmlparser.exceptions.CVParamAccessionNotFoundException;
import com.alanmrace.jimzmlparser.obo.OBOTerm;

public class DoubleCVParam extends CVParam {

    protected double value;

    public DoubleCVParam(OBOTerm term, double value, OBOTerm units) throws CVParamAccessionNotFoundException {
        this(term, value);

        this.units = units;
    }

    public DoubleCVParam(OBOTerm term, double value) throws CVParamAccessionNotFoundException {
        if (term == null) {
            throw (new CVParamAccessionNotFoundException("" + value));
        }

        this.term = term;
        this.value = value;
    }

    public DoubleCVParam(DoubleCVParam cvParam) {
        this.term = cvParam.term;
        this.value = cvParam.value;
        this.units = cvParam.units;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
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
    public int getValueAsInteger() {
        return (int) Math.round(value);
    }

    @Override
    public long getValueAsLong() {
        return Math.round(value);
    }

    @Override
    public void setValueAsString(String newValue) {
        value = Double.parseDouble(newValue);

    }
}
