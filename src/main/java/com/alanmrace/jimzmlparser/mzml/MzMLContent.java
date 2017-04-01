package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.exceptions.InvalidXPathException;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Base class for all mzML tags. This includes default handling for inclusion of 
 * lists of {@link ReferenceableParamGroupRef}, {@link CVParam} and {@link UserParam}.
 * 
 * <p>TODO: Consider refactoring so that inclusion of CVParams is only possible on tags
 * that are allowed to have CVParams. This could be done using an interface.
 *
 * @author Alan Race
 */
public abstract class MzMLContent implements Serializable, MzMLTag {

    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * List of {@link ReferenceableParamGroup} references associated with this mzML tag.
     */
    private List<ReferenceableParamGroupRef> referenceableParamGroupRefs;

    /**
     * List of CVParams associated with this mzML tag.
     */
    private List<CVParam> cvParams;

    /**
     * List of UserParams associated with this mzML tag.
     */
    private List<UserParam> userParams;

    /**
     * Empty constructor.
     * 
     * <p>TODO: Remove this?
     */
    public MzMLContent() {
    }

    /**
     * Copy constructor, with new ReferenceableParamGroupList.
     * 
     * <p>ReferenceableParamGroupRefs are created to match the old MzMLContent instance
     * but an attempt is made to link this to the new ReferenceableParamGroupList based
     * on the ID of each ReferenceableParamGroup.
     * 
     * <p>CVParams are copied (new instances created) using the appropriate copy constructor
     * based on the subclass of CVParam.
     * 
     * <p>UserParams are copied over using UserParam copy constructor.
     * 
     * @param mzMLContent Old MzMLContent to copy
     * @param rpgList New ReferenceableParamGroupList to link new references to
     * @see ReferenceableParamGroupRef
     * @see ReferenceableParamGroup
     * @see CVParam
     * @see UserParam
     */
    public MzMLContent(MzMLContent mzMLContent, ReferenceableParamGroupList rpgList) {
        if (mzMLContent.referenceableParamGroupRefs != null) {
            referenceableParamGroupRefs = new ArrayList<ReferenceableParamGroupRef>(mzMLContent.referenceableParamGroupRefs.size());

            if (rpgList != null) {
                for (ReferenceableParamGroupRef ref : mzMLContent.referenceableParamGroupRefs) {
                    for (ReferenceableParamGroup rpg : rpgList) {
                        if (rpg.getID().equals(ref.getRef().getID())) {
                            referenceableParamGroupRefs.add(new ReferenceableParamGroupRef(rpg));
                            break;
                        }
                    }
                }
            }
        }

        if (mzMLContent.cvParams != null && mzMLContent.cvParams.size() > 0) {
            cvParams = new ArrayList<CVParam>();

            for (CVParam cvParam : mzMLContent.cvParams) {
                if (cvParam instanceof StringCVParam) {
                    cvParams.add(new StringCVParam((StringCVParam) cvParam));
                } else if (cvParam instanceof LongCVParam) {
                    cvParams.add(new LongCVParam((LongCVParam) cvParam));
                } else if (cvParam instanceof DoubleCVParam) {
                    cvParams.add(new DoubleCVParam((DoubleCVParam) cvParam));
                } else if (cvParam instanceof EmptyCVParam) {
                    cvParams.add(new EmptyCVParam((EmptyCVParam) cvParam));
                } else {
                    throw new RuntimeException("Unknown CVParam type, unable to replicate: " + cvParam.getClass());
                }
            }
        }

//		if(mzMLContent.longCVParams != null) {
//			longCVParams = new ArrayList<CVParam<Long>>();
//			
//			for(CVParam<Long> cvParam : mzMLContent.longCVParams)
//				longCVParams.add(new CVParam<Long>(cvParam));
//		}
//		
//		if(mzMLContent.doubleCVParams != null) {
//			doubleCVParams = new ArrayList<CVParam<Double>>();
//			
//			for(CVParam<Double> cvParam : mzMLContent.doubleCVParams)
//				doubleCVParams.add(new CVParam<Double>(cvParam));
//		}
        if (mzMLContent.userParams != null) {
            userParams = new ArrayList<UserParam>();

            for (UserParam userParam : mzMLContent.userParams) {
                userParams.add(new UserParam(userParam));
            }
        }
    }

