/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alanmrace.jimzmlparser.writer;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author Alan Race
 */
public class ImzMLWriter extends MzMLWriter {
    
    protected RandomAccessFile dataRAF;
    protected DataOutputStream dataOutput;
    
    public ImzMLWriter(String outputLocation) throws IOException {
        super(outputLocation);
        
        int pos = outputLocation.lastIndexOf(".");
        if (pos > 0) {
            outputLocation = outputLocation.substring(0, pos);
        }
        
        dataRAF = new RandomAccessFile(outputLocation + ".ibd", "rw");
        dataOutput = new DataOutputStream(new FileOutputStream(dataRAF.getFD()));
    }
    
    @Override
    public void writeData(byte[] data) throws IOException {
        dataOutput.write(data);
    }
    
    @Override
    public long getDataPointer() throws IOException {
        dataOutput.flush();
        
        return dataRAF.getFilePointer();
    }
}
