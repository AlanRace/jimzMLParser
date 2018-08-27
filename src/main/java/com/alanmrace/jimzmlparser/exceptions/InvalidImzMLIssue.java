package com.alanmrace.jimzmlparser.exceptions;

/**
 * Exception occurred during the parsing of an imzML file, due to an 
 * invalid imzML file.
 * 
 * @author Alan Race
 */
public class InvalidImzMLIssue extends FatalParseIssue {

    public InvalidImzMLIssue(String title, String message) {
        super(title, message);
    }
}
