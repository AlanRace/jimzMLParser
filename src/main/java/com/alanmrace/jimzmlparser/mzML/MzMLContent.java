package com.alanmrace.jimzmlparser.mzML;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

//import com.fasterxml.jackson.annotation.JsonIgnore;
public abstract class MzMLContent implements Serializable { //, MutableTreeNode {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

//	private MutableTreeNode parentTreeNode;
    private ArrayList<ReferenceableParamGroupRef> referenceableParamGroupRefs;

    // Store different parameters differently to save on memory
    private ArrayList<CVParam> cvParams;
//	private ArrayList<CVParam<Long>> longCVParams;
//	private ArrayList<CVParam<Double>> doubleCVParams;

    private ArrayList<UserParam> userParams;

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

//	@JsonIgnore
    protected ArrayList<ReferenceableParamGroupRef> getReferenceableParamGroupRefList() {
        if (referenceableParamGroupRefs == null) {
            referenceableParamGroupRefs = new ArrayList<ReferenceableParamGroupRef>();
        }

        return referenceableParamGroupRefs;
    }

//	@JsonIgnore
    protected ArrayList<CVParam> getCVParamList() {
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
    protected ArrayList<UserParam> getUserParamList() {
        if (userParams == null) {
            userParams = new ArrayList<UserParam>();
        }

        return userParams;
    }

//	@JsonIgnore
    public ArrayList<OBOTermInclusion> getListOfRequiredCVParams() {
        return null;
    }

//	@JsonIgnore
    public ArrayList<OBOTermInclusion> getListOfOptionalCVParams() {
        return null;
    }

    public static class OBOTermInclusion {

        String id;
        boolean childrenAllowed;
        boolean onlyOnce;
        boolean parentIncluded;

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
        
        for(ReferenceableParamGroupRef ref : getReferenceableParamGroupRefList()) {
            if(ref.getRef().getID().equals(rpg.getRef().getID())) {
                exists = true;
                break;
            }
        }
        
        if(!exists)
            getReferenceableParamGroupRefList().add(rpg);
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
//		cvParam.setParent(this);

        getCVParamList().add(cvParam);
    }

//	public void addLongCVParam(CVParam<Long> cvParam) {
//		getLongCVParamList().add(cvParam);
//	}
//	
//	public void addDoubleCVParam(CVParam<Double> cvParam) {
//		getDoubleCVParamList().add(cvParam);
//	}
    public void removeCVParam(int index) {
        if (cvParams == null) {
            return;
        }

        CVParam removed = getCVParamList().remove(index);

//		removed.setParent(null);
    }

//	public void removeLongCVParam(int index) {
//		if(longCVParams == null)
//			return;
//		
//		getLongCVParamList().remove(index);
//	}
//	
//	public void removeDoubleCVParam(int index) {
//		if(doubleCVParams == null)
//			return;
//		
//		getDoubleCVParamList().remove(index);
//	}
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
//			cvParam.setParent(null);

            cvParams.remove(cvParam);
        }
    }

