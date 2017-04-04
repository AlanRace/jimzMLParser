package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.exceptions.InvalidXPathException;
import com.alanmrace.jimzmlparser.exceptions.UnfollowableXPathException;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class ScanSettingsList extends MzMLContentList<ScanSettings> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private ArrayList<ScanSettings> scanSettingsList;

    public ScanSettingsList(int count) {
        scanSettingsList = new ArrayList<ScanSettings>(count);
    }

    public ScanSettingsList(ScanSettingsList scanSettingsList, ReferenceableParamGroupList rpgList, SourceFileList sourceFileList) {
        this.scanSettingsList = new ArrayList<ScanSettings>(scanSettingsList.size());

        for (ScanSettings scanSettings : scanSettingsList) {
            this.scanSettingsList.add(new ScanSettings(scanSettings, rpgList, sourceFileList));
        }
    }

    public int size() {
        return scanSettingsList.size();
    }

    public void addScanSettings(ScanSettings scanSettings) {
        scanSettings.setParent(this);

        scanSettingsList.add(scanSettings);
    }

    public ScanSettings getScanSettings(String id) {
        for (ScanSettings scanSettings : scanSettingsList) {
            if (scanSettings.getID().equals(id)) {
                return scanSettings;
            }
        }

        return null;
    }

    public ScanSettings getScanSettings(int index) {
        if (index >= scanSettingsList.size()) {
            return null;
        }

        return scanSettingsList.get(index);
    }
    
    @Override
    protected void addTagSpecificElementsAtXPathToCollection(Collection<MzMLContent> elements, String fullXPath, String currentXPath) throws InvalidXPathException {
        if (currentXPath.startsWith("/scanSettings")) {
            if (scanSettingsList == null) {
                throw new UnfollowableXPathException("No scanSettingsList exists, so cannot go to " + fullXPath, fullXPath, currentXPath);
            }

            for (ScanSettings scanSettings : scanSettingsList) {
                scanSettings.addElementsAtXPathToCollection(elements, fullXPath, currentXPath);
            }
        }
    }

    @Override
    public void outputXML(BufferedWriter output, int indent) throws IOException {
        MzMLContent.indent(output, indent);
        output.write("<scanSettingsList");
        output.write(" count=\"" + scanSettingsList.size() + "\"");
        output.write(">\n");

        for (ScanSettings scanSettings : scanSettingsList) {
            scanSettings.outputXML(output, indent + 1);
        }

        MzMLContent.indent(output, indent);
        output.write("</scanSettingsList>\n");
    }

    @Override
    public Iterator<ScanSettings> iterator() {
        return scanSettingsList.iterator();
    }

    @Override
    public String toString() {
        return "scanSettingsList";
    }

    @Override
    public String getTagName() {
        return "scanSettingsList";
    }
    
    @Override
    public void addChildrenToCollection(Collection<MzMLTag> children) {
        if(scanSettingsList != null)
            children.addAll(scanSettingsList);
        
        super.addChildrenToCollection(children);
    }
}
