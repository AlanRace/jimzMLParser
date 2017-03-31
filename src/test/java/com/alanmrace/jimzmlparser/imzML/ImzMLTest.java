/*
 * 
 */
package com.alanmrace.jimzmlparser.imzml;

import com.alanmrace.jimzmlparser.exceptions.ImzMLParseException;
import com.alanmrace.jimzmlparser.mzml.Spectrum;
import com.alanmrace.jimzmlparser.parser.ImzMLHandler;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author amr1
 */
public class ImzMLTest {
    
    private static final Logger logger = Logger.getLogger(ImzMLTest.class.getName());
    
    public static final String TEST_RESOURCE = "/MatrixTests_N2.imzML"; // "/2012_5_2_medium(120502,20h18m).wiff"; 
    
    private ImzML instance;
    
       
    @BeforeClass
    public static void setUpClass() {
        System.out.println("Setting up MzMLToImzMLConverterTest");
        assertNotNull("Test file missing", ImzMLTest.class.getResource(TEST_RESOURCE));
    }

    @Before
    public void setUp() {
        try {
            instance = ImzMLHandler.parseimzML(ImzMLTest.class.getResource(TEST_RESOURCE).getPath());
        } catch (ImzMLParseException ex) {
            Logger.getLogger(ImzMLTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("ImzMLParseException: " + ex);
        }
    }

    /**
     * Test of getFullmzList method, of class ImzML.
     */
    @Test
    public void testGetFullmzList() {
        System.out.println("getFullmzList");
        
//        double[] expResult = null;
        double[] result = instance.getFullmzList();
        
        assertNotNull(result);
        
        assertEquals(50, result[0], 1);
        assertEquals(2000, result[result.length-1], 1);
        
        
//        assertArrayEquals(expResult, result, 0.01);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of getSpectrum method, of class ImzML.
     */
    @Test
    @Ignore
    public void testGetSpectrum_int_int() {
        System.out.println("getSpectrum");
        int x = 0;
        int y = 0;
        ImzML instance = null;
        Spectrum expResult = null;
        Spectrum result = instance.getSpectrum(x, y);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSpectrum method, of class ImzML.
     */
    @Test
    @Ignore
    public void testGetSpectrum_3args() {
        System.out.println("getSpectrum");
        int x = 0;
        int y = 0;
        int z = 0;
        ImzML instance = null;
        Spectrum expResult = null;
        Spectrum result = instance.getSpectrum(x, y, z);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getWidth method, of class ImzML.
     */
    @Test
    @Ignore
    public void testGetWidth() {
        System.out.println("getWidth");
        ImzML instance = null;
        int expResult = 0;
        int result = instance.getWidth();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getHeight method, of class ImzML.
     */
    @Test
    @Ignore
    public void testGetHeight() {
        System.out.println("getHeight");
        ImzML instance = null;
        int expResult = 0;
        int result = instance.getHeight();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDepth method, of class ImzML.
     */
    @Test
    @Ignore
    public void testGetDepth() {
        System.out.println("getDepth");
        ImzML instance = null;
        int expResult = 0;
        int result = instance.getDepth();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMinimumDetectedmz method, of class ImzML.
     */
    @Test
    @Ignore
    public void testGetMinimumDetectedmz() {
        System.out.println("getMinimumDetectedmz");
        ImzML instance = null;
        double expResult = 0.0;
        double result = instance.getMinimumDetectedmz();
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMaximumDetectedmz method, of class ImzML.
     */
    @Test
    @Ignore
    public void testGetMaximumDetectedmz() {
        System.out.println("getMaximumDetectedmz");
        ImzML instance = null;
        double expResult = 0.0;
        double result = instance.getMaximumDetectedmz();
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getIBDFile method, of class ImzML.
     */
    @Test
    @Ignore
    public void testGetIBDFile() {
        System.out.println("getIBDFile");
        ImzML instance = null;
        File expResult = null;
        File result = instance.getIBDFile();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isProcessed method, of class ImzML.
     */
    @Test
    @Ignore
    public void testIsProcessed() {
        System.out.println("isProcessed");
        ImzML instance = null;
        boolean expResult = false;
        boolean result = instance.isProcessed();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isContinuous method, of class ImzML.
     */
    @Test
    @Ignore
    public void testIsContinuous() {
        System.out.println("isContinuous");
        ImzML instance = null;
        boolean expResult = false;
        boolean result = instance.isContinuous();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getBinnedmzList method, of class ImzML.
     */
    @Test
    @Ignore
    public void testGetBinnedmzList() {
        System.out.println("getBinnedmzList");
        double minMZ = 0.0;
        double maxMZ = 0.0;
        double binSize = 0.0;
        ImzML instance = null;
        double[] expResult = null;
        double[] result = instance.getBinnedmzList(minMZ, maxMZ, binSize);
        assertArrayEquals(expResult, result, 0.01);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of generateTICImage method, of class ImzML.
     */
    @Test
    @Ignore
    public void testGenerateTICImage() {
        System.out.println("generateTICImage");
        ImzML instance = null;
        double[][] expResult = null;
        double[][] result = instance.generateTICImage();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setibdFile method, of class ImzML.
     */
    @Test
    @Ignore
    public void testSetibdFile() {
        System.out.println("setibdFile");
        File ibdFile = null;
        ImzML instance = null;
        instance.setibdFile(ibdFile);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of write method, of class ImzML.
     */
    @Test
    @Ignore
    public void testWrite_0args() throws Exception {
        System.out.println("write");
        ImzML instance = null;
        instance.write();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of write method, of class ImzML.
     */
    @Test
    @Ignore
    public void testWrite_String() throws Exception {
        System.out.println("write");
        String filename = "";
        ImzML instance = null;
        instance.write(filename);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
