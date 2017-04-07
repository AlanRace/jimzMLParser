package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.util.XMLHelper;
import java.util.ArrayList;
import java.util.List;

public class SourceFile extends MzMLContentWithParams implements ReferenceableTag {

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
    public List<OBOTermInclusion> getListOfRequiredCVParams() {
        ArrayList<OBOTermInclusion> required = new ArrayList<OBOTermInclusion>();
        required.add(new OBOTermInclusion(nativeSpectrumIdentifierFormat, true, true, false));
        required.add(new OBOTermInclusion(dataFileChecksumType, false, true, false));
        required.add(new OBOTermInclusion(massSpectrometerFileFormat, true, true, false));

        return required;
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
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
    protected String getXMLAttributeText() {
        String attributeText = "id=\"" + XMLHelper.ensureSafeXML(id) + "\"";
        attributeText += " location=\"" + XMLHelper.ensureSafeXML(location) + "\"";
        attributeText += " name=\"" + XMLHelper.ensureSafeXML(name) + "\"";
        
        return attributeText;
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
