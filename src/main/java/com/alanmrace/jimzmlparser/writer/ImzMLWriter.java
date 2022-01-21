package com.alanmrace.jimzmlparser.writer;

import com.alanmrace.jimzmlparser.mzml.BinaryDataArray;
import com.alanmrace.jimzmlparser.mzml.BooleanCVParam;
import com.alanmrace.jimzmlparser.mzml.CV;
import com.alanmrace.jimzmlparser.mzml.CVParam;
import com.alanmrace.jimzmlparser.mzml.Chromatogram;
import com.alanmrace.jimzmlparser.mzml.DoubleCVParam;
import com.alanmrace.jimzmlparser.mzml.EmptyCVParam;
import com.alanmrace.jimzmlparser.mzml.FileContent;
import com.alanmrace.jimzmlparser.mzml.MzML;
import com.alanmrace.jimzmlparser.mzml.ReferenceableParamGroup;
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
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * Writer for exporting both the metadata into an imzML file and the binary data
 * to an IBD file.
 *
 * @author Alan Race
 */
public class ImzMLWriter extends ImzMLWriterAbstract {

    /**
     * Logger for the class.
     */
    private static final Logger LOGGER = Logger.getLogger(ImzMLWriter.class.getName());



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
     * Create an ImzMLWriter with defaults (processed output type and SHA-1
     * checksum).
     */
    public ImzMLWriter() {
        super();

        this.outputType = OutputType.PROCESSED;
        checksum = Checksum.SHA1;
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

                            // Update the TIC
                            if (bda.isIntensityArray()) {
                                double total = 0;

                                for (double dataPoint : ddata) {
                                    total += dataPoint;
                                }

                                CVParam ticParam = spectrum.getCVParam(Spectrum.TOTAL_ION_CURRENT_ID);

                                if (ticParam == null) {
                                    ticParam = new DoubleCVParam(OBO.getOBO().getTerm(Spectrum.TOTAL_ION_CURRENT_ID), total);
                                    spectrum.addCVParam(ticParam);
                                } else {
                                    ((DoubleCVParam) ticParam).setValue(total);
                                }
                            }
                        } else {
                            LOGGER.log(Level.SEVERE, "Null data in BinaryDataArray {0}", bda);
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



}
