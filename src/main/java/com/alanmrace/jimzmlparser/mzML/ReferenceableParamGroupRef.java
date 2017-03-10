package com.alanmrace.jimzmlparser.mzML;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;

public class ReferenceableParamGroupRef implements Serializable { //, MutableTreeNode {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private ReferenceableParamGroup ref;

    public ReferenceableParamGroupRef(ReferenceableParamGroup ref) {
        this.ref = ref;
    }

    public ReferenceableParamGroup getRef() {
        return ref;
    }

    public void outputXML(BufferedWriter output) throws IOException {
        output.write("<referenceableParamGroupRef");
        output.write(" ref=\"" + ref.getID() + "\"");
        output.write("/>\n");
    }

    @Override
    public String toString() {
        return "referenceableParamGroupRef: " + ref.getID();
    }
}
