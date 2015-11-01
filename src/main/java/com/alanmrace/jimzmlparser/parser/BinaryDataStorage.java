/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alanmrace.jimzmlparser.parser;

import com.alanmrace.jimzmlparser.mzML.MzML;
import com.alanmrace.jimzmlparser.mzML.Spectrum;
import java.io.File;
import java.io.FileNotFoundException;

/**
 *
 * @author Alan
 */
public class BinaryDataStorage extends DataStorage {

    public BinaryDataStorage(File dataFile) throws FileNotFoundException {
        super(dataFile);
    }

//    @Override
//    public byte[] getData(long offset, int length) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
    
    
    public static void main(String[] args) throws FileNotFoundException {
        MzML mzML = MzMLHeaderHandler.parsemzMLHeader("C:\\Users\\Alan\\Documents\\Work\\jimzMLParser\\src\\main\\resources\\imzMLConverterTestData_23.898, 29.745, 30.898, 41.766, 7.000, 0.100, 1_1.mzML");
        
        Spectrum spectrum = mzML.getRun().getSpectrumList().getSpectrum(0);
        
//        System.out.println(spectrum.getmzArray()[0]);
    }
}
