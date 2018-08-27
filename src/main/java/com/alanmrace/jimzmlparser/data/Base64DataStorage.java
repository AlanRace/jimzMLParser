package com.alanmrace.jimzmlparser.data;

import org.apache.commons.codec.binary.Base64;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Description of location of data stored in Base64 encoding. This class allows
 * decoding of data from Base64 to a byte[].
 *
 * @author Alan Race
 */
public class Base64DataStorage extends DataStorage {

    /**
     * Set up a file containing data in Base64 encoding. It is not necessary
     * that the entire file be in Base64.
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

        return Base64.decodeBase64(buffer);
    }
}
