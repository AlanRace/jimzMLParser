package com.alanmrace.jimzmlparser.exceptions;

/**
 * The XPath did not match the expected (i)mzML schema.
 * 
 * @author Alan Race
 */
public class InvalidXPathException extends Exception {
    
    /**
     * XPath that is invalid with respect to (i)mzML.
     */
    protected String XPath;
    
    /**
     * Set up InvalidMzML with a message and an invalid XPath.
     * 
     * @param message   Description of the exception
     * @param XPath     Invalid XPath within the context of (i)mzML
     */
    public InvalidXPathException(String message, String XPath) {
        super(message);
        
        this.XPath = XPath;
    }
    
    /**
     * Get the XPath that is invalid with respect to (i)mzML.
     * 
     * @return Invalid XPath
     */
    public String getXPath() {
        return XPath;
    }
}
