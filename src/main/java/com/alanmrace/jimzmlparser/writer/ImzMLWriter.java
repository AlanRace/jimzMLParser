package com.alanmrace.jimzmlparser.writer;

import com.alanmrace.jimzmlparser.data.DataTransformation;
import com.alanmrace.jimzmlparser.mzml.BinaryDataArray;
import com.alanmrace.jimzmlparser.mzml.EmptyCVParam;
import com.alanmrace.jimzmlparser.mzml.IntegerCVParam;
import com.alanmrace.jimzmlparser.mzml.LongCVParam;
import com.alanmrace.jimzmlparser.obo.OBO;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;

/**
 * Writer for exporting both the metadata into an imzML file and the binary data
 * to an IBD file.
 * 
 * @author Alan Race
 */
public class ImzMLWriter extends ImzMLHeaderWriter {

    /**
     * RandomAccessFile for the file containing the binary data.
     */
    protected RandomAccessFile dataRAF;

    /**
     * BufferedWriter created from the data RandomAccessFile for writing out to.
     */
    protected DataOutputStream dataOutput;

    /**
     * Create an imzML file and an IBD file at the specified outputLocation. 
     * The extension of the outputLocation will be replaced with .ibd for the 
     * location and name of the IBD file. These files will be open as 'rw' 
     * in a RandomAccessFile.
     * 
     * @param outputLocation Location to write the new imzML file
     * @throws IOException Issue with opening file for writing
     */
    public ImzMLWriter(String outputLocation) throws IOException {
        super(outputLocation);

        int pos = outputLocation.lastIndexOf(".");
        if (pos > 0) {
            outputLocation = outputLocation.substring(0, pos);
        }

        dataRAF = new RandomAccessFile(outputLocation + ".ibd", "rw");
        dataOutput = new DataOutputStream(new FileOutputStream(dataRAF.getFD()));
    }

    @Override
    public void writeData(byte[] data) throws IOException {
        dataOutput.write(data);
    }

    @Override
    public byte[] prepareData(double[] data, BinaryDataArray binayDataArray) throws IOException {
        byte[] byteData = null;
        
        try {
            DataTransformation transformation = binayDataArray.generateDataTransformation();
            byte[] transformedData = transformation.performForwardTransform(data);

            binayDataArray.removeCVParam(BinaryDataArray.externalDataID);
            binayDataArray.addCVParam(new EmptyCVParam(OBO.getOBO().getTerm(BinaryDataArray.externalDataID)));

            binayDataArray.removeCVParam(BinaryDataArray.externalOffsetID);
            binayDataArray.addCVParam(new LongCVParam(OBO.getOBO().getTerm(BinaryDataArray.externalOffsetID), getDataPointer()));

            binayDataArray.removeCVParam(BinaryDataArray.externalArrayLengthID);
            binayDataArray.addCVParam(new IntegerCVParam(OBO.getOBO().getTerm(BinaryDataArray.externalArrayLengthID), data.length));

            binayDataArray.removeCVParam(BinaryDataArray.externalEncodedLengthID);
            binayDataArray.addCVParam(new IntegerCVParam(OBO.getOBO().getTerm(BinaryDataArray.externalEncodedLengthID), transformedData.length));

            byteData = transformedData;
        } catch (DataFormatException ex) {
            Logger.getLogger(MzMLWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return byteData;
    }
    
    @Override
    public long getDataPointer() throws IOException {
        dataOutput.flush();

        return dataRAF.getFilePointer();
    }
}
