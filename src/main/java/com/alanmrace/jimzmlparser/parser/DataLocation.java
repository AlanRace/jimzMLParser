/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alanmrace.jimzmlparser.parser;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alan
 */
public class DataLocation {
    
    private static final Logger logger = Logger.getLogger(DataLocation.class.getName());

    public static long EXTENDED_OFFSET = 4294967296l; // 2^32
    
    protected DataStorage dataStorage;
    protected long offset;
    protected int length;
    
    public DataLocation(DataStorage dataStorage, long offset, int length) {
        this.dataStorage = dataStorage;
        this.offset = offset;
        this.length = length;
    }
    
    public DataStorage getDataStorage() {
        return dataStorage;
    }
    
    public long getOffset(){
	return offset;
    }
    
    public int getLength() {
	return length;
    }
    
    public byte[] getData() throws IOException {
        if(length <= 0) {
            logger.log(Level.FINER, "Data is of size {0} for {1}", new Object[] {length, dataStorage});
//            System.out.println("Index less than " + length);
//            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            return new byte[0];
        }
        if(offset < 0) {
            logger.log(Level.SEVERE, "Offset is {0} for {1}. Attempting to fix integer overflow.", new Object[] {offset, dataStorage});
//            System.out.println("Index less than " + length);
//            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            //return new byte[0];
            offset += EXTENDED_OFFSET; //2^32;
        }
        
        return dataStorage.getData(offset, length);
    }
    
    @Override
    public String toString() {
	return "[" + offset + " (" + length + ")] " + dataStorage;
    }
}
