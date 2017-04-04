package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.exceptions.InvalidXPathException;
import com.alanmrace.jimzmlparser.exceptions.UnfollowableXPathException;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class SelectedIonList extends MzMLContentList<SelectedIon> {

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
    protected void addTagSpecificElementsAtXPathToCollection(Collection<MzMLContent> elements, String fullXPath, String currentXPath) throws InvalidXPathException {
        if (currentXPath.startsWith("/selectedIon")) {
            if (selectedIonList == null) {
                throw new UnfollowableXPathException("No selectedIonList exists, so cannot go to " + fullXPath, fullXPath, currentXPath);
            }

            for (SelectedIon selectedIon : selectedIonList) {
                selectedIon.addElementsAtXPathToCollection(elements, fullXPath, currentXPath);
            }
        }
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
    
    @Override
    public void addChildrenToCollection(Collection<MzMLTag> children) {
        if(selectedIonList != null)
            children.addAll(selectedIonList);
        
        super.addChildrenToCollection(children);
    }
}
