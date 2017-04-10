/*
 * 
 */
package com.alanmrace.jimzmlparser.data;

import java.io.File;
import java.io.FileNotFoundException;

/**
 *
 * @author Alan Race
 */
public class BinaryDataStorage extends DataStorage {

    public BinaryDataStorage(File dataFile) throws FileNotFoundException {
        super(dataFile);
    }
}
