package com.alanmrace.jimzmlparser.mzml;

import java.util.ArrayList;
import java.util.List;

public class IsolationWindow extends MzMLContentWithParams {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static String isolationWindowAttributeID = "MS:1000792"; // Required child (1+)

    public IsolationWindow() {
        super();
    }

    public IsolationWindow(IsolationWindow isolationWindow, ReferenceableParamGroupList rpgList) {
        super(isolationWindow, rpgList);
    }

    @Override
    public List<OBOTermInclusion> getListOfRequiredCVParams() {
        ArrayList<OBOTermInclusion> required = new ArrayList<OBOTermInclusion>();
        required.add(new OBOTermInclusion(isolationWindowAttributeID, false, true, false));

        return required;
    }

    @Override
    public String getTagName() {
        return "isolationWindow";
    }
}
