package com.alanmrace.jimzmlparser.mzml;

/**
 * MzMLContent which can be indexed within the {@literal <indexList>} in the indexed
 * extension to mzML.
 * 
 * @author Alan Race
 */
public abstract class MzMLIndexedContentWithParams extends MzMLOrderedContentWithParams implements ReferenceableTag {
    
    /**
     * Unique identifier for the MzML tag.
     */
    protected String id;

    /**
     * The file pointer within the mzML file of the start point for this specific 
     * MzML tag.
     */
    protected long mzMLLocation;
    
    /**
     * Set up default constructor, does nothing.
     */
    protected MzMLIndexedContentWithParams() {
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
    public MzMLIndexedContentWithParams(MzMLIndexedContentWithParams mzMLContent, ReferenceableParamGroupList rpgList) {
        super(mzMLContent, rpgList);
    }
    
    @Override
    public String getID() {
        return id;
    }
    
    @Override
    public void setID(String id) {
        this.id = id;
    }
    
    /**
     * Set the location of this mzML tag within the mzML file.
     * 
     * @param mzMLLocation File pointer location within mzML file
     */
    protected void setmzMLLocation(long mzMLLocation) {
        this.mzMLLocation = mzMLLocation;
    }
    
    /**
     * Returns the location of this mzML tag within the mzML file.
     * 
     * @return File pointer location within mzML file
     */
    public long getmzMLLocation() {
        return mzMLLocation;
    }
        
    /**
     * Output attribute in the form of XML.
     *
     * @param output where to write the XML
     * @param indent how large an indent is needed for this tag
     * @param index index of the content
     * @throws IOException Signals that an I/O exception has occurred.
     */
//    @Override
//    public void outputXML(MzMLWritable output, int indent, int index) throws IOException {
//        String attributeText = getXMLAttributeText();
//        
//        MzMLContent.indent(output, indent);
//        output.writeMetadata("<" + getTagName() + " index=\"" + index + "\"");
//        
//        if(attributeText != null && !attributeText.isEmpty())
//            output.writeMetadata(" " + attributeText);
//        
//        output.writeMetadata(">\n");
//
//        outputXMLContent(output, indent + 1);
//
//        MzMLContent.indent(output, indent);
//        output.writeMetadata("</" + getTagName() + ">\n");
//    }
    
}
