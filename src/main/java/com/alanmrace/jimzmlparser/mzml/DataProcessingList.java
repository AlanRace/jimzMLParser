package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.obo.OBO;

public class DataProcessingList extends MzMLIDContentList<DataProcessing> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;


    public DataProcessingList(int count) {
        super(count);
    }

    public DataProcessingList(DataProcessingList dpList, ReferenceableParamGroupList rpgList, SoftwareList softwareList) {
        this(dpList.size());

        for (DataProcessing dp : dpList) {
            this.add(new DataProcessing(dp, rpgList, softwareList));
        }
    }

    public void addDataProcessing(DataProcessing dataProcessing) {
        add(dataProcessing);
    }
    
    public DataProcessing getDataProcessing(int index) {
        return get(index);
    }
    
    public DataProcessing getDataProcessing(String id) {
        return get(id);
    }
    
    @Override
    public String getTagName() {
        return "dataProcessingList";
    }
    
    public static DataProcessingList create(Software software) {
        DataProcessingList dpList = new DataProcessingList(1);
        DataProcessing dp = new DataProcessing("imzML-creation");
        dpList.add(dp);
        
        ProcessingMethod pm = new ProcessingMethod(software);
        dp.add(pm);
        
        pm.addCVParam(new EmptyCVParam(OBO.getOBO().getTerm(ProcessingMethod.conversionTomzMLID)));
        
        return dpList;
    }
}
