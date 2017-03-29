package com.alanmrace.jimzmlparser.exceptions;

public class InvalidMzML extends RuntimeException implements ParseIssue {

    /**
     *
     */
    private static final long serialVersionUID = -5265931318748556126L;

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
