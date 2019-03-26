package com.alanmrace.jimzmlparser.writer;

import com.alanmrace.jimzmlparser.imzml.ImzML;
import com.alanmrace.jimzmlparser.mzml.*;
import com.alanmrace.jimzmlparser.obo.OBO;
import com.alanmrace.jimzmlparser.util.HexHelper;
import com.alanmrace.jimzmlparser.util.UUIDHelper;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ImzMLSteamWriter extends ImzMLWriterAbstract {

    String outputLocation;
    UUID uuid;

    ReferenceableParamGroup mzRPG;
    ReferenceableParamGroup countsRPG;

    List<Spectrum> spectrumList = new ArrayList<Spectrum>();

    public ImzMLSteamWriter(String outputLocation, ReferenceableParamGroup mzRPG, ReferenceableParamGroup countsRPG) throws NoSuchAlgorithmException, IOException {
        super();

        this.outputType = ImzMLWriter.OutputType.PROCESSED;
        checksum = Checksum.SHA1;

        this.outputLocation = outputLocation;
        this.mzRPG = mzRPG;
        this.countsRPG = countsRPG;

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

        String ibdLocation = getIBDLocationFromOutput(outputLocation);

        // Open the IBD file ready for writing, using the RandomAccessFile to get
        // current location
        dataRAF = new RandomAccessFile(ibdLocation + ".ibd", "rw");
        dataOutput = new DataOutputStream(new FileOutputStream(dataRAF.getFD()));

        // Create new UUID and write it to the start of the IBD file
        uuid = UUID.randomUUID();
        writeData(UUIDHelper.uuidToByteArray(uuid));
    }

    public void write(Spectrum spectrum, double[] mzs, double[] counts) throws IOException {
        Spectrum newSpectrum = new Spectrum(spectrum.getID(), 0);
        newSpectrum.setPixelLocation(spectrum.getPixelLocation());

        BinaryDataArray mzBDA = new BinaryDataArray(0);
        newSpectrum.getBinaryDataArrayList().addBinaryDataArray(mzBDA);
        mzBDA.addReferenceableParamGroupRef(new ReferenceableParamGroupRef(mzRPG));

        byte[] mzData = prepareData(mzs, mzBDA);
        writeData(mzData);

        BinaryDataArray countsBDA = new BinaryDataArray(0);
        newSpectrum.getBinaryDataArrayList().addBinaryDataArray(countsBDA);
        countsBDA.addReferenceableParamGroupRef(new ReferenceableParamGroupRef(countsRPG));

        byte[] countsData = prepareData(counts, countsBDA);
        writeData(countsData);

        double total = 0;

        for (double dataPoint : counts) {
            total += dataPoint;
        }

        CVParam ticParam = newSpectrum.getCVParam(Spectrum.TOTAL_ION_CURRENT_ID);

        if (ticParam == null) {
            ticParam = new DoubleCVParam(OBO.getOBO().getTerm(Spectrum.TOTAL_ION_CURRENT_ID), total);
            newSpectrum.addCVParam(ticParam);
        } else {
            ((DoubleCVParam) ticParam).setValue(total);
        }

//        // Update the TIC
//        if (bda.isIntensityArray()) {
//            double total = 0;
//
//            for (double dataPoint : ddata) {
//                total += dataPoint;
//            }
//
//            CVParam ticParam = spectrum.getCVParam(Spectrum.TOTAL_ION_CURRENT_ID);
//
//            if (ticParam == null) {
//                ticParam = new DoubleCVParam(OBO.getOBO().getTerm(Spectrum.TOTAL_ION_CURRENT_ID), total);
//                spectrum.addCVParam(ticParam);
//            } else {
//                ((DoubleCVParam) ticParam).setValue(total);
//            }
//        }

        spectrumList.add(newSpectrum);
    }

    public void write(ImzML imzML) throws IOException {
        imzML.getReferenceableParamGroupList().addReferenceableParamGroup(this.mzRPG);
        imzML.getReferenceableParamGroupList().addReferenceableParamGroup(this.countsRPG);

        imzML.getSpectrumList().clear();

        for(Spectrum spectrum : spectrumList)
            imzML.getSpectrumList().add(spectrum);

        dataRAF.setLength(dataRAF.getFilePointer());
        dataOutput.close();

        // Update UUID in the metadata
        FileContent fileContent = imzML.getFileDescription().getFileContent();
        fileContent.removeCVParam(FileContent.UUID_IDENTIFICATION_ID);
        fileContent.addCVParam(new StringCVParam(OBO.getOBO().getTerm(FileContent.UUID_IDENTIFICATION_ID), uuid.toString()));

        // Update SHA-1
        fileContent.removeChildrenOfCVParam(FileContent.IBD_CHECKSUM_ID, false);
        fileContent.addCVParam(new StringCVParam(OBO.getOBO().getTerm(FileContent.SHA1_CHECKSUM_ID), HexHelper.byteArrayToHexString(messageDigest.digest())));

        // Update max x and max y coordinates
        updateMaxCoordinateInformation(imzML);

        // Write out metadata
        super.write(imzML, outputLocation);
    }
}
