package com.alanmrace.jimzmlparser.mzml;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.binary.Base64;
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
        double[] data = {1.0, 2.54};

        byte[] bytes = new byte[8 * data.length];

        for (int i = 0; i < data.length; i++) {
            ByteBuffer buffer = ByteBuffer.wrap(bytes);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            buffer.putDouble(i * 8, data[i]);
        }

        String encoded = new String(Base64.encodeBase64(bytes));

        System.out.println(encoded);

        byte[] revBytes = Base64.decodeBase64(encoded);

        double[] convertedData = new double[revBytes.length / 8];
        ByteBuffer buffer = ByteBuffer.wrap(revBytes);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        // Convert to double
        for (int j = 0; j < convertedData.length; j++) {
            convertedData[j] = buffer.getDouble();
        }

        System.out.println(convertedData[0]);
        System.out.println(convertedData[1]);
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
