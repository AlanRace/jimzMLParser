package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.parser.DataLocation;
import com.alanmrace.jimzmlparser.parser.MzMLSpectrumDataStorage;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author Alan Race
 */
public abstract class MzMLDataContainer extends MzMLContentWithParams implements ReferenceableTag {
    protected DataLocation dataLocation;
    protected BinaryDataArrayList binaryDataArrayList;
    
    protected int defaultArrayLength;
    protected String id;
    
    protected DataProcessing dataProcessingRef;
    
    protected long mzMLLocation;
    protected RandomAccessFile raf;
    
    public MzMLDataContainer(MzMLContentWithParams mzMLContent, ReferenceableParamGroupList rpgList) {
        super(mzMLContent, rpgList);
    }
    
    public MzMLDataContainer(String id, int defaultArrayLength) { 
        this.id = id;
        this.defaultArrayLength = defaultArrayLength;
    }
    
    @Override
    public String getID() {
        return id;
    }
    
    @Override
    public void setID(String id) {
        this.id = id;
    }
    
    // Functions to enable the setting of <index> in indexedmzML
    protected void setRAF(RandomAccessFile raf) {
        this.raf = raf;
    }
    
    protected void setmzMLLocation(long mzMLLocation) {
        this.mzMLLocation = mzMLLocation;
    }
    
    protected long getmzMLLocation() {
        return mzMLLocation;
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
    }
    
    /**
     * Get the intensity array as a double[].
     * 
     * @return Intensity array
     * @throws IOException
     */
    public double[] getIntensityArray() throws IOException {
        return getIntensityArray(false);
    }

    /**
     * Get the intensity array as a double[] and optionally keep the 
     * array within memory.
     * 
     * @param keepInMemory true to keep the data within memory, false otherwise
     * @return
     * @throws IOException
     */
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
