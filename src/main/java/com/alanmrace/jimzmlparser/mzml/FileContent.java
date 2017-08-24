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
    public static final String dataFileContentID = "MS:1000524";

    /**
     * Accession: Spectrum representation (MS:1000525). MAY supply a child once only
     */
    public static final String spectrumRepresentationID = "MS:1000525";		// Optional child (1)

    /**
     * Accession: Mass spectrum (MS:1000294).
     */
    public static final String massSpectrumID = "MS:1000294";
    
    /**
     * Accession: Binary type (IMS:1000003).
     */
    public static final String binaryTypeID = "IMS:1000003";
    
    /**
     * Accession: Binary type (continuous) (IMS:1000030).
     */
    public static final String binaryTypeContinuousID = "IMS:1000030";
    
    /**
     * Accession: Binary type (processed) (IMS:1000030).
     */
    public static final String binaryTypeProcessedID = "IMS:1000031";

    /**
     * Accession: IBD identification (IMS:1000008).
     */
    public static final String ibdIdentificationID = "IMS:1000008";
    
    /**
     * Accession: UUID identification (IMS:1000080).
     */
    public static final String uuidIdntificationID = "IMS:1000080";

    /**
     * Accession: IBD checksum (IMS:1000009).
     */
    public static final String ibdChecksumID = "IMS:1000009";
    
    /**
     * Accession: MD5 checksum (IMS:1000090).
     */
    public static final String md5ChecksumID = "IMS:1000090";
    
    /**
     * Accession: SHA-1 checksum (IMS:1000091).
     */
    public static final String sha1ChecksumID = "IMS:1000091";

    /**
     * Accession: IBD file (IMS:1000007).
     */
    public static final String ibdFileID = "IMS:1000007";

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
     * {@link FileContent#dataFileContentID} ontology parameter.
     * 
     * @return List of CVParam
     */
    public List<CVParam> getDataFileContents() {
        return getChildrenOf(dataFileContentID);
    }

    @Override
    public String getTagName() {
        return "fileContent";
    }
    
    /**
     * Create the default, valid (according to imzML) FileContent. Default file contents
     * CV parameter is mass spectrum ({@link FileContent#massSpectrumID}).
     * 
     * @return Default valid FileContent
     */
    public static FileContent create() {
        FileContent fc = new FileContent();
        
        fc.addCVParam(new EmptyCVParam(OBO.getOBO().getTerm(massSpectrumID)));
        
        return fc;
    }
}
