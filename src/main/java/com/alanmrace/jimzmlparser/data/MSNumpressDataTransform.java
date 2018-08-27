/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alanmrace.jimzmlparser.data;

import java.util.Arrays;
import java.util.zip.DataFormatException;
import ms.numpress.MSNumpress;

/**
 *
 * @author Alan
 */
public class MSNumpressDataTransform implements DataTransform {

    public enum NumpressAlgorithm {
        LINEAR,
        PIC,
        SLOF
    }
    
    private final NumpressAlgorithm algorithm;
    private String accession;
    private double mzError = 1e5;
    
    public MSNumpressDataTransform(NumpressAlgorithm algorithm) {
        switch(algorithm) {
            case LINEAR:
                accession = MSNumpress.ACC_NUMPRESS_LINEAR;
                break;
            case PIC:
                accession = MSNumpress.ACC_NUMPRESS_PIC;
                break;
            case SLOF:
                accession = MSNumpress.ACC_NUMPRESS_SLOF;
                break;
        }
        
        this.algorithm = algorithm;
    }
    
    @Override
    public byte[] forwardTransform(byte[] data) throws DataFormatException {
        byte[] encoded = new byte[data.length];
        double[] dataAsDouble = DataTypeTransform.convertDataToDouble(data, DataTypeTransform.DataType.DOUBLE);
        int numBytes = -1;
        
        switch(algorithm) {
            case LINEAR:
                numBytes = MSNumpress.encodeLinear(dataAsDouble, dataAsDouble.length, encoded, mzError);
                
                break;
            case PIC:
                numBytes = MSNumpress.encodePic(dataAsDouble, dataAsDouble.length, encoded);
                
                break;
            case SLOF:
                double fixedPoint = MSNumpress.optimalSlofFixedPoint(dataAsDouble, dataAsDouble.length);
                numBytes = MSNumpress.encodeSlof(dataAsDouble, dataAsDouble.length, encoded, fixedPoint);
                
                break;
        }
        
        if(numBytes >= 0 && numBytes != encoded.length)
            encoded = Arrays.copyOfRange(encoded, 0, numBytes);
        
        return encoded;
    }

    @Override
    public byte[] reverseTransform(byte[] data) throws DataFormatException {
        double[] result = MSNumpress.decode(accession, data, data.length);
        
        return DataTypeTransform.convertDoublesToBytes(result);
    }
    
}
