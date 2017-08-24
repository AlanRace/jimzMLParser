package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.obo.OBO;
import com.alanmrace.jimzmlparser.util.XMLHelper;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Software extends MzMLContentWithParams implements ReferenceableTag {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static String softwareID = "MS:1000531"; // Required child (1)
    
    public static final String customUnreleasedToolID = "MS:1000799";

    protected String id;			// Required
    protected String version;		// Required

    public Software(String id, String version) {
        this.id = id;
        this.version = version;
    }

    public Software(Software software, ReferenceableParamGroupList rpgList) {
        super(software, rpgList);

        this.id = software.id;
        this.version = software.version;
    }

    @Override
    public String getID() {
        return id;
    }

    public String getVersion() {
        return version;
    }

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

    public static Software create() {
        String version = "Unknown";
        
        // Get the version of the software from the properites file
        Properties prop = new Properties();
        InputStream in = SoftwareList.class.getResourceAsStream("/jimzMLParser.properties");
        
        try {
            prop.load(in);
            version = prop.getProperty("version");
            
            in.close();
        } catch (IOException ex) {
            Logger.getLogger(SoftwareList.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Software software = new Software("jimzMLParser", version);
        software.addCVParam(new EmptyCVParam(OBO.getOBO().getTerm(Software.customUnreleasedToolID)));
        
        return software;
    }
}
