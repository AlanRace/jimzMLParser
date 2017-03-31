/*
 * 
 */
package com.alanmrace.jimzmlparser.exceptions;

import com.alanmrace.jimzmlparser.mzml.MzMLContent;

/**
 *
 * @author Alan Race
 */
public class MissingReferenceIssue extends NonFatalParseException {
    private String reference;
    private String tagName;
    private String attributeName;
    
    private MzMLContent newReference;
    
    public MissingReferenceIssue(String reference, String tagName, String attributeName) {
        this.reference = reference;
        this.tagName = tagName;
        this.attributeName = attributeName;
    }
    
    // Attempt fix by removing reference
    public void fixAttemptedByRemovingReference() {
        this.attemptedFix = true;
    }
    
    public void fixAttemptedByChangingReference(MzMLContent newReference) {
        this.attemptedFix = true;
        
        this.newReference = newReference;
    }
    
    @Override
    public String getIssueTitle() {
        return "Missing reference " + reference + " at <" + tagName + ">";
    }
    
    @Override
    public String getIssueMessage() {
        String message = "Expected the attribute " + attributeName + " to have a valid reference (" + reference + ") at <" + tagName + ">\n";
        
 //       message += "\t" + this.location;
        
        if(attemptedFix) {
//            message += "\n";
            
            if(newReference == null) {
                message += "Attempted to fix by removing the reference";
            } else {
                message += "Attempted to fix by changing the reference to " + newReference;
            }
        }
        
        return message;
    }
    
    @Override
    public IssueLevel getIssueLevel() {
        return IssueLevel.WARNING;
    }
}
