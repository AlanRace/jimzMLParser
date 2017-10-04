package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.event.ValueCVParamChangeEvent;
import com.alanmrace.jimzmlparser.obo.OBOTerm;

/**
 * CVParam with a boolean value.
 * 
 * @author Alan Race
 */
public class BooleanCVParam extends CVParam {

    /**
     * Value of the cvParam.
     */
    protected boolean value;
    
    /**
     * Initialise a BooleanCVParam from an ontology term for the parameter, a 
     * value and an ontology term for the units.
     * 
     * @param term  Ontology term for the parameter
     * @param value Value of the parameter
     * @param units Ontology term for the units of the parameter
     */
    public BooleanCVParam(OBOTerm term, boolean value, OBOTerm units) {
        this(term, value);

        this.units = units;
    }

    /**
     * Initialise a BooleanCVParam from an ontology term for the parameter and a 
     * value.
     * 
     * @param term  Ontology term for the parameter
     * @param value Value of the parameter
     */
    public BooleanCVParam(OBOTerm term, boolean value) {
        if (term == null) {
            throw (new IllegalArgumentException("OBOTerm cannot be null for BooleanCVParam"));
        }

        this.term = term;
        this.value = value;
    }

    /**
     * Copy constructor for BooleanCVParam.
     * 
     * @param cvParam BooleanCVParam to copy
     */
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
        boolean oldValue = this.value;
        
        value = Boolean.parseBoolean(newValue);
        
        if(hasListeners())
            notifyListeners(new ValueCVParamChangeEvent<Boolean>(this, oldValue, value));
    }

    @Override
    protected void resetValue() {
        value = false;
    }
    
}
