package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.data.DataLocation;
import com.alanmrace.jimzmlparser.data.DataTypeTransform.DataType;
import com.alanmrace.jimzmlparser.data.MzMLSpectrumDataStorage;
import com.alanmrace.jimzmlparser.util.XMLHelper;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Base class with default implementations of methods for MzMLTags which describe 
 * data (Spectrum and Chromatogram).
 * 
 * @author Alan Race
 * @see Spectrum
 * @see Chromatogram
 */
public abstract class MzMLDataContainer extends MzMLIndexedContentWithParams {

    /**
     * The location of the data that this tag instance describes.
     */
    protected DataLocation dataLocation;

    /**
     * 
     */
    protected BinaryDataArrayList binaryDataArrayList;
    
    protected int defaultArrayLength;
    
    protected DataProcessing dataProcessingRef;
    
    protected ReferenceList<DataProcessing> dataProcessingList;
    
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
        
        ensureValidReferences();
    }
    
    public DataProcessing getDataProcessingRef() {
        return dataProcessingRef;
    }
    
    protected void setDataProcessingList(ReferenceList<DataProcessing> dataProcessingList) {
        this.dataProcessingList = dataProcessingList;
        
        ensureValidReferences();
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
    
    public void ensureLoadableData() throws IOException {
        if (dataLocation != null && dataLocation.getDataStorage() instanceof MzMLSpectrumDataStorage) {
            convertMzMLDataStorageToBase64();
        }
    }
    
    protected void convertMzMLDataStorageToBase64() throws IOException {
        if (dataLocation != null && dataLocation.getDataStorage() instanceof MzMLSpectrumDataStorage) {
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
                        // TODO: WARNING??
//                        System.out.println("Data: " + spectrumData);
                    }
                }
            }

            dataLocation = null;
        }
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

        ensureLoadableData();

        return binaryDataArrayList.getIntensityArray().getDataAsDouble(keepInMemory);
    }
        
    /**
     * Sets the compression (or none) to all BinaryDataArray within this data 
     * container. This only takes effect when the data is written out.
     * 
     * @param compression Compression
     */
    public void setCompression(BinaryDataArray.CompressionType compression) {
        binaryDataArrayList.setCompression(compression);
    }
    
    /**
     * Sets the data type to be used when storing data. This only takes effect
     * when the data is written out.
     * 
     * @param dataType New data type
     */
    public void setDataType(DataType dataType) {
        binaryDataArrayList.setDataType(dataType);
    }
    
    @Override
    public String getXMLAttributeText() {
        String attributeText = super.getXMLAttributeText();
        
        if(!attributeText.isEmpty())
            attributeText += " ";
        
        attributeText += "defaultArrayLength=\"" + defaultArrayLength + "\"";
        
        if (dataProcessingRef != null) {
            attributeText += " dataProcessingRef=\"" + XMLHelper.ensureSafeXML(dataProcessingRef.getID()) + "\"";
        }
        
        return attributeText;
    }
    
    @Override
    protected void ensureValidReferences() {
        if(dataProcessingList != null && dataProcessingRef != null)
            dataProcessingRef = dataProcessingList.getValidReference(dataProcessingRef);
    }
}
