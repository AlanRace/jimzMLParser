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
public class InvalidXPathException extends Exception {
    
    public InvalidXPathException(String message) {
        super(message);
    }

    public InvalidXPathException(String message, Exception exception) {
        super(message, exception);
    }
}
