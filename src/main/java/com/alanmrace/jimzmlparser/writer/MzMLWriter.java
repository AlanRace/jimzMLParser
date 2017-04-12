/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 *
 * @author Alan Race
 */
public class MzMLWriter implements MzMLWritable {

    protected String encoding = "ISO-8859-1";
    
    protected RandomAccessFile metadataRAF;
    protected BufferedWriter output;
    
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
