/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alanmrace.jimzmlparser.imzml;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author amr1
 */
public class PixelLocationTest {
    

    /**
     * Test of getX method, of class PixelLocation.
     */
    @Test
    @Ignore
    public void testGetX() {
        System.out.println("getX");
        PixelLocation instance = null;
        int expResult = 0;
        int result = instance.getX();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getY method, of class PixelLocation.
     */
    @Test
    @Ignore
    public void testGetY() {
        System.out.println("getY");
        PixelLocation instance = null;
        int expResult = 0;
        int result = instance.getY();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getZ method, of class PixelLocation.
     */
    @Test
    @Ignore
    public void testGetZ() {
        System.out.println("getZ");
        PixelLocation instance = null;
        int expResult = 0;
        int result = instance.getZ();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of equals method, of class PixelLocation.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        Object other = new PixelLocation(1, 2, 3);
        PixelLocation instance = new PixelLocation(1, 2, 3);
        boolean expResult = true;
        boolean result = instance.equals(other);
        assertEquals(expResult, result);
        
        other = new PixelLocation(3, 2, 1);
        instance = new PixelLocation(1, 2, 3);
        expResult = false;
        result = instance.equals(other);
        assertEquals(expResult, result);
    }

    /**
     * Test of hashCode method, of class PixelLocation.
     */
    @Test
    @Ignore
    public void testHashCode() {
        System.out.println("hashCode");
        PixelLocation instance = null;
        int expResult = 0;
        int result = instance.hashCode();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of canEqual method, of class PixelLocation.
     */
    @Test
    @Ignore
    public void testCanEqual() {
        System.out.println("canEqual");
        Object other = null;
        PixelLocation instance = null;
        boolean expResult = false;
        boolean result = instance.canEqual(other);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
