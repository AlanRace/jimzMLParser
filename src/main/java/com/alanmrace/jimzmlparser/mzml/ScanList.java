package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.exceptions.InvalidXPathException;
import com.alanmrace.jimzmlparser.exceptions.UnfollowableXPathException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class ScanList extends MzMLContentWithParams implements MzMLTagList<Scan> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static String spectraCombinationID = "MS:1000570";

    protected List<Scan> scanList;

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

    @Override
    public List<OBOTermInclusion> getListOfRequiredCVParams() {
        ArrayList<OBOTermInclusion> required = new ArrayList<OBOTermInclusion>();
        required.add(new OBOTermInclusion(spectraCombinationID, true, true, false));

        return required;
    }

    public void addScan(Scan scan) {
        add(scan);
    }
    
    @Override
    public void add(Scan scan) {
        scan.setParent(this);

        scanList.add(scan);
    }

    @Override
    public int size() {
        return scanList.size();
    }

    public Scan getScan(int index) {
        return scanList.get(index);
    }
    
    @Override
    public Scan get(int index) {
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
    protected void addTagSpecificElementsAtXPathToCollection(Collection<MzMLTag> elements, String fullXPath, String currentXPath) throws InvalidXPathException {
        if (currentXPath.startsWith("/scan")) {
            if (scanList == null) {
                throw new UnfollowableXPathException("No scanList exists, so cannot go to " + fullXPath, fullXPath, currentXPath);
            }

            for (Scan scan : scanList) {
                scan.addElementsAtXPathToCollection(elements, fullXPath, currentXPath);
            }
        }
    }

    @Override
    protected String getXMLAttributeText() {
        return "count=\"" + scanList.size() + "\"";
    }

    @Override
    public Iterator<Scan> iterator() {
        return scanList.iterator();
    }

    @Override
    public String getTagName() {
        return "scanList";
    }
    
    @Override
    public void addChildrenToCollection(Collection<MzMLTag> children) {
        super.addChildrenToCollection(children);
        
        if(scanList != null)
            children.addAll(scanList);
    }

    @Override
    public int indexOf(Scan item) {
        return scanList.indexOf(item);
    }

    @Override
    public Scan remove(int index) {
        return scanList.remove(index);
    }
}
