/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alanmrace.jimzmlparser.exceptions;

/**
 *
 * @author Alan
 */
public class UnfollowableXPathException extends InvalidXPathException {
    public UnfollowableXPathException(String message) {
        super(message);
    }

    public UnfollowableXPathException(String message, Exception exception) {
        super(message, exception);
    }
}
