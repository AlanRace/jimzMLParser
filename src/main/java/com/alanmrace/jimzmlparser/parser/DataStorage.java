/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alanmrace.jimzmlparser.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author Alan
 */
public abstract class DataStorage {
    protected File dataFile;
    
//    protected FileInputStream fileInputStream;
    protected final RandomAccessFile randomAccessFile;
    protected boolean fileStreamOpen = false;
    
//    public abstract byte[] getData(long offset, int length) throws IOException;
    
    public DataStorage(File dataFile) throws FileNotFoundException {
        this.dataFile = dataFile;
        
        randomAccessFile = new RandomAccessFile(dataFile, "r");

        fileStreamOpen = true;
    }
    
    public File getFile() {
	return dataFile;
    }
    
    
    public byte[] getData(long offset, int length) throws IOException {
	byte[] buffer = new byte[length];
	
        synchronized(randomAccessFile) {
            randomAccessFile.seek(offset);
            randomAccessFile.read(buffer);
        }
	
	return buffer;
		
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void close() throws IOException {
        if(fileStreamOpen){
            randomAccessFile.close();
            
            fileStreamOpen = false;
        }
    }
    
    @Override
    protected void finalize() throws Throwable {
        if(fileStreamOpen) {
            try {
                close();
            }
            finally {
                super.finalize();
            }
        }
    }
}
