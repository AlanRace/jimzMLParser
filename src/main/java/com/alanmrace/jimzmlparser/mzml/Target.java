package com.alanmrace.jimzmlparser.mzml;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;

public class Target extends MzMLContent implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public Target() {
        super();
    }

    public Target(Target target, ReferenceableParamGroupList rpgList) {
        super(target, rpgList);
    }

    @Override
    public void outputXML(BufferedWriter output, int indent) throws IOException {
        MzMLContent.indent(output, indent);
        output.write("<target>\n");

        super.outputXML(output, indent + 1);

        MzMLContent.indent(output, indent);
        output.write("</target>\n");
    }

    @Override
    public String toString() {
        return "target";
    }

    @Override
    public String getTagName() {
        return "target";
    }
}
