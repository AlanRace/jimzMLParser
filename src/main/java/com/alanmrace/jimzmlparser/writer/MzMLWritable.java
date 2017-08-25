package com.alanmrace.jimzmlparser.writer;

import com.alanmrace.jimzmlparser.mzml.BinaryDataArray;
import com.alanmrace.jimzmlparser.mzml.MzML;
import java.io.IOException;

/**
 * Interface for writing out (i)mzML data. Two files are kept track of (although
 * in the case of mzML this will be the same file), the metadata file and the 
 * data file. 
 * 
 * @author Alan Race
 */
public interface MzMLWritable {
    
    /**
     * Add a writer listener to the writer.
     * 
     * @param listener Listener to add
     */
    public void addListener(WriterListener listener);
    
    /**
     * Returns whether the writer should output indexed mzML.
     * 
     * @return true if indexed mzML, false otherwise
     */
    public boolean shouldOutputIndex();
    
    /**
     * Returns the full path location of the metadata file.
     * 
     * @return File and path of the metadata file
     */
    public String getMetadataLocation();
    
    /**
     * Returns the current file pointer location in the metadata file. When 
     * writing mzML this will be the same as getDataPointer(), but for writing 
     * imzML this will be different.
     * 
     * @return Current location of file pointer in metadata file
     * @throws IOException Issue retrieving the file pointer location
     */
    public long getMetadataPointer() throws IOException;

    /**
     * Returns the current file pointer location in the data file. When 
     * writing mzML this will be the same as getMetadataPointer(), but for writing 
     * imzML this will be different.
     * 
     * @return Current location of file pointer in data file
     * @throws IOException Issue retrieving the file pointer location
     */
    public long getDataPointer() throws IOException;
        
    /**
     * Prepare data to be written out. This involves the conversion of double[] 
     * data to byte[] data using the DataTransformation described by the 
     * parameters in the BinaryDataArray. In the case of writing out imzML data
     * this will also update the BinaryDataArray to include the correct 
     * CVParams describing the location and size of the data within the IBD file.
     * Therefore, this should always be called prior to writing data.
     * 
     * @param data Data to convert to byte[]
     * @param binayDataArray BinaryDataArray describing how the data should be stored
     * @return byte[] as it should be written out
     * @throws IOException Issue preparing or reading the data
     */
    public byte[] prepareData(double[] data, BinaryDataArray binayDataArray) throws IOException;
    
    /**
     * Write out mzML file.
     * 
     * @param mzML MzML to write
     * @param outputLocation Location to write mzML to
     * @throws IOException Issue writing data
     */
    public void write(MzML mzML, String outputLocation) throws IOException;
    
    /**
     * Write metadata out to metadata file.
     * 
     * @param str Metadata to write out
     * @throws IOException Issue writing data
     */
    public void writeMetadata(String str) throws IOException;

    /**
     * Write binary data out to data file.
     * 
     * @param data Data to write out
     * @throws IOException Issue writing data
     */
    public void writeData(byte[] data) throws IOException;
    
    /**
     * Flush both the metadata and the data file.
     * 
     * @throws IOException Issue caused by flushing
     */
    public void flush() throws IOException;

    /**
     * Close both the metadata and the data file.
     * 
     * @throws IOException Issue caused by closing
     */
    public void close() throws IOException;
}
