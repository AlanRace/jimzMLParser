package com.alanmrace.jimzmlparser.parser;

import com.alanmrace.jimzmlparser.exceptions.FatalParseException;
import com.alanmrace.jimzmlparser.mzml.MzML;
import com.alanmrace.jimzmlparser.writer.MzMLWriter;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;

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
    private static final String TINY_PWIZ_RESOURCE = "/tiny.pwiz.1.1.mzML";

    /**
     * Test of setDocumentLocator method, of class MzMLHeaderHandler.
     */
    @Test
    @Ignore
    public void testSetDocumentLocator() {
        System.out.println("setDocumentLocator");
        Locator locator = null;
        MzMLHeaderHandler instance = null;
        instance.setDocumentLocator(locator);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setOpenDataStorage method, of class MzMLHeaderHandler.
     */
    @Test
    @Ignore
    public void testSetOpenDataStorage() {
        System.out.println("setOpenDataStorage");
        boolean openDataStorage = false;
        MzMLHeaderHandler instance = null;
        instance.setOpenDataStorage(openDataStorage);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of parsemzMLHeader method, of class MzMLHeaderHandler.
     * 
     * @throws Exception ParseException thrown if invalid mzML or file missing
     */
    @Test
    public void testParsemzMLHeader_String_boolean() throws Exception {
        System.out.println("parsemzMLHeader");
        
        assertNotNull("Test file missing", MzMLHeaderHandlerTest.class.getResource(TEST_RESOURCE));
        
        String resourcePath = MzMLHeaderHandlerTest.class.getResource(TEST_RESOURCE).getPath(); //"D:\\Bristol\\141024_HM_02.mzML"; // // //
        
        boolean openDataFile = false;
        MzML result = MzMLHeaderHandler.parsemzMLHeader(resourcePath, openDataFile);
        
        //result.write("test.mzML");
    }

    /**
     * Test of parsemzMLHeader method, of class MzMLHeaderHandler.
     * 
     * @throws Exception ParseException thrown if invalid mzML or file missing
     */
    @Test
    public void testParsemzMLHeader_String() throws Exception {
        System.out.println("parsemzMLHeader");
        
        assertNotNull("Test file missing", MzMLHeaderHandlerTest.class.getResource(TEST_RESOURCE));
        
        String resourcePath = MzMLHeaderHandlerTest.class.getResource(TEST_RESOURCE).getPath();
        
        MzML result = MzMLHeaderHandler.parsemzMLHeader(resourcePath);
        result.close();
    }

    /**
     * Test of startElement method, of class MzMLHeaderHandler.
     * 
     * @throws Exception ParseException thrown if invalid mzML or file missing
     */
    @Test
    @Ignore
    public void testStartElement() throws Exception {
        System.out.println("startElement");
        String uri = "";
        String localName = "";
        String qName = "";
        Attributes attributes = null;
        MzMLHeaderHandler instance = null;
        instance.startElement(uri, localName, qName, attributes);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of characters method, of class MzMLHeaderHandler.
     * 
     * @throws Exception ParseException thrown if invalid mzML or file missing
     */
    @Test
    @Ignore
    public void testCharacters() throws Exception {
        System.out.println("characters");
        char[] ch = null;
        int start = 0;
        int length = 0;
        MzMLHeaderHandler instance = null;
        instance.characters(ch, start, length);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of endElement method, of class MzMLHeaderHandler.
     * 
     * @throws Exception ParseException thrown if invalid mzML or file missing
     */
    @Test
    @Ignore
    public void testEndElement() throws Exception {
        System.out.println("endElement");
        String uri = "";
        String localName = "";
        String qName = "";
        MzMLHeaderHandler instance = null;
        instance.endElement(uri, localName, qName);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getmzML method, of class MzMLHeaderHandler.
     */
    @Test
    @Ignore
    public void testGetmzML() {
        System.out.println("getmzML");
        MzMLHeaderHandler instance = null;
        MzML expResult = null;
        MzML result = instance.getmzML();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testmzML() throws FatalParseException, IOException {
        String resourcePath = MzMLHeaderHandlerTest.class.getResource(TINY_PWIZ_RESOURCE).getFile();
        
        System.out.println(" ---- testmzML() ---- ");
        System.out.println(resourcePath);
        MzML mzML = MzMLHandler.parsemzML(resourcePath);
        
        MzMLWriter writer = new MzMLWriter();
        writer.write(mzML, resourcePath.replace(".mzML", ".output.mzML"));
        
        MzML mzMLReloaded = MzMLHandler.parsemzML(resourcePath);
        
        // TODO: Compare?
    }
    
    @Test
    public void testmzMLHeader() throws FatalParseException, IOException {
        String resourcePath = MzMLHeaderHandlerTest.class.getResource(TINY_PWIZ_RESOURCE).getFile();
        
        System.out.println(" ---- testmzML() ---- ");
        System.out.println(resourcePath);
        MzML mzML = MzMLHeaderHandler.parsemzMLHeader(resourcePath);
        
        MzMLWriter writer = new MzMLWriter();
        writer.write(mzML, resourcePath.replace(".mzML", ".outputHeader.mzML"));
        
        MzML mzMLReloaded = MzMLHeaderHandler.parsemzMLHeader(resourcePath);
        
        // TODO: Compare?
    }
}
