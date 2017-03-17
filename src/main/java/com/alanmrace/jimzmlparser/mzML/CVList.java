package com.alanmrace.jimzmlparser.mzML;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class CVList extends MzMLContent implements Serializable, Iterable<CV> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private ArrayList<CV> cvList;

    public CVList(int count) {
        cvList = new ArrayList<CV>(count);
    }

    public CVList(CVList cvList) {
        this.cvList = new ArrayList<CV>(cvList.size());

        for (CV cv : cvList) {
            this.cvList.add(cv);
        }
    }

    public void addCV(CV cv) {
        cv.setParent(this);

        cvList.add(cv);
    }

    public CV getCV(int index) {
        return cvList.get(index);
    }

    public CV getCV(String id) {
        for (CV cv : cvList) {
            if (cv.getID().equals(id)) {
                return cv;
            }
        }

        return null;
    }

    public int getIndexOf(CV cv) {
        return cvList.indexOf(cv);
    }

    public int size() {
        return cvList.size();
    }

    @Override
    public void outputXML(BufferedWriter output, int indent) throws IOException {
        MzMLContent.indent(output, indent);
        output.write("<cvList");
        output.write(" count=\"" + cvList.size() + "\"");
        output.write(">\n");

        for (CV cv : cvList) {
            cv.outputXML(output, indent + 1);
        }

        MzMLContent.indent(output, indent);
        output.write("</cvList>\n");
    }

    @Override
    public String toString() {
        return "cvList";
    }

    @Override
    public Iterator<CV> iterator() {
        return cvList.iterator();
    }

    @Override
    public String getTagName() {
        return "cvList";
    }
    
    @Override
    public void addChildrenToCollection(Collection<MzMLTag> children) {
        if(cvList != null)
            children.addAll(cvList);
        
        super.addChildrenToCollection(children);
    }
}
