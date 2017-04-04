package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.util.XMLHelper;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class ProcessingMethod extends MzMLContentWithParams implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static String dataTransformationID = "MS:1000452"; // Required child (1+)
    public static String dataProcessingParameterID = "MS:1000630"; // Optional child (1+)

    public static String fileFormatConversionID = "MS:1000530";

    private Software softwareRef;

    public ProcessingMethod(Software softwareRef) {
        this.softwareRef = softwareRef;
    }

    public ProcessingMethod(ProcessingMethod pm, ReferenceableParamGroupList rpgList, SoftwareList softwareList) {
        super(pm, rpgList);

        if (pm.softwareRef != null && softwareList != null) {
            for (Software software : softwareList) {
                if (pm.softwareRef.getID().equals(software.getID())) {
                    softwareRef = software;

                    break;
                }
            }
        }
    }

    @Override
    public ArrayList<OBOTermInclusion> getListOfRequiredCVParams() {
        ArrayList<OBOTermInclusion> required = new ArrayList<OBOTermInclusion>();
        required.add(new OBOTermInclusion(dataTransformationID, false, true, false));

        return required;
    }

    @Override
    public ArrayList<OBOTermInclusion> getListOfOptionalCVParams() {
        ArrayList<OBOTermInclusion> optional = new ArrayList<OBOTermInclusion>();
        optional.add(new OBOTermInclusion(dataProcessingParameterID, false, true, false));

        return optional;
    }

    public void setSoftwareRef(Software softwareRef) {
        this.softwareRef = softwareRef;
    }

    public Software getSoftwareRef() {
        return softwareRef;
    }

    public void outputXML(BufferedWriter output, int indent, int order) throws IOException {
        MzMLContent.indent(output, indent);
        output.write("<processingMethod");
        output.write(" order=\"" + order + "\"");
        output.write(" softwareRef=\"" + XMLHelper.ensureSafeXML(softwareRef.getID()) + "\"");
        output.write(">\n");

        super.outputXML(output, indent + 1);

        MzMLContent.indent(output, indent);
        output.write("</processingMethod>\n");
    }

    @Override
    public String toString() {
        return "processingMethod: softwareRef=\"" + softwareRef.getID() + "\"";
    }

    @Override
    public String getTagName() {
        return "processingMethod";
    }
}
