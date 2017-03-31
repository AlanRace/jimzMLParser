/*
 * 
 */
package com.alanmrace.jimzmlparser.parser;

import com.alanmrace.jimzmlparser.exceptions.Issue;

/**
 *
 * @author Alan
 */
public interface ParserListener {
    void issueFound(Issue exception);
}
