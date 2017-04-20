package com.alanmrace.jimzmlparser.util;

/**
 * Helper static functions for ensuring safe XML.
 * 
 * @author Alan Race
 */
public class XMLHelper {

    /**
     * Escape all characters that would result in invalid XML when exporting the 
     * input as an attribute value in an XML tag.
     * 
     * @param input Text to escape
     * @return Safe text to include within XML attributes
     */
    public static String ensureSafeXML(String input) {		
	if(input == null)
            return "";
	
        String output = input.replaceAll("&amp;", "&");
        output = output.replaceAll("&", "&amp;");
        output = output.replaceAll("<", "&lt;");
        output = output.replaceAll(">", "&rt;");
        
	output = output.replaceAll("\"", "&quot;");
        
		
	return output;
    }
}
