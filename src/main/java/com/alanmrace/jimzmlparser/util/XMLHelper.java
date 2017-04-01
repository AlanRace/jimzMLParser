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
	// TODO: Remove invalid characters such as '<' and '>'
		
	if(input == null)
            return "";
	
        String output = input.replaceAll("&amp;", "&");
        output = output.replaceAll("&", "&amp;");
        
	output = output.replaceAll("\"", "&quot;");
        
		
	return output;
    }
}
