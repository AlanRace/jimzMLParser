package com.alanmrace.jimzmlparser.mzML;

import com.alanmrace.jimzmlparser.exceptions.InvalidXPathException;
import com.alanmrace.jimzmlparser.exceptions.UnfollowableXPathException;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class ScanList extends MzMLContent implements Iterable<Scan>, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static String spectraCombinationID = "MS:1000570";

    protected ArrayList<Scan> scanList;

    public ScanList(int count) {
        scanList = new ArrayList<Scan>(count);
    }

    public ScanList(ScanList scanList, ReferenceableParamGroupList rpgList, SourceFileList sourceFileList,
            InstrumentConfigurationList icList) {
        super(scanList, rpgList);

        this.scanList = new ArrayList<Scan>(scanList.size());

        for (Scan scan : scanList) {
            this.scanList.add(new Scan(scan, rpgList, icList, sourceFileList));
        }
    }

    public ArrayList<OBOTermInclusion> getListOfRequiredCVParams() {
        ArrayList<OBOTermInclusion> required = new ArrayList<OBOTermInclusion>();
        required.add(new OBOTermInclusion(spectraCombinationID, true, true, false));

        return required;
    }

    public void addScan(Scan scan) {
        scan.setParent(this);

        scanList.add(scan);
    }

    public int size() {
        return scanList.size();
    }

    public Scan getScan(int index) {
        return scanList.get(index);
    }

//	public double getScanStartTime() {
//		// TODO: Take into account multiple scans
//		if(scanList.size() > 0)
//			return scanList.get(0).getScanStartTime();
//		
//		return -1;
//	}
    @Override
    protected Collection<MzMLContent> getTagSpecificElementsAtXPath(String fullXPath, String currentXPath) throws InvalidXPathException {
        ArrayList<MzMLContent> elements = new ArrayList<MzMLContent>();

        if (currentXPath.startsWith("/scan")) {
            if (scanList == null) {
                throw new UnfollowableXPathException("No scanList exists, so cannot go to " + fullXPath);
            }

            for (Scan scan : scanList) {
                elements.addAll(scan.getElementsAtXPath(fullXPath, currentXPath));
            }

            return elements;
        }

        return elements;
    }

    @Override
    public void outputXML(BufferedWriter output, int indent) throws IOException {
        MzMLContent.indent(output, indent);
        output.write("<scanList");
        output.write(" count=\"" + scanList.size() + "\"");
        output.write(">\n");

        super.outputXML(output, indent + 1);

        for (Scan scan : scanList) {
            scan.outputXML(output, indent + 1);
        }

        MzMLContent.indent(output, indent);
        output.write("</scanList>\n");
    }

    @Override
    public Iterator<Scan> iterator() {
        return scanList.iterator();
    }

    @Override
    public String toString() {
        return "scanList";
    }

    @Override
    public String getTagName() {
        return "scanList";
    }
}
