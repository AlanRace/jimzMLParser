package com.alanmrace.jimzmlparser.exceptions;

/**
 * Issue interface.
 * 
 * @author Alan Race
 */
public interface Issue {
    
    /**
     * The severity level of the issue, SEVERE, ERROR or WARNING. 
     */
    enum IssueLevel {
        /** Severe failure that probably prevents continuation. */
        SEVERE,
        /** Error, but possible to attempt to continue. */
        ERROR,
        /** No functionality should be affected. */
        WARNING
    }
    
    /**
     * Provides a short overview of the issue, such that it could be used as a 
     * general title.
     * 
     * @return Short description of the issue.
     */
    String getIssueTitle();

    /**
     * Provides detail about the issue, in prose, such that a non-expert could
     * understand why an error has occurred.
     * 
     * @return Description of the issue.
     */
    String getIssueMessage();
    
    /**
     * Provides the assigned severity level of the issue.
     * 
     * @return IssueLevel of the issue
     * @see IssueLevel
     */
    IssueLevel getIssueLevel();
}
