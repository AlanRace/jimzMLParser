/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alanmrace.jimzmlparser.writer;

import java.io.IOException;

/**
 *
 * @author Alan Race
 */
public interface MzMLWriteable {
    
    public long getMetadataPointer() throws IOException;
    public long getDataPointer() throws IOException;
    
    public void write(String str) throws IOException;
    public void writeData(byte[] data) throws IOException;
    public void flush() throws IOException;
    public void close() throws IOException;
}
