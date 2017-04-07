package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.util.XMLHelper;
import java.util.ArrayList;
import java.util.List;

public class ReferenceableParamGroup extends MzMLContentWithParams implements ReferenceableTag {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private static int idNumber = 0;

    private String id;	// Required

    public ReferenceableParamGroup() {
        id = "refParam" + idNumber++;
    }

    public ReferenceableParamGroup(ReferenceableParamGroup rpg) {
        super(rpg, null);

        id = rpg.id;
    }

    @Override
    public List<OBOTermInclusion> getListOfOptionalCVParams() {
        ArrayList<OBOTermInclusion> optional = new ArrayList<OBOTermInclusion>();
        optional.add(new OBOTermInclusion("IMS:0000000", false, true, true));
        optional.add(new OBOTermInclusion("MS:0000000", false, true, true));

        return optional;
    }

    public ReferenceableParamGroup(String id) {
        if(id == null)
            throw new IllegalArgumentException("ID cannot be null for ReferenceableParamGroup.");
        
        this.id = id;
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public void setID(String id) {
        this.id = id;
    }

    @Override
    protected String getXMLAttributeText() {
        return "id=\"" + XMLHelper.ensureSafeXML(this.getID()) + "\"";
    }

    @Override
    public String toString() {
        return "referenceableParamGroup: " + id;
    }

    @Override
    public String getTagName() {
        return "referenceableParamGroup";
    }
}
