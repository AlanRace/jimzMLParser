package com.alanmrace.jimzmlparser.mzml;

/**
 * Class describing {@literal <scanSettingsList>} tag.
 * 
 * @author Alan Race
 */
public class ScanSettingsList extends MzMLIDContentList<ScanSettings> {

    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Create an empty list with specified capacity.
     * 
     * @param count Capacity
     */
    public ScanSettingsList(int count) {
        super(count);
    }

    /**
     * Copy constructor.
     *
     * @param scanSettingsList Old ScanSettingsList to copy
     * @param rpgList New ReferenceableParamGroupList to match references to
     * @param sourceFileList New SourceFileList to match references to
     */
    public ScanSettingsList(ScanSettingsList scanSettingsList, ReferenceableParamGroupList rpgList, SourceFileList sourceFileList) {
        this(scanSettingsList.size());

        for (ScanSettings scanSettings : scanSettingsList) {
            this.add(new ScanSettings(scanSettings, rpgList, sourceFileList));
        }
    }

    /**
     * Add ScanSettings. Helper method to retain API, calls 
     * {@link ScanSettingsList#add(com.alanmrace.jimzmlparser.mzml.MzMLTag)}.
     * 
     * @param scanSettings ScanSettings to add to list
     */
    public void addScanSettings(ScanSettings scanSettings) {
        add(scanSettings);
    }

    /**
     * Returns ScanSettings at specified index in list. Helper method to retain 
     * API, calls {@link ScanSettingsList#get(int)}.
     * 
     * @param index Index in the list
     * @return ScanSettings at index, or null if none exists
     */
    public ScanSettings getScanSettings(int index) {
        return get(index);
    }
    
    /**
     * Returns ScanSettings in the list with the specified ID. Helper 
     * method to retain API, calls {@link ScanSettingsList#get(java.lang.String)}.
     * 
     * @param id Unique ID
     * @return ScanSettings with ID, or null if none exists
     */
    public ScanSettings getScanSettings(String id) {
        return get(id);
    }
    
    @Override
    public String getTagName() {
        return "scanSettingsList";
    }
}
