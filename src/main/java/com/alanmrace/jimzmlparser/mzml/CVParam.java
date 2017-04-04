package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.exceptions.InvalidFormatIssue;
import com.alanmrace.jimzmlparser.exceptions.NonFatalParseException;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;

import com.alanmrace.jimzmlparser.obo.OBOTerm;
import com.alanmrace.jimzmlparser.util.XMLHelper;
import java.util.Collection;

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
public abstract class CVParam implements Serializable, MzMLTag { 

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
     * Get the ontology term for the units of the value of this cvParam, or null if none.
     * 
     * @return Ontology term for units, or null if no units
     */
    public OBOTerm getUnits() {
        return units;
    }

    @Override
    public String getTagName() {
        return "cvParam";
    }
    
    @Override
    public void addChildrenToCollection(Collection<MzMLTag> children) {
        // No children to add
    }
    
    @Override
    public String toString() {
        String description = "(" + term.getID() + ") " + term.getName();
        String value = getValueAsString();
        
        if(!value.isEmpty()) {
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
    public void outputXML(BufferedWriter output, int indent) throws IOException {
        MzMLContent.indent(output, indent);
        
        output.write("<cvParam");

        output.write(" cvRef=\"" + XMLHelper.ensureSafeXML(term.getNamespace()) + "\"");
        output.write(" accession=\"" + XMLHelper.ensureSafeXML(term.getID()) + "\"");
        output.write(" name=\"" + XMLHelper.ensureSafeXML(term.getName()) + "\"");

        String value = getValueAsString();

        if (value != null && !value.equals("null")) {
            output.write(" value=\"" + XMLHelper.ensureSafeXML(value) + "\"");
        }

        if (units != null) {
            output.write(" unitCvRef=\"" + XMLHelper.ensureSafeXML(units.getNamespace()) + "\"");
            output.write(" unitAccession=\"" + XMLHelper.ensureSafeXML(units.getID()) + "\"");
            output.write(" unitName=\"" + XMLHelper.ensureSafeXML(units.getName()) + "\"");
        }

        output.write("/>\n");
    }

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
    
//    public static CVParamType getCVParamType(String accession) {
//        if (typeMap == null) {
//            generateTypeMap();
//        }
//
//        CVParamType type = typeMap.get(accession);
//
//        if (type == null) {
//            return CVParamType.String;
//        }
//
//        return type;
//    }
//
//    private static void generateTypeMap() {
//        typeMap = new HashMap<String, CVParamType>();
//
//        // Insert Long params
//        typeMap.put(BinaryDataArray.externalArrayLengthID, CVParamType.Long);
//        typeMap.put(BinaryDataArray.externalEncodedLengthID, CVParamType.Long);
//        typeMap.put(BinaryDataArray.externalOffsetID, CVParamType.Long);
//
//        // Insert Double CVParams
//        typeMap.put(Spectrum.basePeakMZID, CVParamType.Double);
//        typeMap.put(Spectrum.lowestObservedmzID, CVParamType.Double);
//        typeMap.put(Spectrum.highestObservedmzID, CVParamType.Double);
//
//        typeMap.put(Scan.elutionTimeID, CVParamType.Double);
//        typeMap.put(Scan.scanStartTimeID, CVParamType.Double);
//        typeMap.put(Scan.ionMobilityDriftTimeID, CVParamType.Double);
//
//        typeMap.put(Spectrum.MS1Spectrum, CVParamType.Empty);
//        typeMap.put(Spectrum.positiveScanID, CVParamType.Empty);
//        typeMap.put(Spectrum.profileSpectrumID, CVParamType.Empty);
//
//        typeMap.put(BinaryDataArray.zlibCompressionID, CVParamType.Empty);
//        typeMap.put(BinaryDataArray.noCompressionID, CVParamType.Empty);
//        typeMap.put(BinaryDataArray.mzArrayID, CVParamType.Empty);
//        typeMap.put(BinaryDataArray.intensityArrayID, CVParamType.Empty);
//        typeMap.put(BinaryDataArray.doublePrecisionID, CVParamType.Empty);
//        typeMap.put(BinaryDataArray.singlePrecisionID, CVParamType.Empty);
//        typeMap.put(BinaryDataArray.signed32bitIntegerID, CVParamType.Empty);
//        typeMap.put(BinaryDataArray.imsSigned32bitIntegerID, CVParamType.Empty);
//        typeMap.put(BinaryDataArray.signed64bitIntegerID, CVParamType.Empty);
//        typeMap.put(BinaryDataArray.imsSigned64bitIntegerID, CVParamType.Empty);
//        typeMap.put(BinaryDataArray.signed8bitIntegerID, CVParamType.Empty);
//        typeMap.put(BinaryDataArray.signed16bitIntegerID, CVParamType.Empty);
//    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof CVParam)) {
            return false;
        }

        CVParam cvParam = (CVParam) o;

        return cvParam.getTerm().equals(term)
                && (cvParam.getValueAsString().equals(getValueAsString()))
                && //				((value == null || cvParam.getValue() == null) ? true : cvParam.getValue().equals(value)) && 
                ((units == null || cvParam.getUnits() == null) ? true : cvParam.getUnits().equals(units));
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + (this.term != null ? this.term.hashCode() : 0);
        hash = 53 * hash + (this.units != null ? this.units.hashCode() : 0);
        hash = 53 * hash + (getValueAsString() != null ? getValueAsString().hashCode() : 0);
        return hash;
    }

}
