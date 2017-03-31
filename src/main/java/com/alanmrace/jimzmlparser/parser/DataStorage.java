/*
 * 
 */
package com.alanmrace.jimzmlparser.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alan Race
 */
public abstract class DataStorage {
    private static final Logger logger = Logger.getLogger(DataStorage.class.getName());
    
    protected File dataFile;
    
    protected final RandomAccessFile randomAccessFile;
    protected boolean fileStreamOpen = false;
    
    
    public DataStorage(File dataFile) throws FileNotFoundException {
        this.dataFile = dataFile;
        
        randomAccessFile = new RandomAccessFile(dataFile, "r");

	logger.log(Level.FINER, MessageFormat.format("[Opened] {0} ({1})", new Object[]{dataFile, randomAccessFile}));
	
        fileStreamOpen = true;
    }
    
    public File getFile() {
	return dataFile;
    }
    
    
    public byte[] getData(long offset, int length) throws IOException {
	if(!fileStreamOpen) {
	    logger.log(Level.SEVERE, MessageFormat.format("Trying to access data from a closed stream ({0})", randomAccessFile));
	    
	    return new byte[0];
	}
	
	byte[] buffer = new byte[length];
	
        synchronized(randomAccessFile) {
            randomAccessFile.seek(offset);
            randomAccessFile.read(buffer);
        }
	
	return buffer;
    }
    
    public void close() throws IOException {
        if(fileStreamOpen){
            randomAccessFile.close();
	    
	    logger.log(Level.FINER, MessageFormat.format("[Closed] {0} ({1})", new Object[]{dataFile, randomAccessFile}));
            
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
    
    @Override
    public String toString() {
        return this.getClass().getName() + ": " + dataFile.getAbsolutePath();
    }
}
