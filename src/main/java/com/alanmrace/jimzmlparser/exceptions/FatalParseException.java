/*
 * 
 */
package com.alanmrace.jimzmlparser.exceptions;

/**
 *
 * @author Alan Race
 */
public class FatalParseException extends Exception implements ParseIssue {

    public FatalParseException(String message) {
        super(message);
    }

    public FatalParseException(String message, Exception exception) {
        super(message, exception);
    }
    
    @Override
    public String getIssueTitle() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getIssueMessage() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public IssueLevel getIssueLevel() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
