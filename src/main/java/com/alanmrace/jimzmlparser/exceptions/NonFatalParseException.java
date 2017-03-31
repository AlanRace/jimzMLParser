/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alanmrace.jimzmlparser.exceptions;

import com.alanmrace.jimzmlparser.mzml.MzMLContent;

/**
 *
 * @author Alan
 */
public class NonFatalParseException extends Exception implements ParseIssue {

    protected MzMLContent location; 
    
    protected boolean attemptedFix = false;
    protected String fixMessage;
    
    public void setIssueLocation(MzMLContent location) {
        this.location = location;
    }
    
    public boolean hasFixBeenAttempted() {
        return attemptedFix;
    }
    
    public void fixAttempted(String fixMessage) {
        this.fixMessage = fixMessage;
    }
    
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
