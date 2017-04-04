package com.alanmrace.jimzmlparser.mzml;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class IsolationWindow extends MzMLContentWithParams implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static String isolationWindowAttributeID = "MS:1000792"; // Required child (1+)

    public IsolationWindow() {
        super();
    }

    public IsolationWindow(IsolationWindow isolationWindow, ReferenceableParamGroupList rpgList) {
        super(isolationWindow, rpgList);
    }

    @Override
    public ArrayList<OBOTermInclusion> getListOfRequiredCVParams() {
        ArrayList<OBOTermInclusion> required = new ArrayList<OBOTermInclusion>();
        required.add(new OBOTermInclusion(isolationWindowAttributeID, false, true, false));

        return required;
    }

    @Override
    public void outputXML(BufferedWriter output, int indent) throws IOException {
        MzMLContent.indent(output, indent);
        output.write("<isolationWindow>\n");

        super.outputXML(output, indent + 1);

        MzMLContent.indent(output, indent);
        output.write("</isolationWindow>\n");
    }

    @Override
    public String toString() {
        return "isolationWindow";
    }

    @Override
    public String getTagName() {
        return "isolationWindow";
    }
}
