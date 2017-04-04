package com.alanmrace.jimzmlparser.mzml;

public class ScanWindowList extends MzMLContentList<ScanWindow> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public ScanWindowList(int count) {
        super(count);
    }

    public ScanWindowList(ScanWindowList scanWindowList, ReferenceableParamGroupList rpgList) {
        this(scanWindowList.size());

        for (ScanWindow scanWindow : scanWindowList) {
            this.add(new ScanWindow(scanWindow, rpgList));
        }
    }
    
    public void addScanWindow(ScanWindow scanWindow) {
        add(scanWindow);
    }

    public ScanWindow getScanWindow(int index) {
        return get(index);
    }

    @Override
    public String getTagName() {
        return "scanWindowList";
    }
}
