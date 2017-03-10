package com.alanmrace.jimzmlparser.mzML;

import com.alanmrace.jimzmlparser.util.XMLHelper;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class SourceFile extends MzMLContent implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static String nativeSpectrumIdentifierFormat = "MS:1000767";	// Required child (1)
    public static String dataFileChecksumType = "MS:1000561";	// Required child (1+)
    public static String massSpectrometerFileFormat = "MS:1000560"; // Required child (1)

    public static String mzMLFileFormat = "MS:1000584";

    public static String sha1FileChecksumType = "MS:1000569";

    private String id;			// Required
    private String location;	// Required
    private String name;		// Required

    public SourceFile(String id, String location, String name) {
        this.id = id;
        this.location = location;
        this.name = name;
    }

    public SourceFile(SourceFile sourceFile, ReferenceableParamGroupList rpgList) {
        super(sourceFile, rpgList);

        this.id = sourceFile.id;
        this.location = sourceFile.location;
        this.name = sourceFile.name;
    }

    @Override
    public ArrayList<OBOTermInclusion> getListOfRequiredCVParams() {
        ArrayList<OBOTermInclusion> required = new ArrayList<OBOTermInclusion>();
        required.add(new OBOTermInclusion(nativeSpectrumIdentifierFormat, true, true, false));
        required.add(new OBOTermInclusion(dataFileChecksumType, false, true, false));
        required.add(new OBOTermInclusion(massSpectrometerFileFormat, true, true, false));

        return required;
    }

    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    @Override
    public void outputXML(BufferedWriter output, int indent) throws IOException {
        MzMLContent.indent(output, indent);
        output.write("<sourceFile");
        output.write(" id=\"" + XMLHelper.ensureSafeXML(id) + "\"");
        output.write(" location=\"" + XMLHelper.ensureSafeXML(location) + "\"");
        output.write(" name=\"" + XMLHelper.ensureSafeXML(name) + "\"");
        output.write(">\n");

        super.outputXML(output, indent + 1);

        MzMLContent.indent(output, indent);
        output.write("</sourceFile>\n");
    }

    @Override
    public String toString() {
        return "sourceFile: id=\"" + id + "\" location=\"" + location + "\" name=\"" + name + "\"";
    }

    @Override
    public String getTagName() {
        return "sourceFile";
    }

}
