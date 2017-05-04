package com.alanmrace.jimzmlparser.mzml;

/**
 * Binary tag. When an mzML file is parsed the data is output into a temporary
 * file. This object allows access to the temporary file.
 *
 * @author Alan Race
 */
public class Binary extends MzMLContent {

    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * Default constructor.
     */
    public Binary() {
    }
    
    
    @Override
    public String getTagName() {
        return "binary";
    }
}