    //public abstract MzMLContent getElementAtXPath(String xPath) throws InvalidXPathException;
    @Override
    public abstract String getTagName();

//    public Collection<MzMLContent> getElementsAtXPath(String fullXPath, String currentXPath) throws InvalidXPathException {
//        ArrayList<MzMLContent> elements = new ArrayList<MzMLContent>();
//
//        if (currentXPath.equals("/" + getTagName())) {
//            elements.add(this);
//
//            return elements;
//        } else if(currentXPath.startsWith("/" + getTagName() + "/cvParam")) {
////            for(CVParam cvParam : cvParams) {
////                elements.add(cvParam.)
////            }
//        }
//
//        throw new InvalidXPathException("Not implemented sub-XPath (" + currentXPath + ") as part of " + fullXPath);
//    }

    /**
     * Add all child MzMLContent (mzML tags) that match the specified XPath, 
     * which are specific to this tag (i.e. child tags which are not 
     * {@literal <referenceableParamGroupRef>}, {@literal <cvParam>} and 
     * {@literal <userParam>}), to the specified collection.
     * 
     * <p>This method should be overridden in subclasses (mzML tags) which have specific
     * children (i.e. child tags in addition to {@literal <referenceableParamGroupRef>}, 
     * {@literal <cvParam>} and {@literal <userParam>}.
     * 
     * @param elements Collection of MzMLContent to add children to
     * @param fullXPath Full XPath to query
     * @param currentXPath Sub XPath that should start with the tag name for the current MzMLContent
     * @throws InvalidXPathException thrown if the XPath can not be followed
     */
    protected void addTagSpecificElementsAtXPathToCollection(Collection<MzMLContent> elements, String fullXPath, String currentXPath) throws InvalidXPathException {

    }

    /**
     * Add all child MzMLContent (mzML tags) that match the specified XPath to 
     * the specified collection.
     * 
     * @param elements      Collection of MzMLContent to add children to
     * @param xPath         Full XPath to query
     * @throws InvalidXPathException thrown if the XPath can not be followed
     */
    public void addElementsAtXPathToCollection(Collection<MzMLContent> elements, String xPath) throws InvalidXPathException {
        addElementsAtXPathToCollection(elements, xPath, xPath);
    }
    
    /**
     * Add all child MzMLContent (mzML tags) that match the specified XPath to 
     * the specified collection.
     * 
     * @param elements      Collection of MzMLContent to add children to
     * @param fullXPath     Full XPath to query
     * @param currentXPath  Sub XPath that should start with the tag name for the current MzMLContent
     * @throws InvalidXPathException thrown if the XPath can not be followed
     */
    public final void addElementsAtXPathToCollection(Collection<MzMLContent> elements, String fullXPath, String currentXPath) throws InvalidXPathException {
        if (currentXPath.startsWith("/" + getTagName())) {
            currentXPath = currentXPath.replaceFirst("/" + getTagName(), "");

            if (currentXPath.isEmpty()) {
                elements.add(this);

                return;
            }

            addTagSpecificElementsAtXPathToCollection(elements, fullXPath, currentXPath);

            if (elements.isEmpty()) {
                throw new InvalidXPathException("Invalid sub-XPath (" + currentXPath + ") in XPath " + fullXPath, fullXPath);
            }
        } else {
            throw new InvalidXPathException("XPath does not start with /" + getTagName() + " in sub-XPath [" + currentXPath + "] of [" + fullXPath + "]", fullXPath);
        }
    }

