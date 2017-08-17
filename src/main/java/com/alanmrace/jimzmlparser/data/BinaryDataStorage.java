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
     * @param openForWriting Whether to open the file as writable too. Used for temporary storage of mzML, for exmaple
     * @throws FileNotFoundException Error occurred opening the file
     */
    public BinaryDataStorage(File dataFile, boolean openForWriting) throws FileNotFoundException {
        super(dataFile, openForWriting);
    }
}
