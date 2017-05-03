package com.alanmrace.jimzmlparser.writer;

import com.alanmrace.jimzmlparser.data.DataTransformation;
import com.alanmrace.jimzmlparser.mzml.BinaryDataArray;
import com.alanmrace.jimzmlparser.mzml.Chromatogram;
import com.alanmrace.jimzmlparser.mzml.EmptyCVParam;
import com.alanmrace.jimzmlparser.mzml.FileContent;
import com.alanmrace.jimzmlparser.mzml.IntegerCVParam;
import com.alanmrace.jimzmlparser.mzml.LongCVParam;
import com.alanmrace.jimzmlparser.mzml.MzML;
import com.alanmrace.jimzmlparser.mzml.Spectrum;
import com.alanmrace.jimzmlparser.mzml.StringCVParam;
import com.alanmrace.jimzmlparser.obo.OBO;
import com.alanmrace.jimzmlparser.util.HexHelper;
import com.alanmrace.jimzmlparser.util.UUIDHelper;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
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

    public enum OutputType {
        Continuous,
        Processed
    }

    protected OutputType outputType;

    public enum Checksum {
        SHA1,
        MD5
    }

    protected Checksum checksum;
    protected MessageDigest messageDigest;

    public ImzMLWriter() {
        super();

        this.outputType = OutputType.Processed;
        checksum = Checksum.SHA1;
    }

    @Override
    public void write(MzML mzML, String outputLocation) throws IOException {
        try {
            String ibdLocation = outputLocation;

            int pos = ibdLocation.lastIndexOf(".");
            if (pos > 0) {
                ibdLocation = ibdLocation.substring(0, pos);
            }

            // Update the imzML header information about storage type
            FileContent fileContent = mzML.getFileDescription().getFileContent();
            fileContent.removeChildOfCVParam(FileContent.binaryTypeID);

            switch (outputType) {
                case Continuous:
                    fileContent.addCVParam(new EmptyCVParam(OBO.getOBO().getTerm(FileContent.binaryTypeContinuousID)));
                    break;
                case Processed:
                default:
                    fileContent.addCVParam(new EmptyCVParam(OBO.getOBO().getTerm(FileContent.binaryTypeProcessedID)));
                    break;
            }

            // Set up the checksum
            // TODO: move this to a different method?
            String algorithm;

            switch (checksum) {
                case MD5:
                    algorithm = "md5";
                    break;
                case SHA1:
                default:
                    algorithm = "sha-1";
                    break;
            }

            messageDigest = MessageDigest.getInstance(algorithm);

            // Open the IBD file ready for writing, using the RandomAccessFile to get
            // current location
            dataRAF = new RandomAccessFile(ibdLocation + ".ibd", "rw");
            dataOutput = new DataOutputStream(new FileOutputStream(dataRAF.getFD()));

            // Create new UUID and write it to the start of the IBD file
            UUID uuid = UUID.randomUUID();
            writeData(UUIDHelper.uuidToByteArray(uuid));

            // TODO: For continuous data only write the m/z list once
            for (Spectrum spectrum : mzML.getRun().getSpectrumList()) {
                for (BinaryDataArray bda : spectrum.getBinaryDataArrayList()) {
                    double[] ddata = bda.getDataAsDouble();
                    byte[] bdata = prepareData(ddata, bda);

                    writeData(bdata);
                }
            }

            // Write out all chromatograms
            for (Chromatogram chromatogram : mzML.getRun().getChromatogramList()) {
                for (BinaryDataArray bda : chromatogram.getBinaryDataArrayList()) {
                    double[] ddata = bda.getDataAsDouble();
                    byte[] bdata = prepareData(ddata, bda);

                    writeData(bdata);
                }
            }
            
            dataRAF.setLength(dataRAF.getFilePointer());
            dataOutput.close();

            // Update UUID in the metadata
            fileContent.removeCVParam(FileContent.uuidIdntificationID);
            fileContent.addCVParam(new StringCVParam(OBO.getOBO().getTerm(FileContent.uuidIdntificationID), uuid.toString()));

            // Update SHA-1
            fileContent.removeChildOfCVParam(FileContent.ibdChecksumID);
            fileContent.addCVParam(new StringCVParam(OBO.getOBO().getTerm(FileContent.sha1ChecksumID), HexHelper.byteArrayToHexString(messageDigest.digest())));

            // Write out metadata
            super.write(mzML, outputLocation);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ImzMLWriter.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void writeData(byte[] data) throws IOException {
        if (data.length > 0) {
            dataOutput.write(data);
            
            messageDigest.update(data, 0, data.length);
        }
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
