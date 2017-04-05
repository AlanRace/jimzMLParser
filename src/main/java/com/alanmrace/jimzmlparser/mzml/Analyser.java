package com.alanmrace.jimzmlparser.mzml;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Analyser tag.
 *
 * @author Alan Race
 */
public class Analyser extends MzMLContentWithParams implements Serializable {

    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Accession: Mass analyser type (MS:1000443). MUST be supplied (or any
     * children) only once.
     */
    public static String analyserTypeID = "MS:1000443";

    /**
     * Accession: Mass analyser attribute (MS:1000480). MAY supply a child one
     * or more times.
     */
    public static String analyserAttributeID = "MS:1000480";

    /**
     * Instantiates a new analyser attribute.
     */
    public Analyser() {
    }

    /**
     * Copy constructor, requiring new versions of lists to match old references to.
     * 
     * @param analyser Old Analyser to copy
     * @param rpgList New ReferenceableParamGroupList
     */
    public Analyser(Analyser analyser, ReferenceableParamGroupList rpgList) {
        super(analyser, rpgList);
    }

    @Override
    public ArrayList<OBOTermInclusion> getListOfRequiredCVParams() {
        ArrayList<OBOTermInclusion> required = new ArrayList<OBOTermInclusion>();
        required.add(new OBOTermInclusion(analyserTypeID, true, true, true));

        return required;
    }

    @Override
    public ArrayList<OBOTermInclusion> getListOfOptionalCVParams() {
        ArrayList<OBOTermInclusion> optional = new ArrayList<OBOTermInclusion>();
        optional.add(new OBOTermInclusion(analyserAttributeID, false, true, false));

        return optional;
    }
    
    /**
     * Output attribute in the form of XML.
     *
     * @param output where to write the XML
     * @param indent how large an indent is needed for this tag
     * @param order order of the component from source to detector
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void outputXML(BufferedWriter output, int indent, int order) throws IOException {
        MzMLContent.indent(output, indent);
        output.write("<analyzer");
        output.write(" order=\"" + order + "\"");
        output.write(">\n");

        super.outputXMLContent(output, indent + 1);

        MzMLContent.indent(output, indent);
        output.write("</analyzer>\n");
    }

    @Override
    public String toString() {
        return "analyser";
    }

    @Override
    public String getTagName() {
        return "analyzer";
    }
}
