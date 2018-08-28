package com.alanmrace.jimzmlparser.exceptions;

import com.alanmrace.jimzmlparser.mzml.CVParam;
import com.alanmrace.jimzmlparser.mzml.StringCVParam;
import com.alanmrace.jimzmlparser.obo.OBOTerm;
import com.alanmrace.jimzmlparser.obo.OBOTerm.XMLType;

/**
 * Non-fatal parse exception occurred when trying to parse a value with a specific
 * type or format.
 * 
 * @author Alan Race
 */
public class InvalidFormatIssue extends NonFatalParseIssue {
    
    /**
     * String representation of the value that was parsed.
     */
    protected String value;
    
    /**
     * XML attribute that was parsed (if parsing an attribute) or null if not.
     */
    protected String attribute;

    /**
     * The expected format of the value.
     */
    protected String expectedFormat;
    
    /**
     * The ontology term that contained the problematic value (if parsing a cvParam)
     * or null if not.
     */
    protected OBOTerm term;

    /**
     * Expected type of the CVParam value.
     */
    protected CVParam.CVParamType expectedParamType;
    
    /**
     * Expected type of the CVParam value.
     */
    protected XMLType xmlType;
    
    /**
     * If a fix was attempted by switching the cvParam to a String type then this 
     * will be non-null.
     */
    protected StringCVParam newParam;
    
    /**
     * Invalid format when parsing the value of the specified cvParam.
     * 
     * @param term Ontology term of the cvParam
     * @param value Value that was poorly formed 
     */
    public InvalidFormatIssue(OBOTerm term, String value) {
        this.term = term;
        this.value = value;
    }
    
    // NO CVPARAM TYPE FOR THE XML TYPE

    /**
     * Unknown or unexpected XMLType was specified for the ontology term.
     * 
     * @param term      Ontology term of the cvParam
     * @param xmlType   XMLType that is not supported
     */
    public InvalidFormatIssue(OBOTerm term, XMLType xmlType) {
        this.term = term;
        this.xmlType = xmlType;
    }
    
    /**
     * Unknown or unexpected CVParamType was specified for the ontology term.
     * 
     * @param term      Ontology term of the cvParam
     * @param paramType CVParamType that is not supported
     */
    public InvalidFormatIssue(OBOTerm term, CVParam.CVParamType paramType) {
        this.term = term;
        this.expectedParamType = paramType;
    }
    
    /**
     * XML attribute value did not match the expected format.
     * 
     * @param attribute         Attribute name
     * @param expectedFormat    Expected format
     * @param value             Value that did not match expected format
     */
    public InvalidFormatIssue(String attribute, String expectedFormat, String value) {
        this.attribute = attribute;
        this.expectedFormat = expectedFormat;
        this.value = value;
    }
    
    /**
     * A fix has been attempted by switching to a StringCVParam.
     * 
     * @param param StringCVParam that replaced the original CVParam.
     */
    public void fixAttemptedByChangingType(StringCVParam param) {        
        newParam = param;
        this.attemptedFix = true;
    }
    
    @Override
    public String getIssueTitle() {
        return "Invalid value";
    }
    
    @Override
    public String getIssueMessage() {
        StringBuilder message = new StringBuilder("");
        
        if(term != null && xmlType == null && expectedParamType == null) {
            message.append("Expected");
            
            if(term.getValueType() == null)
                message.append(" no value");
            else
                message.append(" a value of type ").append(term.getValueType());
            
            message.append(" for CVParam ").append(term.getID());
            
            if(term.getName() != null)
                message.append(" (").append(term.getName()).append(")");
            
            if(value == null)
                message.append(" but the value attribute was ommited");
            else
                message.append(" but got value \"").append(value).append("\"");
        }
        
        if(term != null && xmlType != null) {
            message.append("Unimplemented or unexpected XMLType ");
            message.append(term.getValueType().toString());
            message.append(" (assigned to term ");
            message.append(term.getID());
            message.append(")");
        }
        
        if(term == null) {
            message.append("Expected format ").append(expectedFormat).append(" but got ").append(value);
        }
        
        if(attemptedFix) {
            message.append("\nAttempted to fix by changing CVParam value type to String.");
        }
        
        return message.toString();
    }
}
