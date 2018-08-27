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
    private final String subXPath;
    
    /**
     * Set up UnfollowableXPathException with a message and an unfollowable XPath.
     * 
     * @param message   Description of the exception
     * @param xPath     Unfollowable XPath within the context of a specific (i)mzML file
     */
    protected UnfollowableXPathException(String message, String xPath) {
        super(message, xPath);
        
        this.subXPath = "";
    }
    
    /**
     * Set up UnfollowableXPathException with a message and an unfollowable XPath.
     * 
     * @param message   Description of the exception
     * @param xPath     Unfollowable XPath within the context of a specific (i)mzML file
     * @param subXPath  The subset of the XPath where the issue occurred
     */
    public UnfollowableXPathException(String message, String xPath, String subXPath) {
        super(message, xPath);
        
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
