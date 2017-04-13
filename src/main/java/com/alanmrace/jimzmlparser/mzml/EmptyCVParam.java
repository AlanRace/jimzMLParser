package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.obo.OBOTerm;

/**
 * CVParam with no value.
 * 
 * <p>TODO: Change the get values to throw an exception and change the RuntimeException 
 * to this exception too. 
 * 
 * @author Alan Race
 */
public class EmptyCVParam extends CVParam {

    /**
     * Initialise an EmptyCVParam from an ontology term.
     * 
     * @param term  Ontology term for the parameter
     */
    public EmptyCVParam(OBOTerm term) {
        this.term = term;
    }
    
    /**
     * Initialise a BooleanCVParam from an ontology term for the parameter, a 
     * value and an ontology term for the units.
     * 
     * @param term  Ontology term for the parameter
     * @param units Ontology term for the units of the parameter
     */
    public EmptyCVParam(OBOTerm term, OBOTerm units) {
        this.term = term;
        this.units = units;
    }

    /**
     * Copy constructor for EmptyCVParam.
     * 
     * @param cvParam EmptyCVParam to copy
     */
    public EmptyCVParam(EmptyCVParam cvParam) {
        this.term = cvParam.term;
        this.units = cvParam.units;
    }

    @Override
    public String getValueAsString() {
        return null;
    }

    @Override
    public double getValueAsDouble() {
        return Double.NaN;
    }

    @Override
    public int getValueAsInteger() {
        return Integer.MIN_VALUE;
    }

    @Override
    public long getValueAsLong() {
        return Long.MIN_VALUE;
    }

    @Override
    public void setValueAsString(String newValue) {
        throw new UnsupportedOperationException("Cannot change the value of an empty CV param");
    }

}
