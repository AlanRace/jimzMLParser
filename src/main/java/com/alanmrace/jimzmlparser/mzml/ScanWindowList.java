package com.alanmrace.jimzmlparser.mzml;

/**
 * Class describing {@literal <scanWindowList>} tag.
 * 
 * @author Alan Race
 */
public class ScanWindowList extends MzMLContentList<ScanWindow> {

    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Create an empty list with specified capacity.
     * 
     * @param count Capacity
     */
    public ScanWindowList(int count) {
        super(count);
    }

    /**
     * Copy constructor.
     *
     * @param scanWindowList Old ScanWindowList to copy
     * @param rpgList New ReferenceableParamGroupList to match references to
     */
    public ScanWindowList(ScanWindowList scanWindowList, ReferenceableParamGroupList rpgList) {
        this(scanWindowList.size());

        for (ScanWindow scanWindow : scanWindowList) {
            this.add(new ScanWindow(scanWindow, rpgList));
        }
    }
    
    /**
     * Add ScanWindow. Helper method to retain API, calls 
     * {@link ScanWindowList#add(com.alanmrace.jimzmlparser.mzml.MzMLTag)}.
     * 
     * @param scanWindow ScanWindow to add to list
     */
    public void addScanWindow(ScanWindow scanWindow) {
        add(scanWindow);
    }

    /**
     * Returns ScanWindow at specified index in list. Helper method to retain 
     * API, calls {@link ScanWindowList#get(int)}.
     * 
     * @param index Index in the list
     * @return ScanWindow at index, or null if none exists
     */
    public ScanWindow getScanWindow(int index) {
        return get(index);
    }

    @Override
    public String getTagName() {
        return "scanWindowList";
    }
}
