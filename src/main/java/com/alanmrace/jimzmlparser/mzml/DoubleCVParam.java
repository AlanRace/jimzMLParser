package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.event.ValueCVParamChangeEvent;
import com.alanmrace.jimzmlparser.obo.OBOTerm;

/**
 * CVParam with a double value.
 * 
 * @author Alan Race
 */
public class DoubleCVParam extends CVParam {

    /**
     * Value of the cvParam.
     */
    protected double value;

    /**
     * Initialise a DoubleCVParam from an ontology term for the parameter, a 
     * value and an ontology term for the units.
     * 
     * @param term  Ontology term for the parameter
     * @param value Value of the parameter
     * @param units Ontology term for the units of the parameter
     */
    public DoubleCVParam(OBOTerm term, double value, OBOTerm units) {
        this(term, value);

        this.units = units;
    }

    /**
     * Initialise a DoubleCVParam from an ontology term for the parameter and a 
     * value.
     * 
     * @param term  Ontology term for the parameter
     * @param value Value of the parameter
     */
    public DoubleCVParam(OBOTerm term, double value) {
        if (term == null) {
            throw (new IllegalArgumentException("OBOTerm cannot be null for DoubleCVParam"));
        }

        this.term = term;
        this.value = value;
    }

    /**
     * Copy constructor for DoubleCVParam.
     * 
     * @param cvParam DoubleCVParam to copy
     */
    public DoubleCVParam(DoubleCVParam cvParam) {
        this.term = cvParam.term;
        this.value = cvParam.value;
        this.units = cvParam.units;
    }

    /**
     * Get the value with its native type, double.
     * 
     * @return Value as a double
     */
    public double getValue() {
        return value;
    }

    /**
     * Set the value in its native type, double.
     * 
     * @param value Value as a double
     */
    public void setValue(double value) {
        double oldValue = this.value;
        this.value = value;
        
        if(hasListeners())
            notifyListeners(new ValueCVParamChangeEvent<Double>(this, oldValue, value));
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
        double parsedValue = Double.parseDouble(newValue);
        
        setValue(parsedValue);
    }

    @Override
    protected void resetValue() {
        value = 0;
    }
}
