package com.alanmrace.jimzmlparser.exceptions;

/**
 * Exception occurred during the parsing of an mzML file, due to an 
 * invalid mzML file.
 * 
 * @author Alan Race
 */
public class InvalidMzML extends FatalParseException implements ParseIssue {

    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = -5265931318748556126L;

    /**
     * Set up InvalidMzML with a message and a previous exception which triggered
     * this being created.
     * 
     * @param message Description of the exception
     * @param ex Issue that caused this exception
     */
    public InvalidMzML(String message, Exception ex) {
        super(message, ex);
    }
    
    /**
     * Set up InvalidMzML with a message.
     * 
     * @param message Description of the exception
     */
    public InvalidMzML(String message) {
        super(message);
    }

    @Override
    public String getIssueTitle() {
        return this.getMessage();
    }

    @Override
    public String getIssueMessage() {
        return this.getMessage();
    }

    @Override
    public IssueLevel getIssueLevel() {
        return IssueLevel.SEVERE;
    }
}
