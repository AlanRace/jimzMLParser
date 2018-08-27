package com.alanmrace.jimzmlparser.mzml;

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

    @Override
    public String toString() {
        String name = getCVParam(CONTACT_NAME_ID).getValueAsString();
        String organisation = getCVParam(CONTACT_ORGANISATION_ID).getValueAsString();

        return "contact: " + name + " (" + organisation + ")";
    }

    @Override
    public String getTagName() {
        return "contact";
    }
}
