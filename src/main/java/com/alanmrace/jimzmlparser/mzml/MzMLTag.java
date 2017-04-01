package com.alanmrace.jimzmlparser.mzml;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Collection;

/**
 * Interface for any class which describes a tag within an MzML file.
 * 
 * @author Alan Race
 */
public interface MzMLTag {

    /**
     * Get the name of the tag, as it appears in the MzML file.
     * 
     * @return tag name
     */
    public String getTagName();
    
    /**
     * Add all child tags, and their children (repeated recursively) to the supplied 
     * collection. 
     * 
     * @param children Collection to add the children to
     */
    public void addChildrenToCollection(Collection<MzMLTag> children);
    
    /**
     * Format the contents of the class into XML at a specified number of tab
     * indents and output to a BufferedReader.
     * 
     * @param output BufferedReader to output the XML to
     * @param indent Number of tabs to indent each tag in the XML
     * @throws IOException Exception occurred during writing data
     */
    public void outputXML(BufferedWriter output, int indent) throws IOException;
}
