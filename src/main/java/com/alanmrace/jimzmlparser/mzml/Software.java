package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.util.XMLHelper;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Software extends MzMLContentWithParams implements ReferenceableTag {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static String softwareID = "MS:1000531"; // Required child (1)

    protected static int idNumber = 0;

    protected String id;			// Required
//	protected OBOTerm software;
    protected String version;		// Required

//	public Software(OBOTerm software, String version) {
//		id = "software" + idNumber++;
//		
//		this.software = software;
//		this.version = version;
//	}
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
    public void outputXML(BufferedWriter output, int indent) throws IOException {
        MzMLContent.indent(output, indent);
        output.write("<software");
        output.write(" id=\"" + XMLHelper.ensureSafeXML(id) + "\"");
        output.write(" version=\"" + XMLHelper.ensureSafeXML(version) + "\"");
        output.write(">\n");

        super.outputXML(output, indent + 1);

        MzMLContent.indent(output, indent);
        output.write("</software>\n");
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
