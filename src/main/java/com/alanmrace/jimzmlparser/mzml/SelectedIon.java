package com.alanmrace.jimzmlparser.mzml;

import java.io.Serializable;
import java.util.ArrayList;

public class SelectedIon extends MzMLContentWithParams implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static String ionSelectionAttributeID = "MS:1000455"; // Required child (1+)

    public SelectedIon() {
        super();
    }

    public SelectedIon(SelectedIon si, ReferenceableParamGroupList rpgList) {
        super(si, rpgList);
    }

    @Override
    public ArrayList<OBOTermInclusion> getListOfRequiredCVParams() {
        ArrayList<OBOTermInclusion> required = new ArrayList<OBOTermInclusion>();
        required.add(new OBOTermInclusion(ionSelectionAttributeID, false, true, false));

        return required;
    }

    @Override
    public String getTagName() {
        return "selectedIon";
    }

}
