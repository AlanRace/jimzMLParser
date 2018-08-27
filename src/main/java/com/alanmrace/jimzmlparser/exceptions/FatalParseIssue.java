/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alanmrace.jimzmlparser.exceptions;

import java.io.Serializable;

/**
 *
 * @author Alan
 */
public class FatalParseIssue implements ParseIssue, Serializable {

    String title;
    String message;
    
    public FatalParseIssue(String title, String message) {
        this.title = title;
        this.message = message;
    }

    @Override
    public String getIssueTitle() {
        return title;
    }

    @Override
    public String getIssueMessage() {
        return message;
    }

    @Override
    public IssueLevel getIssueLevel() {
        return IssueLevel.SEVERE;
    }
    
    
}
