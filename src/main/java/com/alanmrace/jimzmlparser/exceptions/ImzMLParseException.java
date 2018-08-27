package com.alanmrace.jimzmlparser.exceptions;

/**
 * Exception occurred during the parsing of an imzML file, probably due to an 
 * invalid imzML file.
 * 
 * @author Alan Race
 */
public class ImzMLParseException extends MzMLParseException {

    public ImzMLParseException(FatalParseIssue issue) {
        super(issue);
    }

    public ImzMLParseException(FatalParseIssue issue, Exception exception) {
        super(issue, exception);
    }
    
}
