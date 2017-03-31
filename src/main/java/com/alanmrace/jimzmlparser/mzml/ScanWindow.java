package com.alanmrace.jimzmlparser.mzml;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;

public class ScanWindow extends MzMLContent implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public ScanWindow() {
        super();
    }

    public ScanWindow(ScanWindow scanWindow, ReferenceableParamGroupList rpgList) {
        super(scanWindow, rpgList);
    }

    @Override
    public void outputXML(BufferedWriter output, int indent) throws IOException {
        MzMLContent.indent(output, indent);
        output.write("<scanWindow");
        output.write(">\n");

        super.outputXML(output, indent + 1);

        MzMLContent.indent(output, indent);
        output.write("</scanWindow>\n");
    }

    @Override
    public String toString() {
        return "scanWindow";
    }

    @Override
    public String getTagName() {
        return "scanWindow";
    }

}
