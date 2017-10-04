package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.util.XMLHelper;

/**
 * Class describing a sample analysed.
 * 
 * @author Alan Race
 */
public class Sample extends MzMLContentWithParams implements ReferenceableTag {

    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Static integer used to ensure unique IDs are generated if one is not supplied.
     */
    private static int idNumber = 0;

    /**
     * Unique identifier for the sample [Required].
     */
    private String id;		// Required

    /**
     * Name for the sample [Optional].
     */
    private String name;	// Optional

    /**
     * Create an empty Sample with a unique ID of the form 
     * 'sample#' where # is an increasing integer value each time this 
     * constructor is called.
     */
    public Sample() {
        this("sample" + idNumber++);
    }

    /**
     * Create an empty Sample with specified unique ID.
     * 
     * @param id Unique identifier.
     */
    public Sample(String id) {
        this(id, null);
    }

    /**
     * Create an empty Sample with specified unique ID and name.
     * 
     * @param id Unique identifier.
     * @param name Name of the sample
     */
    public Sample(String id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Copy constructor.
     * 
     * @param sample Old Sample to copy
     * @param rpgList New ReferenceableParamGroupList to match references to
     */
    public Sample(Sample sample, ReferenceableParamGroupList rpgList) {
        super(sample, rpgList);

        this.id = sample.id;
        this.name = sample.name;
    }
    
    /**
     * Set the name of the sample.
     * 
     * @param name New name of the sample
     */
    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public String getID() {
        return id;
    }

    @Override
    public String getXMLAttributeText() {
        String attributeText = "id=\"" + XMLHelper.ensureSafeXML(id) + "\"";
        
        if (name != null) {
            attributeText += " name=\"" + XMLHelper.ensureSafeXML(name) + "\"";
        }
        
        return attributeText;
    }

    @Override
    public String toString() {
        return "sample: id=\"" + id + "\""
                + ((name != null && !name.isEmpty()) ? " name=\"" + name + "\"" : "");
    }

    @Override
    public String getTagName() {
        return "sample";
    }

    @Override
    public void setID(String id) {
        this.id = id;
    }
}
