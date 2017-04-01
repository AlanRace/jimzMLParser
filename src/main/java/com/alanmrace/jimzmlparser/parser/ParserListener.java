package com.alanmrace.jimzmlparser.parser;

import com.alanmrace.jimzmlparser.exceptions.Issue;

/**
 * Listener for parsing issues encountered during parsing of (i)mzML files.
 * 
 * @author Alan Race
 */
public interface ParserListener {

    /**
     * Parser issue was encountered.
     * 
     * @param exception Parser issue encountered
     */
    void issueFound(Issue exception);
}