//	public void removeLongCVParam(String id) {
//		if(longCVParams == null)
//			return;
//		
//		ArrayList<CVParam<Long>> cvParamList = new ArrayList<CVParam<Long>>();
//		
//		for(CVParam<Long> cvParam : longCVParams)
//			if(cvParam.getTerm().getID().equals(id))
//				cvParamList.add(cvParam);
//		
//		for(CVParam<Long> cvParam : cvParamList) {
//			cvParams.remove(cvParam);
//		}
//	}
//	
//	public void removeDoubleCVParam(String id) {
//		if(doubleCVParams == null)
//			return;
//		
//		ArrayList<CVParam<Double>> cvParamList = new ArrayList<CVParam<Double>>();
//		
//		for(CVParam<Double> cvParam : doubleCVParams)
//			if(cvParam.getTerm().getID().equals(id))
//				cvParamList.add(cvParam);
//		
//		for(CVParam<Double> cvParam : cvParamList) {
//			cvParams.remove(cvParam);
//		}
//	}
    public void removeChildOfCVParam(String id) {
        if (cvParams == null) {
            return;
        }

        ArrayList<CVParam> children = getChildrenOf(id);

        for (CVParam cvParam : children) {
//			cvParam.setParent(null);

            cvParams.remove(cvParam);
        }
    }

    public void addUserParam(UserParam userParam) {
//		userParam.setParent(this);

        getUserParamList().add(userParam);
    }

    public void removeUserParam(int index) {
        if (userParams == null) {
            return;
        }

        UserParam removed = userParams.remove(index);

//		removed.setParent(null);
    }

    public CVParam getCVParam(String id) {
//		if(id.equals("MS:1000514"))
//			System.out.println("RPGR: " + referenceableParamGroupRefs);

        CVParam.CVParamType paramType = CVParam.getCVParamType(id);

        if (referenceableParamGroupRefs != null) {
            for (ReferenceableParamGroupRef ref : referenceableParamGroupRefs) {
                if (ref == null) {
                    continue;
                }

                CVParam cvParam = ref.getRef().getCVParam(id);

//				System.out.println("id: " + id + " | " + cvParam);
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

//	public CVParam<Long> getLongCVParam(String id) {
//		//if(longCVParams == null)
//		//	return null;
//		
//		//System.out.println(id);
//		
//		if(referenceableParamGroupRefs != null) {
//			for(ReferenceableParamGroupRef ref : referenceableParamGroupRefs) {
//				if(ref == null)
//					continue;
//				
//				CVParam<Long> cvParam = ref.getRef().getLongCVParam(id);
//					
//				if(cvParam != null)
//					return cvParam;
//			}
//		}
//		
//		if(longCVParams != null)
//			for(CVParam<Long> cvParam : longCVParams)
//				if(cvParam.getTerm().getID().equals(id))
//					return cvParam;				
//		
//		return null;
//	}
//	
//	public CVParam<Double> getDoubleCVParam(String id) {
//		//if(doubleCVParams == null)
//		//	return null;
//		
//		//System.out.println(id);
//		
//		if(referenceableParamGroupRefs != null) {
//			for(ReferenceableParamGroupRef ref : referenceableParamGroupRefs) {
//				if(ref == null)
//					continue;
//				
//				CVParam<Double> cvParam = ref.getRef().getDoubleCVParam(id);
//					
//				if(cvParam != null)
//					return cvParam;
//			}
//		}
//		
//		if(doubleCVParams != null)
//			for(CVParam<Double> cvParam : doubleCVParams)
//				if(cvParam.getTerm().getID().equals(id))
//					return cvParam;				
//		
//		return null;
//	}
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

                ArrayList<CVParam> children = ref.getRef().getChildrenOf(id);

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

        ArrayList<CVParam> children = getChildrenOf(id);

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

    public ArrayList<CVParam> getChildrenOf(String id) {
        ArrayList<CVParam> children = new ArrayList<CVParam>();

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
                MzMLContent.indent(output, indent);
                cvParam.outputXML(output);
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

//	public static String ensureSafeXML(String input) {
//		// TODO: Remove invalid characters such as '<' and '>'
//		
//		if(input == null)
//			return "";
//		
//		input = input.replaceAll("\"", "&quot;");
//		
//		return input;
//	}
    public void setParent(MzMLContent parent) {
        // This is a dummy function only included to allow the removal
    }

//	@Override
//	@JsonIgnore
//	public Enumeration<TreeNode> children() {
//		Vector<TreeNode> children = new Vector<TreeNode>();
//		children.addAll(referenceableParamGroupRefs);
//		children.addAll(cvParams);
//		children.addAll(userParams);
//		
//		return children.elements();
//	}
//
////	public void setParent(TreeNode parent) {
////		this.parentTreeNode = (MutableTreeNode) parent;
////	}
//	
//	@Override
//	@JsonIgnore
//	public boolean getAllowsChildren() {
//		return true;
//	}
//
//	@Override
//	@JsonIgnore
//	public TreeNode getChildAt(int index) {
//		if(index < referenceableParamGroupRefs.size()) {
//			return referenceableParamGroupRefs.get(index);
//		} else if(index < (referenceableParamGroupRefs.size() + cvParams.size())) {
//			return cvParams.get(index - referenceableParamGroupRefs.size());
//		} else if(index < getChildCount()) {
//			return userParams.get(index - (referenceableParamGroupRefs.size() + cvParams.size()));
//		}
//				
//		return null;
//	}
//
//	@Override
//	@JsonIgnore
//	public int getChildCount() {
//		// 
//		return referenceableParamGroupRefs.size() + cvParams.size() + userParams.size();
//	}
//
//	@Override
//	@JsonIgnore
//	public int getIndex(TreeNode childNode) {
//		if(childNode instanceof ReferenceableParamGroup) {
//			return referenceableParamGroupRefs.indexOf(childNode);
//		} else if(childNode instanceof CVParam) {
//			return cvParams.indexOf(childNode) + referenceableParamGroupRefs.size();
//		} else if(childNode instanceof UserParam) {
//			return userParams.indexOf(childNode) + referenceableParamGroupRefs.size() + cvParams.size();
//		}
//
//		return 0;
//	}
//
//	@Override
//	@JsonIgnore
//	public TreeNode getParent() {
//		return parentTreeNode;
//	}
//
//	@Override
//	@JsonIgnore
//	public boolean isLeaf() {
//		// Check if any children exist
//		return getChildCount() == 0;
//	}
//	
//	@Override
//	@JsonIgnore
//	public void setParent(MutableTreeNode parent) {
//		this.parentTreeNode = parent;
//	}
//	
//	@Override
//	@JsonIgnore
//	public void insert(MutableTreeNode child, int index) {
//		child.setParent(this);
//		
//		if(child instanceof ReferenceableParamGroupRef) {
//			referenceableParamGroupRefs.add(index, (ReferenceableParamGroupRef) child);
//		} else if(child instanceof CVParam) {
//			cvParams.add(index - referenceableParamGroupRefs.size(), (CVParam) child);
//		} else if(child instanceof UserParam) {
//			userParams.add(index - (referenceableParamGroupRefs.size() + cvParams.size()), (UserParam) child);
//		}
//	}
//	
//	@Override
//	@JsonIgnore
//	public void remove(int index) {
//		TreeNode child = getChildAt(index);
//		
//		remove((MutableTreeNode) child);
//	}
//	
//	@Override
//	@JsonIgnore
//	public void remove(MutableTreeNode child) {
//		child.setParent(null);
//		
//		if(child instanceof ReferenceableParamGroup) {
//			referenceableParamGroupRefs.remove(child);
//		} else if(child instanceof CVParam) {
//			cvParams.remove(child);
//		} else if(child instanceof UserParam) {
//			userParams.remove(child);
//		}
//	}
//	
//	@Override
//	@JsonIgnore
//	public void removeFromParent() {
//		parentTreeNode.remove(this);
//	}
//	
//	@Override
//	public void setUserObject(Object object) {
//		System.out.println("NEED TO IMPLEMENT setUserObject for: " + this);
//	}
}
