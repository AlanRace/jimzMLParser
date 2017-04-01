package com.alanmrace.jimzmlparser.exceptions;

import com.alanmrace.jimzmlparser.mzml.UserParam;

/**
 * CVParam accession (ID) could not be found within the OBO ontology. 
 * 
 * @author Alan Race
 */
public class CVParamAccessionNotFoundException extends NonFatalParseException {

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
    private UserParam userParam;

    /**
     * Create CVParamAccessionNotFoundException with the specified accession (ID).
     * 
     * @param accession ID of the ontology term.
     */
    public CVParamAccessionNotFoundException(String accession) {
        this.accession = accession;
    }

    /**
     * A fix was attempted by replacing the CVParam with UserParam.
     *
     * @param userParam UserParam that replaced the CVParam
     */
    public void fixAttempted(UserParam userParam) {
        this.userParam = userParam;
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
