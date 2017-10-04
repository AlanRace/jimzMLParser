package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.obo.OBO;
import com.alanmrace.jimzmlparser.util.XMLHelper;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class describing a {@literal <software>} tag.
 *
 * @author Alan Race
 */
public class Software extends MzMLContentWithParams implements ReferenceableTag {

    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Accession: software (MS:1000531).
     */
    public static String softwareID = "MS:1000531"; // Required child (1)
    
    /**
     * Accession: custom unreleased software tool (MS:1000799).
     */
    public static final String customUnreleasedToolID = "MS:1000799";

    /**
     * Unique identifier for the software.
     */
    protected String id;		// Required

    /**
     * Software version number.
     */
    protected String version;		// Required

    /**
     * Create Software with required attributes from XML tag.
     * 
     * @param id Unique identifier for the software
     * @param version Version number for the software
     */
    public Software(String id, String version) {
        this.id = id;
        this.version = version;
    }

    /**
     * Copy constructor, requiring new versions of lists to match old references
     * to.
     * 
     * @param software Software to copy
     * @param rpgList New ReferenceableParamGroupList
     */
    public Software(Software software, ReferenceableParamGroupList rpgList) {
        super(software, rpgList);

        this.id = software.id;
        this.version = software.version;
    }

    @Override
    public String getID() {
        return id;
    }

    /**
     * Returns the version number of the software.
     * 
     * @return Software version number
     */
    public String getVersion() {
        return version;
    }

    /**
     * Set the version number of the software.
     * 
     * @param version Version number of the software
     */
    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "software: " + id + " " + version;
    }
    
    @Override
    public String getXMLAttributeText() {
        return "id=\"" + XMLHelper.ensureSafeXML(id) + "\"" +
            " version=\"" + XMLHelper.ensureSafeXML(version) + "\"";
    }

    @Override
    public String getTagName() {
        return "software";
    }

    @Override
    public void setID(String id) {
        this.id = id;
    }

    /**
     * Create default software, describing jimzMLParser with current version as 
     * present within the resource /jimzMLParser.properties.
     *  
     * @return Default Software instance
     */
    public static Software create() {
        String version = "Unknown";
        
        // Get the version of the software from the properites file
        Properties prop = new Properties();
        InputStream in = SoftwareList.class.getResourceAsStream("/jimzMLParser.properties");
        
        try {
            prop.load(in);
            version = prop.getProperty("version");
        } catch (IOException ex) {
            Logger.getLogger(SoftwareList.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                Logger.getLogger(SoftwareList.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Software software = new Software("jimzMLParser", version);
        software.addCVParam(new EmptyCVParam(OBO.getOBO().getTerm(Software.customUnreleasedToolID)));
        
        return software;
    }
}
