package com.alanmrace.jimzmlparser.parser;

import com.alanmrace.jimzmlparser.exceptions.FatalParseException;
import com.alanmrace.jimzmlparser.mzml.MzML;
import com.alanmrace.jimzmlparser.writer.MzMLWriter;
import java.io.IOException;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for MzMLHeaderHandler.
 * 
 * @author Alan Race
 */
public class MzMLHeaderHandlerTest {
    
    /**
     * Class logger.
     */
    private static final Logger logger = Logger.getLogger(MzMLHeaderHandlerTest.class.getName());
    
    /**
     * Resource mzML file for testing.
     */
    private static final String TEST_RESOURCE = "/2012_5_2_medium_81.mzML";
    /**
     * Resource mzML file for testing (from http://www.psidev.info/mzml_1_0_0%20).
     */
    private static final String TINY_PWIZ_RESOURCE = "/tiny.pwiz.1.1.mzML";
    /**
     * Resource mzML file for testing (modified version of above to have more fields).
     */
    private static final String FULL_PWIZ_RESOURCE = "/full.pwiz.1.1.mzML";
    
    /**
     * Boolean used to check whether an issue has been encountered during validation.
     * Has to be class variable so that it can be used within lambda function/anonymous class.
     */
    private boolean encounteredIssue;

    /**
     * Test of parsemzMLHeader method, of class MzMLHeaderHandler.
     * 
     * @throws Exception ParseException thrown if invalid mzML or file missing
     */
    @Test
    public void testParsemzMLHeader_String_boolean() throws Exception {
        System.out.println(" --- testParsemzMLHeader_String_boolean ---");
        
        assertNotNull("Test file missing", MzMLHeaderHandlerTest.class.getResource(TEST_RESOURCE));
        
        String resourcePath = MzMLHeaderHandlerTest.class.getResource(TEST_RESOURCE).getPath(); //"D:\\Bristol\\141024_HM_02.mzML"; // // //
        
        boolean openDataFile = false;
        MzML result = MzMLHeaderHandler.parsemzMLHeader(resourcePath, openDataFile);
        
        assertNotNull(result);
    }

    /**
     * Test of parsemzMLHeader method, of class MzMLHeaderHandler.
     * 
     * @throws Exception ParseException thrown if invalid mzML or file missing
     */
    @Test
    public void testParsemzMLHeader_String() throws Exception {
        System.out.println(" --- testParsemzMLHeader_String --- ");
        
        assertNotNull("Test file missing", MzMLHeaderHandlerTest.class.getResource(TEST_RESOURCE));
        
        String resourcePath = MzMLHeaderHandlerTest.class.getResource(TEST_RESOURCE).getPath();
        
        MzML result = MzMLHeaderHandler.parsemzMLHeader(resourcePath);
        result.close();
    }

    /**
     * Validate the generated MzML.
     * 
     * @param resourcePath Location of the file to validate
     */
    protected void validateMzML(String resourcePath) {
        encounteredIssue = false;
        
//        MzMLValidator instance = new MzMLValidator();
//        instance.setFile(resourcePath);
//
//        instance.registerImzMLValidatorListener(new ImzMLValidatorListener() {
//            @Override
//            public void startingStep(ImzMLValidator.ValidatorStep step) {
//            }
//
//            @Override
//            public void finishingStep(ImzMLValidator.ValidatorStep step) {
//            }
//
//            @Override
//            public void issueFound(Issue issue) {
//                Logger.getLogger(CreateSpectrumTest.class.getName()).log(Level.INFO, "({0}) {1}", new Object[]{issue.getIssueTitle(), issue.getIssueMessage()});
//                encounteredIssue = true;
//            }
//        });
//        
//        instance.validate();
        
        if(encounteredIssue)
            fail("Encountered too many issues!");
    }
    
    /**
     * Test of parsing and subsequent writing out of MzML (using MzMLHandler.parsemzML) using
     * example file from http://www.psidev.info/mzml_1_0_0%20.
     * 
     * @throws com.alanmrace.jimzmlparser.exceptions.FatalParseException Unrecoverable parse error occurred
     * @throws java.io.IOException Issue reading or writing out the MzML
     */
    @Test
    public void testmzML() throws FatalParseException, IOException {
        String resourcePath = MzMLHeaderHandlerTest.class.getResource(TINY_PWIZ_RESOURCE).getFile();
        
        System.out.println(" ---- testmzML() ---- ");
        System.out.println(resourcePath);
        MzML mzML = MzMLHandler.parsemzML(resourcePath);
        
        MzMLWriter writer = new MzMLWriter();
        resourcePath = resourcePath.replace(".mzML", ".output.mzML");
        
        writer.write(mzML, resourcePath);
        
        //MzML mzMLReloaded = MzMLHandler.parsemzML(resourcePath);
        
        validateMzML(resourcePath);
    }
    
    /**
     * Test of parsing and subsequent writing out of MzML (using MzMLHandler.parsemzMLHeader).
     * 
     * @throws com.alanmrace.jimzmlparser.exceptions.FatalParseException Unrecoverable parse error occurred
     * @throws java.io.IOException Issue reading or writing out the MzML
     */
    @Test
    public void testmzMLHeader() throws FatalParseException, IOException {
        String resourcePath = MzMLHeaderHandlerTest.class.getResource(TINY_PWIZ_RESOURCE).getFile();
        
        System.out.println(" ---- testmzMLHeader() ---- ");
        System.out.println(resourcePath);
        MzML mzML = MzMLHeaderHandler.parsemzMLHeader(resourcePath);
        
        MzMLWriter writer = new MzMLWriter();
        resourcePath = resourcePath.replace(".mzML", ".outputHeader.mzML");
        
        writer.write(mzML, resourcePath);
        
        //MzML mzMLReloaded = MzMLHeaderHandler.parsemzMLHeader(resourcePath);
        
        validateMzML(resourcePath);
    }
    
    /**
     * Test of parsing and subsequent writing out of MzML (using MzMLHandler.parsemzMLHeader).
     * 
     * @throws com.alanmrace.jimzmlparser.exceptions.FatalParseException Unrecoverable parse error occurred
     * @throws java.io.IOException Issue reading or writing out the MzML
     */
    @Test
    public void testfullmzMLHeader() throws FatalParseException, IOException {
        System.out.println(" ---- testfullmzMLHeader() ---- ");
        
        String resourcePath = MzMLHeaderHandlerTest.class.getResource(FULL_PWIZ_RESOURCE).getFile();
        
        MzML mzML = MzMLHeaderHandler.parsemzMLHeader(resourcePath);
        
        MzMLWriter writer = new MzMLWriter();
        resourcePath = resourcePath.replace(".mzML", ".outputHeader.mzML");
        
        writer.write(mzML, resourcePath);
        
        //MzML mzMLReloaded = MzMLHeaderHandler.parsemzMLHeader(resourcePath);
        
        validateMzML(resourcePath);
    }
}
