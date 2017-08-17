package com.alanmrace.jimzmlparser.mzml;

import java.util.List;

/**
 * Interface for MzMLTags which allow parameters (therefore must allow 
 * references to a {@link ReferenceableParamGroup}, and inclusion of {@link CVParam}
 * and {@link UserParam} child elements).
 * 
 * @author Alan Race
 */
public interface HasParams {
    
    /**
     * Add cvParam to MzMLContent.
     * 
     * @param cvParam CVParam to add
     */
    public void addCVParam(CVParam cvParam);
    
    /**
     * Get the cvParam which has the specified id. Checks list of CVParams as well
     * as all ReferenceableParamGroups associated with this MzMLContent.
     * 
     * @param id Ontology ID
     * @return CVParam with id if found, null otherwise
     */
    public CVParam getCVParam(String id);
    
    /**
     * Get cvParam at specified index.
     * 
     * @param index Index within the list of cvParams
     * @return CVParam if exists at index, null if not
     */
    public CVParam getCVParam(int index);
    
    /**
     * Get the first cvParam, or cvParam with child ontology 
     * term, with the specified id. Checks list of CVParams as well
     * as all ReferenceableParamGroups associated with this MzMLContent.
     * 
     * @param id Ontology ID 
     * @return CVParam with id (or child of) if found, null otherwise
     */
    public CVParam getCVParamOrChild(String id);
    
    /**
     * Get all cvParam with have ontology terms which are children of the specified
     * ontology id. Checks list of CVParams as well as all ReferenceableParamGroups 
     * associated with this MzMLContent.
     * 
     * @param id Ontology ID
     * @return List of CVParams which have IDs listed as children of the input ontology ID
     */
    public List<CVParam> getChildrenOf(String id);
    
    /**
     * Get the list of CVParams associated with this MzMLContent.
     * 
     * @return List of cvParams
     */
    public List<CVParam> getCVParamList();
    
    /**
     * Get count of cvParams (only includes cvParams within the CVParam list).
     * 
     * @return Count of cvParams within the list
     */
    public int getCVParamCount();
    
    /**
     * Remove cvParam at the specified index.
     * 
     * @param index Index within the list of cvParams
     */
    public void removeCVParam(int index);
    
    /**
     * Remove cvParam with the specified ID.
     * 
     * @param id Ontology ID
     */
    public void removeCVParam(String id);
    
    /**
     * Remove specified cvParam.
     * 
     * @param param CVParam to remove
     */
    public void removeCVParam(CVParam param);
    
    /**
     * Remove all cvParams which are defined as children of the specified ontology term 
     * with the ID id. 
     * 
     * @param id Ontology ID
     */
    public void removeChildOfCVParam(String id);
        
    /**
     * Add userParam to MzMLContent.
     * 
     * @param userParam UserParam to add
     */
    public void addUserParam(UserParam userParam);
    
    /**
     * Get userParam with the specified name.
     * 
     * @param name Name of the userParam
     * @return UserParam if it exists, or null if not
     */
    public UserParam getUserParam(String name);
    
    /**
     * Get userParam at the specified index.
     * 
     * @param index Index within the list of userParams
     * @return UserParam if it exists, or null if not
     */
    public UserParam getUserParam(int index);
    
    /**
     * Remove userParam at the specified index.
     * 
     * @param index Index within the list of userParams
     */
    public void removeUserParam(int index);
    
    /**
     * Get list of UserParams associated with this MzMLContent.
     * 
     * @return List of userParams
     */
    public List<UserParam> getUserParamList();
    
    /**
     * Add a referenceableParamGroup reference to the MzMLContent.
     * 
     * @param rpg referenceableParamGroup reference to add
     * @see ReferenceableParamGroup
     */
    public void addReferenceableParamGroupRef(ReferenceableParamGroupRef rpg);
    
    /**
     * Get the count of ReferenceableParamGroupRefs in the MzMLContent.
     * 
     * @return Count of referenceableParamGroup references
     * @see ReferenceableParamGroupRef
     */
    public int getReferenceableParamGroupRefCount();
    
    /**
     * Get the referenceableParamGroup reference at the specified index.
     * 
     * @param index Index of the referenceableParamGroup reference
     * @return referenceableParamGroup reference at the specified index
     */
    public ReferenceableParamGroupRef getReferenceableParamGroupRef(int index);
    
    /**
     * Get the referenceableParamGroup reference where the referenceableParamGroup 
     * has the specified ID.
     * 
     * @param id ID of the referenceableParamGroup
     * @return Reference to the referenceableParamGroup with the specified ID, or null if not found
     */
    public ReferenceableParamGroupRef getReferenceableParamGroupRef(String id);
}
