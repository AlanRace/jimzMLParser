package com.alanmrace.jimzmlparser.mzML;

import com.alanmrace.jimzmlparser.exceptions.InvalidXPathException;
import com.alanmrace.jimzmlparser.exceptions.UnfollowableXPathException;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class SoftwareList extends MzMLContent implements Iterable<Software>, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private ArrayList<Software> softwareList;

    public SoftwareList(int count) {
        softwareList = new ArrayList<Software>(count);
    }

    public SoftwareList(SoftwareList softwareList, ReferenceableParamGroupList rpgList) {
        if (softwareList != null) {
            this.softwareList = new ArrayList<Software>(softwareList.size());

            for (Software software : softwareList) {
                this.softwareList.add(new Software(software, rpgList));
            }
        } else {
            this.softwareList = new ArrayList<Software>(0);
        }
    }

    public void addSoftware(Software software) {
        software.setParent(this);

        softwareList.add(software);
    }

    public void removeSoftware(int index) {
        Software removed = softwareList.remove(index);

        removed.setParent(null);
    }

    public Software getSoftware(String id) {
        for (Software software : softwareList) {
            if (software.getID().equals(id)) {
                return software;
            }
        }

        return null;
    }

    public Software getSoftware(int index) {
        return softwareList.get(index);
    }

    public int size() {
        return softwareList.size();
    }

    @Override
    protected Collection<MzMLContent> getTagSpecificElementsAtXPath(String fullXPath, String currentXPath) throws InvalidXPathException {
        ArrayList<MzMLContent> elements = new ArrayList<MzMLContent>();

        if (currentXPath.startsWith("/software")) {
            if (softwareList == null) {
                throw new UnfollowableXPathException("No softwareList exists, so cannot go to " + fullXPath);
            }

            for (Software software : softwareList) {
                elements.addAll(software.getElementsAtXPath(fullXPath, currentXPath));
            }

            return elements;
        }

        return elements;
    }
    
    @Override
    public void outputXML(BufferedWriter output, int indent) throws IOException {
        MzMLContent.indent(output, indent);
        output.write("<softwareList");
        output.write(" count=\"" + softwareList.size() + "\"");
        output.write(">\n");

        for (Software software : softwareList) {
            software.outputXML(output, indent + 1);
        }

        MzMLContent.indent(output, indent);
        output.write("</softwareList>\n");
    }

    @Override
    public Iterator<Software> iterator() {
        return softwareList.iterator();
    }

    @Override
    public String toString() {
        return "softwareList";
    }

    @Override
    public String getTagName() {
        return "softwareList";
    }
}
