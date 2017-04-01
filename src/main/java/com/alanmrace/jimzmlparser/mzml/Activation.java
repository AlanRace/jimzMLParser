package com.alanmrace.jimzmlparser.mzml;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Activation tag.
 *
 * @author Alan Race
 */
public class Activation extends MzMLContent {

    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Accession: Precursor activation attribute (MS:1000510). MAY be supplied
     * (or any children) one or more times
     */
    public static final String precursorActivationAttributeID = "MS:1000510"; // Optional child (1+)

    /**
     * Accession: Dissociation method (MS:1000044). MUST be supplied (or any
     * children) one or more times
     */
    public static final String dissociationMethodID = "MS:1000044";	// Required child (1+)

    /**
     * Create an Activation tag.
     */
    public Activation() {
        super();
    }

    /**
     * Copy constructor, requiring new versions of lists to match old references to.
     * 
     * @param activation Old Activation to copy
     * @param rpgList New ReferenceableParamGroupList
     */
    public Activation(Activation activation, ReferenceableParamGroupList rpgList) {
        super(activation, rpgList);
    }

    @Override
    public ArrayList<OBOTermInclusion> getListOfRequiredCVParams() {
        ArrayList<OBOTermInclusion> required = new ArrayList<OBOTermInclusion>();
        required.add(new OBOTermInclusion(dissociationMethodID, false, true, true));

        return required;
    }

    @Override
    public ArrayList<OBOTermInclusion> getListOfOptionalCVParams() {
        ArrayList<OBOTermInclusion> optional = new ArrayList<OBOTermInclusion>();
        optional.add(new OBOTermInclusion(precursorActivationAttributeID, false, true, false));

        return optional;
    }

    @Override
    public void outputXML(BufferedWriter output, int indent) throws IOException {
        MzMLContent.indent(output, indent);
        output.write("<activation>\n");

        super.outputXML(output, indent + 1);

        MzMLContent.indent(output, indent);
        output.write("</activation>\n");
    }

    @Override
    public String toString() {
        return "activation";
    }

    @Override
    public String getTagName() {
        return "activation";
    }
}