    @Override
    public void addChildrenToCollection(Collection<MzMLTag> children) {
        if (referenceableParamGroupRefs != null) {
            children.addAll(referenceableParamGroupRefs);
        }
        if (cvParams != null) {
            children.addAll(cvParams);
        }
        if (userParams != null) {
            children.addAll(userParams);
        }
    }

    /**
     * Get the list of ReferenceableParamGroupRef asssociated with this MzMLContent.
     * 
     * @return List of ReferenceableParamGroup references
     */
    protected List<ReferenceableParamGroupRef> getReferenceableParamGroupRefList() {
        if (referenceableParamGroupRefs == null) {
            referenceableParamGroupRefs = new ArrayList<ReferenceableParamGroupRef>();
        }

        return referenceableParamGroupRefs;
    }

    /**
     * Get the list of CVParams associated with this MzMLContent.
     * 
     * @return List of cvParams
     */
    public List<CVParam> getCVParamList() {
        if (cvParams == null) {
            cvParams = new ArrayList<CVParam>();
        }

        return cvParams;
    }

    /**
     * Get list of UserParams associated with this MzMLContent.
     * 
     * @return List of userParams
     */
    public List<UserParam> getUserParamList() {
        if (userParams == null) {
            userParams = new ArrayList<UserParam>();
        }

        return userParams;
    }

    /**
     * Get the list of MUST include CVParam for this MzMLContent. These are 
     * currently hard coded, based on ms-mapping.xml and so each subclass should
     * overwrite this method if there are any MUST include CVParams.
     * 
     * <p>TODO: Consider deprecating for the validator (CVMappingRule).
     * 
     * @return List of cvParams that MUST be included
     */
    public List<OBOTermInclusion> getListOfRequiredCVParams() {
        return null;
    }

    /**
     * Get the list of MAY include CVParam for this MzMLContent. These are 
     * currently hard coded, based on ms-mapping.xml and so each subclass should
     * overwrite this method if there are any MAY include CVParams.
     * 
     * <p>TODO: Consider deprecating for the validator (CVMappingRule).
     * 
     * @return List of cvParams that MAY be included
     */
    public List<OBOTermInclusion> getListOfOptionalCVParams() {
        return null;
    }

    /**
     * Description of the inclusion of an ontology term within the current MzMLContent.
     * 
     * <p>TODO: Consider deprecating for the validator (CVTerm within CVMappingRule).
     */
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

    /**
     * Add a referenceableParamGroup reference to the MzMLContent.
     * 
     * @param rpg referenceableParamGroup reference to add
     * @see ReferenceableParamGroup
     */
    public void addReferenceableParamGroupRef(ReferenceableParamGroupRef rpg) {
//		rpg.setParent(this);

        boolean exists = false;

        for (ReferenceableParamGroupRef ref : getReferenceableParamGroupRefList()) {
            if (ref.getRef().getID().equals(rpg.getRef().getID())) {
                exists = true;
                break;
            }
        }

        if (!exists) {
            getReferenceableParamGroupRefList().add(rpg);
        }
    }

    /**
     * Get the count of ReferenceableParamGroupRefs in the MzMLContent.
     * 
     * @return Count of referenceableParamGroup references
     * @see ReferenceableParamGroupRef
     */
    public int getReferenceableParamGroupRefCount() {
        if (referenceableParamGroupRefs == null) {
            return 0;
        }

        return getReferenceableParamGroupRefList().size();
    }

    /**
     * Get the referenceableParamGroup reference at the specified index.
     * 
     * @param index Index of the referenceableParamGroup reference
     * @return referenceableParamGroup reference at the specified index
     */
    public ReferenceableParamGroupRef getReferenceableParamGroupRef(int index) {
        if (referenceableParamGroupRefs == null) {
            return null;
        }

        return getReferenceableParamGroupRefList().get(index);
    }

