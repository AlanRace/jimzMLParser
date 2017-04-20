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
    
    /**
     * Get the list of MUST include CVParam for this MzMLContent. These are 
     * currently hard coded, based on ms-mapping.xml and so each subclass should
     * overwrite this method if there are any MUST include CVParams.
     *  
     * @return List of cvParams that MUST be included
     * @deprecated Use jimzMLValidator instead.
     */
    @Deprecated
    public List<MzMLContentWithParams.OBOTermInclusion> getListOfRequiredCVParams();
    
    /**
     * Get the list of MAY include CVParam for this MzMLContent. These are 
     * currently hard coded, based on ms-mapping.xml and so each subclass should
     * overwrite this method if there are any MAY include CVParams.
     *  
     * @return List of cvParams that MAY be included
     * @deprecated Use jimzMLValidator instead.
     */
    @Deprecated
    public List<MzMLContentWithParams.OBOTermInclusion> getListOfOptionalCVParams();
    
    /**
     * Description of the inclusion of an ontology term within the current MzMLContent.
     * 
     * @deprecated Use jimzMLValidator instead.
     */
    @Deprecated
    public static class OBOTermInclusion {

        /**
         * Ontology term ID.
         */
        protected String id;

        /**
         * Boolean describing whether children of the ontology term are permitted.
         */
        protected boolean childrenAllowed;
        
        /**
         * Boolean describing whether only one instance of this term (or children) is permitted.
         */
        protected boolean onlyOnce;
        
        /**
         * Boolean describing whether it is permitted to use the term defined by id (true) or only children (false).
         */
        protected boolean parentIncluded;

        /**
         * Constructor for describing the inclusion of an ontology term.
         * 
         * @param id                ID of the ontology term
         * @param onlyOnce          true if only one instance of the ontology term defined by id (or it's children) is permitted, false if it is repeatable
         * @param childrenAllowed   true if children of the ontology term defined by id are permitted, false otherwise
         * @param parentIncluded    true if the ontology term defined by id is permitted, false otherwise
         */
        public OBOTermInclusion(String id, boolean onlyOnce, boolean childrenAllowed, boolean parentIncluded) {
            this.id = id;
            this.onlyOnce = onlyOnce;
            this.childrenAllowed = childrenAllowed;
            this.parentIncluded = parentIncluded;
        }

        /**
         * Get the unique ID for the ontology term this inclusion rule describes.
         * 
         * @return ID
         */
        public String getID() {
            return id;
        }

        /**
         * Whether only one instance of the ontology term defined by id (or it's children) 
         * is permitted.
         * 
         * @return  true if only one instance of the ontology term defined by id 
         *          (or it's children) is permitted, false if it is repeatable
         */
        public boolean onlyOnce() {
            return onlyOnce;
        }

        /**
         * Whether children of the ontology term defined by id are permitted.
         * 
         * @return true if children of the ontology term defined by id are permitted, false otherwise
         */
        public boolean childrenAllowed() {
            return childrenAllowed;
        }

        /**
         * Whether the ontology term defined by id is permitted.
         * 
         * @return true if the ontology term defined by id is permitted, false otherwise
         */
        public boolean parentIncluded() {
            return parentIncluded;
        }
    }
}
