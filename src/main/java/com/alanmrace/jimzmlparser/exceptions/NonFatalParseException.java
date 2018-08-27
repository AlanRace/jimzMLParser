package com.alanmrace.jimzmlparser.exceptions;

/**
 * Non-fatal parse exception occurred. It is possible to continue parsing, and 
 * and a fix may or may not have been attempted.
 * 
 * @author Alan Race
 */
public class NonFatalParseException extends Exception {
    
    private final NonFatalParseIssue issue;

    public NonFatalParseException(NonFatalParseIssue issue) {
        super(issue.getIssueTitle());
        
        this.issue = issue;
    }

    public NonFatalParseIssue getIssue() {
        return issue;
    }
}
