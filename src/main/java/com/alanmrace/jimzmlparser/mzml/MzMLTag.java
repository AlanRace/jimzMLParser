package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.exceptions.InvalidXPathException;
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
     * Add all child MzMLContent (mzML tags) that match the specified XPath to
     * the specified collection.
     *
     * @param elements Collection of MzMLContent to add children to
     * @param xPath Full XPath to query
     * @throws InvalidXPathException thrown if the XPath can not be followed
     */
    public void addElementsAtXPathToCollection(Collection<MzMLTag> elements, String xPath) throws InvalidXPathException;

    /**
     * Add all child MzMLContent (mzML tags) that match the specified XPath to
     * the specified collection.
     *
     * @param elements Collection of MzMLContent to add children to
     * @param fullXPath Full XPath to query
     * @param currentXPath Sub XPath that should start with the tag name for the
     * current MzMLContent
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
     * Set the parent MzMLContent of this MzMLContent. This method currently
     * does nothing.
     *
     * @param parent Parent MzMLContent to add
     */
    public void setParent(MzMLTag parent);

    /**
     * Returns the parent MzMLTag of this MzMLTag, or null if top level (I)mzML
     * tag.
     *
     * @return Parent MzMLContent to add
     */
    public MzMLTag getParent();

    /**
     * Returns the XPath of the current MzMLTag.
     *
     * @return XPath
     */
    public String getXPath();
}
