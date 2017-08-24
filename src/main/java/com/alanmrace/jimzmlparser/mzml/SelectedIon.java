package com.alanmrace.jimzmlparser.mzml;

import java.io.Serializable;

/**
 * Class describing a selected ion.
 * 
 * @author Alan Race
 */
public class SelectedIon extends MzMLContentWithParams implements Serializable {

    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Accession: Ion selection attribute (MS:1000455). MUST supply a child
     */
    public static String ionSelectionAttributeID = "MS:1000455"; // Required child (1+)

    /**
     * Create empty SelectedIon.
     */
    public SelectedIon() {
        super();
    }

    /**
     * Copy constructor.
     *
     * @param selectedIon Old SelectedIon to copy
     * @param rpgList New ReferenceableParamGroupList to match references to
     */
    public SelectedIon(SelectedIon selectedIon, ReferenceableParamGroupList rpgList) {
        super(selectedIon, rpgList);
    }

    @Override
    public String getTagName() {
        return "selectedIon";
    }

}
