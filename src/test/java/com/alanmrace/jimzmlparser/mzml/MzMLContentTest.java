/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alanmrace.jimzmlparser.mzml;

import static com.alanmrace.jimzmlparser.mzml.BinaryDataArray.binaryDataArrayID;
import static com.alanmrace.jimzmlparser.mzml.BinaryDataArray.mzArrayID;
import com.alanmrace.jimzmlparser.obo.OBO;
import java.util.List;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

/**
 *
 * @author Alan
 */
public class MzMLContentTest {
    @Test
    public void isChildOfTest() {
        assert(OBO.getOBO().getTerm(mzArrayID).isChildOf(binaryDataArrayID));
    }
    
    @Test
    public void getChildrenOfTest() {
        DataProcessing dataProcessing = DataProcessing.create();
        ProcessingMethod method = dataProcessing.getProcessingMethod(0);
        
        CVParam conversion = method.getCVParamOrChild(ProcessingMethod.conversionTomzMLID);
        
        assertNotNull(conversion);
        
        List<CVParam> processingList = method.getChildrenOf(ProcessingMethod.dataTransformationID, false);
        
        assert(!processingList.isEmpty());
        
        double[] mzs = {100.256, 150.326, 200.2565};
        double[] intensities = {1000, 432, 2439.439};
        
        Spectrum spectrum = Spectrum.createSpectrum(mzs, intensities, 1, 1);
        
        List<CVParam> spectrumType = spectrum.getChildrenOf(Spectrum.spectrumTypeID, false);
        
        assert(!spectrumType.isEmpty());
    }
}
