/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alanmrace.jimzmlparser.mzML;

import com.alanmrace.jimzmlparser.parser.DataLocation;
import com.alanmrace.jimzmlparser.parser.MzMLSpectrumDataStorage;
import java.io.IOException;

/**
 *
 * @author amr1
 */
public class MzMLDataContainer extends MzMLContent {
    protected DataLocation dataLocation;
    protected BinaryDataArrayList binaryDataArrayList;
    
    protected int defaultArrayLength;
    protected String id;
    
    protected DataProcessing dataProcessingRef;
    
    public MzMLDataContainer(MzMLContent mzMLContent, ReferenceableParamGroupList rpgList) {
        super(mzMLContent, rpgList);
    }
    
    public MzMLDataContainer(String id, int defaultArrayLength, int index) {
        this.id = id;
        this.defaultArrayLength = defaultArrayLength;
    }
    
    public String getID() {
        return id;
    }
    
    public void setBinaryDataArrayList(BinaryDataArrayList binaryDataArrayList) {
        binaryDataArrayList.setParent(this);

        this.binaryDataArrayList = binaryDataArrayList;
    }

    public BinaryDataArrayList getBinaryDataArrayList() {
        if (binaryDataArrayList == null) {
            binaryDataArrayList = new BinaryDataArrayList(0);
        }

        return binaryDataArrayList;
    }
    
    // Set optional attributes
    public void setDataProcessingRef(DataProcessing dataProcessingRef) {
        this.dataProcessingRef = dataProcessingRef;
    }
    
    public DataLocation getDataLocation() {
        return dataLocation;
    }

    /**
     *
     * @param dataLocation
     */
    public void setDataLocation(DataLocation dataLocation) {
        this.dataLocation = dataLocation;
    }
    
    protected void convertMzMLDataStorageToBase64() throws IOException {
        // Load in the data from the data storage
        byte[] data = dataLocation.getData();
        String spectrumData = new String(data);

        MzMLSpectrumDataStorage mzMLDataStorage = (MzMLSpectrumDataStorage) dataLocation.getDataStorage();

        // Identify where each binary data array is within the spectrum mzML
        if (binaryDataArrayList != null) {
            for (BinaryDataArray bda : binaryDataArrayList) {
                
                CVParam cvParam = bda.getCVParamOrChild(BinaryDataArray.binaryDataArrayID);
                String cvParamID = cvParam.getTerm().getID();

//                System.out.println("Looking for " + cvParam + " in " + this.id);
                
                int cvParamLocation = spectrumData.indexOf(cvParamID);
                //		    System.out.println(cvParamLocation);
                //		    System.out.println(cvParamID);
                
                if(cvParamLocation != -1) {
                    String subSpectrumData = spectrumData.substring(cvParamLocation);

                    int binaryStart = subSpectrumData.indexOf("<binary>") + cvParamLocation + "<binary>".length();
                    int binaryEnd = subSpectrumData.indexOf("</binary>") + cvParamLocation;

                    bda.setDataLocation(new DataLocation(mzMLDataStorage.getBase64DataStorage(), binaryStart + dataLocation.getOffset(), binaryEnd - binaryStart));
                    //		    System.out.println(cvParam);
                    //		    System.out.println(spectrumData.substring(binaryStart, binaryEnd));
                } else {
                    System.out.println("Data: " + spectrumData);
                }
            }
        }
        
        dataLocation = null;

//		System.out.println("Data: ");
//		System.out.println(spectrumData);
        // Create Base64DataStorage with the calculated offsets
        // Add the new DataStorage to the corresponding BinaryDataArray
//		throw new UnsupportedOperationException("Not yet supported");
    }
    
    public double[] getIntensityArray() throws IOException {
        return getIntensityArray(false);
    }

    public double[] getIntensityArray(boolean keepInMemory) throws IOException {
        if (binaryDataArrayList == null) {
            return null;
        }

        if (dataLocation != null && dataLocation.getDataStorage() instanceof MzMLSpectrumDataStorage) {
            convertMzMLDataStorageToBase64();
        }

        return binaryDataArrayList.getIntensityArray().getDataAsDouble(keepInMemory);
    }
}
