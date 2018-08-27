package com.alanmrace.jimzmlparser.exceptions;

import com.alanmrace.jimzmlparser.mzml.MzMLContent;

/**
 * The reference ID could not be matched or resolved to a definition within (i)mzML file.
 * 
 * @author Alan Race
 */
public class MissingReferenceIssue extends NonFatalParseIssue {

    /**
     * The reference ID that could not be resolved.
     */
    private final String reference;

    /**
     * The tag name that contained the unresolvable reference.
     */
    private final String tagName;

    /**
     * The name of the attribute that contained the unresolvable reference.
     */
    private final String attributeName;
    
    /**
     * If a fix was attempted then this will be non-null and contain the MzMLContent
     * that is now referenced.
     */
    private MzMLContent newReference;
    
    /**
     * Set up MissingReferenceIssue.
     * 
     * @param reference     Reference ID that could not be resolved.
     * @param tagName       Tag name that contained the unresolvable reference.
     * @param attributeName Name of the attribute that contained the unresolvable reference.
     */
    public MissingReferenceIssue(String reference, String tagName, String attributeName) {
        this.reference = reference;
        this.tagName = tagName;
        this.attributeName = attributeName;
    }
    
    // Attempt fix by removing reference

    /**
     * Fix was attempted by removing the reference.
     */
    public void fixAttemptedByRemovingReference() {
        this.attemptedFix = true;
    }
    
    /**
     * Fix was attempted by changing the reference to new MzMLContent.
     * 
     * @param newReference New MzMLContent
     */
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
        StringBuilder message = new StringBuilder("Expected the attribute ");
        
        message.append(attributeName);
        message.append(" to have a valid reference (");
        message.append(reference);
        message.append(") at <");
        message.append(tagName);
        message.append(">\n");
                
        if(attemptedFix) {    
            if(newReference == null) {
                message.append("Attempted to fix by removing the reference");
            } else {
                message.append("Attempted to fix by changing the reference to ");
                message.append(newReference);
            }
        }
        
        return message.toString();
    }
    
    @Override
    public IssueLevel getIssueLevel() {
        return IssueLevel.WARNING;
    }
}
