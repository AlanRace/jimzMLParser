package com.alanmrace.jimzmlparser.mzML;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Activation tag. 
 *
 * @author Alan Race
 */
public class Activation extends MzMLContent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Accession: Precursor activation attribute (MS:1000510). MAY be supplied (or any children) one or more times  */
	public static String precursorActivationAttributeID	= "MS:1000510"; // Optional child (1+)
	
	/** Accession: Dissociation method (MS:1000044). MUST be supplied (or any children) one or more times */
	public static String dissociationMethodID 			= "MS:1000044";	// Required child (1+)
	
	public Activation() {
		super();
	}
	
	public Activation(Activation activation, ReferenceableParamGroupList rpgList) {
		super(activation, rpgList);
	}
	
	public ArrayList<OBOTermInclusion> getListOfRequiredCVParams() {
		ArrayList<OBOTermInclusion> required = new ArrayList<OBOTermInclusion>();
		required.add(new OBOTermInclusion(dissociationMethodID, false, true, true));
				
		return required; 
	}
	
	public ArrayList<OBOTermInclusion> getListOfOptionalCVParams() {
		ArrayList<OBOTermInclusion> optional = new ArrayList<OBOTermInclusion>();
		optional.add(new OBOTermInclusion(precursorActivationAttributeID, false, true, false));
		
		return optional;
	}
	
	/* (non-Javadoc)
	 * @see mzML.MzMLContent#outputXML(java.io.BufferedWriter, int)
	 */
	public void outputXML(BufferedWriter output, int indent) throws IOException {
		MzMLContent.indent(output, indent);
		output.write("<activation>\n");
		
		super.outputXML(output, indent+1);
	
		MzMLContent.indent(output, indent);
		output.write("</activation>\n");
	}
	
	public String toString() {
		return "activation";
	}
}
