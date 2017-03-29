package com.alanmrace.jimzmlparser.exceptions;

public class InvalidImzML extends RuntimeException implements ParseIssue {

    /**
     *
     */
    private static final long serialVersionUID = -621709697196099119L;

    public InvalidImzML(String message) {
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
