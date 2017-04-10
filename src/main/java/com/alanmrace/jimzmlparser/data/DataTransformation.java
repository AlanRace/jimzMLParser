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
        for(DataTransform transform : transformation) {
            data = transform.forwardTransform(data);
        }
        
        return data;
    }
    
    public double[] performReverseTransform(byte[] data) throws DataFormatException {
        for(DataTransform transform : transformation) {
            data = transform.reverseTransform(data);
        }
        
        return DataTypeTransform.convertDataToDouble(data, DataTypeTransform.DataType.Double);
    }
}
