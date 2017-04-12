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
public class ImzMLHeaderWriter extends MzMLWriter {

    public ImzMLHeaderWriter(String outputLocation) throws IOException {
        super(outputLocation);
    }

    @Override
    public void writeData(byte[] data) throws IOException {
        // Do nothing
    }
}
