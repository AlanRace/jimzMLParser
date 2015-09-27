/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alanmrace.jimzmlparser.parser;

import com.alanmrace.jimzmlparser.mzML.BinaryDataArray;
import com.alanmrace.jimzmlparser.mzML.MzML;
import com.alanmrace.jimzmlparser.mzML.Spectrum;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author Alan
 */
public class Base64DataStorage extends DataStorage {
    public Base64DataStorage(File dataFile) {
        super(dataFile);
    }

    @Override
    public byte[] getData(long offset, int length) throws IOException {
	byte[] buffer = super.getData(offset, length);
	
	return DatatypeConverter.parseBase64Binary(new String(buffer));
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public static void main(String[] args) throws IOException {
	String mzMLFile = "C:\\Users\\Alan\\Documents\\Work\\jimzMLParser\\src\\main\\resources\\imzMLConverterTestData_23.898, 29.745, 30.898, 41.766, 7.000, 0.100, 1_1.mzML";
	
        MzML mzML = MzMLHeaderHandler.parsemzMLHeader(mzMLFile);
        
        Spectrum spectrum = mzML.getRun().getSpectrumList().getSpectrum(0);
        BinaryDataArray bda = spectrum.getBinaryDataArrayList().getBinaryDataArray(0);
        
	System.out.println(spectrum.getmzArray()[0]);
	
	mzML = MzMLHandler.parsemzML(mzMLFile);
	
	spectrum = mzML.getRun().getSpectrumList().getSpectrum(0);
	bda = spectrum.getBinaryDataArrayList().getBinaryDataArray(0);
	
	System.out.println(spectrum.getmzArray()[0]);
	
//        System.out.println(bda.getBinary().getData()[0]);
    }
}
