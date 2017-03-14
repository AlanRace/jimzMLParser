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
public class UnfollowableXPathException extends InvalidXPathException {
    
    protected String subXPath;
    
    protected UnfollowableXPathException(String message, String XPath) {
        super(message, XPath);
    }
    
    public UnfollowableXPathException(String message, String XPath, String subXPath) {
        this(message, XPath);
        
        this.subXPath = subXPath;
    }
    
    public String getSubXPath() {
        return subXPath;
    }
}
