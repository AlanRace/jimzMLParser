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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alan
 */
public abstract class DataStorage {
    private static final Logger logger = Logger.getLogger(DataStorage.class.getName());
    
    protected File dataFile;
    
//    protected FileInputStream fileInputStream;
    protected final RandomAccessFile randomAccessFile;
    protected boolean fileStreamOpen = false;
    
//    public abstract byte[] getData(long offset, int length) throws IOException;
    
    public DataStorage(File dataFile) throws FileNotFoundException {
        this.dataFile = dataFile;
        
        randomAccessFile = new RandomAccessFile(dataFile, "r");

	logger.log(Level.INFO, "[Opened] {0} ({1})", new Object[]{dataFile, randomAccessFile});
	
        fileStreamOpen = true;
    }
    
    public File getFile() {
	return dataFile;
    }
    
    
    public byte[] getData(long offset, int length) throws IOException {
	if(!fileStreamOpen) {
	    logger.log(Level.SEVERE, "Trying to access data from a closed stream (" + randomAccessFile + ")");
	    
	    return new byte[0];
	}
	
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
	    
	    logger.log(Level.INFO, "[Closed] {0} ({1})", new Object[]{dataFile, randomAccessFile});
            
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
