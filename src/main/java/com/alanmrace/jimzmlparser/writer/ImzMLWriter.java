package com.alanmrace.jimzmlparser.writer;

import com.alanmrace.jimzmlparser.data.DataTransformation;
import com.alanmrace.jimzmlparser.imzml.PixelLocation;
import com.alanmrace.jimzmlparser.mzml.BinaryDataArray;
import com.alanmrace.jimzmlparser.mzml.BooleanCVParam;
import com.alanmrace.jimzmlparser.mzml.CV;
import com.alanmrace.jimzmlparser.mzml.CVParam;
import com.alanmrace.jimzmlparser.mzml.Chromatogram;
import com.alanmrace.jimzmlparser.mzml.DoubleCVParam;
import com.alanmrace.jimzmlparser.mzml.EmptyCVParam;
import com.alanmrace.jimzmlparser.mzml.FileContent;
import com.alanmrace.jimzmlparser.mzml.IntegerCVParam;
import com.alanmrace.jimzmlparser.mzml.LongCVParam;
import com.alanmrace.jimzmlparser.mzml.MzML;
import com.alanmrace.jimzmlparser.mzml.ReferenceableParamGroup;
import com.alanmrace.jimzmlparser.mzml.ScanSettings;
import com.alanmrace.jimzmlparser.mzml.ScanSettingsList;
import com.alanmrace.jimzmlparser.mzml.Spectrum;
import com.alanmrace.jimzmlparser.mzml.StringCVParam;
import com.alanmrace.jimzmlparser.mzml.UserParam;
import com.alanmrace.jimzmlparser.obo.OBO;
import com.alanmrace.jimzmlparser.util.HexHelper;
import com.alanmrace.jimzmlparser.util.UUIDHelper;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
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
     * Logger for the class.
     */
    private static final Logger LOGGER = Logger.getLogger(ImzMLWriter.class.getName());

    /**
     * RandomAccessFile for the file containing the binary data.
     */
    protected RandomAccessFile dataRAF;

    /**
     * BufferedWriter created from the data RandomAccessFile for writing out to.
     */
    protected DataOutputStream dataOutput;

    /**
     * Possible means of outputting data, as defined by the ImzML standard.
     * Continuous type means that each spectrum of an image has the same m/z
     * values. Processed type means that each spectrum has its own m/z array.
     */
    public enum OutputType {
        /**
         * Continuous type means that each spectrum of an image has the same m/z
         * values.
         */
        CONTINUOUS,
        /**
         * Processed type means that each spectrum has its own m/z array.
         */
        PROCESSED
    }

    /**
     * How to write the ImzML file, continuous or processed.
     */
    protected OutputType outputType;

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
     * Checksum algorithm to use for IBD file.
     */
    protected Checksum checksum;

    /**
     * Current message digest for the checksum.
     */
    protected MessageDigest messageDigest;

    /**
     * Create an ImzMLWriter with defaults (processed output type and SHA-1
     * checksum).
     */
    public ImzMLWriter() {
        super();

        this.outputType = OutputType.PROCESSED;
        checksum = Checksum.SHA1;
    }

    protected String getIBDLocationFromOutput(String outputLocation) {
        String ibdLocation = outputLocation;

        int pos = ibdLocation.toLowerCase().lastIndexOf(".imzml");

        if (pos > 0) {
            ibdLocation = ibdLocation.substring(0, pos);
        }

        return ibdLocation;
    }

    protected void updateOBOList(MzML mzML) {
        mzML.getCVList().clear();
        OBO obo = OBO.getOBO();
        List<OBO> fullOBOList = obo.getFullImportHeirarchy();

        for (OBO currentOBO : fullOBOList) {
            mzML.getCVList().addCV(new CV(currentOBO));
        }
    }

    protected void updateStorageInformation(MzML mzML) {
        FileContent fileContent = mzML.getFileDescription().getFileContent();
        fileContent.removeChildrenOfCVParam(FileContent.BINARY_TYPE_ID, false);

        switch (outputType) {
            case CONTINUOUS:
                fileContent.addCVParam(new EmptyCVParam(OBO.getOBO().getTerm(FileContent.BINARY_TYPE_CONTINUOUS_ID)));
                break;
            case PROCESSED:
            default:
                fileContent.addCVParam(new EmptyCVParam(OBO.getOBO().getTerm(FileContent.BINARY_TYPE_PROCESSED_ID)));
                break;
        }

        // Make sure that any referanceableParamGroup for the data arrays state that the data is external
        ReferenceableParamGroup rpgmzArray = mzML.getReferenceableParamGroupList().getReferenceableParamGroup("mzArray");
        if (rpgmzArray != null) {
            rpgmzArray.removeCVParam(BinaryDataArray.EXTERNAL_DATA_ID);
            rpgmzArray.addCVParam(new BooleanCVParam(OBO.getOBO().getTerm(BinaryDataArray.EXTERNAL_DATA_ID), true));
        }
        ReferenceableParamGroup rpgintensityArray = mzML.getReferenceableParamGroupList().getReferenceableParamGroup("intensityArray");
        if (rpgintensityArray != null) {
            rpgintensityArray.removeCVParam(BinaryDataArray.EXTERNAL_DATA_ID);
            rpgintensityArray.addCVParam(new BooleanCVParam(OBO.getOBO().getTerm(BinaryDataArray.EXTERNAL_DATA_ID), true));
        }
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
    public void write(MzML mzML, String outputLocation) throws IOException {
        try {
            String ibdLocation = getIBDLocationFromOutput(outputLocation);

            // Update the ontology list
            updateOBOList(mzML);

            // Update the imzML header information about storage type
            updateStorageInformation(mzML);

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

            if (mzML.getRun().getSpectrumList() != null) {
                // TODO: For continuous data only write the m/z list once
                for (Spectrum spectrum : mzML.getRun().getSpectrumList()) {

                    for (BinaryDataArray bda : spectrum.getBinaryDataArrayList()) {
                        double[] ddata = bda.getDataAsDouble();

                        if (ddata != null) {
                            byte[] bdata = prepareData(ddata, bda);

                            writeData(bdata);
                        } else {
                            LOGGER.log(Level.SEVERE, "Null data in BinaryDataArray {0}", bda);
                        }

                        // Update the TIC 
                        if (bda.isIntensityArray()) {
                            double total = 0;

                            for (int i = 0; i < ddata.length; i++) {
                                total += ddata[i];
                            }

                            CVParam ticParam = spectrum.getCVParam(Spectrum.TOTAL_ION_CURRENT_ID);

                            if (ticParam == null) {
                                ticParam = new DoubleCVParam(OBO.getOBO().getTerm(Spectrum.TOTAL_ION_CURRENT_ID), total);
                                spectrum.addCVParam(ticParam);
                            } else {
                                ((DoubleCVParam) ticParam).setValue(total);
                            }
                        }
                    }
                }
            }

            // Write out all chromatograms
            if (mzML.getRun().getChromatogramList() != null) {
                for (Chromatogram chromatogram : mzML.getRun().getChromatogramList()) {
                    for (BinaryDataArray bda : chromatogram.getBinaryDataArrayList()) {
                        double[] ddata = bda.getDataAsDouble();
                        byte[] bdata = prepareData(ddata, bda);

                        writeData(bdata);
                    }
                }
            }

            dataRAF.setLength(dataRAF.getFilePointer());
            dataOutput.close();

            // Update UUID in the metadata
            FileContent fileContent = mzML.getFileDescription().getFileContent();
            fileContent.removeCVParam(FileContent.UUID_IDENTIFICATION_ID);
            fileContent.addCVParam(new StringCVParam(OBO.getOBO().getTerm(FileContent.UUID_IDENTIFICATION_ID), uuid.toString()));

            // Update SHA-1
            fileContent.removeChildrenOfCVParam(FileContent.IBD_CHECKSUM_ID, false);
            fileContent.addCVParam(new StringCVParam(OBO.getOBO().getTerm(FileContent.SHA1_CHECKSUM_ID), HexHelper.byteArrayToHexString(messageDigest.digest())));

            // Update max x and max y coordinates
            updateMaxCoordinateInformation(mzML);

            // Write out metadata
            super.write(mzML, outputLocation);
        } catch (NoSuchAlgorithmException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
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
    public long getDataPointer() throws IOException {
        dataOutput.flush();

        return dataRAF.getFilePointer();
    }

}
