package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.util.XMLHelper;
import java.util.ArrayList;

public class Software extends MzMLContentWithParams implements ReferenceableTag {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static String softwareID = "MS:1000531"; // Required child (1)

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
    public ArrayList<OBOTermInclusion> getListOfRequiredCVParams() {
        ArrayList<OBOTermInclusion> required = new ArrayList<OBOTermInclusion>();
        required.add(new OBOTermInclusion(softwareID, true, true, false));

        return required;
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
    protected String getXMLAttributeText() {
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

}
