package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.event.CVParamChangeEvent;
import com.alanmrace.jimzmlparser.event.OBOTermCVParamChangeEvent;
import com.alanmrace.jimzmlparser.exceptions.InvalidFormatIssue;
import com.alanmrace.jimzmlparser.exceptions.NonFatalParseException;

import com.alanmrace.jimzmlparser.obo.OBOTerm;
import com.alanmrace.jimzmlparser.util.XMLHelper;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Base class of CVParam with no type. Describes the {@literal <cvParam>} tag.
 * 
 * @author Alan Race
 * 
 * @see BooleanCVParam
 * @see DoubleCVParam
 * @see EmptyCVParam
 * @see IntegerCVParam
 * @see LongCVParam
 * @see StringCVParam
 */
public abstract class CVParam extends MzMLContent { 
    
    
    /**
     * Class logger.
     */
    private static final Logger LOGGER = Logger.getLogger(CVParam.class.getName());

    /**
     * List of possible cvParam sub types, corresponding to a value type.
     */
    public enum CVParamType {

        /**
         * Double precision: {@link DoubleCVParam}.
         */
        DOUBLE,

        /**
         * Long: {@link LongCVParam}.
         */
        LONG,

        /**
         * String: {@link StringCVParam}.
         */
        STRING,

        /**
         * Integer: {@link IntegerCVParam}.
         */
        INTEGER,

        /**
         * Boolean: {@link BooleanCVParam}.
         */
        BOOLEAN,

        /**
         * No value: {@link EmptyCVParam}.
         */
        EMPTY
    }
    
    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Ontology term this cvParam describes an instance of.
     */
    protected OBOTerm term;

    /**
     * Units used for the value of this cvParam, or null if no units.
     */
    protected OBOTerm units;

    /**
     * Get the ontology term this cvParam describes.
     * 
     * @return Ontology term
     */
    public OBOTerm getTerm() {
        return term;
    }
    
    /**
     * Set the OBOTerm and reset value and units to null.
     * 
     * @param term 
     */
    public void setTerm(OBOTerm term) {
        OBOTerm oldTerm = this.term;
        
        this.term = term;
        this.units = null;
        
        resetValue();
        
        if(hasListeners())
            notifyListeners(new OBOTermCVParamChangeEvent(this, oldTerm, term));
    }
    
    /**
     * Set the value of the CV parameter to be the default value.
     */
    protected abstract void resetValue();

    /**
     * Get the ontology term for the units of the value of this cvParam, or null if none.
     * 
     * @return Ontology term for units, or null if no units
     */
    public OBOTerm getUnits() {
        return units;
    }

    /**
     * Set the units of the CV parameter as the specified ontology term.
     * 
     * @param units Ontology term describing the units
     */
    public void setUnits(OBOTerm units) {
        this.units = units;
        
        if(hasListeners())
            notifyListeners(new CVParamChangeEvent(this));
    }
    
    @Override
    public String getTagName() {
        return "cvParam";
    }
    
    @Override
    public String toString() {
        String description = "(" + term.getID() + ") " + term.getName();
        String value = getValueAsString();
        
        if(value != null && !value.isEmpty()) {
            description += ": " +  getValueAsString();
            
            if(units != null) {
                description += " " + units.getName();
            }
        }        
        
        return description;
    }

    /**
     * Get the value of the cvParam as a String (converting if necessary).
     * 
     * @return Value as a String
     */
    public abstract String getValueAsString();

    /**
     * Get the value of the cvParam as a double (converting if necessary).
     * 
     * @return Value as a double
     */
    public abstract double getValueAsDouble();

    /**
     * Get the value of the cvParam as an int (converting if necessary).
     * 
     * @return Value as an int
     */
    public abstract int getValueAsInteger();

    /**
     * Get the value of the cvParam as a long (converting if necessary).
     * 
     * @return Value as a long
     */
    public abstract long getValueAsLong();

    /**
     * Set the value of the cvParam as a String (converting if necessary).
     * 
     * @param newValue Value as a String
     */
    public abstract void setValueAsString(String newValue);

    @Override
    public String getXMLAttributeText() {
        String attributes = "cvRef=\"" + XMLHelper.ensureSafeXML(term.getOntology().getOntology().toUpperCase()) + "\"";
        attributes += " accession=\"" + XMLHelper.ensureSafeXML(term.getID()) + "\"";
        attributes += " name=\"" + XMLHelper.ensureSafeXML(term.getName()) + "\"";
        
        String value = getValueAsString();
        
        if (value != null && !value.equals("null")) {
            attributes += " value=\"" + XMLHelper.ensureSafeXML(value) + "\"";
        }

        if (units != null) {
            attributes += " unitCvRef=\"" + XMLHelper.ensureSafeXML(units.getOntology().getOntology().toUpperCase()) + "\"";
            attributes += " unitAccession=\"" + XMLHelper.ensureSafeXML(units.getID()) + "\"";
            attributes += " unitName=\"" + XMLHelper.ensureSafeXML(units.getName()) + "\"";
        }
        
        return attributes;
    }
    
    /**
     * Convert the value type stored within an ontology term, to a CVParamType.
     * 
     * @param term  Ontology term to determine the value type of
     * @return      CVParamType describing the value type
     * @throws NonFatalParseException   Failure to convert the type, possibly an unknown type found in the ontology term
     */
    public static CVParamType getCVParamType(OBOTerm term) throws NonFatalParseException {
        if(term == null || term.getValueType() == null)
            return CVParamType.EMPTY;
        
        CVParamType type;
        
        switch(term.getValueType()) {
            case STRING:
                type = CVParamType.STRING;
                break;
            case FLOAT:
            case DOUBLE:
            case NON_NEGATIVE_FLOAT:
                type = CVParamType.DOUBLE;
                break;
            case INT:
                type = CVParamType.INTEGER;
                break;
            case BOOLEAN:
                type = CVParamType.BOOLEAN;
                break;
            case NON_NEGATIVE_INTEGER:
                type = CVParamType.LONG;
                break;
            default:
                LOGGER.log(Level.FINE, "Unknown CVParamType: {0} (assigned to term {1})", new Object[] {term.getValueType().toString(), term.getID()});
                
                InvalidFormatIssue issue = new InvalidFormatIssue(term, term.getValueType());
                
                throw new NonFatalParseException(issue);
        }
        
        return type;
    }
    
    /**
     * Create a CV parameter with the correct type based on the ontology term.
     * 
     * @param term Ontology term around which a CV parameter to be created
     * @param units Ontology term describing the units of the CV parameter
     * @return Subclass instance of CVParam depending on ontology term supplied
     * @throws NonFatalParseException If no type can be determined from {@code term} then NonFatalParseException is thrown
     */
    public static CVParam createCVParam(OBOTerm term, OBOTerm units) throws NonFatalParseException {
        CVParam param;
        
        switch(getCVParamType(term)) {
            case STRING:
                param = new StringCVParam(term, "", units);
                break;
            case DOUBLE:
                param = new DoubleCVParam(term, 0.0, units);
                break;
            case LONG:
                param = new LongCVParam(term, 0, units);
                break;
            case INTEGER:
                param = new IntegerCVParam(term, 0, units);
                break;
            case BOOLEAN:
                param = new BooleanCVParam(term, false, units);
                break;
            default:
                param = new EmptyCVParam(term, units);
                break;
        }
        
        return param;
    }
    
}
