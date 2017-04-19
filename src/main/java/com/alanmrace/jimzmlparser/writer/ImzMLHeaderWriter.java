package com.alanmrace.jimzmlparser.writer;

import java.io.IOException;

/**
 * Writer for exporting only the metadata file of imzML. Binary data will not
 * be written out.
 * 
 * @author Alan Race
 */
public class ImzMLHeaderWriter extends MzMLWriter {

    /**
     * Create an imzML file at the specified outputLocation. This file will be open
     * as 'rw' in a RandomAccessFile. No IBD file will be created.
     * 
     * @param outputLocation Location to write the new imzML file
     * @throws IOException Issue with opening file for writing
     */
    public ImzMLHeaderWriter(String outputLocation) throws IOException {
        super(outputLocation);
    }

    @Override
    public void writeData(byte[] data) throws IOException {
        // Do nothing
    }
}
