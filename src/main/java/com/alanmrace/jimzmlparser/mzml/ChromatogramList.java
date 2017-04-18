package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.util.XMLHelper;

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
     * Listener for when/if the 
     */
    private ReferenceList<DataProcessing> dataProcessingList;

    public ChromatogramList(int count, DataProcessing defaultDataProcessingRef) {
        super(count);
        
        this.defaultDataProcessingRef = defaultDataProcessingRef;
    }

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

    public DataProcessing getDefaultDataProcessingRef() {
        return defaultDataProcessingRef;
    }
    
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
    
    public void addChromatogram(Chromatogram chromatogram) {
        add(chromatogram);
    }
    
    public Chromatogram getChromatogram(int index) {
        return get(index);
    }
    
    public Chromatogram getChromatogram(String id) {
        return get(id);
    }
    
    @Override
    protected String getXMLAttributeText() {
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
