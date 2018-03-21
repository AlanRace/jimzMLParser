package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.obo.OBOTerm;

/**
 * CVParam with no value.
 * 
 * @author Alan Race
 */
public class EmptyCVParam extends CVParam {

	private static final String NO_VALUE_ERROR = "No value to get in EmptyCVParam";
	
    /**
     * Initialise an EmptyCVParam from an ontology term.
     * 
     * @param term  Ontology term for the parameter
     */
    public EmptyCVParam(OBOTerm term) {
        if (term == null) {
            throw (new IllegalArgumentException("OBOTerm cannot be null for EmptyCVParam"));
        }
        
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
        if (term == null) {
            throw (new IllegalArgumentException("OBOTerm term cannot be null for EmptyCVParam"));
        }
        
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
        throw new UnsupportedOperationException(NO_VALUE_ERROR);
    }

    @Override
    public int getValueAsInteger() {
        throw new UnsupportedOperationException(NO_VALUE_ERROR);
    }

    @Override
    public long getValueAsLong() {
        throw new UnsupportedOperationException(NO_VALUE_ERROR);
    }

    @Override
    public void setValueAsString(String newValue) {
        throw new UnsupportedOperationException("Cannot change the value of an EmptyCVParam");
    }

    @Override
    protected void resetValue() {
    	// No value to reset.
    }

}
