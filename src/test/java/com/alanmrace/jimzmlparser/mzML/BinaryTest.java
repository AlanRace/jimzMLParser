package com.alanmrace.jimzmlparser.mzml;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 * Tests for Binary class.
 * 
 * @author Alan Race
 */
public class BinaryTest {
    
    /**
     * Test the binary data conversion.
     */
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
    public void testGetTagName() {
        System.out.println("getTagName");
        Binary instance = new Binary();
        String expResult = "binary";
        String result = instance.getTagName();
        assertEquals(expResult, result);
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
//        instance.addChildrenToCollection(children);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
