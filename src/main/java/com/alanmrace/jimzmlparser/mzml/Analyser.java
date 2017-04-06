package com.alanmrace.jimzmlparser.mzml;

import java.util.ArrayList;

/**
 * Analyser tag.
 *
 * @author Alan Race
 */
public class Analyser extends Component {

    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Accession: Mass analyser type (MS:1000443). MUST be supplied (or any
     * children) only once.
     */
    public static String analyserTypeID = "MS:1000443";

    /**
     * Accession: Mass analyser attribute (MS:1000480). MAY supply a child one
     * or more times.
     */
    public static String analyserAttributeID = "MS:1000480";

    /**
     * Instantiates a new analyser attribute.
     */
    public Analyser() {
    }

    /**
     * Copy constructor, requiring new versions of lists to match old references to.
     * 
     * @param analyser Old Analyser to copy
     * @param rpgList New ReferenceableParamGroupList
     */
    public Analyser(Analyser analyser, ReferenceableParamGroupList rpgList) {
        super(analyser, rpgList);
    }

    @Override
    public ArrayList<OBOTermInclusion> getListOfRequiredCVParams() {
        ArrayList<OBOTermInclusion> required = new ArrayList<OBOTermInclusion>();
        required.add(new OBOTermInclusion(analyserTypeID, true, true, true));

        return required;
    }

    @Override
    public ArrayList<OBOTermInclusion> getListOfOptionalCVParams() {
        ArrayList<OBOTermInclusion> optional = new ArrayList<OBOTermInclusion>();
        optional.add(new OBOTermInclusion(analyserAttributeID, false, true, false));

        return optional;
    }

    @Override
    public String toString() {
        return "analyser";
    }

    @Override
    public String getTagName() {
        return "analyzer";
    }
}
