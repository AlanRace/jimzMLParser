package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.event.CVParamAddedEvent;
import com.alanmrace.jimzmlparser.event.CVParamRemovedEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Abstract class implementing the basic functionality for a tag in MzML which has
 * child tags and ReferenceableParamGroupRef, CVParam and UserParam tags.
 *
 * @author Alan Race
 * 
 * @see ReferenceableParamGroupRef
 * @see CVParam
 * @see UserParam
 */
public abstract class MzMLContentWithParams extends MzMLContentWithChildren implements HasParams {
    /**
     * List of {@link ReferenceableParamGroup} references associated with this mzML tag.
     */
    private List<ReferenceableParamGroupRef> referenceableParamGroupRefs = null;

    /**
     * List of CVParams associated with this mzML tag.
     */
    private List<CVParam> cvParams = null;

    /**
     * List of UserParams associated with this mzML tag.
     */
    private List<UserParam> userParams = null;
    
    /**
     * Default constructor.
     */
    public MzMLContentWithParams() {
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
    public MzMLContentWithParams(MzMLContentWithParams mzMLContent, ReferenceableParamGroupList rpgList) {
        if (mzMLContent.referenceableParamGroupRefs != null) {
            referenceableParamGroupRefs = new ArrayList<ReferenceableParamGroupRef>(mzMLContent.referenceableParamGroupRefs.size());

            if (rpgList != null) {
                for (ReferenceableParamGroupRef ref : mzMLContent.referenceableParamGroupRefs) {
                    for (ReferenceableParamGroup rpg : rpgList) {
                        if (rpg.getID().equals(ref.getReference().getID())) {
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
                } else if (cvParam instanceof IntegerCVParam) {
                    cvParams.add(new IntegerCVParam((IntegerCVParam) cvParam));
                } else if (cvParam instanceof EmptyCVParam) {
                    cvParams.add(new EmptyCVParam((EmptyCVParam) cvParam));
                } else {
                    throw new IllegalArgumentException("Unknown CVParam type, unable to replicate: " + cvParam.getClass());
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
//        if (referenceableParamGroupRefs == null) {
//            referenceableParamGroupRefs = new ArrayList<ReferenceableParamGroupRef>();
//        }

        return referenceableParamGroupRefs;
    }

    public ReferenceableParamGroup findBestFittingRPG(ReferenceableParamGroupList rpgList) {
        if(rpgList != null) {
            ReferenceableParamGroup bestFittingGroup = null;
            
            for(ReferenceableParamGroup rpg : rpgList) {
                int numParamsFound = 0;
                
                for(CVParam cvParam : rpg.getCVParamList()) {
                    CVParam curParam = this.getCVParam(cvParam.getTerm().getID());
                    
                    if(curParam != null && containsCVParam(curParam)) {
                        String value1 = curParam.getValueAsString();
                        String value2 = cvParam.getValueAsString();
                        
                        if((value1 == null && value2 == null) || (value1 != null && value2 != null && value1.equals(value2)))
                            numParamsFound++;
                    }
                }
                
//                System.out.println("Found " + numParamsFound + " for " + rpg);
                
                if(numParamsFound == rpg.getCVParamCount()) {
                    if(bestFittingGroup == null) {
                        bestFittingGroup = rpg;
                    } else if(numParamsFound > bestFittingGroup.getCVParamCount()) {
                        bestFittingGroup = rpg;
                    }
                }
            }
            
            return bestFittingGroup;
        }
        
        return null;
    }
    
    public void replaceCVParamsWithRPG(ReferenceableParamGroup rpg) {
        for(CVParam replacementParam : rpg.getCVParamList()) {
            CVParam paramToRemove = this.getCVParam(replacementParam.getTerm().getID());
            
            removeCVParam(paramToRemove);
        }
        
        this.addReferenceableParamGroupRef(new ReferenceableParamGroupRef(rpg));
    }
    
    @Override
    public List<CVParam> getCVParamList() {
//        if (cvParams == null) {
//            cvParams = new ArrayList<CVParam>();
//        }

        return cvParams;
    }

    @Override
    public List<UserParam> getUserParamList() {
//        if (userParams == null) {
//            userParams = new ArrayList<UserParam>();
//        }

        return userParams;
    }

//    @Override
//    public List<OBOTermInclusion> getListOfRequiredCVParams() {
//        return null;
//    }

//    @Override
//    public List<OBOTermInclusion> getListOfOptionalCVParams() {
//        return null;
//    }

    @Override
    public void addReferenceableParamGroupRef(ReferenceableParamGroupRef rpg) {
//		rpg.setParent(this);

        boolean exists = false;

        if(referenceableParamGroupRefs != null) {
            for (ReferenceableParamGroupRef ref : getReferenceableParamGroupRefList()) {
                if (ref.getReference().getID().equals(rpg.getReference().getID())) {
                    exists = true;
                    break;
                }
            }
        }
        
        if (!exists) {
            if(referenceableParamGroupRefs instanceof ArrayList) {
                referenceableParamGroupRefs.add(rpg);
            } else if(referenceableParamGroupRefs != null) {
                referenceableParamGroupRefs = new ArrayList<ReferenceableParamGroupRef>(referenceableParamGroupRefs);
                referenceableParamGroupRefs.add(rpg);
            } else {
                referenceableParamGroupRefs = Collections.singletonList(rpg);
            }
        }
    }

    @Override
    public int getReferenceableParamGroupRefCount() {
        if (referenceableParamGroupRefs == null) {
            return 0;
        }

        return getReferenceableParamGroupRefList().size();
    }

    @Override
    public ReferenceableParamGroupRef getReferenceableParamGroupRef(int index) {
        if (referenceableParamGroupRefs == null) {
            return null;
        }

        return getReferenceableParamGroupRefList().get(index);
    }

    @Override
    public ReferenceableParamGroupRef getReferenceableParamGroupRef(String id) {
        if (referenceableParamGroupRefs == null) {
            return null;
        }

        for (ReferenceableParamGroupRef ref : referenceableParamGroupRefs) {
            if (ref.getReference().getID().equals(id)) {
                return ref;
            }
        }

        return null;
    }

    @Override
    public void addCVParam(CVParam cvParam) {
        if(cvParam != null) {
            if (cvParams instanceof ArrayList) {
                cvParams.add(cvParam);
            } else if (cvParams != null) {
                cvParams = new ArrayList<CVParam>(cvParams);
                cvParams.add(cvParam);
            } else {
                cvParams = Collections.singletonList(cvParam);
            }

            cvParam.setParent(this);

            if(hasListeners())
                notifyListeners(new CVParamAddedEvent(this, cvParam));
        }
    }

    public boolean containsCVParam(CVParam param) {
        if(cvParams == null)
            return false;
        
        return cvParams.contains(param);
    }
    
    @Override
    public void removeCVParam(int index) {
        if (cvParams == null) {
            return;
        }
        
        CVParam paramRemoved = getCVParamList().remove(index);
        
        if(hasListeners())
            notifyListeners(new CVParamRemovedEvent(this, paramRemoved));
        
        paramRemoved.setParent(null);
    }
    
    @Override
    public void removeCVParam(CVParam param) {
        if(cvParams != null) {
            if(cvParams.remove(param)) {
                if(hasListeners())
                    notifyListeners(new CVParamRemovedEvent(this, param));
                
                param.setParent(null);
            }
        }
    }

    @Override
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
            
            if(hasListeners())
                notifyListeners(new CVParamRemovedEvent(this, cvParam));
            
            cvParam.setParent(null);
        }
    }

    @Override
    public void removeChildOfCVParam(String id) {
        if (cvParams == null) {
            return;
        }

        List<CVParam> children = getChildrenOf(id);

        for (CVParam cvParam : children) {
            cvParams.remove(cvParam);
            
            if(hasListeners())
                notifyListeners(new CVParamRemovedEvent(this, cvParam));
            
            cvParam.setParent(null);
        }
    }

    @Override
    public void addUserParam(UserParam userParam) {        
        if(userParam != null) {
            if (userParams instanceof ArrayList) {
                userParams.add(userParam);
            } else if (userParams != null) {
                userParams = new ArrayList<UserParam>(userParams);
                userParams.add(userParam);
            } else {
                userParams = Collections.singletonList(userParam);
            }

            userParam.setParent(this);
        }
    }

    @Override
    public void removeUserParam(int index) {
        if (userParams == null) {
            return;
        }

        UserParam removedParam = userParams.remove(index);
        removedParam.setParent(null);
    }

    @Override
    public CVParam getCVParam(String id) {
        if (referenceableParamGroupRefs != null) {
            for (ReferenceableParamGroupRef ref : referenceableParamGroupRefs) {
                if (ref == null) {
                    continue;
                }

                CVParam cvParam = ref.getReference().getCVParam(id);

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

    @Override
    public CVParam getCVParam(int index) {
        if (cvParams == null) {
            return null;
        }

        return cvParams.get(index);
    }

    @Override
    public int getCVParamCount() {
        if (cvParams == null) {
            return 0;
        }

        return cvParams.size();
    }

    @Override
    public CVParam getCVParamOrChild(String id) {
        if (referenceableParamGroupRefs != null) {
            for (ReferenceableParamGroupRef ref : referenceableParamGroupRefs) {
                if (ref == null) {
                    continue;
                }

                CVParam cvParam = ref.getReference().getCVParam(id);

                if (cvParam != null) {
                    return cvParam;
                }

                List<CVParam> children = ref.getReference().getChildrenOf(id);

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

    @Override
    public UserParam getUserParam(String name) {
        if (referenceableParamGroupRefs != null) {
            for (ReferenceableParamGroupRef ref : referenceableParamGroupRefs) {
                if (ref == null) {
                    continue;
                }

                UserParam userParam = ref.getReference().getUserParam(name);

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

    @Override
    public UserParam getUserParam(int index) {
        if (userParams == null) {
            return null;
        }

        return userParams.get(index);
    }

    @Override
    public List<CVParam> getChildrenOf(String id) {
        List<CVParam> children = new LinkedList<CVParam>();

        if (referenceableParamGroupRefs != null) {
            for (ReferenceableParamGroupRef ref : referenceableParamGroupRefs) {
                if (ref == null) {
                    continue;
                }

                children.addAll(ref.getReference().getChildrenOf(id));
            }
        }

        if (cvParams != null) {
            for (CVParam cvParam : cvParams) {
                if (cvParam.getTerm().isChildOf(id)) {
                    children.add(cvParam);
                }
            }
        }

        return children;
    }
}
