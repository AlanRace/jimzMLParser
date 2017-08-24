package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.event.ValueCVParamChangeEvent;
import com.alanmrace.jimzmlparser.obo.OBOTerm;

/**
 * CVParam with a long value.
 * 
 * @author Alan Race
 */
public class LongCVParam extends CVParam {

    /**
     * Value of the cvParam.
     */
    protected long value;

    /**
     * Initialise a LongCVParam from an ontology term for the parameter, a 
     * value and an ontology term for the units.
     * 
     * @param term  Ontology term for the parameter
     * @param value Value of the parameter
     * @param units Ontology term for the units of the parameter
     */
    public LongCVParam(OBOTerm term, long value, OBOTerm units) {
        this(term, value);

        this.units = units;
    }

    /**
     * Initialise a LongCVParam from an ontology term for the parameter and a 
     * value.
     * 
     * @param term  Ontology term for the parameter
     * @param value Value of the parameter
     */
    public LongCVParam(OBOTerm term, long value) {
        if (term == null) {
            throw (new IllegalArgumentException("OBOTerm cannot be null for LongCVParam"));
        }

        this.term = term;
        this.value = value;
    }

    /**
     * Copy constructor for LongCVParam.
     * 
     * @param cvParam LongCVParam to copy
     */
    public LongCVParam(LongCVParam cvParam) {
        this.term = cvParam.term;
        this.value = cvParam.value;
        this.units = cvParam.units;
    }

    /**
     * Get the value with its native type, long.
     * 
     * @return Value as a long
     */
    public long getValue() {
        return value;
    }

    /**
     * Set the value in its native type, long.
     * 
     * @param value Value as a long
     */
    public void setValue(long value) {
        long oldValue = this.value;
        this.value = value;
        
        if(hasListeners())
            notifyListeners(new ValueCVParamChangeEvent<Long>(this, oldValue, this.value));
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
    public int getValueAsInteger() {
        return (int) value;
    }

    @Override
    public long getValueAsLong() {
        return value;
    }

    @Override
    public void setValueAsString(String newValue) {
        long parsedValue = Long.parseLong(newValue);
        
        setValue(parsedValue);
    }

    @Override
    protected void resetValue() {
        value = 0;
    }

}
