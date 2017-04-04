package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.exceptions.InvalidXPathException;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Base class for all mzML tags. This includes default handling for inclusion of 
 * lists of {@link ReferenceableParamGroupRef}, {@link CVParam} and {@link UserParam}.
 * 
 * <p>TODO: Consider refactoring so that inclusion of CVParams is only possible on tags
 * that are allowed to have CVParams. This could be done using an interface.
 *
 * @author Alan Race
 */
public abstract class MzMLContent implements Serializable, MzMLTag {

    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = 1L;

    

    /**
     * Empty constructor.
     * 
     * <p>TODO: Remove this?
     */
    public MzMLContent() {
    }    

    //public abstract MzMLContent getElementAtXPath(String xPath) throws InvalidXPathException;

//    public Collection<MzMLContent> getElementsAtXPath(String fullXPath, String currentXPath) throws InvalidXPathException {
//        ArrayList<MzMLContent> elements = new ArrayList<MzMLContent>();
//
//        if (currentXPath.equals("/" + getTagName())) {
//            elements.add(this);
//
//            return elements;
//        } else if(currentXPath.startsWith("/" + getTagName() + "/cvParam")) {
////            for(CVParam cvParam : cvParams) {
////                elements.add(cvParam.)
////            }
//        }
//
//        throw new InvalidXPathException("Not implemented sub-XPath (" + currentXPath + ") as part of " + fullXPath);
//    }

    /**
     * Add all child MzMLContent (mzML tags) that match the specified XPath, 
     * which are specific to this tag (i.e. child tags which are not 
     * {@literal <referenceableParamGroupRef>}, {@literal <cvParam>} and 
     * {@literal <userParam>}), to the specified collection.
     * 
     * <p>This method should be overridden in subclasses (mzML tags) which have specific
     * children (i.e. child tags in addition to {@literal <referenceableParamGroupRef>}, 
     * {@literal <cvParam>} and {@literal <userParam>}.
     * 
     * @param elements Collection of MzMLContent to add children to
     * @param fullXPath Full XPath to query
     * @param currentXPath Sub XPath that should start with the tag name for the current MzMLContent
     * @throws InvalidXPathException thrown if the XPath can not be followed
     */
    protected void addTagSpecificElementsAtXPathToCollection(Collection<MzMLContent> elements, String fullXPath, String currentXPath) throws InvalidXPathException {

    }

    /**
     * Add all child MzMLContent (mzML tags) that match the specified XPath to 
     * the specified collection.
     * 
     * @param elements      Collection of MzMLContent to add children to
     * @param xPath         Full XPath to query
     * @throws InvalidXPathException thrown if the XPath can not be followed
     */
    public void addElementsAtXPathToCollection(Collection<MzMLContent> elements, String xPath) throws InvalidXPathException {
        addElementsAtXPathToCollection(elements, xPath, xPath);
    }
    
    /**
     * Add all child MzMLContent (mzML tags) that match the specified XPath to 
     * the specified collection.
     * 
     * @param elements      Collection of MzMLContent to add children to
     * @param fullXPath     Full XPath to query
     * @param currentXPath  Sub XPath that should start with the tag name for the current MzMLContent
     * @throws InvalidXPathException thrown if the XPath can not be followed
     */
    public final void addElementsAtXPathToCollection(Collection<MzMLContent> elements, String fullXPath, String currentXPath) throws InvalidXPathException {
        if (currentXPath.startsWith("/" + getTagName())) {
            currentXPath = currentXPath.replaceFirst("/" + getTagName(), "");

            if (currentXPath.isEmpty()) {
                elements.add(this);

                return;
            }

            addTagSpecificElementsAtXPathToCollection(elements, fullXPath, currentXPath);

            if (elements.isEmpty()) {
                throw new InvalidXPathException("Invalid sub-XPath (" + currentXPath + ") in XPath " + fullXPath, fullXPath);
            }
        } else {
            throw new InvalidXPathException("XPath does not start with /" + getTagName() + " in sub-XPath [" + currentXPath + "] of [" + fullXPath + "]", fullXPath);
        }
    }

    

    /**
     * Indent the output by the specified number of spaces (indent).
     * 
     * @param output BufferedReader to output the indents to
     * @param indent Number of tabs to indent
     * @throws IOException Exception occurred during writing data
     */
    public static void indent(BufferedWriter output, int indent) throws IOException {
        for (int i = 0; i < indent; i++) {
            output.write("  ");
        }
    }

    @Override
    public void setParent(MzMLTag parent) {
        // This is a dummy function only included to allow the removal
    }
}
