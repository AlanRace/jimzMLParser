package com.alanmrace.jimzmlparser.mzML;

import com.alanmrace.jimzmlparser.exceptions.InvalidXPathException;
import com.alanmrace.jimzmlparser.exceptions.UnfollowableXPathException;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class SelectedIonList extends MzMLContent implements Serializable, Iterable<SelectedIon> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private ArrayList<SelectedIon> selectedIonList;

    public SelectedIonList(int count) {
        selectedIonList = new ArrayList<SelectedIon>(count);
    }

    public SelectedIonList(SelectedIonList siList, ReferenceableParamGroupList rpgList) {
        this(siList.size());

        for (SelectedIon si : siList) {
            this.selectedIonList.add(new SelectedIon(si, rpgList));
        }
    }

    public int size() {
        return selectedIonList.size();
    }

    public void addSelectedIon(SelectedIon selectedIon) {
        selectedIon.setParent(this);

        selectedIonList.add(selectedIon);
    }

    public SelectedIon getSelectedIon(int index) {
        return selectedIonList.get(index);
    }

    @Override
    protected Collection<MzMLContent> getTagSpecificElementsAtXPath(String fullXPath, String currentXPath) throws InvalidXPathException {
        ArrayList<MzMLContent> elements = new ArrayList<MzMLContent>();

        if (currentXPath.startsWith("/selectedIon")) {
            if (selectedIonList == null) {
                throw new UnfollowableXPathException("No selectedIonList exists, so cannot go to " + fullXPath);
            }

            for (SelectedIon selectedIon : selectedIonList) {
                elements.addAll(selectedIon.getElementsAtXPath(fullXPath, currentXPath));
            }

            return elements;
        }

        return elements;
    }
    
    @Override
    public void outputXML(BufferedWriter output, int indent) throws IOException {
        MzMLContent.indent(output, indent);
        output.write("<selectedIonList");
        output.write(" count=\"" + selectedIonList.size() + "\"");
        output.write(">\n");

        for (SelectedIon selectedIon : selectedIonList) {
            selectedIon.outputXML(output, indent + 1);
        }

        MzMLContent.indent(output, indent);
        output.write("</selectedIonList>\n");
    }

    @Override
    public String toString() {
        return "selectedIonList";
    }

    @Override
    public Iterator<SelectedIon> iterator() {
        return selectedIonList.iterator();
    }

    @Override
    public String getTagName() {
        return "selectedIonList";
    }
}
