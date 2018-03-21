package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.event.CVParamChangeEvent;
import com.alanmrace.jimzmlparser.event.OBOTermCVParamChangeEvent;
import com.alanmrace.jimzmlparser.exceptions.InvalidFormatIssue;
import com.alanmrace.jimzmlparser.exceptions.NonFatalParseException;

import com.alanmrace.jimzmlparser.obo.OBOTerm;
import com.alanmrace.jimzmlparser.util.XMLHelper;

/**
 * Base class of CVParam with no type. Describes the {@literal <cvParam>} tag.
 * 
 * @author Alan Race
 * 
 * @see BooleanCVParam
 * @see DoubleCVParam
 * @see EmptyCVParam
 * @see IntgerCVParam
 * @see LongCVParam
 * @see StringCVParam
 */
public abstract class CVParam extends MzMLContent { 

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
    
//    @Override
//    public void outputXML(MzMLWritable output, int indent) throws IOException {
//        MzMLContent.indent(output, indent);
//        
//        output.writeMetadata("<cvParam");
//
//        output.writeMetadata(" cvRef=\"" + XMLHelper.ensureSafeXML(term.getNamespace()) + "\"");
//        output.writeMetadata(" accession=\"" + XMLHelper.ensureSafeXML(term.getID()) + "\"");
//        output.writeMetadata(" name=\"" + XMLHelper.ensureSafeXML(term.getName()) + "\"");
//
//        String value = getValueAsString();
//
//        if (value != null && !value.equals("null")) {
//            output.writeMetadata(" value=\"" + XMLHelper.ensureSafeXML(value) + "\"");
//        }
//
//        if (units != null) {
//            output.writeMetadata(" unitCvRef=\"" + XMLHelper.ensureSafeXML(units.getNamespace()) + "\"");
//            output.writeMetadata(" unitAccession=\"" + XMLHelper.ensureSafeXML(units.getID()) + "\"");
//            output.writeMetadata(" unitName=\"" + XMLHelper.ensureSafeXML(units.getName()) + "\"");
//        }
//
//        output.writeMetadata("/>\n");
//    }

    /**
     * List of possible cvParam sub types, corresponding to a value type.
     */
    public enum CVParamType {

        /**
         * Double precision: {@link DoubleCVParam}.
         */
        Double,

        /**
         * Long: {@link LongCVParam}.
         */
        Long,

        /**
         * String: {@link StringCVParam}.
         */
        String,

        /**
         * Integer: {@link IntegerCVParam}.
         */
        Integer,

        /**
         * Boolean: {@link BooleanCVParam}.
         */
        Boolean,

        /**
         * No value: {@link EmptyCVParam}.
         */
        Empty
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
            return CVParamType.Empty;
        
        CVParamType type;
        
        switch(term.getValueType()) {
            case String:
                type = CVParamType.String;
                break;
            case Float:
            case Double:
            case NonNegativeFloat:
                type = CVParamType.Double;
                break;
            case Int:
                type = CVParamType.Integer;
                break;
            case Boolean:
                type = CVParamType.Boolean;
                break;
            case NonNegativeInteger:
                type = CVParamType.Long;
                break;
            default:
                throw new InvalidFormatIssue(term, term.getValueType());
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
            case String:
                param = new StringCVParam(term, "", units);
                break;
            case Double:
                param = new DoubleCVParam(term, 0.0, units);
                break;
            case Long:
                param = new LongCVParam(term, 0, units);
                break;
            case Integer:
                param = new IntegerCVParam(term, 0, units);
                break;
            case Boolean:
                param = new BooleanCVParam(term, false, units);
                break;
            default:
                param = new EmptyCVParam(term, units);
                break;
        }
        
        return param;
    }
    
//    @Override
//    public boolean equals(Object o) {
//        if (o == this) {
//            return true;
//        }
//
//        if (!(o instanceof CVParam)) {
//            return false;
//        }
//
//        CVParam cvParam = (CVParam) o;
//
//        if(cvParam instanceof EmptyCVParam)
//            return cvParam.getTerm().equals(term);
//        
//        return cvParam.getTerm().equals(term)
//                && (cvParam.getValueAsString().equals(getValueAsString()))
//                && //				((value == null || cvParam.getValue() == null) ? true : cvParam.getValue().equals(value)) && 
//                ((units == null || cvParam.getUnits() == null) ? true : cvParam.getUnits().equals(units));
//    }
//
//    @Override
//    public int hashCode() {
//        int hash = 5;
//        hash = 53 * hash + (this.term != null ? this.term.hashCode() : 0);
//        hash = 53 * hash + (this.units != null ? this.units.hashCode() : 0);
//        hash = 53 * hash + (getValueAsString() != null ? getValueAsString().hashCode() : 0);
//        return hash;
//    }
}
