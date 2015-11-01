/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alanmrace.jimzmlparser.parser;

import java.io.File;
import java.io.FileNotFoundException;

/**
 *
 * @author Alan
 */
public class MzMLSpectrumDataStorage extends DataStorage {

    protected Base64DataStorage base64DataStorage;
    
    public MzMLSpectrumDataStorage(File dataFile) throws FileNotFoundException {
        super(dataFile);
	
	base64DataStorage = new Base64DataStorage(dataFile);
    }
    
    public Base64DataStorage getBase64DataStorage() {
	return base64DataStorage;
    }
}
