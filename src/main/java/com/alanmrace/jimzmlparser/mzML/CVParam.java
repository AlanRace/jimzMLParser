package com.alanmrace.jimzmlparser.mzML;

//import com.alanmrace.jimzmlparser.exceptions.CVParamAccessionNotFoundException;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

import com.alanmrace.jimzmlparser.obo.OBOTerm;
import com.alanmrace.jimzmlparser.util.XMLHelper;

public abstract class CVParam implements Serializable { //, MutableTreeNode {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    protected OBOTerm term;
    protected OBOTerm units;

//	protected Type value;
//	private MutableTreeNode parentTreeNode;
    public OBOTerm getTerm() {
        return term;
    }

//	public Type getValue() {
//		return value;
//	}
//	public void setValue(Type value) {
//		this.value = value;
//	}
    public OBOTerm getUnits() {
        return units;
    }

    @Override
    public String toString() {
        return "(" + term.getID() + ") " + term.getName() + getValueAsString(); //((value != null) ? " - " : "" ) + value;
    }

    public abstract String getValueAsString();

    public abstract double getValueAsDouble();

    public abstract int getValueAsInteger();

    public abstract long getValueAsLong();

    public abstract void setValueAsString(String newValue);

    public void outputXML(BufferedWriter output) throws IOException {
        output.write("<cvParam");

        output.write(" cvRef=\"" + XMLHelper.ensureSafeXML(term.getNamespace()) + "\"");
        output.write(" accession=\"" + XMLHelper.ensureSafeXML(term.getID()) + "\"");
        output.write(" name=\"" + XMLHelper.ensureSafeXML(term.getName()) + "\"");

//		System.out.println("About to output value: " + value);
        //if(value != null)
        //	output.write(" value=\"" + XMLHelper.ensureSafeXML(value.toString()) + "\"");
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

    public enum CVParamType {
        Double,
        Long,
        String,
        Integer,
        Empty
    }

    private static HashMap<String, CVParamType> typeMap;

    public static CVParamType getCVParamType(String accession) {
        if (typeMap == null) {
            generateTypeMap();
        }

        CVParamType type = typeMap.get(accession);

        if (type == null) {
            return CVParamType.String;
        }

        return type;
    }

    private static void generateTypeMap() {
        typeMap = new HashMap<String, CVParamType>();

        // Insert Long params
        typeMap.put(BinaryDataArray.externalArrayLengthID, CVParamType.Long);
        typeMap.put(BinaryDataArray.externalEncodedLengthID, CVParamType.Long);
        typeMap.put(BinaryDataArray.externalOffsetID, CVParamType.Long);

        // Insert Double CVParams
        typeMap.put(Spectrum.basePeakMZID, CVParamType.Double);
        typeMap.put(Spectrum.lowestObservedmzID, CVParamType.Double);
        typeMap.put(Spectrum.highestObservedmzID, CVParamType.Double);

        typeMap.put(Scan.elutionTimeID, CVParamType.Double);
        typeMap.put(Scan.scanStartTimeID, CVParamType.Double);
        typeMap.put(Scan.ionMobilityDriftTimeID, CVParamType.Double);

        typeMap.put(Spectrum.MS1Spectrum, CVParamType.Empty);
        typeMap.put(Spectrum.positiveScanID, CVParamType.Empty);
        typeMap.put(Spectrum.profileSpectrumID, CVParamType.Empty);

        typeMap.put(BinaryDataArray.zlibCompressionID, CVParamType.Empty);
        typeMap.put(BinaryDataArray.noCompressionID, CVParamType.Empty);
        typeMap.put(BinaryDataArray.mzArrayID, CVParamType.Empty);
        typeMap.put(BinaryDataArray.intensityArrayID, CVParamType.Empty);
        typeMap.put(BinaryDataArray.doublePrecisionID, CVParamType.Empty);
        typeMap.put(BinaryDataArray.singlePrecisionID, CVParamType.Empty);
        typeMap.put(BinaryDataArray.signed32bitIntegerID, CVParamType.Empty);
        typeMap.put(BinaryDataArray.imsSigned32bitIntegerID, CVParamType.Empty);
        typeMap.put(BinaryDataArray.signed64bitIntegerID, CVParamType.Empty);
        typeMap.put(BinaryDataArray.imsSigned64bitIntegerID, CVParamType.Empty);
        typeMap.put(BinaryDataArray.signed8bitIntegerID, CVParamType.Empty);
        typeMap.put(BinaryDataArray.signed16bitIntegerID, CVParamType.Empty);
    }

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

}
