package com.alanmrace.jimzmlparser.mzML;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;

import javax.swing.tree.MutableTreeNode;

public class SourceFileRef implements Serializable, MzMLTag { //, MutableTreeNode {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private SourceFile ref;

    private MutableTreeNode parentTreeNode;

    public SourceFileRef(SourceFile ref) {
        this.ref = ref;
    }

    public SourceFile getRef() {
        return ref;
    }

    public void outputXML(BufferedWriter output) throws IOException {
        output.write("<sourceFileRef");
        output.write(" ref=\"" + ref.getID() + "\"");
        output.write("/>\n");
    }

    @Override
    public String toString() {
        return "sourceFileRef: " + ref.getID();
    }

    @Override
    public String getTagName() {
        return "sourceFileRef";
    }

    @Override
    public void addChildrenToCollection(Collection<MzMLTag> children) {
        // No children to add
    }
}
