package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.util.XMLHelper;
import java.util.ArrayList;

public class ProcessingMethod extends MzMLOrderedContentWithParams {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static String dataTransformationID = "MS:1000452"; // Required child (1+)
    public static String dataProcessingParameterID = "MS:1000630"; // Optional child (1+)

    public static String fileFormatConversionID = "MS:1000530";
    
    public static String conversionTomzMLID = "MS:1000544";

    private Software softwareRef;

    public ProcessingMethod(Software softwareRef) {
        this.softwareRef = softwareRef;
    }

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

    public void setSoftwareRef(Software softwareRef) {
        this.softwareRef = softwareRef;
    }

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
