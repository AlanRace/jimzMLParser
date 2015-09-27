package com.alanmrace.jimzmlparser.mzML;

import com.alanmrace.jimzmlparser.util.XMLHelper;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
public class Detector extends MzMLContent  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public static String detectorTypeID = "MS:1000026";
	public static String detectorAttributeID = "MS:1000481";
	public static String detectorAcquisitionModeID = "MS:1000027";
	
//	private int order;
	
	public Detector(int order) {
//		this.order = order;
	}
	
	public Detector(Detector detector, ReferenceableParamGroupList rpgList) {
		super(detector, rpgList);
	}
	
	public ArrayList<OBOTermInclusion> getListOfRequiredCVParams() {
		ArrayList<OBOTermInclusion> required = new ArrayList<OBOTermInclusion>();
		required.add(new OBOTermInclusion(detectorTypeID, true, true, true));
				
		return required; 
	}
	
	public ArrayList<OBOTermInclusion> getListOfOptionalCVParams() {
		ArrayList<OBOTermInclusion> optional = new ArrayList<OBOTermInclusion>();
		optional.add(new OBOTermInclusion(detectorAttributeID, false, true, false));
		optional.add(new OBOTermInclusion(detectorAcquisitionModeID, false, true, false));
		
		return optional;
	}
	
	public void outputXML(BufferedWriter output, int indent, int order) throws IOException {
		MzMLContent.indent(output, indent);
		output.write("<detector");
		output.write(" order=\"" + XMLHelper.ensureSafeXML(""+order) + "\"");
		output.write(">\n");
		
		super.outputXML(output, indent+1);
		
		MzMLContent.indent(output, indent);
		output.write("</detector>\n");
	}
	
	public String toString() {
		return "detector";
	}
}
