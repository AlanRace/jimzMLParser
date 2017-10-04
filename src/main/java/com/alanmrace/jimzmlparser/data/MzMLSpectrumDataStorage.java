package com.alanmrace.jimzmlparser.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Access to binary data stored in Base64 within an MzML file.
 *  
 * @author Alan Race
 */
public class MzMLSpectrumDataStorage extends DataStorage {

    /**
     * Base64 data storage for accessing the spectral data in Base64 encoding.
     */
    protected Base64DataStorage base64DataStorage;
    
    /**
     * Create an MzMLSpectrumDataStorage from a mzML file.
     * 
     * @param dataFile mzML file
     * @throws FileNotFoundException mzML file not found
     */
    public MzMLSpectrumDataStorage(File dataFile) throws FileNotFoundException {
        super(dataFile);
	
	base64DataStorage = new Base64DataStorage(dataFile);
    }
    
    /**
     * Returns the Base64DataStorage representation of the MzMLSpectrumDataStorage 
     * file.
     * 
     * @return Base64DataStorage of the mzML file.
     */
    public Base64DataStorage getBase64DataStorage() {
	return base64DataStorage;
    }
    
    @Override
    public void close() throws IOException {
        super.close();
        base64DataStorage.close();
    }
}
