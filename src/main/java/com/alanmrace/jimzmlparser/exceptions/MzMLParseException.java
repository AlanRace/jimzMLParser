package com.alanmrace.jimzmlparser.exceptions;

/**
 * Exception occurred during the parsing of an mzML file, probably due to an 
 * invalid mzML file.
 * 
 * @author Alan Race
 */
public class MzMLParseException extends FatalParseException {

    /**
     * Set up MzMLParseException with a message.
     * 
     * @param message Description of the exception
     */
//    public MzMLParseException(String message) {
//        super(message);
//    }

    /**
     * Set up MzMLParseException with a message and a previous exception.
     * 
     * @param message Description of the issue with the parsing
     * @param exception Exception that was thrown describing the issue
     */
//    public MzMLParseException(String message, Exception exception) {
//        super(message, exception);
//    }

    public MzMLParseException(FatalParseIssue issue) {
        super(issue);
    }

    public MzMLParseException(FatalParseIssue issue, Exception exception) {
        super(issue, exception);
    }
    
    /**
     * Set up MzMLParseException with a message.
     * 
     * @param message Description of the exception
     */
//    public MzMLParseException(String message) {
//        super(message);
//    }

    /**
     * Set up MzMLParseException with a message and a previous exception.
     * 
     * @param message Description of the issue with the parsing
     * @param exception Exception that was thrown describing the issue
     */
//    public MzMLParseException(String message, Exception exception) {
//        super(message, exception);
//    }
}
