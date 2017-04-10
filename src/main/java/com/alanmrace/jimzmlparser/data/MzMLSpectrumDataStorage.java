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

    protected Base64DataStorage base64DataStorage;
    
    public MzMLSpectrumDataStorage(File dataFile) throws FileNotFoundException {
        super(dataFile);
	
	base64DataStorage = new Base64DataStorage(dataFile);
    }
    
    public Base64DataStorage getBase64DataStorage() {
	return base64DataStorage;
    }
    
    @Override
    public void close() throws IOException {
        super.close();
        base64DataStorage.close();
    }
}
