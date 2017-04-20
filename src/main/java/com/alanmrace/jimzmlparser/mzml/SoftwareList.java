package com.alanmrace.jimzmlparser.mzml;

/**
 * Class describing {@literal <softwareList>} tag.
 * 
 * @author Alan Race
 */
public class SoftwareList extends MzMLIDContentList<Software> {

    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Create an empty list with specified capacity with the supplied default
     * DataProcessing.
     * 
     * @param count Capacity
     */
    public SoftwareList(int count) {
        super(count);
    }

    /**
     * Copy constructor.
     *
     * @param softwareList Old SoftwareList to copy
     * @param rpgList New ReferenceableParamGroupList to match references to
     */
    public SoftwareList(SoftwareList softwareList, ReferenceableParamGroupList rpgList) {
        this(softwareList.size());

        for (Software software : softwareList) {
            this.add(new Software(software, rpgList));
        }
    }

    /**
     * Add Software. Helper method to retain API, calls 
     * {@link SoftwareList#add(com.alanmrace.jimzmlparser.mzml.MzMLTag)}.
     * 
     * @param software Software to add to list
     */
    public void addSoftware(Software software) {
        add(software);
    }

    /**
     * Returns Software at specified index in list. Helper method to retain 
     * API, calls {@link SoftwareList#get(int)}.
     * 
     * @param index Index in the list
     * @return Software at index, or null if none exists
     */
    public Software getSoftware(int index) {
        return get(index);
    }

    /**
     * Returns Software in the list with the specified ID. Helper 
     * method to retain API, calls {@link SoftwareList#get(java.lang.String)}.
     * 
     * @param id Unique ID
     * @return Software with ID, or null if none exists
     */
    public Software getSoftware(String id) {
        return get(id);
    }

    /**
     * Removes Software at specified index from the list. Helper method to retain 
     * API, calls {@link SoftwareList#remove(int)}.
     * 
     * @param index Index in the list
     * @return Software removed at index, or null if none exists
     */
    public Software removeSoftware(int index) {
        return remove(index);
    }

    @Override
    public String getTagName() {
        return "softwareList";
    }
    
    /**
     * Create a default valid SoftwareList. This creates a list with a single 
     * item, created by {@code Software.create()}.
     * 
     * @return
     */
    public static SoftwareList create() {
        SoftwareList softwareList = new SoftwareList(1);
        
        softwareList.add(Software.create());
        
        return softwareList;
    }
}
