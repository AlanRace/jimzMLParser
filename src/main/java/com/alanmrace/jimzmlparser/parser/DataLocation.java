package com.alanmrace.jimzmlparser.parser;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The location of specific data within a {@link DataStorage}.
 * 
 * @author Alan Race
 */
public class DataLocation {
    
    /**
     * Logger for the class.
     */
    private static final Logger logger = Logger.getLogger(DataLocation.class.getName());

    /**
     * The value that should be added if an 32-bit signed integer overflow 
     * (overflow into the negative, when only a positive value expected)
     * has occurred in order to correct to the correct positive value. 
     * 
     * <p>EXTENDED_OFFSET = 2^32 = 4294967296.
     */
    public static long EXTENDED_OFFSET = 4294967296L; // 2^32
    
    /**
     * The location and storage type of the data.
     * @see DataStorage
     */
    protected DataStorage dataStorage;
    
    /**
     * The offset in bytes where the data is located within the dataStorage.
     */
    protected long offset;
    
    /**
     * The length in bytes of the data.
     */
    protected int length;
    
    /**
     * Construct a DataLocation at a specific offset, with a specific length within
     * a DataStorage.
     * 
     * @param dataStorage DataStorage containing the data
     * @param offset offset in bytes of the data within the dataStorage
     * @param length length in bytes of the data within the dataStorage
     * @see DataStorage
     */
    public DataLocation(DataStorage dataStorage, long offset, int length) {
        this.dataStorage = dataStorage;
        this.offset = offset;
        this.length = length;
    }
    
    public DataStorage getDataStorage() {
        return dataStorage;
    }
    
    /**
     * Get the offset in bytes of the data with respect to the dataStorage.
     * 
     * @return offset in bytes
     */
    public long getOffset(){
	return offset;
    }
    
    /**
     * Get the length in bytes of the data with respect to the dataStorage.
     * 
     * @return length in bytes
     */
    public int getLength() {
	return length;
    }
    
    /**
     * Read the data from the data storage into a byte array.
     * 
     * @return byte[] containing the data 
     * @throws IOException can be thrown by dataStorage if the data storage is on disk
     */
    public byte[] getData() throws IOException {
        if(length <= 0) {
            logger.log(Level.FINER, "Data is of size {0} for {1}", new Object[] {length, dataStorage});

            return new byte[0];
        }
        if(offset < 0) {
            logger.log(Level.SEVERE, "Offset is {0} for {1}. Attempting to fix integer overflow.", new Object[] {offset, dataStorage});

            offset += EXTENDED_OFFSET; //2^32;
        }
        
        return dataStorage.getData(offset, length);
    }
    
    @Override
    public String toString() {
	return "[" + offset + " (" + length + ")] " + dataStorage;
    }
}
