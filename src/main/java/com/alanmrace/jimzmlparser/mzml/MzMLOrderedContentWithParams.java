/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alanmrace.jimzmlparser.mzml;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author Alan Race
 */
public abstract class MzMLOrderedContentWithParams extends MzMLContentWithParams {
    
    protected MzMLOrderedContentWithParams() {
    }
    
    /**
     * Copy constructor, with new ReferenceableParamGroupList.
     * 
     * <p>ReferenceableParamGroupRefs are created to match the old MzMLContent instance
     * but an attempt is made to link this to the new ReferenceableParamGroupList based
     * on the ID of each ReferenceableParamGroup.
     * 
     * <p>CVParams are copied (new instances created) using the appropriate copy constructor
     * based on the subclass of CVParam.
     * 
     * <p>UserParams are copied over using UserParam copy constructor.
     * 
     * @param mzMLContent Old MzMLContent to copy
     * @param rpgList New ReferenceableParamGroupList to link new references to
     * @see ReferenceableParamGroupRef
     * @see ReferenceableParamGroup
     * @see CVParam
     * @see UserParam
     */
    public MzMLOrderedContentWithParams(MzMLOrderedContentWithParams mzMLContent, ReferenceableParamGroupList rpgList) {
        super(mzMLContent, rpgList);
    }
    
    /**
     * Output attribute in the form of XML.
     *
     * @param raf   RandomAccessFile being output to - used for getting current file location
     * @param output where to write the XML
     * @param indent how large an indent is needed for this tag
     * @param order order of the component within the list
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void outputXML(RandomAccessFile raf, BufferedWriter output, int indent, int order) throws IOException {
        String attributeText = getXMLAttributeText();
        
        MzMLContent.indent(output, indent);
        output.write("<" + getTagName() + " order=\"" + order + "\"");
        
        if(attributeText != null && !attributeText.isEmpty())
            output.write(" " + attributeText);
        
        output.write(">\n");

        outputXMLContent(raf, output, indent + 1);

        MzMLContent.indent(output, indent);
        output.write("</" + getTagName() + ">\n");
    }
}
