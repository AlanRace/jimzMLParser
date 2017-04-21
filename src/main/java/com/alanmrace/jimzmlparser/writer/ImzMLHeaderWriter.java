package com.alanmrace.jimzmlparser.writer;

import java.io.IOException;

/**
 * Writer for exporting only the metadata file of imzML. Binary data will not
 * be written out.
 * 
 * @author Alan Race
 */
public class ImzMLHeaderWriter extends MzMLWriter {

    @Override
    public void writeData(byte[] data) throws IOException {
        // Do nothing
    }
}
