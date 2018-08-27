package com.alanmrace.jimzmlparser.exceptions;

import com.alanmrace.jimzmlparser.mzml.UserParam;

/**
 * CVParam accession (ID) could not be found within the OBO ontology. 
 * 
 * @author Alan Race
 */
public class CVParamAccessionNotFoundIssue extends NonFatalParseIssue {

    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = 1470705281628394244L;

    /**
     * Accession (ID) of the CVParam that could not be found within the ontology.
     */
    private final String accession;
    
    /**
     * If not null, then a fix was attempted by converting the CVParam to this UserParam.
     */
    private final UserParam userParam;

    /**
     * Create CVParamAccessionNotFoundException with the specified accession (ID).
     * 
     * @param accession ID of the ontology term.
     */
    public CVParamAccessionNotFoundIssue(String accession) {
        this.accession = accession;
        this.userParam = null;
    }
    
    /**
     * A fix was attempted by replacing the CVParam with UserParam.
     *
     * @param accession ID of the ontology term.
     * @param fixAttempted UserParam that replaced the CVParam
     */
    public CVParamAccessionNotFoundIssue(String accession, UserParam fixAttempted) {
        this.accession = accession;
        this.userParam = fixAttempted;
        
    }
    
    @Override
    public String getIssueMessage() {
        return "Couldn't find " + accession + " in any OBO.";
    }
    
    @Override
    protected String getFixMessage() {
        if(userParam == null)
            return fixMessage;
        else
            return "Changed CVParam to UserParam: " + userParam;
    }
}
