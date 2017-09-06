package com.alanmrace.jimzmlparser.exceptions;

/**
 * Fatal parse exception occurred. It is not possible to continue parsing.
 * 
 * @author Alan Race
 */
public class FatalParseException extends RuntimeException implements ParseIssue {

    /**
     * Construct FatalParseException with a message describing the issue.
     * 
     * @param message Description of the issue
     */
    public FatalParseException(String message) {
        super(message);
    }

    /**
     * Construct FatalParseException with a message describing the issue and the
     * original exception.
     * 
     * @param message   Description of the issue
     * @param exception Original exception that describes the issue
     */
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
