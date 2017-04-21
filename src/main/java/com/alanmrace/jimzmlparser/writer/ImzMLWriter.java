package com.alanmrace.jimzmlparser.writer;

import com.alanmrace.jimzmlparser.data.DataTransformation;
import com.alanmrace.jimzmlparser.mzml.BinaryDataArray;
import com.alanmrace.jimzmlparser.mzml.EmptyCVParam;
import com.alanmrace.jimzmlparser.mzml.IntegerCVParam;
import com.alanmrace.jimzmlparser.mzml.LongCVParam;
import com.alanmrace.jimzmlparser.mzml.MzML;
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
    
    @Override
    public void write(MzML mzML, String outputLocation) throws IOException {
        String ibdLocation = outputLocation;
        
        int pos = ibdLocation.lastIndexOf(".");
        if (pos > 0) {
            ibdLocation = ibdLocation.substring(0, pos);
        }

        dataRAF = new RandomAccessFile(ibdLocation + ".ibd", "rw");
        dataOutput = new DataOutputStream(new FileOutputStream(dataRAF.getFD()));
        
        // TODO: Update UUID
        
        super.write(mzML, outputLocation);
        
        // TODO: Update SHA-1
        
        dataOutput.close();
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
    
    @Override
    public boolean shouldOutputIndex() {
        return false;
    }
}
