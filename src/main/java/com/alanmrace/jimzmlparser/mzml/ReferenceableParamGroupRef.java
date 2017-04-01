package com.alanmrace.jimzmlparser.mzml;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;

public class ReferenceableParamGroupRef implements Serializable, MzMLTag { //, MutableTreeNode {

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

    @Override
    public void outputXML(BufferedWriter output, int indent) throws IOException {
        MzMLContent.indent(output, indent);
        
        output.write("<referenceableParamGroupRef");
        output.write(" ref=\"" + ref.getID() + "\"");
        output.write("/>\n");
    }

    @Override
    public String toString() {
        return "referenceableParamGroupRef: " + ref.getID();
    }

    @Override
    public String getTagName() {
        return "referenceableParamGroupRef";
    }
    
    @Override
    public void addChildrenToCollection(Collection<MzMLTag> children) {
        // No children to add
    }
}
