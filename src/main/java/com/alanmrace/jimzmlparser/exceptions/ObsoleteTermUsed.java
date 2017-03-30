/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alanmrace.jimzmlparser.exceptions;

import com.alanmrace.jimzmlparser.obo.OBOTerm;

/**
 *
 * @author Alan
 */
public class ObsoleteTermUsed extends NonFatalParseException {
    
    private OBOTerm term;
    
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
