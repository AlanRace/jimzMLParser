/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alanmrace.jimzmlparser.parser;

import java.io.IOException;

/**
 *
 * @author Alan
 */
public class DataLocation {

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
        return dataStorage.getData(offset, length);
    }
    
    public String toString() {
	return "[" + offset + " (" + length + ")] " + dataStorage;
    }
}
