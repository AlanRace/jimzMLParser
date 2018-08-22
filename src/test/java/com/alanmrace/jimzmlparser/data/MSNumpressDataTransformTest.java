/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alanmrace.jimzmlparser.data;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author Alan
 */
public class MSNumpressDataTransformTest {
    
    public MSNumpressDataTransformTest() {
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

    /**
     * Test of forwardTransform method, of class MSNumpressDataTransform.
     */
    @Test
    public void testForwardTransform() throws Exception {
    }

    /**
     * Test of reverseTransform method, of class MSNumpressDataTransform.
     */
    @Test
    public void testLinearTransform() throws Exception {
        double[] data = new double[10877];
        for(int i = 0; i < data.length; i++) {
            data[i] = Math.random() * 1000;
        }
        
        DataTransformation transform = new DataTransformation();
        transform.addTransform(new MSNumpressDataTransform(MSNumpressDataTransform.NumpressAlgorithm.Linear));
        
        byte[] compressedData = transform.performForwardTransform(data);
        
        double[] rawData = transform.performReverseTransform(compressedData);
        
        assertEquals(data.length, rawData.length);
        
        for(int i = 0; i < data.length; i++) {
            assertEquals(data[i], rawData[i], 0.001);
        }
    }
    
    @Test
    public void testPicTransform() throws Exception {
        double[] data = new double[10877];
        for(int i = 0; i < data.length; i++) {
            data[i] = Math.random() * 1000;
        }
        
        DataTransformation transform = new DataTransformation();
        transform.addTransform(new MSNumpressDataTransform(MSNumpressDataTransform.NumpressAlgorithm.Pic));
        
        byte[] compressedData = transform.performForwardTransform(data);
        
        double[] rawData = transform.performReverseTransform(compressedData);
        
        assertEquals(data.length, rawData.length);
        
        for(int i = 0; i < data.length; i++) {
            assertEquals(data[i], rawData[i], 1);
        }
    }
    
    @Test
    public void testSlofTransform() throws Exception {
        double[] data = new double[10877];
        for(int i = 0; i < data.length; i++) {
            data[i] = Math.random() * 1000;
        }
        
        DataTransformation transform = new DataTransformation();
        transform.addTransform(new MSNumpressDataTransform(MSNumpressDataTransform.NumpressAlgorithm.Slof));
        
        byte[] compressedData = transform.performForwardTransform(data);
        
        double[] rawData = transform.performReverseTransform(compressedData);
        
        //assertEquals(data.length, rawData.length);
        
        assertThat(rawData[0], not(0.0));
        
        for(int i = 0; i < data.length; i++) {
            //System.out.println
            assertEquals(data[i], rawData[i], 0.1);
        }
    }
    
}
