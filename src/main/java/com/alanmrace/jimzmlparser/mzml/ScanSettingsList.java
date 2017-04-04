package com.alanmrace.jimzmlparser.mzml;

public class ScanSettingsList extends MzMLIDContentList<ScanSettings> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;


    public ScanSettingsList(int count) {
        super(count);
    }

    public ScanSettingsList(ScanSettingsList scanSettingsList, ReferenceableParamGroupList rpgList, SourceFileList sourceFileList) {
        this(scanSettingsList.size());

        for (ScanSettings scanSettings : scanSettingsList) {
            this.add(new ScanSettings(scanSettings, rpgList, sourceFileList));
        }
    }

    public void addScanSettings(ScanSettings scanSettings) {
        add(scanSettings);
    }

    public ScanSettings getScanSettings(int index) {
        return get(index);
    }
    
    public ScanSettings getScanSettings(String id) {
        return get(id);
    }
    
    @Override
    public String getTagName() {
        return "scanSettingsList";
    }
}
