package com.alanmrace.jimzmlparser.data;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Default DataStorage where data is stored in binary.
 * 
 * @author Alan Race
 */
public class BinaryDataStorage extends DataStorage {

    /**
     * Set up a file containing binary data. 
     * 
     * @param dataFile File containing binary data
     * @throws FileNotFoundException Error occurred opening the file
     */
    public BinaryDataStorage(File dataFile) throws FileNotFoundException {
        super(dataFile);
    }
}
