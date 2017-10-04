package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.util.XMLHelper;

/**
 * Class describing a file used to generate this ImzML/MzML.
 * 
 * @author alan.race
 */
public class SourceFile extends MzMLContentWithParams implements ReferenceableTag {

    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Accession: Native spectrum identifier format (MS:1000767).
     */
    public static String nativeSpectrumIdentifierFormatID = "MS:1000767";	// Required child (1)
    
    /**
     * Accession: Data file checksum type (MS:1000561).
     */
    public static String dataFileChecksumTypeID = "MS:1000561";	// Required child (1+)
    /**
     * Accession: Mass spectrometer file format (MS:1000560).
     */
    public static String massSpectrometerFileFormatID = "MS:1000560"; // Required child (1)

    /**
     * Accession: mzML format (MS:1000584).
     */
    public static String mzMLFileFormatID = "MS:1000584";

    /**
     * Accession: SHA-1 file checksum (MS:1000569).
     */
    public static String sha1FileChecksumID = "MS:1000569";

    /**
     * Unique identifier for the source file.
     */
    private String id;			// Required
    
    /**
     * Location of the source file.
     */
    private String location;	// Required
    
    /**
     * Name of the source file.
     */
    private String name;		// Required

    /**
     * Create source file with specified unique id, location and file name.
     * 
     * @param id Unique identifier for the source file
     * @param location Location of the source file
     * @param name File name of the source file
     */
    public SourceFile(String id, String location, String name) {
        this.id = id;
        this.location = location;
        this.name = name;
    }

    /**
     * Copy constructor.
     *
     * @param sourceFile Old SourceFile to copy
     * @param rpgList New ReferenceableParamGroupList to match references to
     */
    public SourceFile(SourceFile sourceFile, ReferenceableParamGroupList rpgList) {
        super(sourceFile, rpgList);

        this.id = sourceFile.id;
        this.location = sourceFile.location;
        this.name = sourceFile.name;
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
     * Returns the location of the source file.
     * 
     * @return Source file location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Returns the name of the source file.
     * 
     * @return Name of the source file
     */
    public String getName() {
        return name;
    }

    @Override
    public String getXMLAttributeText() {
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
