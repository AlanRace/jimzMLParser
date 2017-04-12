package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.obo.OBO;
import java.util.ArrayList;
import java.util.List;

public class FileContent extends MzMLContentWithParams {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static final String dataFileContentID = "MS:1000524";		// Required child (1+)
    public static final String spectrumRepresentationID = "MS:1000525";		// Optional child (1)

    public static final String massSpectrumID = "MS:1000294";
    
    public static final String binaryTypeID = "IMS:1000003";
    public static final String binaryTypeContinuousID = "IMS:1000030";
    public static final String binaryTypeProcessedID = "IMS:1000031";

    public static final String ibdIdentificationID = "IMS:1000008";
    public static final String uuidIdntificationID = "IMS:1000080";

    public static final String ibdChecksumID = "IMS:1000009";
    public static final String md5ChecksumID = "IMS:1000090";
    public static final String sha1ChecksumID = "IMS:1000091";

    public static final String ibdFileID = "IMS:1000007";

    public FileContent() {
        super();
    }

    public FileContent(FileContent fileContent, ReferenceableParamGroupList rpgList) {
        super(fileContent, rpgList);
    }

    @Override
    public ArrayList<OBOTermInclusion> getListOfRequiredCVParams() {
        ArrayList<OBOTermInclusion> required = new ArrayList<OBOTermInclusion>();
        required.add(new OBOTermInclusion(dataFileContentID, false, true, false));
        required.add(new OBOTermInclusion(binaryTypeID, true, true, false));
        required.add(new OBOTermInclusion(ibdChecksumID, true, true, false));
        required.add(new OBOTermInclusion(ibdIdentificationID, true, true, false));

        return required;
    }

    @Override
    public ArrayList<OBOTermInclusion> getListOfOptionalCVParams() {
        ArrayList<OBOTermInclusion> optional = new ArrayList<OBOTermInclusion>();
        optional.add(new OBOTermInclusion(spectrumRepresentationID, true, true, false));
        optional.add(new OBOTermInclusion(ibdFileID, true, true, false));

        return optional;
    }

    public void addDataFileContent(CVParam dataFileContent) {
        this.addCVParam(dataFileContent);
    }

    public List<CVParam> getDataFileContents() {
        return getChildrenOf(dataFileContentID);
    }

    @Override
    public String getTagName() {
        return "fileContent";
    }
    
    public static FileContent create() {
        FileContent fc = new FileContent();
        
        fc.addCVParam(new EmptyCVParam(OBO.getOBO().getTerm(massSpectrumID)));
        
        return fc;
    }
}
