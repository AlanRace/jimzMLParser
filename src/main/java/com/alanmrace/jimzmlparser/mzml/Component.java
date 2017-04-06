/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alanmrace.jimzmlparser.mzml;

/**
 *
 * @author Alan Race
 */
public abstract class Component extends MzMLOrderedContentWithParams {
    
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
