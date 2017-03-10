package com.alanmrace.jimzmlparser.mzML;

import com.alanmrace.jimzmlparser.exceptions.InvalidXPathException;
import com.alanmrace.jimzmlparser.exceptions.UnfollowableXPathException;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class PrecursorList extends MzMLContent implements Serializable, Iterable<Precursor> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private ArrayList<Precursor> precursorList;

    public PrecursorList(int count) {
        precursorList = new ArrayList<Precursor>(count);
    }

    public PrecursorList(PrecursorList precursorList, ReferenceableParamGroupList rpgList, SourceFileList sourceFileList) {
        this(precursorList.size());

        for (Precursor precursor : precursorList) {
            this.precursorList.add(new Precursor(precursor, rpgList, sourceFileList));
        }
    }

    public int size() {
        return precursorList.size();
    }

    public void addPrecursor(Precursor precursor) {
        precursor.setParent(this);

        precursorList.add(precursor);
    }

    public Precursor getPrecursor(int index) {
        return precursorList.get(index);
    }

    @Override
    protected Collection<MzMLContent> getTagSpecificElementsAtXPath(String fullXPath, String currentXPath) throws InvalidXPathException {
        ArrayList<MzMLContent> elements = new ArrayList<MzMLContent>();

        if (currentXPath.startsWith("/precursor")) {
            if (precursorList == null) {
                throw new UnfollowableXPathException("No precursorList exists, so cannot go to " + fullXPath);
            }

            for (Precursor precursor : precursorList) {
                elements.addAll(precursor.getElementsAtXPath(fullXPath, currentXPath));
            }

            return elements;
        }

        return elements;
    }
    
    @Override
    public void outputXML(BufferedWriter output, int indent) throws IOException {
        MzMLContent.indent(output, indent);
        output.write("<precursorList");
        output.write(" count=\"" + precursorList.size() + "\"");
        output.write(">\n");

        for (Precursor precursor : precursorList) {
            precursor.outputXML(output, indent + 1);
        }

        MzMLContent.indent(output, indent);
        output.write("</precursorList>\n");
    }

    @Override
    public String toString() {
        return "precursorList";
    }

    @Override
    public Iterator<Precursor> iterator() {
        return precursorList.iterator();
    }

    @Override
    public String getTagName() {
        return "precursorList";
    }
}
