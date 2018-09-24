/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alanmrace.jimzmlparser.exceptions;

import com.alanmrace.jimzmlparser.mzml.MzMLContent;
import java.io.Serializable;

/**
 *
 * @author Alan
 */
public abstract class NonFatalParseIssue implements ParseIssue, Serializable {

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


    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(getIssueTitle());
        stringBuilder.append(": ");

        String[] splitString = getIssueMessage().split("\n");

        if(splitString.length > 0) {
            stringBuilder.append(splitString[0]);
        } else {
            stringBuilder.append(getIssueMessage());
        }

        stringBuilder.append(" at ");
        stringBuilder.append(location);

        return stringBuilder.toString();
    }
}
