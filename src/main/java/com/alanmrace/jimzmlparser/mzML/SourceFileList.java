package com.alanmrace.jimzmlparser.mzML;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class SourceFileList extends MzMLContent implements Serializable, Iterable<SourceFile> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private ArrayList<SourceFile> sourceFileList;

    public SourceFileList(int count) {
        sourceFileList = new ArrayList<SourceFile>(count);
    }

    public SourceFileList(SourceFileList sourceFileList, ReferenceableParamGroupList rpgList) {
        this.sourceFileList = new ArrayList<SourceFile>(sourceFileList.size());

        for (SourceFile sourceFile : sourceFileList) {
            this.sourceFileList.add(new SourceFile(sourceFile, rpgList));
        }
    }

    public void addSourceFile(SourceFile sourceFile) {
        sourceFile.setParent(this);

        sourceFileList.add(sourceFile);
    }

    public SourceFile getSourceFile(String id) {
        for (SourceFile sourceFile : sourceFileList) {
            if (sourceFile.getID().equals(id)) {
                return sourceFile;
            }
        }

        return null;
    }

    public SourceFile getSourceFile(int index) {
        return sourceFileList.get(index);
    }

    public int getIndexOf(SourceFile sourceFile) {
        return sourceFileList.indexOf(sourceFile);
    }

    public int size() {
        return sourceFileList.size();
    }

    @Override
    public void outputXML(BufferedWriter output, int indent) throws IOException {
        MzMLContent.indent(output, indent);
        output.write("<sourceFileList");
        output.write(" count=\"" + sourceFileList.size() + "\"");
        output.write(">\n");

        for (SourceFile sourceFile : sourceFileList) {
            sourceFile.outputXML(output, indent + 1);
        }

        MzMLContent.indent(output, indent);
        output.write("</sourceFileList>\n");
    }

    @Override
    public String toString() {
        return "sourceFileList";
    }

    @Override
    public Iterator<SourceFile> iterator() {
        return sourceFileList.iterator();
    }

    @Override
    public String getTagName() {
        return "sourceFileList";
    }
}
