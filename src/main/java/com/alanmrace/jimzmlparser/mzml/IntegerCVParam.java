package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.event.ValueCVParamChangeEvent;
import com.alanmrace.jimzmlparser.obo.OBOTerm;

/**
 * CVParam with a int value.
 * 
 * @author Alan Race
 */
public class IntegerCVParam extends CVParam {

    /**
     * Value of the cvParam.
     */
    protected int value;

    /**
     * Initialise a IntegerCVParam from an ontology term for the parameter, a 
     * value and an ontology term for the units.
     * 
     * @param term  Ontology term for the parameter
     * @param value Value of the parameter
     * @param units Ontology term for the units of the parameter
     */
    public IntegerCVParam(OBOTerm term, int value, OBOTerm units) {
        this(term, value);

        this.units = units;
    }

    /**
     * Initialise a IntegerCVParam from an ontology term for the parameter and a 
     * value.
     *  
     * @param term  Ontology term for the parameter
     * @param value Value of the parameter
     */
    public IntegerCVParam(OBOTerm term, int value) {
        if (term == null) {
            throw (new IllegalArgumentException("OBOTerm cannot be null for IntegerCVParam"));
        }

        this.term = term;
        this.value = value;
    }

    /**
     * Copy constructor for IntegerCVParam.
     * 
     * @param cvParam IntegerCVParam to copy
     */
    public IntegerCVParam(IntegerCVParam cvParam) {
        this.term = cvParam.term;
        this.value = cvParam.value;
        this.units = cvParam.units;
    }

    /**
     * Get the value with its native type, int.
     * 
     * @return Value as a int
     */
    public int getValue() {
        return value;
    }

    /**
     * Set the value in its native type, double.
     * 
     * @param value Value as a double
     */
    public void setValue(int value) {
        int oldValue = this.value;
        this.value = value;
        
        if(hasListeners())
            notifyListeners(new ValueCVParamChangeEvent<Integer>(this, oldValue, this.value));
    }

    @Override
    public String getValueAsString() {
        return "" + getValueAsInteger();
    }

    @Override
    public double getValueAsDouble() {
        return value;
    }

    @Override
    public int getValueAsInteger() {
        return value;
    }

    @Override
    public long getValueAsLong() {
        return value;
    }

    @Override
    public void setValueAsString(String newValue) {
        int parsedValue = Integer.parseInt(newValue);
        
        setValue(parsedValue);
    }

    @Override
    protected void resetValue() {
        value = 0;
    }
}
