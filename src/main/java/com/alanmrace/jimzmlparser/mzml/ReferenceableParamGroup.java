package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.util.XMLHelper;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class ReferenceableParamGroup extends MzMLContentWithParams implements ReferenceableTag {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private static int idNumber = 0;

    private String id;	// Required

    public ReferenceableParamGroup() {
        id = "refParam" + idNumber++;
    }

    public ReferenceableParamGroup(ReferenceableParamGroup rpg) {
        super(rpg, null);

        id = rpg.id;
    }

    @Override
    public ArrayList<OBOTermInclusion> getListOfOptionalCVParams() {
        ArrayList<OBOTermInclusion> optional = new ArrayList<OBOTermInclusion>();
        optional.add(new OBOTermInclusion("IMS:0000000", false, true, true));
        optional.add(new OBOTermInclusion("MS:0000000", false, true, true));

        return optional;
    }

    public ReferenceableParamGroup(String id) {
        this.id = id;
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public void setID(String id) {
        this.id = id;
    }

    @Override
    public void outputXML(BufferedWriter output, int indent) throws IOException {
        MzMLContent.indent(output, indent);
        output.write("<referenceableParamGroup");
        output.write(" id=\"" + XMLHelper.ensureSafeXML(id) + "\"");
        output.write(">\n");

        super.outputXML(output, indent + 1);

        MzMLContent.indent(output, indent);
        output.write("</referenceableParamGroup>\n");
    }

    @Override
    public String toString() {
        return "referenceableParamGroup: " + id;
    }

    @Override
    public String getTagName() {
        return "referenceableParamGroup";
    }
}
