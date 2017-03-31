package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.util.XMLHelper;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Analyser tag.
 *
 * @author Alan Race
 */
public class Analyser extends MzMLContent implements Serializable {

    /**
     *
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
     * Order in which the components are encountered from source to detector.
     */
//	private int order;
    /**
     * Instantiates a new analyser attribute.
     *
     * @param order order of the component
     */
    public Analyser(int order) {
//		this.order = order;
    }

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
        output.write(" order=\"" + XMLHelper.ensureSafeXML("" + order) + "\"");
        output.write(">\n");

        super.outputXML(output, indent + 1);

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
