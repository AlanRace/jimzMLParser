package com.alanmrace.jimzmlparser.exceptions;

import com.alanmrace.jimzmlparser.mzml.CVParam;

/**
 *
 * @author Alan Race
 */
public class InvalidCVParamValue extends NonFatalParseIssue {
    
    CVParam param;
    
    String value;
    
    public InvalidCVParamValue(String message) {
        this.issueMessage = message;
    }
    
    
}
