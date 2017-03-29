/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
