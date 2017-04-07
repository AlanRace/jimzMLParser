/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alanmrace.jimzmlparser.mzml;

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
            MzMLWriter output = new MzMLWriter(targetDir() + "/spectrum.xml");
            
            spectrum.outputXML(output, 0);
            
            output.close();
            
            ImzMLWriter imzMLOutput = new ImzMLWriter(targetDir() + "/spectrum.imzML");
            
            spectrum.outputXML(imzMLOutput, 0);
            
            imzMLOutput.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CreateSpectrumTest.class.getName()).log(Level.SEVERE, null, ex);
            
            fail("Unexpected exception: " + ex);
        } catch (IOException ex) {
            Logger.getLogger(CreateSpectrumTest.class.getName()).log(Level.SEVERE, null, ex);
            
            fail("Unexpected exception: " + ex);
        }
        
    }
}
