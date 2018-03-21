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
     * RandomAccessFile for the file containing the binary data.
     */
    protected RandomAccessFile dataRAF;

    /**
     * BufferedWriter created from the data RandomAccessFile for writing out to.
     */
    protected DataOutputStream dataOutput;

    /**
     * Possible means of outputting data, as defined by the ImzML standard. 
     * Continuous type means that each spectrum of an image has the same m/z values.
     * Processed type means that each spectrum has its own m/z array.
     */
    public enum OutputType {
        /**
         * Continuous type means that each spectrum of an image has the same m/z values.
         */
        Continuous,
        
        /**
         * Processed type means that each spectrum has its own m/z array.
         */
        Processed
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
     * Create an ImzMLWriter with defaults (processed output type and SHA-1 checksum).
     */
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

            // Update the ontology list
            mzML.getCVList().clear();
            OBO obo = OBO.getOBO();
            List<OBO> fullOBOList = obo.getFullImportHeirarchy();

            for(OBO currentOBO : fullOBOList) {
                mzML.getCVList().addCV(new CV(currentOBO));
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
            
            // Make sure that any referanceableParamGroup for the data arrays state that the data is external
            ReferenceableParamGroup rpgmzArray = mzML.getReferenceableParamGroupList().getReferenceableParamGroup("mzArray");
            if(rpgmzArray != null) {
                rpgmzArray.removeChildOfCVParam(BinaryDataArray.externalDataID);
                rpgmzArray.addCVParam(new BooleanCVParam(OBO.getOBO().getTerm(BinaryDataArray.externalDataID), true));
            }
            ReferenceableParamGroup rpgintensityArray = mzML.getReferenceableParamGroupList().getReferenceableParamGroup("intensityArray");
            if(rpgintensityArray != null) {
                rpgintensityArray.removeChildOfCVParam(BinaryDataArray.externalDataID);
                rpgintensityArray.addCVParam(new BooleanCVParam(OBO.getOBO().getTerm(BinaryDataArray.externalDataID), true));
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

            if (mzML.getRun().getSpectrumList() != null) {
                // TODO: For continuous data only write the m/z list once
                for (Spectrum spectrum : mzML.getRun().getSpectrumList()) {

                    for (BinaryDataArray bda : spectrum.getBinaryDataArrayList()) {
                        //if (bda.getCVParamList().isEmpty()) {
                        //    System.out.println("Empty CVParamList: " + spectrum + " [" + bda.getReferenceableParamGroupRef(0).getReference().getCVParamCount() + "]");
                        //}

                        double[] ddata = bda.getDataAsDouble();

                        if (ddata != null) {
                            byte[] bdata = prepareData(ddata, bda);

                            writeData(bdata);
                        } else {
                            Logger.getLogger(ImzMLWriter.class.getName()).log(Level.SEVERE, "Null data in BinaryDataArray {0}", bda);
                        }
                        
                        // Update the TIC 
                        if(bda.isIntensityArray()) {
                            double total = 0;
                            
                            for(int i = 0; i < ddata.length; i++)
                                total += ddata[i];
                            
                            CVParam ticParam = spectrum.getCVParam(Spectrum.totalIonCurrentID);
                            
                            if(ticParam == null) {
                                ticParam = new DoubleCVParam(OBO.getOBO().getTerm(Spectrum.totalIonCurrentID), total);
                                spectrum.addCVParam(ticParam);
                            } else
                                ((DoubleCVParam) ticParam).setValue(total);
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
            fileContent.removeCVParam(FileContent.uuidIdntificationID);
            fileContent.addCVParam(new StringCVParam(OBO.getOBO().getTerm(FileContent.uuidIdntificationID), uuid.toString()));

            // Update SHA-1
            fileContent.removeChildOfCVParam(FileContent.ibdChecksumID);
            fileContent.addCVParam(new StringCVParam(OBO.getOBO().getTerm(FileContent.sha1ChecksumID), HexHelper.byteArrayToHexString(messageDigest.digest())));

            // Update max x and max y coordinates
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
                scanSettings.removeCVParam(ScanSettings.maxCountPixelXID);
                scanSettings.removeCVParam(ScanSettings.maxCountPixelYID);
            }

            scanSettings.addCVParam(new IntegerCVParam(OBO.getOBO().getTerm(ScanSettings.maxCountPixelXID), maxX));
            scanSettings.addCVParam(new IntegerCVParam(OBO.getOBO().getTerm(ScanSettings.maxCountPixelYID), maxY));

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
