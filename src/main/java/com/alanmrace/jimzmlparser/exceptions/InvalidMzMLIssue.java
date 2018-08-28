package com.alanmrace.jimzmlparser.exceptions;

/**
 * Exception occurred during the parsing of an mzML file, due to an 
 * invalid mzML file.
 * 
 * @author Alan Race
 */
public class InvalidMzMLIssue extends FatalParseIssue {

    public InvalidMzMLIssue(String message) {
        super(message);
    }
    
    public InvalidMzMLIssue(String title, String message) {
        super(title, message);
    }

}
