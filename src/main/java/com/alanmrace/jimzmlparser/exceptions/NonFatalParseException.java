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
     * A short description of the issue, suitable to be used as a title.
     */
    protected String issueTitle;

    /**
     * A detailed description of the issue, providing detail to a user.
     */
    protected String issueMessage;
    
    /**
     * Base constructor that sets up default title and messages, but shouldn't be 
     * used.
     */
    protected NonFatalParseException() {
        this("[NonFatalParseException] - Temporary Title", "[NonFatalParseException] - Temporary Message");
    }
    
    /**
     * Describe a non-fatal parsing exception by supplying a title and a detailed
     * message.
     * 
     * @param title     Short description of the issue, suitable to be used as a title
     * @param message   Detailed description of the issue, providing detail to a user
     */
    public NonFatalParseException(String title, String message) {
        this.issueTitle = title;
        this.issueMessage = message;
    }
    
    /**
     * Set the parent MzMLContent that was being parsed when the issue occurred.
     * 
     * @param location Parent MzMLContent
     */
    public void setIssueLocation(MzMLContent location) {
        this.location = location;
    }
    
    public MzMLContent getIssueLocation() {
        return location;
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
        return issueTitle;
    }

    @Override
    public String getIssueMessage() {
        String message = issueMessage;
        
        if(attemptedFix)
            message += "\n" + fixMessage;
        
        return message;
    }

    @Override
    public IssueLevel getIssueLevel() {
        return IssueLevel.SEVERE;
    }
    
    
}
