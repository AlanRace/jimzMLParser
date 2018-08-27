/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alanmrace.jimzmlparser.imzml;

import com.alanmrace.jimzmlparser.mzml.BinaryDataArray;
import com.alanmrace.jimzmlparser.mzml.MzML;
import com.alanmrace.jimzmlparser.mzml.Spectrum;
import com.alanmrace.jimzmlparser.parser.ImzMLHandler;
import com.alanmrace.jimzmlparser.parser.MzMLHeaderHandler;
import com.alanmrace.jimzmlparser.writer.ImzMLWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

/**
 *
 * @author Alan
 */
public class ImzMLWriterTest {
    @Test
    public void testMzMLToImzMLWriter() throws IOException {
        MzML mzML = MzMLHeaderHandler.parsemzMLHeader("D:\\Compression\\M451_mousebrain_01_measure1_29_716_400-1200.mzML");
        
        ImzML imzML = new ImzML(mzML);
        imzML.getRun().getSpectrumList().clear();
        imzML.getRun().setChromatogramList(null);
        
        Spectrum spectrum = mzML.getSpectrumList().getSpectrum(0);
        spectrum.setPixelLocation(1, 1);
        //spectrum.setCompression(BinaryDataArray.CompressionType.MSNumpressLinearLZ4);
        spectrum.getBinaryDataArrayList().getmzArray().setCompression(BinaryDataArray.CompressionType.MSNUMPRESS_POSITIVE_LZ4);
        spectrum.getBinaryDataArrayList().getIntensityArray().setCompression(BinaryDataArray.CompressionType.MSNUMPRESS_LINEAR_LZ4);
        
        imzML.addSpectrum(spectrum);
        
        ImzMLWriter writer = new ImzMLWriter();
        
        try {
            writer.write(imzML, "test.imzML");
        } catch (IOException ex) {
            Logger.getLogger(ImzMLWriterTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        imzML = ImzMLHandler.parseimzML("test.imzML"); //"D:\\Compression\\MSNumpressLZ4.imzML");
        
        spectrum = imzML.getSpectrum(1, 1);
        System.out.println(spectrum.getmzArray());
        System.out.println(spectrum.getIntensityArray());
    }
    
    @Test
    public void testImzMLWriter() throws IOException {
        ImzML oldimzML = ImzMLHandler.parseimzML("D:\\M288_Maus Lunge_KW121_ohneCMC_M16_EHM_0.5h_20x40um_270x170Pixel_mz100-400_400-900_Att30.RAW_0.imzML");
        
        ImzML imzML = new ImzML(oldimzML);
//        imzML.getRun().getSpectrumList().clear();
//        imzML.getRun().setChromatogramList(null);
        
        ImzMLWriter writer = new ImzMLWriter();
        
        System.out.println(oldimzML.getSpectrum(1, 1).getBinaryDataArrayList().getBinaryDataArray(0).getDataLocation());
        System.out.println(imzML.getSpectrum(1, 1).getBinaryDataArrayList().getBinaryDataArray(0).getDataLocation());
        
        assertNotNull(imzML.getSpectrum(1, 1).getBinaryDataArrayList().getBinaryDataArray(0).getDataAsDouble());
        
//        try {
//            writer.write(imzML, "test2.imzML");
//        } catch (IOException ex) {
//            Logger.getLogger(ImzMLWriterTest.class.getName()).log(Level.SEVERE, null, ex);
//        }
        
    }
}
