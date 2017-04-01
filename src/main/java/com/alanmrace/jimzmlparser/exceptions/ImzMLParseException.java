package com.alanmrace.jimzmlparser.exceptions;

/**
 * Exception occurred during the parsing of an imzML file, probably due to an 
 * invalid imzML file.
 * 
 * @author Alan Race
 */
public class ImzMLParseException extends MzMLParseException {

    /**
     * Set up ImzMLParseException with a message.
     * 
     * @param message Description of the exception
     */
    public ImzMLParseException(String message) {
        super(message);
    }

    /**
     * Set up ImzMLParseException with a message and a previous exception.
     * 
     * @param message Description of the issue with the parsing
     * @param exception Exception that was thrown describing the issue
     */
    public ImzMLParseException(String message, Exception exception) {
        super(message, exception);
    }
}
