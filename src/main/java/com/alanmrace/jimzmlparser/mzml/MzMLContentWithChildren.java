package com.alanmrace.jimzmlparser.mzml;

import java.io.IOException;
import java.util.ArrayList;
import com.alanmrace.jimzmlparser.writer.MzMLWritable;

/**
 * Abstract class implementing the basic functionality for a tag in MzML which has
 * child tags.
 *  
 * @author Alan Race
 */
public abstract class MzMLContentWithChildren extends MzMLContent implements HasChildren {

    /**
     * Format the all children of the MzMLContent into XML and writes to the 
     * MzMLWriter at the specified indent.
     * 
     * @param output MzMLWriter to output to
     * @param indent Current number of tabs to indent
     * @throws IOException Issue writing out XML
     */
    protected void outputXMLContent(MzMLWritable output, int indent) throws IOException {
        ArrayList<MzMLTag> children = new ArrayList<MzMLTag>();
        
        addChildrenToCollection(children);
        
        for(MzMLTag child : children) {
            child.outputXML(output, indent);
        }
    }
       
    @Override
    public void outputXML(MzMLWritable output, int indent) throws IOException {
        String attributeText = getXMLAttributeText();
        
        MzMLContent.indent(output, indent);
        output.writeMetadata("<" + getTagName());
        
        if(attributeText != null && !attributeText.isEmpty())
            output.writeMetadata(" " + attributeText);
        
        output.writeMetadata(">\n");

        outputXMLContent(output, indent + 1);

        MzMLContent.indent(output, indent);
        output.writeMetadata("</" + getTagName() + ">\n");
    }
}
