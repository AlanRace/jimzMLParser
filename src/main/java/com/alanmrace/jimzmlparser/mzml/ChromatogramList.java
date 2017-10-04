package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.util.XMLHelper;

/**
 * Class describing {@literal <chromatogramList>} tag.
 * 
 * @author Alan Race
 */
public class ChromatogramList extends MzMLIDContentList<Chromatogram> {

    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Default DataProcessing description for the ChromatogramList.
     */
    private DataProcessing defaultDataProcessingRef;

    /**
     * Reference to DataProcessingList so that it can be kept updated if a 
     * chromatogram is added to this list that has a DataProcessing that has not
     * previously been seen.
     */
    private ReferenceList<DataProcessing> dataProcessingList;

    /**
     * Create an empty list with specified capacity with the supplied default
     * DataProcessing.
     * 
     * @param count Capacity
     * @param defaultDataProcessingRef Default DataProcessing
     */
    public ChromatogramList(int count, DataProcessing defaultDataProcessingRef) {
        super(count);
        
        this.defaultDataProcessingRef = defaultDataProcessingRef;
    }

    /**
     * Copy constructor.
     *
     * @param chromatogramList Old ChromatogramList to copy
     * @param rpgList New ReferenceableParamGroupList to match references to
     * @param dpList New DataProcessingList to match references to
     * @param sourceFileList New SourceFileList to match references to
     */
    public ChromatogramList(ChromatogramList chromatogramList, ReferenceableParamGroupList rpgList,
            DataProcessingList dpList, SourceFileList sourceFileList) {
        super(chromatogramList.size());

        if (chromatogramList.defaultDataProcessingRef != null && dpList != null) {
            for (DataProcessing dp : dpList) {
                if (chromatogramList.defaultDataProcessingRef.getID().equals(dp.getID())) {
                    this.defaultDataProcessingRef = dp;

                    break;
                }
            }
        }

        for (Chromatogram chromatogram : chromatogramList) {
            this.add(new Chromatogram(chromatogram, rpgList, dpList, sourceFileList));
        }
    }

    /**
     * Returns the default DataProcessing for the ChromatogramList.
     * 
     * @return Default DataProcessing
     */
    public DataProcessing getDefaultDataProcessingRef() {
        return defaultDataProcessingRef;
    }
    
    /**
     * Sets the DataProcessingList which should be kept up to date with all DataProcessing
     * references. This also sets the DataProcessingList in each Chromatogram in the list.
     * 
     * @param dataProcessingList List to be kept up to date
     */
    protected void setDataProcessingList(ReferenceList<DataProcessing> dataProcessingList) {
        this.dataProcessingList = dataProcessingList;
        
        for(Chromatogram chromatogram : this) {
            chromatogram.setDataProcessingList(dataProcessingList);
        }
    }

    @Override
    public void add(Chromatogram chromatogram) {
        super.add(chromatogram);
        
        if(dataProcessingList != null)
            chromatogram.setDataProcessingList(dataProcessingList);
    }
    
    /**
     * Add Chromatogram. Helper method to retain API, calls 
     * {@link ChromatogramList#add(com.alanmrace.jimzmlparser.mzml.MzMLTag)}.
     * 
     * @param chromatogram Chromatogram to add to list
     */
    public void addChromatogram(Chromatogram chromatogram) {
        add(chromatogram);
    }
    
    /**
     * Returns Chromatogram at specified index in list. Helper method to retain 
     * API, calls {@link ChromatogramList#get(int)}.
     * 
     * @param index Index in the list
     * @return Chromatogram at index, or null if none exists
     */
    public Chromatogram getChromatogram(int index) {
        return get(index);
    }
    
    /**
     * Returns Chromatogram in the list with the specified ID. Helper 
     * method to retain API, calls {@link ChromatogramList#get(java.lang.String)}.
     * 
     * @param id Unique ID
     * @return Chromatogram with ID, or null if none exists
     */
    public Chromatogram getChromatogram(String id) {
        return get(id);
    }
    
    @Override
    public String getXMLAttributeText() {
        return super.getXMLAttributeText() + " defaultDataProcessingRef=\"" + XMLHelper.ensureSafeXML(defaultDataProcessingRef.getID()) + "\""; 
    }

    @Override
    public String getTagName() {
        return "chromatogramList";
    }
    
    @Override
    public void ensureValidReferences() {
        if(dataProcessingList != null)
            defaultDataProcessingRef = dataProcessingList.getValidReference(defaultDataProcessingRef);
    }
}
