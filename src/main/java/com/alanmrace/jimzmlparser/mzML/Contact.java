package com.alanmrace.jimzmlparser.mzml;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class Contact extends MzMLContent implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static String contactOrganisationID = "MS:1000590"; // Required (1)
    public static String contactNameID = "MS:1000586"; // Required (1)
    public static String contactPersonAttributeID = "MS:1000585"; // Optional child (1+)

    public Contact() {
        super();
    }

    public Contact(Contact contact, ReferenceableParamGroupList rpgList) {
        super(contact, rpgList);
    }

    @Override
    public ArrayList<OBOTermInclusion> getListOfRequiredCVParams() {
        ArrayList<OBOTermInclusion> required = new ArrayList<OBOTermInclusion>();
        required.add(new OBOTermInclusion(contactNameID, true, false, true));
        required.add(new OBOTermInclusion(contactOrganisationID, true, false, true));

        return required;
    }

    @Override
    public ArrayList<OBOTermInclusion> getListOfOptionalCVParams() {
        ArrayList<OBOTermInclusion> optional = new ArrayList<OBOTermInclusion>();
        optional.add(new OBOTermInclusion(contactPersonAttributeID, false, true, false));

        return optional;
    }

    @Override
    public void outputXML(BufferedWriter output, int indent) throws IOException {
        MzMLContent.indent(output, indent);
        output.write("<" + getTagName() + ">\n");

        super.outputXML(output, indent + 1);

        MzMLContent.indent(output, indent);
        output.write("</" + getTagName() + ">\n");
    }

    @Override
    public String toString() {
        String name = getCVParam(contactNameID).getValueAsString();
        String organisation = getCVParam(contactOrganisationID).getValueAsString();

        return "contact: " + name + " (" + organisation + ")";
    }

    @Override
    public String getTagName() {
        return "contact";
    }
}
