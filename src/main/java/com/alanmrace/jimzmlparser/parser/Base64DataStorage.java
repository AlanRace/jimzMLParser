/*
 * 
 */
package com.alanmrace.jimzmlparser.parser;

import com.alanmrace.jimzmlparser.exceptions.MzMLParseException;
import com.alanmrace.jimzmlparser.mzml.BinaryDataArray;
import com.alanmrace.jimzmlparser.mzml.MzML;
import com.alanmrace.jimzmlparser.mzml.Spectrum;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author Alan Race
 */
public class Base64DataStorage extends DataStorage {
    public Base64DataStorage(File dataFile) throws FileNotFoundException {
        super(dataFile);
    }

    @Override
    public byte[] getData(long offset, int length) throws IOException {
	byte[] buffer = super.getData(offset, length);
	
	return DatatypeConverter.parseBase64Binary(new String(buffer));
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public static void main(String[] args) {
        try {
            //String mzMLFile = "C:\\Users\\Alan\\Documents\\Work\\jimzMLParser\\src\\main\\resources\\imzMLConverterTestData_23.898, 29.745, 30.898, 41.766, 7.000, 0.100, 1_1.mzML";
            String mzMLFile = "D:\\Test\\Data7_1_2011-acc0.1_cyc10_Sample001_1.mzML";
            
            MzML mzML = MzMLHeaderHandler.parsemzMLHeader(mzMLFile);
            
            Spectrum spectrum = mzML.getRun().getSpectrumList().getSpectrum(0);
            BinaryDataArray bda = spectrum.getBinaryDataArrayList().getBinaryDataArray(0);
            
            System.out.println(spectrum.getmzArray()[0]);
            
            mzML = MzMLHandler.parsemzML(mzMLFile);
            
            spectrum = mzML.getRun().getSpectrumList().getSpectrum(0);
            bda = spectrum.getBinaryDataArrayList().getBinaryDataArray(0);
            
            System.out.println(spectrum.getmzArray()[0]);
            
//        System.out.println(bda.getBinary().getData()[0]);
        } catch (MzMLParseException ex) {
            Logger.getLogger(Base64DataStorage.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Base64DataStorage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