    /**
     * Get the referenceableParamGroup reference where the referenceableParamGroup 
     * has the specified ID.
     * 
     * @param id ID of the referenceableParamGroup
     * @return Reference to the referenceableParamGroup with the specified ID, or null if not found
     */
    public ReferenceableParamGroupRef getReferenceableParamGroupRef(String id) {
        if (referenceableParamGroupRefs == null) {
            return null;
        }

        for (ReferenceableParamGroupRef ref : referenceableParamGroupRefs) {
            if (ref.getRef().getID().equals(id)) {
                return ref;
            }
        }

        return null;
    }

    /**
     * Add cvParam to MzMLContent.
     * 
     * @param cvParam CVParam to add
     */
    public void addCVParam(CVParam cvParam) {
        getCVParamList().add(cvParam);
    }

    /**
     * Remove cvParam at the specified index.
     * 
     * @param index Index within the list of cvParams
     */
    public void removeCVParam(int index) {
        if (cvParams == null) {
            return;
        }

        getCVParamList().remove(index);
    }

    /**
     * Remove cvParam with the specified ID.
     * 
     * @param id Ontology ID
     */
    public void removeCVParam(String id) {
        if (cvParams == null) {
            return;
        }

        ArrayList<CVParam> cvParamList = new ArrayList<CVParam>();

        for (CVParam cvParam : cvParams) {
            if (cvParam.getTerm().getID().equals(id)) {
                cvParamList.add(cvParam);
            }
        }

        for (CVParam cvParam : cvParamList) {
            cvParams.remove(cvParam);
        }
    }

    /**
     * Remove all cvParams which are defined as children of the specified ontology term 
     * with the ID id. 
     * 
     * @param id Ontology ID
     */
    public void removeChildOfCVParam(String id) {
        if (cvParams == null) {
            return;
        }

        List<CVParam> children = getChildrenOf(id);

        for (CVParam cvParam : children) {
            cvParams.remove(cvParam);
        }
    }

    /**
     * Add userParam to MzMLContent.
     * 
     * @param userParam UserParam to add
     */
    public void addUserParam(UserParam userParam) {
        getUserParamList().add(userParam);
    }

    /**
     * Remove userParam at the specified index.
     * 
     * @param index Index within the list of userParams
     */
    public void removeUserParam(int index) {
        if (userParams == null) {
            return;
        }

        userParams.remove(index);
    }

    /**
     * Get the cvParam which has the specified id. Checks list of CVParams as well
     * as all ReferenceableParamGroups associated with this MzMLContent.
     * 
     * @param id Ontology ID
     * @return CVParam with id if found, null otherwise
     */
    public CVParam getCVParam(String id) {
        if (referenceableParamGroupRefs != null) {
            for (ReferenceableParamGroupRef ref : referenceableParamGroupRefs) {
                if (ref == null) {
                    continue;
                }

                CVParam cvParam = ref.getRef().getCVParam(id);

                if (cvParam != null) {
                    return cvParam;
                }
            }
        }

        if (cvParams != null) {
            for (CVParam cvParam : cvParams) {
                if (cvParam.getTerm().getID().equals(id)) {
                    return cvParam;
                }
            }
        }

        return null;
    }

    /**
     * Get cvParam at specified index.
     * 
     * @param index Index within the list of cvParams
     * @return CVParam if exists at index, null if not
     */
    public CVParam getCVParam(int index) {
        if (cvParams == null) {
            return null;
        }

        return cvParams.get(index);
    }

    /**
     * Get count of cvParams (only includes cvParams within the CVParam list).
     * 
     * @return Count of cvParams within the list
     */
    public int getCVParamCount() {
        if (cvParams == null) {
            return 0;
        }

        return cvParams.size();
    }

