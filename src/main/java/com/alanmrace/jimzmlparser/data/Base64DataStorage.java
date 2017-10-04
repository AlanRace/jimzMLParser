package com.alanmrace.jimzmlparser.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.xml.bind.DatatypeConverter;

/**
 * Description of location of data stored in Base64 encoding. This class allows 
 * decoding of data from Base64 to a byte[].
 * 
 * @author Alan Race
 */
public class Base64DataStorage extends DataStorage {

    /**
     * Set up a file containing data in Base64 encoding. It is not necessary that
     * the entire file be in Base64.
     * 
     * @param dataFile File containing Base64 encoded data
     * @throws FileNotFoundException Error occurred opening the file
     */
    public Base64DataStorage(File dataFile) throws FileNotFoundException {
        super(dataFile);
    }

    @Override
    public byte[] getData(long offset, int length) throws IOException {
	byte[] buffer = super.getData(offset, length);
	
	return DatatypeConverter.parseBase64Binary(new String(buffer));
    }
}
