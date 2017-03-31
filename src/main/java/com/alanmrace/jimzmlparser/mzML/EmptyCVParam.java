package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.obo.OBOTerm;

public class EmptyCVParam extends CVParam {

    public EmptyCVParam(OBOTerm term) {
        this.term = term;
    }

    public EmptyCVParam(OBOTerm term, OBOTerm units) {
        this.term = term;
        this.units = units;
    }

    public EmptyCVParam(EmptyCVParam cvParam) {
        this.term = cvParam.term;
        this.units = cvParam.units;
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
    public int getValueAsInteger() {
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
