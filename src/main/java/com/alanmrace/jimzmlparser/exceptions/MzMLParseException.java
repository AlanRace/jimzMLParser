package com.alanmrace.jimzmlparser.exceptions;

/**
 * Exception occurred during the parsing of an mzML file, probably due to an 
 * invalid mzML file.
 * 
 * @author Alan Race
 */
public class MzMLParseException extends FatalParseException {

    public MzMLParseException(FatalParseIssue issue) {
        super(issue);
    }

    public MzMLParseException(FatalParseIssue issue, Exception exception) {
        super(issue, exception);
    }
    
}
