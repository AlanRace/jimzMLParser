package com.alanmrace.jimzmlparser.data;

import java.io.Serializable;
import java.util.zip.DataFormatException;

/**
 * Interface describing a DataTransformation which goes from one byte[] to 
 * another byte[].
 * 
 * @author Alan Race
 */
public interface DataTransform extends Serializable {
    
    /**
     * Perform the data transformation. For example, this could be applying 
     * compression.
     * 
     * @param data Data to perform the transformation on
     * @return Transformed data
     * @throws DataFormatException Issue with the transformation
     */
    byte[] forwardTransform(byte[] data) throws DataFormatException;

    /**
     * Perform the data transformation in reverse. For example, this could be 
     * performing the corresponding decompression algorithm that was applied by
     * the forwardTransform.
     * 
     * @param data Data to reverse the transformation on
     * @return Transformed data
     * @throws DataFormatException Issue with the transformation
     */
    byte[] reverseTransform(byte[] data) throws DataFormatException;
}
