package com.alanmrace.jimzmlparser.exceptions;

import com.alanmrace.jimzmlparser.mzML.UserParam;

public class CVParamAccessionNotFoundException extends NonFatalParseException {

    /**
     *
     */
    private static final long serialVersionUID = 1470705281628394244L;

    private final String accession;
    
    private UserParam userParam;

    public CVParamAccessionNotFoundException(String accession) {
        this.accession = accession;
    }

    /**
     * Attempt fix by replacing with UserParam
     *
     * @param userParam
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
