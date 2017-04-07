/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.obo.OBO;
import com.alanmrace.jimzmlparser.writer.ImzMLWriter;
import com.alanmrace.jimzmlparser.writer.MzMLWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 *
 * @author Alan Race
 */
public class CreateSpectrumTest {
    
    public File targetDir(){
        String relPath = getClass().getProtectionDomain().getCodeSource().getLocation().getFile();
        File targetDir = new File(relPath + "../../target");
        if (!targetDir.exists()) {
            targetDir.mkdir();
        }
        return targetDir;
    }
    
    @Test
    public void createSpectrumTest() {
        System.out.println("---- createSpectrumTest ----");
        
        double[] mzs = {100, 150, 200};
        double[] intensities = {1000, 432, 2439.439};
        
        Spectrum spectrum = Spectrum.createSpectrum(mzs, intensities);
        
        assertNotNull(spectrum);
                
        try {
            double[] mzsBack = spectrum.getmzArray();
        
            assertEquals("Getting m/z when data only stored in memory", 100, mzsBack[0], 0.1);
            
            double[] intensitiesBack = spectrum.getIntensityArray();
        
            assertEquals("Getting intensities when data only stored in memory", 1000, intensitiesBack[0], 0.1);
            
            // Now try outputing the spectral data
            MzMLWriter output = new MzMLWriter(targetDir() + "/spectrum.xml");
            spectrum.outputXML(output, 0);
            output.close();
            
            // Output as imzML
            ImzMLWriter imzMLOutput = new ImzMLWriter(targetDir() + "/spectrum.imzML");
            spectrum.outputXML(imzMLOutput, 0);
            imzMLOutput.close();
            
            // Now try outputing compressed spectral data
            spectrum.getBinaryDataArrayList().getmzArray().removeChildOfCVParam(BinaryDataArray.noCompressionID);
            spectrum.getBinaryDataArrayList().getmzArray().addCVParam(new EmptyCVParam(OBO.getOBO().getTerm(BinaryDataArray.zlibCompressionID)));
            
            output = new MzMLWriter(targetDir() + "/compressed_spectrum.xml");
            spectrum.outputXML(output, 0);
            output.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CreateSpectrumTest.class.getName()).log(Level.SEVERE, null, ex);
            
            fail("Unexpected exception: " + ex);
        } catch (IOException ex) {
            Logger.getLogger(CreateSpectrumTest.class.getName()).log(Level.SEVERE, null, ex);
            
            fail("Unexpected exception: " + ex);
        }
        
    }
}
