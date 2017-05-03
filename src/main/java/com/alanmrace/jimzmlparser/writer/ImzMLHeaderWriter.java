package com.alanmrace.jimzmlparser.writer;

import com.alanmrace.jimzmlparser.mzml.BinaryDataArray;
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
    
    @Override
    protected void writeBinaryTag(BinaryDataArray bda) throws IOException {
        writeMetadata("<binary />\n");
    }
}
