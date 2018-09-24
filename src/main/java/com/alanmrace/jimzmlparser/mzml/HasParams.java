package com.alanmrace.jimzmlparser.mzml;

import java.util.List;

/**
 * Interface for MzMLTags which allow parameters (therefore must allow 
 * references to a {@link ReferenceableParamGroup}, and inclusion of {@link CVParam}
 * and {@link UserParam} child elements).
 * 
 * @author Alan Race
 */
public interface HasParams extends HasChildren {
    
    /**
     * Add cvParam to MzMLContent.
     * 
     * @param cvParam CVParam to add
     */
    void addCVParam(CVParam cvParam);
    
    /**
     * Get the cvParam which has the specified id. Checks list of CVParams as well
     * as all ReferenceableParamGroups associated with this MzMLContent.
     * 
     * @param id Ontology ID
     * @return CVParam with id if found, null otherwise
     */
    CVParam getCVParam(String id);
    
    /**
     * Get cvParam at specified index.
     * 
     * @param index Index within the list of cvParams
     * @return CVParam if exists at index, null if not
     */
    CVParam getCVParam(int index);
    
    /**
     * Get the first cvParam, or cvParam with child ontology 
     * term, with the specified id. Checks list of CVParams as well
     * as all ReferenceableParamGroups associated with this MzMLContent.
     * 
     * @param id Ontology ID 
     * @return CVParam with id (or child of) if found, null otherwise
     */
    CVParam getCVParamOrChild(String id);
    
    /**
     * Get all cvParam with have ontology terms which are children of the specified
     * ontology id. Checks list of CVParams as well as all ReferenceableParamGroups 
     * associated with this MzMLContent.
     * 
     * @param id Ontology ID
     * @param includeCurrent Whether to include the ID specified (if it exists) in the list of children
     * @return List of CVParams which have IDs listed as children of the input ontology ID
     */
    List<CVParam> getChildrenOf(String id, boolean includeCurrent);
    
    /**
     * Get the list of CVParams associated with this MzMLContent.
     * 
     * @return List of cvParams
     */
    List<CVParam> getCVParamList();
    
    /**
     * Get count of cvParams (only includes cvParams within the CVParam list).
     * 
     * @return Count of cvParams within the list
     */
    int getCVParamCount();
    
    /**
     * Remove cvParam at the specified index.
     * 
     * @param index Index within the list of cvParams
     */
    void removeCVParam(int index);
    
    /**
     * Remove cvParam with the specified ID.
     * 
     * @param id Ontology ID
     */
    void removeCVParam(String id);
    
    /**
     * Remove specified cvParam.
     * 
     * @param param CVParam to remove
     */
    void removeCVParam(CVParam param);
    
    /**
     * Remove all cvParams which are defined as children of the specified ontology term 
     * with the ID id. 
     * 
     * @param id Ontology ID 
     * @param includeCurrent Whether to remove the ID specified (if it exists) rather than solely children
     */
    void removeChildrenOfCVParam(String id, boolean includeCurrent);
        
    /**
     * Add userParam to MzMLContent.
     * 
     * @param userParam UserParam to add
     */
    void addUserParam(UserParam userParam);
    
    /**
     * Get userParam with the specified name.
     * 
     * @param name Name of the userParam
     * @return UserParam if it exists, or null if not
     */
    UserParam getUserParam(String name);
    
    /**
     * Get userParam at the specified index.
     * 
     * @param index Index within the list of userParams
     * @return UserParam if it exists, or null if not
     */
    UserParam getUserParam(int index);
    
    /**
     * Remove userParam at the specified index.
     * 
     * @param index Index within the list of userParams
     */
    void removeUserParam(int index);
    
    /**
     * Get list of UserParams associated with this MzMLContent.
     * 
     * @return List of userParams
     */
    List<UserParam> getUserParamList();
    
    /**
     * Add a referenceableParamGroup reference to the MzMLContent.
     * 
     * @param rpg referenceableParamGroup reference to add
     * @see ReferenceableParamGroup
     */
    void addReferenceableParamGroupRef(ReferenceableParamGroupRef rpg);
    
    /**
     * Get the count of ReferenceableParamGroupRefs in the MzMLContent.
     * 
     * @return Count of referenceableParamGroup references
     * @see ReferenceableParamGroupRef
     */
    int getReferenceableParamGroupRefCount();
    
    /**
     * Get the referenceableParamGroup reference at the specified index.
     * 
     * @param index Index of the referenceableParamGroup reference
     * @return referenceableParamGroup reference at the specified index
     */
    ReferenceableParamGroupRef getReferenceableParamGroupRef(int index);
    
    /**
     * Get the referenceableParamGroup reference where the referenceableParamGroup 
     * has the specified ID.
     * 
     * @param id ID of the referenceableParamGroup
     * @return Reference to the referenceableParamGroup with the specified ID, or null if not found
     */
    ReferenceableParamGroupRef getReferenceableParamGroupRef(String id);
}
