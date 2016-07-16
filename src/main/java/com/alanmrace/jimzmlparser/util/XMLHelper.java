/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alanmrace.jimzmlparser.util;

/**
 *
 * @author Alan
 */
public class XMLHelper {
    public static String ensureSafeXML(String input) {
	// TODO: Remove invalid characters such as '<' and '>'
		
	if(input == null)
            return "";
	
        input = input.replaceAll("&amp;", "&");
        input = input.replaceAll("&", "&amp;");
        
	input = input.replaceAll("\"", "&quot;");
        
		
	return input;
    }
}
