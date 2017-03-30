/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alanmrace.jimzmlparser.obo;

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
public class OBOTest {
    public static final String IMAGING_MS_OBO = "/obo/imagingMS.obo"; 
            
    protected OBO obo;
    
    public OBOTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
//        obo = new OBO(OBOTest.class.getResource(IMAGING_MS_OBO).getPath());
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getOBO method, of class OBO.
     */
    @Test
    public void testGetOBO() {
        System.out.println("getOBO");
        
        OBO obo = OBO.getOBO();
        
        assertNotNull(obo);
    }

    /**
     * Test of getTerm method, of class OBO.
     */
    @Test
    @Ignore
    public void testGetTerm() {
        System.out.println("getTerm");
        String id = "";
        OBO instance = null;
        OBOTerm expResult = null;
        OBOTerm result = instance.getTerm(id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toString method, of class OBO.
     */
    @Test
    @Ignore
    public void testToString() {
        System.out.println("toString");
        OBO instance = null;
        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
