/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alanmrace.jimzmlparser.exceptions;

/**
 *
 * @author Alan
 */
public interface Issue {
    
    public enum IssueLevel {
        SEVERE,
        ERROR,
        WARNING
    }
    
    /**
     * Provides a short overview of the issue, such that it could be used as a 
     * general title.
     * 
     * @return Short description of the issue.
     */
    public String getIssueTitle();

    /**
     * Provides detail about the issue, in prose, such that a non-expert could
     * understand why an error has occurred.
     * 
     * @return Description of the issue.
     */
    public String getIssueMessage();
    
    public IssueLevel getIssueLevel();
}
