package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.exceptions.InvalidXPathException;
import com.alanmrace.jimzmlparser.exceptions.UnfollowableXPathException;
import com.alanmrace.jimzmlparser.obo.OBO;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Class describing {@literal <scanList>} tag.
 * 
 * @author Alan Race
 */
public class ScanList extends MzMLContentWithParams implements MzMLTagList<Scan> {

    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Accession: Spectra combination (MS:1000570).
     */
    public static final String spectraCombinationID = "MS:1000570";
    
    /**
     * Accession: No spectral combination (MS:1000795).
     */
    public static final String noCombinationID = "MS:1000795";

    /**
     * List of Scans.
     */
    protected List<Scan> scanList;

    /**
     * Create an empty list with specified capacity.
     * 
     * @param count Capacity
     */
    public ScanList(int count) {
        scanList = new ArrayList<Scan>(count);
    }

    /**
     * Copy constructor.
     *
     * @param scanList Old ScanList to copy
     * @param rpgList New ReferenceableParamGroupList to match references to
     * @param sourceFileList New SourceFileList to match references to
     * @param icList New InstrumentConfigurationList to match references to
     */
    public ScanList(ScanList scanList, ReferenceableParamGroupList rpgList, SourceFileList sourceFileList,
            InstrumentConfigurationList icList) {
        super(scanList, rpgList);

        this.scanList = new ArrayList<Scan>(scanList.size());

        for (Scan scan : scanList) {
            this.scanList.add(new Scan(scan, rpgList, icList, sourceFileList));
        }
    }
    
    @Override
    public void add(Scan scan) {
        scan.setParent(this);

        scanList.add(scan);
    }

    /**
     * Add Scan. Helper method to retain API, calls 
     * {@link ScanList#add(com.alanmrace.jimzmlparser.mzml.MzMLTag)}.
     * 
     * @param scan Scan to add to list
     */
    public void addScan(Scan scan) {
        add(scan);
    }

    @Override
    public Scan get(int index) {
        return scanList.get(index);
    }
    
    /**
     * Returns Scan at specified index in list. Helper method to retain 
     * API, calls {@link ScanList#get(int)}.
     * 
     * @param index Index in the list
     * @return Scan at index, or null if none exists
     */
    public Scan getScan(int index) {
        return scanList.get(index);
    }
    
    @Override
    public int size() {
        return scanList.size();
    }

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
    public String getXMLAttributeText() {
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
    
    @Override
    public boolean remove(Scan item) {
        return scanList.remove(item);
    }
    
    public static ScanList create() {
        ScanList scanList = new ScanList(1);
        scanList.add(Scan.create());
        
        scanList.addCVParam(new EmptyCVParam(OBO.getOBO().getTerm(ScanList.noCombinationID)));
        
        return scanList;
    }
}
