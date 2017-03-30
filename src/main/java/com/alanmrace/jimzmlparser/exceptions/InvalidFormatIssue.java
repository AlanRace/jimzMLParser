/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alanmrace.jimzmlparser.exceptions;

import com.alanmrace.jimzmlparser.mzML.CVParam;
import com.alanmrace.jimzmlparser.mzML.MzMLContent;
import com.alanmrace.jimzmlparser.mzML.StringCVParam;
import com.alanmrace.jimzmlparser.obo.OBOTerm;
import com.alanmrace.jimzmlparser.obo.OBOTerm.XMLType;

/**
 *
 * @author Alan
 */
public class InvalidFormatIssue extends NonFatalParseException {
    protected MzMLContent mzMLContent;
    
    protected String value;
    
    protected String attribute;
    protected String expectedFormat;
    
    protected OBOTerm term;
    protected CVParam.CVParamType expectedParamType;
    
    protected XMLType xmlType;
    
    protected StringCVParam newParam;
    
    public InvalidFormatIssue(OBOTerm term, String value) {
        this.term = term;
        this.value = value;
    }
    
    // NO CVPARAM TYPE FOR THE XML TYPE
    public InvalidFormatIssue(OBOTerm term, XMLType xmlType) {
        this.term = term;
        this.xmlType = xmlType;
    }
    
    public InvalidFormatIssue(OBOTerm term, CVParam.CVParamType paramType) {
        this.term = term;
        this.expectedParamType = paramType;
    }
    
    public InvalidFormatIssue(String attribute, String expectedFormat, String value) {
        this.attribute = attribute;
        this.expectedFormat = expectedFormat;
        this.value = value;
    }
    
    public void fixAttemptedByChangingType(StringCVParam param) {
        //TODO: attempted fix by switching to StringCVParam if type did not match
        
        newParam = param;
        this.attemptedFix = true;
    }
    
    @Override
    public String getIssueTitle() {
        return "Invalid value";
    }
    
    @Override
    public String getIssueMessage() {
        String message = "";
        
        if(term != null && xmlType == null && expectedParamType == null) {
            message += "Expected";
            
            if(term.getValueType() == null)
                message += " no value";
            else
                message += " a value of type " + term.getValueType();
            
            message += " for CVParam " + term.getID();
            
            if(term.getName() != null)
                message += " (" + term.getName() + ")";
            
            if(value == null)
                message += " but the value attribute was ommited";
            else
                message += " but got value \"" + value + "\"";
        }
        
        return message;
    }
}
