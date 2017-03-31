/*
 * 
 */
package com.alanmrace.jimzmlparser.parser;

import com.alanmrace.jimzmlparser.exceptions.MzMLParseException;
import com.alanmrace.jimzmlparser.mzml.MzML;
import com.alanmrace.jimzmlparser.mzml.Spectrum;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    
    
    public static void main(String[] args) {
        try {
            MzML mzML = MzMLHeaderHandler.parsemzMLHeader("C:\\Users\\Alan\\Documents\\Work\\jimzMLParser\\src\\main\\resources\\imzMLConverterTestData_23.898, 29.745, 30.898, 41.766, 7.000, 0.100, 1_1.mzML");
            
            Spectrum spectrum = mzML.getRun().getSpectrumList().getSpectrum(0);
            
//        System.out.println(spectrum.getmzArray()[0]);
        } catch (MzMLParseException ex) {
            Logger.getLogger(BinaryDataStorage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
