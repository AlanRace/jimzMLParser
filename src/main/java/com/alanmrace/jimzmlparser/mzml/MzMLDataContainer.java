package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.data.DataLocation;
import com.alanmrace.jimzmlparser.data.MzMLSpectrumDataStorage;
import com.alanmrace.jimzmlparser.listener.ReferenceListener;
import com.alanmrace.jimzmlparser.util.XMLHelper;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author Alan Race
 */
public abstract class MzMLDataContainer extends MzMLIndexedContentWithParams {
    protected DataLocation dataLocation;
    protected BinaryDataArrayList binaryDataArrayList;
    
    protected int defaultArrayLength;
    
    protected DataProcessing dataProcessingRef;
    
    protected ReferenceListener<DataProcessing> dataProcessingListener;
    
    protected RandomAccessFile raf;
    
    public MzMLDataContainer(MzMLDataContainer mzMLContent, ReferenceableParamGroupList rpgList) {
        super(mzMLContent, rpgList);
    }
    
    public MzMLDataContainer(String id, int defaultArrayLength) { 
        this.id = id;
        this.defaultArrayLength = defaultArrayLength;
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
        
        if(dataProcessingListener != null)
            this.dataProcessingRef = dataProcessingListener.referenceModified(dataProcessingRef);
    }
    
    public DataProcessing getDataProcessingRef() {
        return dataProcessingRef;
    }
    
    protected void setDataProcessingListener(ReferenceListener<DataProcessing> dataProcessingListener) {
        this.dataProcessingListener = dataProcessingListener;
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
        byte[] data = dataLocation.getBytes();
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

                    DataLocation location = new DataLocation(mzMLDataStorage.getBase64DataStorage(), binaryStart + dataLocation.getOffset(), binaryEnd - binaryStart);
                    bda.setDataLocation(location);
                    location.setDataTransformation(bda.generateDataTransformation());
                    
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
    
    @Override
    protected String getXMLAttributeText() {
        String attributeText = super.getXMLAttributeText();
        
        if(!attributeText.isEmpty())
            attributeText += " ";
        
        attributeText += "defaultArrayLength=\"" + defaultArrayLength + "\"";
        
        if (dataProcessingRef != null) {
            attributeText += " dataProcessingRef=\"" + XMLHelper.ensureSafeXML(dataProcessingRef.getID()) + "\"";
        }
        
        return attributeText;
    }
}
