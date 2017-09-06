package com.alanmrace.jimzmlparser.data;

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;

/**
 * The location of specific data within a {@link DataStorage}.
 * 
 * @author Alan Race
 */
public class DataLocation implements Serializable {
    
    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = 1L;
    
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
    public static final long EXTENDED_OFFSET = 4294967296L; // 2^32
    
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
     * The data transformation such that when {@link DataTransformation#performReverseTransform(byte[])}
     * is applied it will convert the raw input binary as it appears in the 
     * {@link DataLocation#dataStorage} to a double[].
     */
    protected DataTransformation dataTransformation;
    
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
    
    /**
     * Get the data storage.
     * 
     * @return DataStorage
     */
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
    public byte[] getBytes() throws IOException {
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
    
//    public byte[] getConvertedBytes() throws DataFormatException, IOException {
//        byte[] data = getBytes();
//        
//        if(dataTransformation == null)
//            return data;
//        
//        return dataTransformation.performReverseTransform(data);
//    }

    /**
     * Gets the raw data from the DataStorage using {@link DataLocation#getBytes()} and
     * applies the {@link DataLocation#dataTransformation} to convert the byte[] 
     * to a double[]. If no {@link DataTransformation} has been supplied then this 
     * method will just convert the byte[] to a double[] directly, therefore if
     * the data is not stored as a double[], then a DataTransformation should be
     * added which performs the necessary data type -> double transformation.
     * 
     * @return Converted and decompressed data as double[]
     * @throws DataFormatException Issue with converting the data
     * @throws IOException Issue reading the raw data
     * 
     * @see DataTransformation
     */
    
    public double[] getData() throws DataFormatException, IOException {
        byte[] data = getBytes();
        
        if(dataTransformation == null)
            return DataTypeTransform.convertDataToDouble(data, DataTypeTransform.DataType.Double);
        
        return dataTransformation.performReverseTransform(data);
    }
    
    /**
     * Set the data transformation which describes how the data was originally 
     * converted from a byte[] representation of double[] to how it was stored in
     * the {@link DataStorage}. This could one or more steps such as data type conversion 
     * and compression.
     *
     * @param transformation DataTransformation applied to generate the data in the DataLocation
     */
    public void setDataTransformation(DataTransformation transformation) {
        this.dataTransformation = transformation;
    }
    
    @Override
    public String toString() {
	return "[" + offset + " (" + length + ")] " + dataStorage;
    }
}