    /**
     * Get the first cvParam, or cvParam with child ontology 
     * term, with the specified id. Checks list of CVParams as well
     * as all ReferenceableParamGroups associated with this MzMLContent.
     * 
     * @param id Ontology ID 
     * @return CVParam with id (or child of) if found, null otherwise
     */
    public CVParam getCVParamOrChild(String id) {
        if (referenceableParamGroupRefs != null) {
            for (ReferenceableParamGroupRef ref : referenceableParamGroupRefs) {
                if (ref == null) {
                    continue;
                }

                CVParam cvParam = ref.getRef().getCVParam(id);

                if (cvParam != null) {
                    return cvParam;
                }

                List<CVParam> children = ref.getRef().getChildrenOf(id);

                if (children.size() > 0) {
                    return children.get(0);
                }
            }
        }

        if (cvParams != null) {
            for (CVParam cvParam : cvParams) {
                if (cvParam.getTerm().getID().equals(id)) {
                    return cvParam;
                }
            }
        }

        List<CVParam> children = getChildrenOf(id);

        if (children.size() > 0) {
            return children.get(0);
        }

        return null;
    }

    /**
     * Get userParam with the specified name.
     * 
     * @param name Name of the userParam
     * @return UserParam if it exists, or null if not
     */
    public UserParam getUserParam(String name) {
        if (referenceableParamGroupRefs != null) {
            for (ReferenceableParamGroupRef ref : referenceableParamGroupRefs) {
                if (ref == null) {
                    continue;
                }

                UserParam userParam = ref.getRef().getUserParam(name);

                if (userParam != null) {
                    return userParam;
                }
            }
        }

        if (userParams != null) {
            for (UserParam userParam : userParams) {
                if (userParam.getName().equals(name)) {
                    return userParam;
                }
            }
        }

        return null;
    }

    /**
     * Get userParam at the specified index.
     * 
     * @param index Index within the list of userParams
     * @return UserParam if it exists, or null if not
     */
    public UserParam getUserParam(int index) {
        if (userParams == null) {
            return null;
        }

        return userParams.get(index);
    }

    /**
     * Get all cvParam with have ontology terms which are children of the specified
     * ontology id. Checks list of CVParams as well as all ReferenceableParamGroups 
     * associated with this MzMLContent.
     * 
     * @param id Ontology ID
     * @return List of CVParams which have IDs listed as children of the input ontology ID
     */
    public List<CVParam> getChildrenOf(String id) {
        List<CVParam> children = new LinkedList<CVParam>();

        if (referenceableParamGroupRefs != null) {
            for (ReferenceableParamGroupRef ref : referenceableParamGroupRefs) {
                if (ref == null) {
                    continue;
                }

                children.addAll(ref.getRef().getChildrenOf(id));
            }
        }

        if (cvParams != null) {
            for (CVParam cvParam : cvParams) {
                if (cvParam.getTerm().isChildOf(id)) {
                    children.add(cvParam);
                }
            }
        }

        // TODO: userParams
        return children;
    }

    @Override
    public void outputXML(BufferedWriter output, int indent) throws IOException {
        if (referenceableParamGroupRefs != null) {
            for (ReferenceableParamGroupRef ref : referenceableParamGroupRefs) {
                // TODO: Remove quick fix
                if (ref == null || ref.getRef() == null || ref.getRef().getID() == null) {
                    continue;
                }

                ref.outputXML(output, indent);
            }
        }

        if (cvParams != null) {
            for (CVParam cvParam : cvParams) {
                if (cvParam != null) {
                    cvParam.outputXML(output, indent);
                }
            }
        }

        if (userParams != null) {
            for (UserParam userParam : userParams) {
                userParam.outputXML(output, indent);
            }
        }
    }

    /**
     * Indent the output by the specified number of spaces (indent).
     * 
     * @param output BufferedReader to output the indents to
     * @param indent Number of tabs to indent
     * @throws IOException Exception occurred during writing data
     */
    public static void indent(BufferedWriter output, int indent) throws IOException {
        for (int i = 0; i < indent; i++) {
            output.write("  ");
        }
    }

    /**
     * Set the parent MzMLContent of this MzMLContent. This method currently 
     * does nothing.
     * 
     * <p>TODO: Remove this.
     * 
     * @param parent Parent MzMLContent to add
     * @deprecated This was removed when the Tree code was decoupled
     */
    @Deprecated
    public void setParent(MzMLContent parent) {
        // This is a dummy function only included to allow the removal
    }
}
