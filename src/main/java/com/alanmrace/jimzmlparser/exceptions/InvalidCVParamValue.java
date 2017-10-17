/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alanmrace.jimzmlparser.exceptions;

/**
 *
 * @author alan.race
 */
public class InvalidCVParamValue extends NonFatalParseException {
    public InvalidCVParamValue(String message) {
        super("InvalidCVParamValue", message);
    }
}
