/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alanmrace.jimzmlparser.mzML;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import java.io.BufferedWriter;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author Alan
 */
public class BinaryTest {
    
    public BinaryTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testBinaryConversion() {
        try {
            double[] data = {1.0, 2.54};
            
            byte[] bytes = new byte[8 * data.length];
            
            for (int i = 0; i < data.length; i++) {
                ByteBuffer buffer = ByteBuffer.wrap(bytes);
                buffer.order(ByteOrder.LITTLE_ENDIAN);
                buffer.putDouble(i * 8, data[i]);
            }
            
            String encoded = Base64.encode(bytes);
            
            System.out.println(encoded);
            
            byte[] revBytes = Base64.decode(encoded);
            
            double[] convertedData = new double[revBytes.length / 8];
            ByteBuffer buffer = ByteBuffer.wrap(revBytes);
            buffer.order(ByteOrder.LITTLE_ENDIAN);

            // Convert to double
            for (int j = 0; j < convertedData.length; j++) {
                convertedData[j] = buffer.getDouble();
            }
            
            System.out.println(convertedData[0]);
            System.out.println(convertedData[1]);
        } catch (Base64DecodingException ex) {
            Logger.getLogger(BinaryTest.class.getName()).log(Level.SEVERE, null, ex);
            
            fail(ex.getLocalizedMessage());
        }
    }
    
    /**
     * Test of getTagName method, of class Binary.
     */
    @Test
    @Ignore
    public void testGetTagName() {
        System.out.println("getTagName");
        Binary instance = null;
        String expResult = "";
        String result = instance.getTagName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addChildrenToCollection method, of class Binary.
     */
    @Test
    @Ignore
    public void testAddChildrenToCollection() {
        System.out.println("addChildrenToCollection");
        Collection<MzMLTag> children = null;
        Binary instance = null;
        instance.addChildrenToCollection(children);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDataTypeFromCV method, of class Binary.
     */
    @Test
    @Ignore
    public void testGetDataTypeFromCV() {
        System.out.println("getDataTypeFromCV");
        CVParam cvParam = null;
        Binary.DataType expResult = null;
        Binary.DataType result = Binary.getDataTypeFromCV(cvParam);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDataTypeCVParam method, of class Binary.
     */
    @Test
    @Ignore
    public void testGetDataTypeCVParam() {
        System.out.println("getDataTypeCVParam");
        Binary instance = null;
        CVParam expResult = null;
        CVParam result = instance.getDataTypeCVParam();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDataType method, of class Binary.
     */
    @Test
    @Ignore
    public void testGetDataType() {
        System.out.println("getDataType");
        Binary instance = null;
        Binary.DataType expResult = null;
        Binary.DataType result = instance.getDataType();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCompressionType method, of class Binary.
     */
    @Test
    @Ignore
    public void testGetCompressionType() {
        System.out.println("getCompressionType");
        Binary instance = null;
        Binary.CompressionType expResult = null;
        Binary.CompressionType result = instance.getCompressionType();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of outputXML method, of class Binary.
     */
    @Test
    @Ignore
    public void testOutputXML() throws Exception {
        System.out.println("outputXML");
        BufferedWriter output = null;
        int indent = 0;
        Binary instance = null;
        instance.outputXML(output, indent);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
