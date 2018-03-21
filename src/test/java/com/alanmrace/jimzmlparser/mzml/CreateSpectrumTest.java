/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.data.DataTypeTransform;
import com.alanmrace.jimzmlparser.data.DataTypeTransform.DataType;
import com.alanmrace.jimzmlparser.exceptions.FatalParseException;
import com.alanmrace.jimzmlparser.exceptions.Issue;
import com.alanmrace.jimzmlparser.exceptions.MzMLParseException;
import com.alanmrace.jimzmlparser.imzml.ImzML;
import com.alanmrace.jimzmlparser.obo.OBO;
import com.alanmrace.jimzmlparser.parser.ImzMLHandler;
import com.alanmrace.jimzmlparser.parser.MzMLHeaderHandler;
import com.alanmrace.jimzmlparser.parser.ParserListener;
import com.alanmrace.jimzmlparser.writer.ImzMLWriter;
import com.alanmrace.jimzmlparser.writer.MzMLWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;
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
        
        double[] mzs = {100.256, 150.326, 200.2565};
        double[] intensities = {1000, 432, 2439.439};
        
        MzML mzML = MzML.create();
        ImzML imzML = ImzML.create();
        
        Spectrum spectrum = Spectrum.createSpectrum(mzs, intensities, 1, 1);
        mzML.getRun().getSpectrumList().add(spectrum);
        imzML.getRun().getSpectrumList().add(spectrum);
        
        assertNotNull(spectrum);
                
        try {
            double[] mzsBack = spectrum.getmzArray();
        
            assertEquals("Getting m/z when data only stored in memory", mzs[0], mzsBack[0], 0.1);
            
            double[] intensitiesBack = spectrum.getIntensityArray();
        
            assertEquals("Getting intensities when data only stored in memory", intensities[0], intensitiesBack[0], 0.1);
            
            MzMLWriter output = new MzMLWriter();
            // Now try outputing the spectral data
            //output ;
            //mzML.outputXML(output, 0);
            //output.close();
            output.write(mzML, targetDir() + "/spectrum.mzML");
//            mzML.write(targetDir() + "/spectrum.mzML");
            
            MzML mzMLBack = MzMLHeaderHandler.parsemzMLHeader(targetDir() + "/spectrum.mzML");
            Spectrum mzMLSpectrum = mzMLBack.getRun().getSpectrumList().getSpectrum(0);
            double[] mzMLmzs = mzMLSpectrum.getmzArray(true);
            
            System.out.println(mzMLmzs);
            System.out.println(mzs);
            
            assertEquals("Getting m/z back from mzML", mzs[0], mzMLmzs[0], 0.1);
            
            // Output as imzML
            ImzMLWriter imzMLOutput = new ImzMLWriter();
            //spectrum.outputXML(imzMLOutput, 0);
            //imzMLOutput.close();
            
            imzMLOutput.write(imzML, targetDir() + "/spectrum.imzML");
            
            // Now try outputing compressed spectral data
            spectrum.getBinaryDataArrayList().getmzArray().removeChildOfCVParam(BinaryDataArray.noCompressionID);
            spectrum.getBinaryDataArrayList().getmzArray().addCVParam(new EmptyCVParam(OBO.getOBO().getTerm(BinaryDataArray.zstdCompressionID)));
            
            //output = new MzMLWriter(targetDir() + "/compressed_spectrum.xml");
            //spectrum.outputXML(output, 0);
            //output.close();
            
            ImzMLWriter compressed = new ImzMLWriter();
            
            compressed.write(imzML, targetDir() + "/compressed_spectrum.imzML");
            
            ImzML compressedmzML = ImzMLHandler.parseimzML(targetDir() + "/compressed_spectrum.imzML");
            Spectrum compressedSpectrum = compressedmzML.getRun().getSpectrumList().getSpectrum(0);
            double[] compressedmzs = compressedSpectrum.getmzArray();
            
            for(int i = 0; i < compressedmzs.length; i++) {
                assertEquals("Getting m/z back from mzML: " + i, mzs[i], compressedmzs[i], 0.0001);
            }
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CreateSpectrumTest.class.getName()).log(Level.SEVERE, null, ex);
            
            fail("Unexpected exception: " + ex);
        } catch (IOException ex) {
            Logger.getLogger(CreateSpectrumTest.class.getName()).log(Level.SEVERE, null, ex);
            
            fail("Unexpected exception: " + ex);
        } catch (MzMLParseException ex) {
            Logger.getLogger(CreateSpectrumTest.class.getName()).log(Level.SEVERE, null, ex);
            
            fail("Unexpected exception: " + ex);
        } catch (FatalParseException ex) {
            Logger.getLogger(CreateSpectrumTest.class.getName()).log(Level.SEVERE, null, ex);
            
            fail("Unexpected exception: " + ex);
        }
        
    }
    
    @Test
    public void modifySpectrumTest() {
        System.out.println("---- modifySpectrumTest ----");
        
        double[] mzs = {100, 150, 200};
        double[] intensities = {1000, 432, 2439.439};
        
        ImzMLWriter writer = new ImzMLWriter();
        
        ImzML imzML = ImzML.create();
        
        Spectrum spectrum = Spectrum.createSpectrum(mzs, intensities, 1, 1);
        imzML.getRun().getSpectrumList().add(spectrum);
        
        spectrum = Spectrum.createSpectrum(mzs, intensities, 1, 2);
        imzML.getRun().getSpectrumList().add(spectrum);
        
        
        DataProcessing processing = new DataProcessing("modification");
        ProcessingMethod method = new ProcessingMethod(Software.create());
        processing.add(method);
        method.addCVParam(new EmptyCVParam(OBO.getOBO().getTerm(ProcessingMethod.conversionTomzMLID)));
        
        spectrum.updatemzArray(intensities, processing);
        
        // Now add a spectrum with DataProcessing that is not part of the list previously
        processing = new DataProcessing("extra-addition");
        method = new ProcessingMethod(Software.create());
        processing.add(method);
        method.addCVParam(new EmptyCVParam(OBO.getOBO().getTerm(ProcessingMethod.conversionTomzMLID)));
        
        spectrum = Spectrum.createSpectrum(mzs, intensities, processing, 1, 3);
        imzML.getRun().getSpectrumList().add(spectrum);
        
        try {
            writer.write(imzML, targetDir() + "/modifySpectrumTest.imzML");
            
            // Check that we can read in the written imzML without any issues - i.e. whether
            // it is valid enough to parse
            ImzML imzMLBack = ImzMLHandler.parseimzML(targetDir() + "/modifySpectrumTest.imzML", false, new ParserListener() {
                @Override
                public void issueFound(Issue exception) {
                    Logger.getLogger(CreateSpectrumTest.class.getName()).log(Level.SEVERE, null, exception);
            
                    System.out.println(" ---- " + exception.getIssueTitle() + " ---- ");
                    System.out.println(exception.getIssueMessage());
                    fail("Unexpected exception: " + exception);
                }
                
            });
            
//            ImzMLValidator instance = new ImzMLValidator();
//            instance.setFile(targetDir() + "/modifySpectrumTest.imzML");
//
//            instance.registerImzMLValidatorListener(new ImzMLValidatorListener() {
//                @Override
//                public void startingStep(ImzMLValidator.ValidatorStep step) {
//                }
//
//                @Override
//                public void finishingStep(ImzMLValidator.ValidatorStep step) {
//                }
//
//                @Override
//                public void issueFound(Issue issue) {
//                    if (issue instanceof UnexpectedCVParamMappingRuleException) {
//                        Logger.getLogger(CreateSpectrumTest.class.getName()).info(issue.getIssueMessage());
//                    } else {
//                        fail("Unexpected issue found: " + issue);
//                    }
//                }
//            });
//
//            instance.validate();
        } catch (IOException ex) {
            Logger.getLogger(CreateSpectrumTest.class.getName()).log(Level.SEVERE, null, ex);
            
            fail("Unexpected exception: " + ex);
        } catch (FatalParseException ex) {
            Logger.getLogger(CreateSpectrumTest.class.getName()).log(Level.SEVERE, null, ex);
            
            fail("Unexpected exception: " + ex);
        }
    }
    
    @Test
    public void testDataTypeTransform() {
        double[] array = {1.25, 65.39};
        byte[] in = DataTypeTransform.convertDoublesToBytes(array);
        
        DataTypeTransform trans = new DataTypeTransform(DataType.Double, DataType.Integer64bit);
        
        try {
            byte[] out = trans.forwardTransform(in);
            double[] convertedArray = DataTypeTransform.convertDataToDouble(out, DataType.Integer64bit);
            
            assertEquals("Converted from double to int to double", 1.25, convertedArray[0], 1);
            assertEquals("Converted from double to int to double", 65.39, convertedArray[1], 1);
            
            byte[] inBack = trans.reverseTransform(out);
            double[] convertedBackArray = DataTypeTransform.convertDataToDouble(inBack, DataType.Double);
            
            assertEquals("Converted from double to int to double (back)", 1.25, convertedBackArray[0], 1);
            assertEquals("Converted from double to int to double (back)", 65.39, convertedBackArray[1], 1);
        } catch (DataFormatException ex) {
            Logger.getLogger(CreateSpectrumTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
