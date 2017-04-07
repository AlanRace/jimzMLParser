package com.alanmrace.jimzmlparser.mzml;

/**
 * Base class for Components within a ComponentList (i.e. {@literal <source>}, 
 * {@literal <analyzer>} and {@literal <detector>}).
 * 
 * @author Alan Race
 */
public abstract class Component extends MzMLOrderedContentWithParams {
    
    /**
     * Protected constructor useful for subclasses.
     */
    protected Component() {
    }
    
    /**
     * Copy constructor, requiring new versions of lists to match old references to.
     * 
     * @param component Old Component to copy
     * @param rpgList New ReferenceableParamGroupList
     */
    public Component(Component component, ReferenceableParamGroupList rpgList) {
        super(component, rpgList);
    }
}
