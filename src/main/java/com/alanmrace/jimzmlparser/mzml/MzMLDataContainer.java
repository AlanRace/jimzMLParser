package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.data.DataLocation;
import com.alanmrace.jimzmlparser.data.DataTypeTransform.DataType;
import com.alanmrace.jimzmlparser.data.MzMLSpectrumDataStorage;
import com.alanmrace.jimzmlparser.util.XMLHelper;
import java.io.IOException;

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
     * The physical location of the data that this tag instance describes.
     */
    protected DataLocation dataLocation;

    /**
     * Binary data associated with this Spectrum or Chromatogram.
     */
    protected BinaryDataArrayList binaryDataArrayList;
    
    /**
     * Default length of the binary data array (number of elements).
     */
    protected int defaultArrayLength;
    
    /**
     * Default description of the data processing applied to this Spectrum or Chromatogram.
     */
    protected DataProcessing dataProcessingRef;
   
    /**
     * List of DataProcessing that is used when creating a new MzMLDataContainer and 
     * subsequently ensuring that the references have remained valid.
     */
    private ReferenceList<DataProcessing> dataProcessingList;
        
    /**
     * Copy constructor, requiring new versions of lists to match old references to.
     * 
     * @param mzMLContent   Content to copy
     * @param rpgList       New ReferenceableParamGroupList
     */
    public MzMLDataContainer(MzMLDataContainer mzMLContent, ReferenceableParamGroupList rpgList) {
        super(mzMLContent, rpgList);
    }
    
    /**
     * Create a MzMLDataContainer tag with an ID and a default array length (in values).
     * 
     * @param id Unique identifier
     * @param defaultArrayLength Length of the chromatogram in data points
     */
    public MzMLDataContainer(String id, int defaultArrayLength) { 
        this.id = id;
        this.defaultArrayLength = defaultArrayLength;
    }
    
    /**
     * Set the BinaryDatayArrayList tag.
     * 
     * @param binaryDataArrayList BinaryDatayArrayList 
     */
    public void setBinaryDataArrayList(BinaryDataArrayList binaryDataArrayList) {
        binaryDataArrayList.setParent(this);

        this.binaryDataArrayList = binaryDataArrayList;
    }

    /**
     * Returns the BinaryDatayArrayList, or creates and returns an empty list if 
     * none currently exists.
     * 
     * @return BinaryDatayArrayList
     */
    public BinaryDataArrayList getBinaryDataArrayList() {
        if (binaryDataArrayList == null) {
            binaryDataArrayList = new BinaryDataArrayList(0);
        }

        return binaryDataArrayList;
    }
    
    // Set optional attributes

    /**
     * Set the DataProcessing which describes how this data has been processed.
     * 
     * @param dataProcessingRef Description of data processing applied
     */
    public void setDataProcessingRef(DataProcessing dataProcessingRef) {
        this.dataProcessingRef = dataProcessingRef;
        
        ensureValidReferences();
    }
    
    /**
     * Returns the description of any data processing applied to this data.
     * 
     * @return DataProcessing
     */
    public DataProcessing getDataProcessingRef() {
        return dataProcessingRef;
    }
    
    /**
     * Set the DataProcessingList to be used when creating a copy of this tag.
     * 
     * @param dataProcessingList DataProcessingList
     */
    protected void setDataProcessingList(ReferenceList<DataProcessing> dataProcessingList) {
        this.dataProcessingList = dataProcessingList;
        
        ensureValidReferences();
    }
    
    /**
     * Get the location of the data this mzML tag describes.
     * 
     * @return Reference to the exact data location
     */
    public DataLocation getDataLocation() {
        return dataLocation;
    }

    /**
     * Set the location of the data this mzML tag describes.
     * 
     * @param dataLocation Reference to the exact data location
     */
    public void setDataLocation(DataLocation dataLocation) {
        this.dataLocation = dataLocation;
    }
    
    /**
     * Ensure that the data can be loaded. If the DataLocation is a MzMLSpectrumDataStorage
     * then this must be converted to Base64DataStorage before it can actually be used. 
     * 
     * @throws IOException Issue when trying to access DataLocation
     */
    public void ensureLoadableData() throws IOException {
        if (dataLocation != null && dataLocation.getDataStorage() instanceof MzMLSpectrumDataStorage) {
            convertMzMLDataStorageToBase64();
        }
    }
    
    /**
     * Convert MzMLDataStorage (which is a reference to the location of a {@literal <spectrum>}
     * tag in an MzML file to Base64DataStorage (which is a reference to the binary data
     * in the MzML file in Base64 format).
     * 
     * @throws IOException Issue when trying to access DataLocation
     */
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
     * @throws IOException Issue when trying to access DataLocation
     */
    public double[] getIntensityArray() throws IOException {
        return getIntensityArray(false);
    }

    /**
     * Get the intensity array as a double[] and optionally keep the 
     * array within memory.
     * 
     * @param keepInMemory true to keep the data within memory, false otherwise
     * @return Intensity array
     * @throws IOException Issue when trying to access DataLocation
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
