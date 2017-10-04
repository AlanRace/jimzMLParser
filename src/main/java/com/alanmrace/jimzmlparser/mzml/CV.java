package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.util.XMLHelper;
import java.io.Serializable;

/**
 * CV tag for describing controlled vocabulary (OBO ontology).
 * 
 * @author Alan Race
 */
public class CV extends MzMLIDContent implements Serializable {

    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Default location for the mass spectrometry imaging ontology.
     */
    public static final String IMS_URI = "http://www.maldi-msi.org/download/imzml/imagingMS.obo";
    
    /**
     * Default location for the mass spectrometry ontology.
     */
    public static final String MS_URI = "http://purl.obolibrary.org/obo/ms.obo";
    
    /**
     * Default location for the units ontology.
     */
    public static final String UO_URI = "http://obo.cvs.sourceforge.net/viewvc/obo/obo/ontology/phenotype/unit.obo";

    /**
     * The URI for the ontology [Required].
     */
    private String uri;

    /**
     * The name for the ontology [Required].
     */
    private String fullName;


    /**
     * Version of the ontology [Optional].
     */
    private String version;

    /**
     * Create a CV tag from a URI, full name and an ID.
     * 
     * @param uri       URI of the ontology
     * @param fullName  Name of the ontology
     * @param id        Unique ID for the ontology
     */
    public CV(String uri, String fullName, String id) {
        this(uri, fullName, id, null);
    }

    /**
     * Create a CV tag from a URI, full name an ID and a version.
     * 
     * @param uri       URI of the ontology
     * @param fullName  Name of the ontology
     * @param id        Unique ID for the ontology
     * @param version   Version of the ontology
     */
    public CV(String uri, String fullName, String id, String version) {
        this.uri = uri;
        this.fullName = fullName;
        this.id = id;
        this.version = version;
    }

    /**
     * Get the URI of the ontology.
     * 
     * @return URI of the ontology
     */
    public String getURI() {
        return uri;
    }

    /**
     * Get the name of the ontology.
     * 
     * @return Ontology name
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Set the version of the ontology used.
     * 
     * @param version Version used
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Get the version of the ontology used.
     * 
     * @return Version used
     */
    public String getVersion() {
        return version;
    }

    @Override
    public String getXMLAttributeText() {
        String attributeText = "URI=\"" + XMLHelper.ensureSafeXML(uri) + "\"";
        attributeText += " fullName=\"" + XMLHelper.ensureSafeXML(fullName) + "\"";
        attributeText += " id=\"" + XMLHelper.ensureSafeXML(id) + "\"";
        
        if (version != null) {
            attributeText += " version=\"" + XMLHelper.ensureSafeXML(version) + "\"";
        }
        
        return attributeText;
    }

    @Override
    public String toString() {
        return "cv : URI=\"" + uri + "\" fullName=\"" + fullName + "\" id=\"" + id + "\"" + ((version != null) ? " version=\"" + version + "\"" : "");
    }

    @Override
    public String getTagName() {
        return "cv";
    }
}
