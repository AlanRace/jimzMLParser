package com.alanmrace.jimzmlparser.exceptions;

/**
 *
 * @author Alan Race
 */
public class FatalRuntimeParseException extends RuntimeException {
    private final FatalParseIssue issue;

    /**
     * Construct FatalParseException with a message describing the issue and the
     * original exception.
     * 
     * @param issue FatalParseIssue that caused the exception
     */
    public FatalRuntimeParseException(FatalParseIssue issue) {
        super(issue.getIssueMessage());
        
        this.issue = issue;
    }
    
    /**
     * Construct FatalParseException with a message describing the issue and the
     * original exception.
     * 
     * @param issue FatalParseIssue that caused the exception
     * @param exception Exception that caused the issue
     */
    public FatalRuntimeParseException(FatalParseIssue issue, Exception exception) {
        super(issue.getIssueMessage(), exception);
        
        this.issue = issue;
    }
    
    public FatalParseIssue getIssue() {
        return issue;
    }
}
