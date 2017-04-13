package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.exceptions.InvalidXPathException;
import com.alanmrace.jimzmlparser.util.XMLHelper;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import com.alanmrace.jimzmlparser.writer.MzMLWritable;

/**
 * Base class for all mzML tags. This includes default handling for inclusion of
 * lists of {@link ReferenceableParamGroupRef}, {@link CVParam} and
 * {@link UserParam}.
 *
 * <p>TODO: Consider refactoring so that inclusion of CVParams is only possible on
 * tags that are allowed to have CVParams. This could be done using an
 * interface.
 *
 * @author Alan Race
 */
public abstract class MzMLContent implements Serializable, MzMLTag {

    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Add all child MzMLContent (mzML tags) that match the specified XPath,
     * which are specific to this tag (i.e. child tags which are not
     * {@literal <referenceableParamGroupRef>}, {@literal <cvParam>} and
     * {@literal <userParam>}), to the specified collection.
     *
     * <p>
     * This method should be overridden in subclasses (mzML tags) which have
     * specific children (i.e. child tags in addition to {@literal <referenceableParamGroupRef>},
     * {@literal <cvParam>} and {@literal <userParam>}.
     *
     * @param elements Collection of MzMLContent to add children to
     * @param fullXPath Full XPath to query
     * @param currentXPath Sub XPath that should start with the tag name for the
     * current MzMLContent
     * @throws InvalidXPathException thrown if the XPath can not be followed
     */
    protected void addTagSpecificElementsAtXPathToCollection(Collection<MzMLTag> elements, String fullXPath, String currentXPath) throws InvalidXPathException {
        // Default is no tag specific elements, so don't do anything.
    }

    @Override
    public void addElementsAtXPathToCollection(Collection<MzMLTag> elements, String xPath) throws InvalidXPathException {
        addElementsAtXPathToCollection(elements, xPath, xPath);
    }

    @Override
    public final void addElementsAtXPathToCollection(Collection<MzMLTag> elements, String fullXPath, String currentXPath) throws InvalidXPathException {
        if (currentXPath.startsWith("/" + getTagName())) {
            String subXPath = currentXPath.replaceFirst("/" + getTagName(), "");

            if (subXPath.isEmpty()) {
                elements.add(this);

                return;
            }

            addTagSpecificElementsAtXPathToCollection(elements, fullXPath, subXPath);

            if (elements.isEmpty()) {
                throw new InvalidXPathException("Invalid sub-XPath (" + subXPath + ") in XPath " + fullXPath, fullXPath);
            }
        } else {
            throw new InvalidXPathException("XPath does not start with /" + getTagName() + " in sub-XPath [" + currentXPath + "] of [" + fullXPath + "]", fullXPath);
        }
    }
    
    protected String getXMLAttributeText() {
        if(this instanceof ReferenceableTag)
            return "id=\"" + XMLHelper.ensureSafeXML(((ReferenceableTag)this).getID()) + "\"";
        
        return "";
    }
    
    @Override
    public void outputXML(MzMLWritable output, int indent) throws IOException {
        String attributeText = getXMLAttributeText();
        
        MzMLContent.indent(output, indent);
        output.write("<" + getTagName());
        
        if(attributeText != null && !attributeText.isEmpty())
            output.write(" " + attributeText);
        
        output.write("/>\n");
    }

    /**
     * Indent the output by the specified number of spaces (indent).
     *
     * @param output BufferedReader to output the indents to
     * @param indent Number of tabs to indent
     * @throws IOException Exception occurred during writing data
     */
    public static void indent(MzMLWritable output, int indent) throws IOException {
        for (int i = 0; i < indent; i++) {
            output.write("  ");
        }
    }

    @Override
    public void setParent(MzMLTag parent) {
        // This is a dummy function only included to allow the removal
    }
    
    @Override
    public String toString() {
        return getTagName();
    }
}
