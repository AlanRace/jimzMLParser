package com.alanmrace.jimzmlparser.mzML;

import com.alanmrace.jimzmlparser.exceptions.CVParamAccessionNotFoundException;
import com.alanmrace.jimzmlparser.obo.OBOTerm;

public class StringCVParam extends CVParam {

    protected String value;

    public StringCVParam(OBOTerm term, String value, OBOTerm units) throws CVParamAccessionNotFoundException {
        this(term, value);

        this.units = units;
    }

    public StringCVParam(OBOTerm term, String value) throws CVParamAccessionNotFoundException {
        if (term == null) {
            throw (new CVParamAccessionNotFoundException("" + value));
        }

        this.term = term;
        this.value = value;
    }

    public StringCVParam(StringCVParam cvParam) {
        this.term = cvParam.term;
        this.value = cvParam.value;
        this.units = cvParam.units;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String getValueAsString() {
        return "" + getValue();
    }

    @Override
    public double getValueAsDouble() {
        double convertedValue = Double.NaN;

        try {
            convertedValue = Double.parseDouble(value);
        } catch (NumberFormatException ex) {
        } catch (NullPointerException ex) {
        }

        return convertedValue;
    }

    @Override
    public int getValueAsInteger() {
        int convertedValue = Integer.MIN_VALUE;

        try {
            convertedValue = Integer.parseInt(value);
        } catch (NumberFormatException ex) {
        } catch (NullPointerException ex) {
        }

        return convertedValue;
    }

    @Override
    public long getValueAsLong() {
        long convertedValue = Long.MIN_VALUE;

        try {
            convertedValue = Long.parseLong(value);
        } catch (NumberFormatException ex) {
        } catch (NullPointerException ex) {
        }

        return convertedValue;
    }

    @Override
    public void setValueAsString(String newValue) {
        setValue(newValue);
    }

}
