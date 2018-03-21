package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.obo.OBO;
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

    private OBO ontology;

    /**
     * Create a CV tag from a URI, full name and an ID.
     * 
     * @param ontology
     */
    public CV(OBO ontology) {
        this.ontology = ontology;
        
        id = ontology.getOntology().toUpperCase();
    }

    /**
     * Get the URI of the ontology.
     * 
     * @return URI of the ontology
     */
    public String getURI() {
        return ontology.getPath();
    }

    /**
     * Get the name of the ontology.
     * 
     * @return Ontology name
     */
    public String getFullName() {
        return OBO.getNameFromID(id);
    }

    /**
     * Get the version of the ontology used.
     * 
     * @return Version used
     */
    public String getVersion() {
        return ontology.getDataVersion();
    }

    @Override
    public String getXMLAttributeText() {
        String attributeText = "URI=\"" + XMLHelper.ensureSafeXML(getURI()) + "\"";
        attributeText += " fullName=\"" + XMLHelper.ensureSafeXML(getFullName()) + "\"";
        attributeText += " id=\"" + XMLHelper.ensureSafeXML(id) + "\"";
        
        if (getVersion() != null) {
            attributeText += " version=\"" + XMLHelper.ensureSafeXML(getVersion()) + "\"";
        }
        
        return attributeText;
    }

    @Override
    public String toString() {
        return "cv : URI=\"" + getURI() + "\" fullName=\"" + getFullName() + "\" id=\"" + id + "\"" + ((getVersion() != null) ? " version=\"" + getVersion() + "\"" : "");
    }

    @Override
    public String getTagName() {
        return "cv";
    }
}
