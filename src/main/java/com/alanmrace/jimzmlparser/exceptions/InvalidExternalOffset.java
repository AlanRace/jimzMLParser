package com.alanmrace.jimzmlparser.exceptions;

import com.alanmrace.jimzmlparser.mzml.MzMLDataContainer;

/**
 *
 * @author Alan Race
 */
public class InvalidExternalOffset extends NonFatalParseException {
    
    private final MzMLDataContainer container;
    private final long offset;
    
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
        StringBuilder message = new StringBuilder("Invalid offset supplied (");
        message.append(offset);
        message.append("). Offset must be a non-negative integer.\n");
                
        if(attemptedFix) {
            message.append("Attempted to fix by adding 2^32 to offset.");
        }
        
        return message.toString();
    }
}
