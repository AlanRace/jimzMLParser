package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.exceptions.InvalidXPathException;
import com.alanmrace.jimzmlparser.exceptions.UnfollowableXPathException;
import com.alanmrace.jimzmlparser.obo.OBO;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
    public static final String SPECTRA_COMBINATION_ID = "MS:1000570";
    
    /**
     * Accession: No spectral combination (MS:1000795).
     */
    public static final String NO_COMBINATION_ID = "MS:1000795";

    /**
     * List of Scans.
     */
    private List<Scan> list = Collections.emptyList();

    /**
     * Create an empty list with specified capacity.
     * 
     * @param count Capacity
     */
    public ScanList(int count) {
        
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

        this.list = new ArrayList<Scan>(scanList.size());

        for (Scan scan : scanList) {
            this.list.add(new Scan(scan, rpgList, icList, sourceFileList));
        }
    }
    
    @Override
    public void add(Scan scan) {
        scan.setParent(this);

        if (list.size() > 1) {
            list.add(scan);
        } else if (list.size() == 1) {
            list = new ArrayList<Scan>(list);
            list.add(scan);
        } else {
            list = Collections.singletonList(scan);
        }
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
        return list.get(index);
    }
    
    /**
     * Returns Scan at specified index in list. Helper method to retain 
     * API, calls {@link ScanList#get(int)}.
     * 
     * @param index Index in the list
     * @return Scan at index, or null if none exists
     */
    public Scan getScan(int index) {
        return get(index);
    }
    
    @Override
    public int size() {        
        return list.size();
    }

    @Override
    protected void addTagSpecificElementsAtXPathToCollection(Collection<MzMLTag> elements, String fullXPath, String currentXPath) throws InvalidXPathException {
        if (currentXPath.startsWith("/scan")) {
            if (list.isEmpty()) {
                throw new UnfollowableXPathException("No scanList exists, so cannot go to " + fullXPath, fullXPath, currentXPath);
            }

            for (Scan scan : list) {
                scan.addElementsAtXPathToCollection(elements, fullXPath, currentXPath);
            }
        }
    }

    @Override
    public String getXMLAttributeText() {
        return "count=\"" + size() + "\"";
    }

    @Override
    public Iterator<Scan> iterator() {
        return list.iterator();
    }

    @Override
    public String getTagName() {
        return "scanList";
    }
    
    @Override
    public void addChildrenToCollection(Collection<MzMLTag> children) {
        super.addChildrenToCollection(children);
        
        children.addAll(list);
    }

    @Override
    public int indexOf(Scan item) {
        return list.indexOf(item);
    }

    @Override
    public Scan remove(int index) {
        return list.remove(index);
    }
    
    @Override
    public boolean remove(Scan item) {
        return list.remove(item);
    }
    
    public static ScanList create() {
        ScanList scanList = new ScanList(1);
        scanList.add(Scan.create());
        
        scanList.addCVParam(new EmptyCVParam(OBO.getOBO().getTerm(ScanList.NO_COMBINATION_ID)));
        
        return scanList;
    }

    @Override
    public boolean contains(Scan item) {
        return list.contains(item);
    }

    @Override
    public void clear() {
        list.clear();
    }
}
