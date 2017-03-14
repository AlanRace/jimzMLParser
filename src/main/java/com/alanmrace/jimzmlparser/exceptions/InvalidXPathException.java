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
public class InvalidXPathException extends Exception {
    
    protected String XPath;
    
    public InvalidXPathException(String message, String XPath) {
        super(message);
        
        this.XPath = XPath;
    }
    
    public String getXPath() {
        return XPath;
    }
}
