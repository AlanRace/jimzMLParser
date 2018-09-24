package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.obo.OBO;
import java.util.List;

/**
 * FileContent tag. Describes the content of the (i)mzML file.
 * 
 * @author Alan Race
 */
public class FileContent extends MzMLContentWithParams {

    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Accession: Data file content (MS:1000524). MUST supply a child one or more times
     */
    public static final String DATA_FILE_CONTENT_ID = "MS:1000524";

    /**
     * Accession: Spectrum representation (MS:1000525). MAY supply a child once only
     */
    public static final String SPECTRUM_REPRESENTATION_ID = "MS:1000525";		// Optional child (1)

    /**
     * Accession: Mass spectrum (MS:1000294).
     */
    public static final String MASS_SPECTRUM_ID = "MS:1000294";
    
    /**
     * Accession: Binary type (IMS:1000003).
     */
    public static final String BINARY_TYPE_ID = "IMS:1000003";
    
    /**
     * Accession: Binary type (continuous) (IMS:1000030).
     */
    public static final String BINARY_TYPE_CONTINUOUS_ID = "IMS:1000030";
    
    /**
     * Accession: Binary type (processed) (IMS:1000030).
     */
    public static final String BINARY_TYPE_PROCESSED_ID = "IMS:1000031";

    /**
     * Accession: IBD identification (IMS:1000008).
     */
    public static final String IDB_IDENTIFICATION_ID = "IMS:1000008";
    
    /**
     * Accession: UUID identification (IMS:1000080).
     */
    public static final String UUID_IDENTIFICATION_ID = "IMS:1000080";

    /**
     * Accession: IBD checksum (IMS:1000009).
     */
    public static final String IBD_CHECKSUM_ID = "IMS:1000009";
    
    /**
     * Accession: MD5 checksum (IMS:1000090).
     */
    public static final String MD5_CHECKSUM_ID = "IMS:1000090";
    
    /**
     * Accession: SHA-1 checksum (IMS:1000091).
     */
    public static final String SHA1_CHECKSUM_ID = "IMS:1000091";

    /**
     * Accession: IBD file (IMS:1000007).
     */
    public static final String IBD_FILE_ID = "IMS:1000007";

    /**
     * Create empty {@literal <fileContent>} tag.
     */
    public FileContent() {
        super();
    }

    /**
     * Copy constructor.
     * 
     * @param fileContent Old {@literal <fileContent>} tag to copy
     * @param rpgList New ReferenceableParamGroupList used to take references from
     */
    public FileContent(FileContent fileContent, ReferenceableParamGroupList rpgList) {
        super(fileContent, rpgList);
    }

    /**
     * Returns all CV parameters which have  child terms of 
     * {@link FileContent#DATA_FILE_CONTENT_ID} ontology parameter.
     * 
     * @return List of CVParam
     */
    public List<CVParam> getDataFileContents() {
        return getChildrenOf(DATA_FILE_CONTENT_ID, false);
    }

    @Override
    public String getTagName() {
        return "fileContent";
    }
    
    /**
     * Create the default, valid (according to imzML) FileContent. Default file contents
     * CV parameter is mass spectrum ({@link FileContent#MASS_SPECTRUM_ID}).
     * 
     * @return Default valid FileContent
     */
    public static FileContent create() {
        FileContent fc = new FileContent();
        
        fc.addCVParam(new EmptyCVParam(OBO.getOBO().getTerm(MASS_SPECTRUM_ID)));
        
        return fc;
    }
}
