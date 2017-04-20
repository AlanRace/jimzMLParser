package com.alanmrace.jimzmlparser.mzml;

/**
 * Class describing {@literal <dataProcessingList>} tag.
 * 
 * @author Alan Race
 */
public class DataProcessingList extends MzMLIDContentList<DataProcessing> {

    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Create an empty list with specified capacity.
     * 
     * @param count Capacity
     */
    public DataProcessingList(int count) {
        super(count);
    }

    /**
     * Copy constructor.
     *
     * @param dpList Old DataProcessingList to copy
     * @param rpgList New ReferenceableParamGroupList to match references to
     * @param softwareList New SoftwareList to match references to
     */
    public DataProcessingList(DataProcessingList dpList, ReferenceableParamGroupList rpgList, SoftwareList softwareList) {
        this(dpList.size());

        for (DataProcessing dp : dpList) {
            this.add(new DataProcessing(dp, rpgList, softwareList));
        }
    }

    /**
     * Add DataProcessing. Helper method to retain API, calls 
     * {@link DataProcessingList#add(com.alanmrace.jimzmlparser.mzml.MzMLTag)}.
     * 
     * @param dataProcessing DataProcessing to add to list
     */
    public void addDataProcessing(DataProcessing dataProcessing) {
        add(dataProcessing);
    }
    
    /**
     * Returns DataProcessing at specified index in list. Helper method to retain 
     * API, calls {@link DataProcessingList#get(int)}.
     * 
     * @param index Index in the list
     * @return DataProcessing at index, or null if none exists
     */
    public DataProcessing getDataProcessing(int index) {
        return get(index);
    }
    
    /**
     * Returns DataProcessing in the list with the specified ID. Helper 
     * method to retain API, calls {@link DataProcessingList#get(java.lang.String)}.
     * 
     * @param id Unique ID
     * @return DataProcessing with ID, or null if none exists
     */
    public DataProcessing getDataProcessing(String id) {
        return get(id);
    }
    
    @Override
    public String getTagName() {
        return "dataProcessingList";
    }
    
    /**
     * Create base valid DataProcessingList. This is a wrapper method for 
     * {@code DataProcessingList.create(Software.create())}.
     * 
     * @return Valid DataProcessingList
     */
    public static DataProcessingList create() {
        return create(Software.create());
    }
    
    /**
     * Create basic valid DataProcessingList with the supplied Software. This
     * creates a single DataProcessing element in the list using 
     * {@code DataProcessing.create(software)}.
     * 
     * @param software Software to pass to DataProcessing
     * @return Valid DataProcessingList
     */
    public static DataProcessingList create(Software software) {
        DataProcessingList dpList = new DataProcessingList(1);
        dpList.add(DataProcessing.create(software));
        
        return dpList;
    }
}
