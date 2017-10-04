package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.event.ValueCVParamChangeEvent;
import com.alanmrace.jimzmlparser.obo.OBOTerm;

/**
 * CVParam with a String value.
 * 
 * @author Alan Race
 */
public class StringCVParam extends CVParam {

    /**
     * Value of the cvParam.
     */
    protected String value;

    /**
     * Initialise a StringCVParam from an ontology term for the parameter, a 
     * value and an ontology term for the units.
     * 
     * @param term  Ontology term for the parameter
     * @param value Value of the parameter
     * @param units Ontology term for the units of the parameter
     */
    public StringCVParam(OBOTerm term, String value, OBOTerm units) {
        this(term, value);

        this.units = units;
    }
    
    /**
     * Initialise a StringCVParam from an ontology term for the parameter and a 
     * value.
     *  
     * @param term  Ontology term for the parameter
     * @param value Value of the parameter
     */
    public StringCVParam(OBOTerm term, String value) {
        if (term == null) {
            throw (new IllegalArgumentException("OBOTerm cannot be null for StringCVParam"));
        }

        this.term = term;
        this.value = value;
    }

    /**
     * Copy constructor for StringCVParam.
     * 
     * @param cvParam StringCVParam to copy
     */
    public StringCVParam(StringCVParam cvParam) {
        this.term = cvParam.term;
        this.value = cvParam.value;
        this.units = cvParam.units;
    }

    /**
     * Get the value with its native type, String.
     * 
     * @return Value as a String
     */
    public String getValue() {
        return value;
    }

    /**
     * Set the value in its native type, String.
     * 
     * @param value Value as a String
     */
    public void setValue(String value) {
        String oldValue = this.value;
        
        this.value = value;
        
        if(hasListeners())
            notifyListeners(new ValueCVParamChangeEvent<String>(this, oldValue, value));
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

    @Override
    protected void resetValue() {
        value = "";
    }

}
