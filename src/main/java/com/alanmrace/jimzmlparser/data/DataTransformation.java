package com.alanmrace.jimzmlparser.data;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;

/**
 *
 * @author Alan Race
 */
public class DataTransformation {

    protected List<DataTransform> transformation;
    
    public DataTransformation() {
        transformation = new ArrayList<DataTransform>();
    }
    
    public void addTransform(DataTransform transform) {
        transformation.add(transform);
    }
    
    public byte[] performForwardTransform(double[] data) throws DataFormatException {
        byte[] byteData = DataTypeTransform.convertDoublesToBytes(data);
        
        return performForwardTransform(byteData);
    }
    
    public byte[] performForwardTransform(byte[] data) throws DataFormatException {
        byte[] transformedData = data;
        
        for(DataTransform transform : transformation) {
            transformedData = transform.forwardTransform(transformedData);
        }
        
        return transformedData;
    }
    
    public double[] performReverseTransform(byte[] data) throws DataFormatException {
        byte[] transformedData = data;
        
        for(DataTransform transform : transformation) {
            transformedData = transform.reverseTransform(transformedData);
        }
        
        return DataTypeTransform.convertDataToDouble(transformedData, DataTypeTransform.DataType.Double);
    }
}
