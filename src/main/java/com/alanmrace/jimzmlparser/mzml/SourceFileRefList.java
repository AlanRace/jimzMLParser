package com.alanmrace.jimzmlparser.mzml;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class SourceFileRefList extends MzMLContent implements Serializable, Iterable<SourceFileRef> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private ArrayList<SourceFileRef> sourceFileRefList;

    public SourceFileRefList(int count) {
        sourceFileRefList = new ArrayList<SourceFileRef>(count);
    }

    public SourceFileRefList(SourceFileRefList sourceFileRefList, SourceFileList sourceFileList) {
        this.sourceFileRefList = new ArrayList<SourceFileRef>(sourceFileRefList.size());

        for (SourceFileRef ref : sourceFileRefList) {
            for (SourceFile sourceFile : sourceFileList) {
                if (ref.getRef().getID().equals(sourceFile.getID())) {
                    this.sourceFileRefList.add(new SourceFileRef(sourceFile));
                }
            }
        }
    }

    public int size() {
        return sourceFileRefList.size();
    }

    public void addSourceFileRef(SourceFileRef sourceFileRef) {
//		sourceFileRef.setParent(this);

        sourceFileRefList.add(sourceFileRef);
    }

    @Override
    public void outputXML(BufferedWriter output, int indent) throws IOException {
        MzMLContent.indent(output, indent);
        output.write("<sourceFileRefList");
        output.write(" count=\"" + sourceFileRefList.size() + "\"");
        output.write(">\n");

        for (SourceFileRef sourceFileRef : sourceFileRefList) {
            MzMLContent.indent(output, indent + 1);
            sourceFileRef.outputXML(output);
        }

        MzMLContent.indent(output, indent);
        output.write("</sourceFileRefList>\n");
    }

    @Override
    public String toString() {
        return "sourceFileRefList";
    }

    @Override
    public Iterator<SourceFileRef> iterator() {
        return sourceFileRefList.iterator();
    }

    @Override
    public String getTagName() {
        return "sourceFileRefList";
    }
    
    @Override
    public void addChildrenToCollection(Collection<MzMLTag> children) {
        if(sourceFileRefList != null)
            children.addAll(sourceFileRefList);
        
        super.addChildrenToCollection(children);
    }
}
