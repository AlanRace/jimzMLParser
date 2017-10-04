package com.alanmrace.jimzmlparser.exceptions;

/**
 * Exception occurred during the parsing of an imzML file, due to an 
 * invalid imzML file.
 * 
 * @author Alan Race
 */
public class InvalidImzML extends FatalParseException implements ParseIssue {

    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = -621709697196099119L;

    /**
     * Set up InvalidImzML with a message.
     * 
     * @param message Description of the exception
     * @param ex Issue that caused this exception
     */
    public InvalidImzML(String message, Exception ex) {
        super(message, ex);
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
