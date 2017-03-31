package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.exceptions.InvalidXPathException;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public abstract class MzMLContent implements Serializable, MzMLTag {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private List<ReferenceableParamGroupRef> referenceableParamGroupRefs;

    // Store different parameters differently to save on memory
    private List<CVParam> cvParams;
//	private ArrayList<CVParam<Long>> longCVParams;
//	private ArrayList<CVParam<Double>> doubleCVParams;

    private List<UserParam> userParams;

    public MzMLContent() {
    }

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
    protected void addTagSpecificElementsAtXPathToCollection(Collection<MzMLContent> elements, String fullXPath, String currentXPath) throws InvalidXPathException {

    }

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

//	@JsonIgnore
    protected List<ReferenceableParamGroupRef> getReferenceableParamGroupRefList() {
        if (referenceableParamGroupRefs == null) {
            referenceableParamGroupRefs = new ArrayList<ReferenceableParamGroupRef>();
        }

        return referenceableParamGroupRefs;
    }

//	@JsonIgnore
    public List<CVParam> getCVParamList() {
        if (cvParams == null) {
            cvParams = new ArrayList<CVParam>();
        }

        return cvParams;
    }

//	@JsonIgnore
//	protected  ArrayList<CVParam<Long>> getLongCVParamList() {
//		if(longCVParams == null)
//			longCVParams = new ArrayList<CVParam<Long>>();
//		
//		return longCVParams;
//	}
//	
//	@JsonIgnore
//	protected  ArrayList<CVParam<Double>> getDoubleCVParamList() {
//		if(doubleCVParams == null)
//			doubleCVParams = new ArrayList<CVParam<Double>>();
//		
//		return doubleCVParams;
//	}
//	@JsonIgnore
    public List<UserParam> getUserParamList() {
        if (userParams == null) {
            userParams = new ArrayList<UserParam>();
        }

        return userParams;
    }

//	@JsonIgnore
    public List<OBOTermInclusion> getListOfRequiredCVParams() {
        return null;
    }

//	@JsonIgnore
    public List<OBOTermInclusion> getListOfOptionalCVParams() {
        return null;
    }

    public static class OBOTermInclusion {

        protected String id;
        protected boolean childrenAllowed;
        protected boolean onlyOnce;
        protected boolean parentIncluded;

        public OBOTermInclusion(String id, boolean onlyOnce, boolean childrenAllowed, boolean parentIncluded) {
            this.id = id;
            this.onlyOnce = onlyOnce;
            this.childrenAllowed = childrenAllowed;
            this.parentIncluded = parentIncluded;
        }

        public String getID() {
            return id;
        }

        public boolean onlyOnce() {
            return onlyOnce;
        }

        public boolean childrenAllowed() {
            return childrenAllowed;
        }

        public boolean parentIncluded() {
            return parentIncluded;
        }
    }

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

    public int getReferenceableParamGroupRefCount() {
        if (referenceableParamGroupRefs == null) {
            return 0;
        }

        return getReferenceableParamGroupRefList().size();
    }

    public ReferenceableParamGroupRef getReferenceableParamGroupRef(int index) {
        if (referenceableParamGroupRefs == null) {
            return null;
        }

        return getReferenceableParamGroupRefList().get(index);
    }

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

    public void addCVParam(CVParam cvParam) {
        getCVParamList().add(cvParam);
    }

    public void removeCVParam(int index) {
        if (cvParams == null) {
            return;
        }

        getCVParamList().remove(index);
    }

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

    public void removeChildOfCVParam(String id) {
        if (cvParams == null) {
            return;
        }

        List<CVParam> children = getChildrenOf(id);

        for (CVParam cvParam : children) {
            cvParams.remove(cvParam);
        }
    }

    public void addUserParam(UserParam userParam) {
        getUserParamList().add(userParam);
    }

    public void removeUserParam(int index) {
        if (userParams == null) {
            return;
        }

        userParams.remove(index);
    }

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

    public CVParam getCVParam(int index) {
        if (cvParams == null) {
            return null;
        }

        return cvParams.get(index);
    }

    public int getCVParamCount() {
        if (cvParams == null) {
            return 0;
        }

        return cvParams.size();
    }

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

    public UserParam getUserParam(int index) {
        if (userParams == null) {
            return null;
        }

        return userParams.get(index);
    }

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

    public void outputXML(BufferedWriter output, int indent) throws IOException {
        if (referenceableParamGroupRefs != null) {
            for (ReferenceableParamGroupRef ref : referenceableParamGroupRefs) {
                // TODO: Remove quick fix
                if (ref == null || ref.getRef() == null || ref.getRef().getID() == null) {
                    continue;
                }

                MzMLContent.indent(output, indent);
                ref.outputXML(output);
            }
        }

        if (cvParams != null) {
            for (CVParam cvParam : cvParams) {
                if (cvParam != null) {
                    MzMLContent.indent(output, indent);
                    cvParam.outputXML(output);
                }
            }
        }

//		if(longCVParams != null) {
//			for(CVParam<Long> cvParam : longCVParams) {
//				MzMLContent.indent(output, indent);
//				cvParam.outputXML(output);
//			}
//		}
//		
//		if(doubleCVParams != null) {
//			for(CVParam<Double> cvParam : doubleCVParams) {
//				MzMLContent.indent(output, indent);
//				cvParam.outputXML(output);
//			}
//		}
        if (userParams != null) {
            for (UserParam userParam : userParams) {
                MzMLContent.indent(output, indent);
                userParam.outputXML(output);
            }
        }
    }

    public static void indent(BufferedWriter output, int indent) throws IOException {
        for (int i = 0; i < indent; i++) {
            output.write("  ");
        }
    }

    public void setParent(MzMLContent parent) {
        // This is a dummy function only included to allow the removal

        // TODO:
    }
}
