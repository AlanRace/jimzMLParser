package com.alanmrace.jimzmlparser.exceptions;

import com.alanmrace.jimzmlparser.obo.OBOTerm;

/**
 * Exception signalling that an OBOTerm was used which has been marked as obsolete.
 *  
 * @author Alan Race
 */
public class ObsoleteTermUsed extends NonFatalParseException {
    
    /**
     * The obsolete term which was used.
     */
    private final OBOTerm term;
    
    /**
     * Create an exception with the specified OBOTerm term.
     * 
     * @param term Obsolete OBOTerm that was used.
     * @see OBOTerm
     */
    public ObsoleteTermUsed(OBOTerm term) {
        this.term = term;
    }
    
    @Override
    public String getIssueTitle() {
        return "Obsolete term used in cvParam";
    }
    
    @Override
    public String getIssueMessage() {
        return term.getID() + " (" + term.getName() + ") used while it is marked OBSOLETE";
    }
    
    @Override
    public IssueLevel getIssueLevel() {
        return IssueLevel.WARNING;
    }
}
