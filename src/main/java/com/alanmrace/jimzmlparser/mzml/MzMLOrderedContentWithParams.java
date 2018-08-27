package com.alanmrace.jimzmlparser.mzml;

/**
 * A base class for MzML tags which include ordered children.
 * 
 * @author Alan Race
 * 
 * @see ProcessingMethod
 * @see Component
 */
public abstract class MzMLOrderedContentWithParams extends MzMLContentWithParams {
    
    /**
     * Default constructor, does nothing.
     */
    protected MzMLOrderedContentWithParams() {
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
    public MzMLOrderedContentWithParams(MzMLOrderedContentWithParams mzMLContent, ReferenceableParamGroupList rpgList) {
        super(mzMLContent, rpgList);
    }
}
