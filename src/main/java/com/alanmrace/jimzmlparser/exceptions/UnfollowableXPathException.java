package com.alanmrace.jimzmlparser.exceptions;

/**
 * The XPath matches the (i)mzML schema but cannot be followed due to tags not 
 * being included with the (i)mzML file.
 * 
 * @author Alan Race
 */
public class UnfollowableXPathException extends InvalidXPathException {
    
    /**
     * The subset of the XPath where the issue occurred.
     */
    protected String subXPath;
    
    /**
     * Set up UnfollowableXPathException with a message and an unfollowable XPath.
     * 
     * @param message   Description of the exception
     * @param XPath     Unfollowable XPath within the context of a specific (i)mzML file
     */
    protected UnfollowableXPathException(String message, String XPath) {
        super(message, XPath);
    }
    
    /**
     * Set up UnfollowableXPathException with a message and an unfollowable XPath.
     * 
     * @param message   Description of the exception
     * @param XPath     Unfollowable XPath within the context of a specific (i)mzML file
     * @param subXPath  The subset of the XPath where the issue occurred
     */
    public UnfollowableXPathException(String message, String XPath, String subXPath) {
        this(message, XPath);
        
        this.subXPath = subXPath;
    }
    
    /**
     * Get the subset of the XPath where the issue occurred.
     * 
     * @return Part of the XPath
     */
    public String getSubXPath() {
        return subXPath;
    }
}
