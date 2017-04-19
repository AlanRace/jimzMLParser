package com.alanmrace.jimzmlparser.writer;

import com.alanmrace.jimzmlparser.data.DataTransformation;
import com.alanmrace.jimzmlparser.mzml.BinaryDataArray;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;

/**
 * Writer for exporting to mzML. Binary data exported as Base64 in the same file
 * as the metadata.
 * 
 * @author Alan Race
 */
public class MzMLWriter implements MzMLWritable {

    /**
     * Encoding to write the XML document out as. Default is ISO-8859-1.
     */
    protected String encoding = "ISO-8859-1";
    
    /**
     * RandomAccessFile for the file containing the XML metadata.
     */
    protected RandomAccessFile metadataRAF;

    /**
     * BufferedWriter created from the metadata RandomAccessFile for writing out to.
     */
    protected BufferedWriter output;
    
    /**
     * Create an mzML file at the specified outputLocation. This file will be open
     * as 'rw' in a RandomAccessFile.
     * 
     * @param outputLocation Location to write the new mzML file
     * @throws IOException Issue with opening file for writing
     */
    public MzMLWriter(String outputLocation) throws IOException {
        metadataRAF = new RandomAccessFile(outputLocation, "rw");
        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(metadataRAF.getFD()), encoding);
        
        output = new BufferedWriter(out);
    }

    @Override
    public void writeData(byte[] data) throws IOException {
        write(Base64.encode(data));
    }

    @Override
    public byte[] prepareData(double[] data, BinaryDataArray binayDataArray) throws IOException {
        DataTransformation transformation = binayDataArray.generateDataTransformation();
        byte[] byteData = null;
        
        if(transformation != null) {
            try {
                byteData = transformation.performForwardTransform(data);
            } catch (DataFormatException ex) {
                Logger.getLogger(MzMLWriter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return byteData;
    }
    
    @Override
    public void write(String str) throws IOException {
        output.write(str);
    }

    @Override
    public void flush() throws IOException {
        output.flush();
    }

    @Override
    public void close() throws IOException {
        output.flush();
            
        metadataRAF.getChannel().truncate(metadataRAF.getFilePointer());
        output.close();
    }

    @Override
    public long getMetadataPointer() throws IOException {
        output.flush();
        
        return metadataRAF.getFilePointer();
    }

    @Override
    public long getDataPointer() throws IOException {
        output.flush();
        
        return metadataRAF.getFilePointer();
    }
}
