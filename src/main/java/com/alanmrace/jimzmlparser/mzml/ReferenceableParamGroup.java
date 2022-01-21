package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.util.XMLHelper;

/**
 * Class to describe a group of CV parameters. These groups can be referenced 
 * throughout the MzML document and so provide a more efficient way to store 
 * repeated parameters (such as those describing how data is stored).
 * 
 * @author Alan Race
 */
public class ReferenceableParamGroup extends MzMLContentWithParams implements ReferenceableTag {

    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Static integer used to ensure unique IDs are generated if one is not supplied.
     */
    private static int idNumber = 0;

    /**
     * Unique identifier for the parameter group.
     */
    private String id;	// Required

    /**
     * Create an empty ReferanceableParamGroup with a unique ID of the form 
     * 'refParam#' where # is an increasing integer value each time this 
     * constructor is called.
     */
    public ReferenceableParamGroup() {
        id = "refParam" + idNumber++;
    }

    /**
     * Create an empty ReferanceableParamGroup with specified unique ID.
     * 
     * @param id Unique identifier.
     */
    public ReferenceableParamGroup(String id) {
        if(id == null)
            throw new IllegalArgumentException("ID cannot be null for ReferenceableParamGroup.");
        
        this.id = id;
    }
    
    /**
     * Copy constructor.
     * 
     * @param rpg Old ReferenceableParamGroup to copy
     */
    public ReferenceableParamGroup(ReferenceableParamGroup rpg) {
        super(rpg, null);

        id = rpg.id;
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public void setID(String id) {
        this.id = id;
    }

    @Override
    public String getXMLAttributeText() {
        return "id=\"" + XMLHelper.ensureSafeXML(this.getID()) + "\"";
    }

    @Override
    public String toString() {
        return "referenceableParamGroup: " + id;
    }

    @Override
    public String getTagName() {
        return "referenceableParamGroup";
    }
}
