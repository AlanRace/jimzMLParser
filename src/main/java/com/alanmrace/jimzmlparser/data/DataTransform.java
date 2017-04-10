package com.alanmrace.jimzmlparser.data;

import java.util.zip.DataFormatException;

/**
 *
 * @author Alan Race
 */
public interface DataTransform {
    static final int BYTE_BUFFER_SIZE = 2 ^ 20;
    
    /**
     * Data being written out. e.g. compressing
     * 
     * @param data
     * @return
     * @throws DataFormatException
     */
    byte[] forwardTransform(byte[] data) throws DataFormatException;

    /**
     * Data being read in. e.g. decompressing
     * 
     * @param data
     * @return
     * @throws DataFormatException
     */
    byte[] reverseTransform(byte[] data) throws DataFormatException;
}
