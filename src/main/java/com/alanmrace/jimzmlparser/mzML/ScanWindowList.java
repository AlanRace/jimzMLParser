package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.exceptions.InvalidXPathException;
import com.alanmrace.jimzmlparser.exceptions.UnfollowableXPathException;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class ScanWindowList extends MzMLContent implements Serializable, Iterable<ScanWindow> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private ArrayList<ScanWindow> scanWindowList;

    public ScanWindowList(int count) {
        scanWindowList = new ArrayList<ScanWindow>(count);
    }

    public ScanWindowList(ScanWindowList scanWindowList, ReferenceableParamGroupList rpgList) {
        this(scanWindowList.size());

        for (ScanWindow scanWindow : scanWindowList) {
            this.scanWindowList.add(new ScanWindow(scanWindow, rpgList));
        }
    }

    public int size() {
        return scanWindowList.size();
    }

    public void addScanWindow(ScanWindow scanWindow) {
        scanWindow.setParent(this);

        scanWindowList.add(scanWindow);
    }

    public ScanWindow getScanWindow(int index) {
        return scanWindowList.get(index);
    }

    @Override
    protected void addTagSpecificElementsAtXPathToCollection(Collection<MzMLContent> elements, String fullXPath, String currentXPath) throws InvalidXPathException {
        if (currentXPath.startsWith("/scanWindow")) {
            if (scanWindowList == null) {
                throw new UnfollowableXPathException("No scanWindow exists, so cannot go to " + fullXPath, fullXPath, currentXPath);
            }

            for (ScanWindow scanWindow : scanWindowList) {
                scanWindow.addElementsAtXPathToCollection(elements, fullXPath, currentXPath);
            }
        }
    }
    
    @Override
    public void outputXML(BufferedWriter output, int indent) throws IOException {
        MzMLContent.indent(output, indent);
        output.write("<scanWindowList");
        output.write(" count=\"" + scanWindowList.size() + "\"");
        output.write(">\n");

        for (ScanWindow scanWindow : scanWindowList) {
            scanWindow.outputXML(output, indent + 1);
        }

        MzMLContent.indent(output, indent);
        output.write("</scanWindowList>\n");
    }

    @Override
    public String toString() {
        return "scanWindowList";
    }

    @Override
    public Iterator<ScanWindow> iterator() {
        return this.scanWindowList.iterator();
    }

    @Override
    public String getTagName() {
        return "scanWindowList";
    }
    
    @Override
    public void addChildrenToCollection(Collection<MzMLTag> children) {
        if(scanWindowList != null)
            children.addAll(scanWindowList);
        
        super.addChildrenToCollection(children);
    }
}
