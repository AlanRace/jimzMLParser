package com.alanmrace.jimzmlparser.writer;

import com.alanmrace.jimzmlparser.data.DataTransformation;
import com.alanmrace.jimzmlparser.imzml.PixelLocation;
import com.alanmrace.jimzmlparser.mzml.*;
import com.alanmrace.jimzmlparser.obo.OBO;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.MessageDigest;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;

public abstract class ImzMLWriterAbstract extends ImzMLHeaderWriter {

    /**
     * Logger for the class.
     */
    private static final Logger LOGGER = Logger.getLogger(ImzMLWriterAbstract.class.getName());

    /**
     * RandomAccessFile for the file containing the binary data.
     */
    protected RandomAccessFile dataRAF;

    /**
     * BufferedWriter created from the data RandomAccessFile for writing out to.
     */
    protected DataOutputStream dataOutput;

    /**
     * Possible algorithms to use when generating a checksum of the IBD file.
     */
    public enum Checksum {

        /**
         * SHA-1.
         */
        SHA1,
        /**
         * MD5.
         */
        MD5
    }

    /**
     * How to write the ImzML file, continuous or processed.
     */
    protected ImzMLWriter.OutputType outputType;

    /**
     * Checksum algorithm to use for IBD file.
     */
    protected Checksum checksum;

    /**
     * Current message digest for the checksum.
     */
    protected MessageDigest messageDigest;

    protected String getIBDLocationFromOutput(String outputLocation) {
        String ibdLocation = outputLocation;

        int pos = ibdLocation.toLowerCase().lastIndexOf(".imzml");

        if (pos > 0) {
            ibdLocation = ibdLocation.substring(0, pos);
        }

        return ibdLocation;
    }

    protected void updateMaxCoordinateInformation(MzML mzML) {
        int maxX = 0;
        int maxY = 0;

        if (mzML.getRun().getSpectrumList() != null) {
            for (Spectrum spectrum : mzML.getRun().getSpectrumList()) {
                PixelLocation location = spectrum.getPixelLocation();

                if (location.getX() > maxX) {
                    maxX = location.getX();
                }
                if (location.getY() > maxY) {
                    maxY = location.getY();
                }
            }
        }

        // If the scan settings does not currently exist, make sure to add one
        ScanSettings scanSettings;

        if (mzML.getScanSettingsList() == null) {
            mzML.setScanSettingsList(new ScanSettingsList(1));
        }

        ScanSettingsList scanSettingsList = mzML.getScanSettingsList();

        if (scanSettingsList.size() == 0) {
            scanSettings = new ScanSettings("scanSettings");
            scanSettingsList.add(scanSettings);
        } else {
            scanSettings = mzML.getScanSettingsList().getScanSettings(0);
            scanSettings.removeCVParam(ScanSettings.MAX_COUNT_PIXEL_X_ID);
            scanSettings.removeCVParam(ScanSettings.MAX_COUNT_PIXEL_Y_ID);
        }

        scanSettings.addCVParam(new IntegerCVParam(OBO.getOBO().getTerm(ScanSettings.MAX_COUNT_PIXEL_X_ID), maxX));
        scanSettings.addCVParam(new IntegerCVParam(OBO.getOBO().getTerm(ScanSettings.MAX_COUNT_PIXEL_Y_ID), maxY));
    }

    @Override
    public byte[] prepareData(double[] data, BinaryDataArray binaryDataArray) throws IOException {
        byte[] byteData = null;

        try {
            DataTransformation transformation = binaryDataArray.generateDataTransformation();
            byte[] transformedData = transformation.performForwardTransform(data);

            // If using LZ4, the size of the decompressed data is required before decompression, so add this in as a UserParam that can be used later
            if (binaryDataArray.getCVParam(BinaryDataArray.MSNUMPRESS_LINEAR_LZ4_ID) != null
                    || binaryDataArray.getCVParam(BinaryDataArray.MSNUMPRESS_POSITIVE_LZ4_ID) != null
                    || binaryDataArray.getCVParam(BinaryDataArray.MSNUMPRESS_SLOF_LZ4_ID) != null) {
                int[] dataSizeAtEachStage = transformation.getDataSizeAtEachStage();

                binaryDataArray.addUserParam(new UserParam("LZ4 decompression size", "" + dataSizeAtEachStage[dataSizeAtEachStage.length - 2]));
            }

            //System.out.println(binaryDataArray.getCVParamOrChild(BinaryDataArray.compressionTypeID));
            //System.out.println(Arrays.toString(transformation.getDataSizeAtEachStage()));
            binaryDataArray.removeCVParam(BinaryDataArray.EXTERNAL_DATA_ID);

            binaryDataArray.removeCVParam(BinaryDataArray.EXTERNAL_OFFSET_ID);
            binaryDataArray.addCVParam(new LongCVParam(OBO.getOBO().getTerm(BinaryDataArray.EXTERNAL_OFFSET_ID), getDataPointer()));

            binaryDataArray.removeCVParam(BinaryDataArray.EXTERNAL_ARRAY_LENGTH_ID);
            binaryDataArray.addCVParam(new IntegerCVParam(OBO.getOBO().getTerm(BinaryDataArray.EXTERNAL_ARRAY_LENGTH_ID), data.length));

            binaryDataArray.removeCVParam(BinaryDataArray.EXTERNAL_ENCODED_LENGTH_ID);
            binaryDataArray.addCVParam(new IntegerCVParam(OBO.getOBO().getTerm(BinaryDataArray.EXTERNAL_ENCODED_LENGTH_ID), transformedData.length));

            byteData = transformedData;
        } catch (DataFormatException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

        return byteData;
    }

    @Override
    public void writeData(byte[] data) throws IOException {
        if (data.length > 0) {
            dataOutput.write(data);

            messageDigest.update(data, 0, data.length);
        }
    }

    @Override
    public long getDataPointer() throws IOException {
        dataOutput.flush();

        return dataRAF.getFilePointer();
    }
}
