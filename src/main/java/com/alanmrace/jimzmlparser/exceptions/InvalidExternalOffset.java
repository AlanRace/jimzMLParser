/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alanmrace.jimzmlparser.exceptions;

import com.alanmrace.jimzmlparser.mzml.MzMLDataContainer;

/**
 *
 * @author Alan
 */
public class InvalidExternalOffset extends NonFatalParseException {
    
    MzMLDataContainer container;
    long offset;
    
    public InvalidExternalOffset(MzMLDataContainer container, long offset) {
        this.container = container;
        this.offset = offset;
        
        this.attemptedFix = true;
    }
    
    @Override
    public String getIssueTitle() {
        return "Invalid offset " + offset + " at " + container + "";
  
    }
    
    @Override
    public String getIssueMessage() {
        String message = "Invalid offset supplied (" + offset + "). Offset must be a non-negative integer.\n";
                
        if(attemptedFix) {
            message += "Attempted to fix by adding 2^32 to offset.";
        }
        
        return message;
    }
}
