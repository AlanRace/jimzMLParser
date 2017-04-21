package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.exceptions.InvalidXPathException;
import java.io.IOException;
import java.util.Collection;
import com.alanmrace.jimzmlparser.writer.MzMLWritable;

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
     * Add all child MzMLContent (mzML tags) that match the specified XPath to 
     * the specified collection.
     * 
     * @param elements      Collection of MzMLContent to add children to
     * @param xPath         Full XPath to query
     * @throws InvalidXPathException thrown if the XPath can not be followed
     */
    public void addElementsAtXPathToCollection(Collection<MzMLTag> elements, String xPath) throws InvalidXPathException;
    
    /**
     * Add all child MzMLContent (mzML tags) that match the specified XPath to 
     * the specified collection.
     * 
     * @param elements      Collection of MzMLContent to add children to
     * @param fullXPath     Full XPath to query
     * @param currentXPath  Sub XPath that should start with the tag name for the current MzMLContent
     * @throws InvalidXPathException thrown if the XPath can not be followed
     */
    public void addElementsAtXPathToCollection(Collection<MzMLTag> elements, String fullXPath, String currentXPath) throws InvalidXPathException;
    
    /**
     * Returns XML formatted text describing XML attributes for the MzMLTag.
     * 
     * @return XML formatted attribute text
     */
    public String getXMLAttributeText();
    
    /**
     * Format the contents of the class into XML at a specified number of tab
     * indents and output to a MzMLWritable.
     * 
     * @param output MzMLWritable to output the XML to
     * @param indent Number of tabs to indent each tag in the XML
     * @throws IOException Exception occurred during writing data
     */
    public void outputXML(MzMLWritable output, int indent) throws IOException;
    
    /**
     * Set the parent MzMLContent of this MzMLContent. This method currently 
     * does nothing.
     * 
     * <p>TODO: Remove this.
     * 
     * @param parent Parent MzMLContent to add
     * @deprecated This was removed when the Tree code was decoupled
     */
    @Deprecated
    public void setParent(MzMLTag parent);
}
