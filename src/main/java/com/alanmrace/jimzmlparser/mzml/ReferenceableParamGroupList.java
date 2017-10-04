package com.alanmrace.jimzmlparser.mzml;

/**
 * Class describing {@literal <referenceableParamGroupList>} tag.
 * 
 * @author Alan Race
 */
public class ReferenceableParamGroupList extends MzMLIDContentList<ReferenceableParamGroup> {

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
    public ReferenceableParamGroupList(int count) {
        super(count);
    }

    /**
     * Copy constructor.
     *
     * @param rpgList Old ReferenceableParamGroupList to copy
     */
    public ReferenceableParamGroupList(ReferenceableParamGroupList rpgList) {
        this(rpgList.size());

        for (ReferenceableParamGroup rpg : rpgList) {
            add(new ReferenceableParamGroup(rpg));
        }
    }
   
//    @Override
//    public void add(ReferenceableParamGroup rpg) {
//        boolean exists = false;
//
//        for (ReferenceableParamGroup currentRPG : list) {
//            if (currentRPG.getID().equals(rpg.getID())) {
//                exists = true;
//                break;
//            }
//        }
//
//        
//        if (!exists) {
//            rpg.setParent(this);
//            list.add(rpg);
//        }
//    }
    
    /**
     * Add ReferenceableParamGroup. Helper method to retain API, calls 
     * {@link ReferenceableParamGroupList#add(com.alanmrace.jimzmlparser.mzml.MzMLTag)}.
     * 
     * @param rpg ReferenceableParamGroup to add to list
     */
    public void addReferenceableParamGroup(ReferenceableParamGroup rpg) {
        add(rpg);
    }

    /**
     * Returns ReferenceableParamGroup at specified index in list. Helper method to retain 
     * API, calls {@link ReferenceableParamGroupList#get(int)}.
     * 
     * @param index Index in the list
     * @return ReferenceableParamGroup at index, or null if none exists
     */
    public ReferenceableParamGroup getReferenceableParamGroup(int index) {
        return get(index);
    }
    
    /**
     * Returns ReferenceableParamGroup in the list with the specified ID. Helper 
     * method to retain API, calls {@link ReferenceableParamGroupList#get(java.lang.String)}.
     * 
     * @param id Unique ID
     * @return ReferenceableParamGroup with ID, or null if none exists
     */
    public ReferenceableParamGroup getReferenceableParamGroup(String id) {
        return get(id);
    }

    @Override
    public String getTagName() {
        return "referenceableParamGroupList";
    }
}
