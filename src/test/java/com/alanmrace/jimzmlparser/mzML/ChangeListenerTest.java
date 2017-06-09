/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.mzml.FileContent;
import com.alanmrace.jimzmlparser.mzml.MzMLContent;
import com.alanmrace.jimzmlparser.mzml.MzMLContentListener;
import com.alanmrace.jimzmlparser.mzml.Scan;
import com.alanmrace.jimzmlparser.mzml.IntegerCVParam;
import com.alanmrace.jimzmlparser.obo.OBO;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author alan.race
 */
public class ChangeListenerTest {
    
    int count = 0;
    
    @Test
    public void testChangeListener() {
        System.out.println("----------");
        System.out.println("Testing change listener");
        System.out.println("----------");
        
        FileContent content = FileContent.create();
        content.addListener(new MzMLContentListener() {
            @Override
            public void changeMade(MzMLContent content) {
                count++;
            }
        });
        
        CVParam cvParam = new IntegerCVParam(OBO.getOBO().getTerm(Scan.positionXID), 0);
        content.addCVParam(cvParam);
        
        cvParam.setValueAsString("100");
        
        content.removeCVParam(cvParam);
        cvParam.setValueAsString("200");
        
        assertEquals("Number of changes called", 3, count);
    }
}
