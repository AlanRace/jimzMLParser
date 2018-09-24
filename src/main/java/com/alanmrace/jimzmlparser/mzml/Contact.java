package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.obo.OBO;
import java.io.Serializable;

/**
 * Class describing a person of contact responsible for the mzML file.
 * 
 * @author Alan Race
 */
public class Contact extends MzMLContentWithParams implements Serializable {

    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Accession: Contact organisation (MS:1000590) [Required].
     */
    public static final String CONTACT_ORGANISATION_ID = "MS:1000590"; // Required (1)

    /**
     * Accession: Contact name (MS:1000586) [Required].
     */
    public static final String CONTACT_NAME_ID = "MS:1000586"; // Required (1)

    /**
     * Accession: Contact person attribute (MS:1000585) [Optional].
     */
    public static final String CONTACT_PERSON_ATTRIBUTE_ID = "MS:1000585"; // Optional child (1+)

    /**
     * Default constructor.
     */
    public Contact() {
        super();
    }

    /**
     * Copy constructor.
     * 
     * @param contact Contact to copy
     * @param rpgList New ReferenceableParamGroupList to match references to
     */
    public Contact(Contact contact, ReferenceableParamGroupList rpgList) {
        super(contact, rpgList);
    }

    /**
     * Returns the name of the contact.
     *
     * @return Contact's name.
     */
    public String getName() {
        return getCVParam(CONTACT_NAME_ID).getValueAsString();
    }

    /**
     * Returns the name of the organisation that the contact belongs to.
     *
     * @return Contact's organisation.
     */
    public String getOrganisation() {
        return getCVParam(CONTACT_ORGANISATION_ID).getValueAsString();
    }

    /**
     * Set the name of the contact. A new CV param will be added if the contact name is not already present, or will be
     * updated if so.
     *
     * @param name Name of the contact.
     */
    public void setName(String name) {
        CVParam param = getCVParam(CONTACT_NAME_ID);
        
        if(param != null)
            param.setValueAsString(name);
        else
            addCVParam(new StringCVParam(OBO.getOBO().getTerm(CONTACT_NAME_ID), name));
    }

    /**
     * Set the organisation of the contact. A new CV param will be added if the contact organisation is not already
     * present, or will be updated if so.
     *
     * @param organisation Organisation of the contact.
     */
    public void setOrganisation(String organisation) {
        CVParam param = getCVParam(CONTACT_ORGANISATION_ID);
        
        if(param != null)
            param.setValueAsString(organisation);
        else
            addCVParam(new StringCVParam(OBO.getOBO().getTerm(CONTACT_ORGANISATION_ID), organisation));
    }
    
    @Override
    public String toString() {
        return "contact: " + getName() + " (" + getOrganisation() + ")";
    }

    @Override
    public String getTagName() {
        return "contact";
    }
}
