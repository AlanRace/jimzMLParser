package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.util.XMLHelper;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class Source extends Component {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static String ionisationTypeID = "MS:1000008";
    public static String sourceAttributeID = "MS:1000482";
    public static String inletTypeID = "MS:1000007";
    public static String sampleStageID = "IMS:1000002";

    public Source() {
    }

    public Source(Source source, ReferenceableParamGroupList rpgList) {
        super(source, rpgList);
    }

    @Override
    public ArrayList<OBOTermInclusion> getListOfRequiredCVParams() {
        ArrayList<OBOTermInclusion> required = new ArrayList<OBOTermInclusion>();
        required.add(new OBOTermInclusion(ionisationTypeID, true, true, true));

        return required;
    }

    @Override
    public ArrayList<OBOTermInclusion> getListOfOptionalCVParams() {
        ArrayList<OBOTermInclusion> optional = new ArrayList<OBOTermInclusion>();
        optional.add(new OBOTermInclusion(sourceAttributeID, false, true, false));
        optional.add(new OBOTermInclusion(inletTypeID, true, true, false));
        optional.add(new OBOTermInclusion(sampleStageID, false, true, false));

        return optional;
    }

    @Override
    public String getTagName() {
        return "source";
    }
}
