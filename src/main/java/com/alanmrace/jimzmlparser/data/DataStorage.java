package com.alanmrace.jimzmlparser.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Access to a dataset. This class provides the base for accessing binary data stored
 * within a given type of file.
 *  
 * @author Alan Race
 * @see Base64DataStorage
 * @see BinaryDataStorage
 * @see MzMLSpectrumDataStorage
 */
public abstract class DataStorage implements Serializable {
    
    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = 1L;
    
    /** Class logger. */
    private static final Logger logger = Logger.getLogger(DataStorage.class.getName());
    
    /** File containing the data. */
    protected File dataFile;
    
    /** Random access to the file. */
    protected final RandomAccessFile randomAccessFile;
    /** Boolean to determine whether the RandomAccessFile is open or not. */
    protected boolean fileStreamOpen = false;
    
    /**
     * Define a data storage by specifying the File containing the data.
     * 
     * @param dataFile File where the data is stored
     * @throws FileNotFoundException Could not find the file specified
     */
    public DataStorage(File dataFile) throws FileNotFoundException {
        this(dataFile, false);
    }
    
    /**
     * Define a data storage by specifying the File containing the data, optionally
     * opening the file for writing. When parsing MzML and using temporary binary data
     * storage, openForWriting must be true.
     * 
     * @param dataFile File where the data is stored
     * @param openForWriting Whether to open the data storage for writing as well as reading
     * @throws FileNotFoundException Could not find the file specified
     */
    public DataStorage(File dataFile, boolean openForWriting) throws FileNotFoundException {
        this.dataFile = dataFile;
        
        if(openForWriting)
            randomAccessFile = new RandomAccessFile(dataFile, "rw");
        else
            randomAccessFile = new RandomAccessFile(dataFile, "r");

        logger.log(Level.FINER, MessageFormat.format("[Opened] {0} ({1})", dataFile, randomAccessFile));
	
        fileStreamOpen = true;
    }
    
    /**
     * Get the File where the data is stored.
     * 
     * @return File where the data is stored
     */
    public File getFile() {
	return dataFile;
    }
    
    /**
     * Get the data from the dataStorage at the specified offset with the specified length.
     * Reading of data is synchronized to the file to allow multithreaded access.
     * 
     * <p>If the randomAccessFile has not been opened successfully (in the constructor) then
     * this will return an empty byte array.
     * 
     * @param offset Offset in bytes within the dataStorage
     * @param length Length of the data in bytes
     * @return byte[] containing data
     * @throws IOException Exception thrown when trying to read data
     */
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
    
    /**
     * Close the randomAccessFile if it is open.
     * 
     * @throws IOException Exception thrown when trying to close randomAccessFile
     */
    public void close() throws IOException {
        if(fileStreamOpen) {
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
