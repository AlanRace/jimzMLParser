package com.alanmrace.jimzmlparser.exceptions;

import com.alanmrace.jimzmlparser.mzml.MzMLContent;

/**
 * Non-fatal parse exception occurred. It is possible to continue parsing, and 
 * and a fix may or may not have been attempted.
 * 
 * @author Alan Race
 */
public class NonFatalParseException extends Exception implements ParseIssue {

    /**
     * The parent MzMLContent that was being parsed when the issue occurred.
     */
    protected MzMLContent location; 
    
    /**
     * True if a fix was attempted, false if no fix was attempted.
     */
    protected boolean attemptedFix = false;

    /**
     * Message describing the attempted fix.
     */
    protected String fixMessage;
    
    /**
     * Set the parent MzMLContent that was being parsed when the issue occurred.
     * 
     * @param location Parent MzMLContent
     */
    public void setIssueLocation(MzMLContent location) {
        this.location = location;
    }
    
    /**
     * Check whether a fix was attempted.
     * 
     * @return True if a fix was attempted, false if not.
     */
    public boolean hasFixBeenAttempted() {
        return attemptedFix;
    }
    
    /**
     * Set that a fix was attempted, with a description of the fix.
     * 
     * @param fixMessage Description of the fix
     */
    public void fixAttempted(String fixMessage) {
        this.fixMessage = fixMessage;
        
        attemptedFix = true;
    }
    
    /**
     * Get the description of the attempted fix.
     * 
     * @return Description of the attempted fix
     */
    protected String getFixMessage() {
        return fixMessage;
    }
    
    @Override
    public String getIssueTitle() {
        return "[NonFatalParseException] - Temporary Title";
    }

    @Override
    public String getIssueMessage() {
        return "[NonFatalParseException] - Temporary Message";
    }

    @Override
    public IssueLevel getIssueLevel() {
        return IssueLevel.SEVERE;
    }
    
    
}
