package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.listener.DataProcessingListener;

public class DataProcessingList extends MzMLIDContentList<DataProcessing> implements DataProcessingListener {

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
    
    public static DataProcessingList create() {
        return create(Software.create());
    }
    
    public static DataProcessingList create(Software software) {
        DataProcessingList dpList = new DataProcessingList(1);
        dpList.add(DataProcessing.create(software));
        
        return dpList;
    }

    @Override
    public DataProcessing referenceCheck(DataProcessing processing) {
        boolean found = false;
        
        for(DataProcessing curProcessing : this) {
            if(processing.equals(curProcessing) || processing.getID().equals(curProcessing.getID())) {
                return curProcessing;
            }
        }
        
        if(!found)
            this.add(processing);
        
        return processing;
    }
}
