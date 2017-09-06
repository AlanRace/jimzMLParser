package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.util.XMLHelper;

/**
 * Class describing an individual processing method applied to some binary data
 * stored within the MzML/ImzML.
 *
 * @author alan.race
 */
public class ProcessingMethod extends MzMLOrderedContentWithParams {

    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Accession: Data transformation (MS:1000452).
     */
    public static final String dataTransformationID = "MS:1000452"; // Required child (1+)
    /**
     * Accession: Data processing parameter (MS:1000630).
     */
    public static final String dataProcessingParameterID = "MS:1000630"; // Optional child (1+)

    /**
     * Accession: File format conversion (MS:1000530).
     */
    public static final String fileFormatConversionID = "MS:1000530";
    
    /**
     * Accession: Conversion to mzML (MS:1000544).
     */
    public static final String conversionTomzMLID = "MS:1000544";

    /**
     * Software which performed the processing method.
     */
    private Software softwareRef;

    /**
     * Create a description of a processing method performed by the specified software.
     * 
     * @param softwareRef Software that performed the processing method
     */
    public ProcessingMethod(Software softwareRef) {
        this.softwareRef = softwareRef;
    }

    /**
     * Copy constructor.
     *
     * @param pm Old ProcessingMethod to copy
     * @param rpgList New ReferenceableParamGroupList to match references to
     * @param softwareList New SoftwareList to match references to
     */
    public ProcessingMethod(ProcessingMethod pm, ReferenceableParamGroupList rpgList, SoftwareList softwareList) {
        super(pm, rpgList);

        if (pm.softwareRef != null && softwareList != null) {
            for (Software software : softwareList) {
                if (pm.softwareRef.getID().equals(software.getID())) {
                    softwareRef = software;

                    break;
                }
            }
        }
    }

    /**
     * Set the software that performed the processing method described within this class.
     *  
     * @param softwareRef Software that performed the processing method
     */
    public void setSoftwareRef(Software softwareRef) {
        this.softwareRef = softwareRef;
    }

    /**
     * Returns the software that performed this processing method.
     * 
     * @return Software that performed this processing method
     */
    public Software getSoftwareRef() {
        return softwareRef;
    }
    
    @Override
    public String getXMLAttributeText() {
        return "softwareRef=\"" + XMLHelper.ensureSafeXML(softwareRef.getID()) + "\"";
    }
    
    @Override
    public String toString() {
        return "processingMethod: softwareRef=\"" + softwareRef.getID() + "\"";
    }

    @Override
    public String getTagName() {
        return "processingMethod";
    }
}
