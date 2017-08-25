package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.data.DataLocation;
import com.alanmrace.jimzmlparser.data.DataTransformation;
import com.alanmrace.jimzmlparser.data.DataTypeTransform;
import com.alanmrace.jimzmlparser.data.DataTypeTransform.DataType;
import com.alanmrace.jimzmlparser.data.XZDataTransform;
import com.alanmrace.jimzmlparser.data.ZlibDataTransform;
import com.alanmrace.jimzmlparser.obo.OBO;
import com.alanmrace.jimzmlparser.obo.OBOTerm;
import com.alanmrace.jimzmlparser.util.XMLHelper;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;

/**
 * BinaryDataArray tag.
 *
 * @author Alan Race
 */
public class BinaryDataArray extends MzMLContentWithParams implements Serializable {

    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * Possible compression methods for compressing binary data.
     */
    public enum CompressionType {

        /**
         * No compression.
         */
        None,

        /**
         * ZLib compression.
         */
        Zlib,
        
        /**
         * XZ compression.
         */
        XZ;
        
        /**
         * Convert CompressionType enum to OBOTerm, using ontology terms found in the 
         * MS ontology.
         * 
         * @param compressionType CompressionType to convert
         * @return Ontology term which describes the CompressionType, or null if no match
         */
        public static OBOTerm toOBOTerm(CompressionType compressionType) {
            switch(compressionType) {
                case None:
                    return OBO.getOBO().getTerm(noCompressionID);
                case Zlib:
                    return OBO.getOBO().getTerm(zlibCompressionID);
                case XZ:
                    return OBO.getOBO().getTerm(xyCompressionID);
                default:
                    return null;
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Accessions">
    
    /**
     * Accession: Binary data compression type (MS:1000572). MUST supply a child
     * only once
     */
    public static final String compressionTypeID = "MS:1000572"; // Required child (1)

    /**
     * Accession: Binary data array (MS:1000513). MUST supply a child only once
     */
    public static final String binaryDataArrayID = "MS:1000513";	// Required child (1)

    /**
     * Accession: Binary data type (MS:1000518). MUST supply a child only once
     */
    public static final String dataTypeID = "MS:1000518";	// Required child (1)

    /**
     * Accession: IBD binary data type (IMS:1000014).
     */
    public static final String ibdDataType = "IMS:1000014";

    /**
     * Accession: m/z array (MS:1000514).
     */
    public static final String mzArrayID = "MS:1000514";

    /**
     * Accession: m/z units (MS:1000040).
     */
    public static final String mzArrayUnitsID = "MS:1000040";

    /**
     * Accession: Intensity array (MS:1000515).
     */
    public static final String intensityArrayID = "MS:1000515";

    /**
     * Accession: Intensity array units: number of counts (MS:1000131).
     */
    public static final String intensityArrayUnitsNumberOfCountsID = "MS:1000131";

    /**
     * Accession: Intensity array units: percentage of basepeak (MS:1000132).
     */
    public static final String intensityArrayUnitsPercentOfBasepeakID = "MS:1000132";

    /**
     * Accession: Intensity array units: counts per second (MS:1000814).
     */
    public static final String intensityArrayUnitsCountsPerSecondID = "MS:1000814";

    /**
     * Accession: Intensity array units: percent of basepeak x 100 (MS:1000905).
     */
    public static final String intensityArrayUnitsPercentOfBasepeakTimes100ID = "MS:1000905";

    /**
     * Accession: Double precision (MS:1000523).
     */
    public static final String doublePrecisionID = "MS:1000523";

    /**
     * Accession: Single precision (MS:1000521).
     */
    public static final String singlePrecisionID = "MS:1000521";

    // The IMS obo has copies of the data types found in the MS obo
    /**
     * Accession: Signed 32-bit integer (MS:1000519).
     */
    public static final String signed32bitIntegerID = "MS:1000519";

    /**
     * Accession: Signed 32-bit integer (IMS:1000141). Copy of MS:1000519.
     *
     * @see BinaryDataArray#signed32bitIntegerID
     */
    public static final String imsSigned32bitIntegerID = "IMS:1000141";

    /**
     * Accession: Signed 64-bit integer (MS:1000522).
     */
    public static final String signed64bitIntegerID = "MS:1000522";

    /**
     * Accession: Signed 64-bit integer (IMS:1000142). Copy of MS:1000522.
     *
     * @see BinaryDataArray#signed64bitIntegerID
     */
    public static final String imsSigned64bitIntegerID = "IMS:1000142";

    /**
     * Accession: Signed 8-bit integer (IMS:1100000).
     */
    public static final String signed8bitIntegerID = "IMS:1100000";

    /**
     * Accession: Signed 16-bit integer (IMS:1100000).
     */
    public static final String signed16bitIntegerID = "IMS:1100001";

    /**
     * Accession: No compression (MS:1000576).
     */
    public static final String noCompressionID = "MS:1000576";

    /**
     * Accession: zlib compression (MS:1000574).
     */
    public static final String zlibCompressionID = "MS:1000574";

    /**
     * Accession: xz compression (IMS:1005001).
     */
    public static final String xyCompressionID = "IMS:1005001";
    
    /**
     * Accession: External array length (IMS:1000103). MUST supply once
     */
    public static final String externalArrayLengthID = "IMS:1000103";

    /**
     * Accession: External data (IMS:1000101). MUST supply once
     */
    public static final String externalDataID = "IMS:1000101";

    /**
     * Accession: External encoded length (IMS:1000104). MUST supply once
     */
    public static final String externalEncodedLengthID = "IMS:1000104";

    /**
     * Accession: External offset (IMS:1000102). MUST supply once
     */
    public static final String externalOffsetID = "IMS:1000102";

    // </editor-fold>
    
    /**
     * Attribute: The array length, overrides defaultArrayLength. OPTIONAL
     */
    private int arrayLength = -1;				// Optional

    /**
     * Attribute: The dataProcessing tag reference. OPTIONAL
     */
    private DataProcessing dataProcessingRef;	// Optional

    /**
     * Attribute: The encoded length of the binary array. REQUIRED
     */
    private int encodedLength = 0;				// Required
    
    /**
     * Data array if storing in memory.
     */
    private double[] data;
    
    /**
     * True if this BinaryDataArray describes an m/z array, false otherwise.
     */
    private boolean ismzArray;

    /**
     * True if this BinaryDataArray describes an intensity array, false
     * otherwise.
     */
    private boolean isIntensityArray;

    /**
     * Location of the binary data array.
     */
    protected DataLocation dataLocation;

    /**
     * Instantiates a new binaryDataArray tag.
     *
     * @param encodedLength The encoded length of the binary data
     */
    public BinaryDataArray(int encodedLength) {
        this.encodedLength = encodedLength;
    }

    /**
     * Copy constructor, requiring new versions of lists to match old references
     * to.
     *
     * @param bda Old BinaryDataArray to copy
     * @param rpgList New ReferenceableParamGroupList
     * @param dpList New DataProcessingList
     */
    public BinaryDataArray(BinaryDataArray bda, ReferenceableParamGroupList rpgList, DataProcessingList dpList) {
        super(bda, rpgList);

        this.arrayLength = bda.arrayLength;
        this.encodedLength = bda.encodedLength;

        if (bda.dataProcessingRef != null && dpList != null) {
            for (DataProcessing dp : dpList) {
                if (bda.dataProcessingRef.getID().equals(dp.getID())) {
                    this.dataProcessingRef = dp;

                    break;
                }
            }
        }
    }

    /**
     * Get the length in bytes of the encoded data (number of bytes for the data
     * type x number of elements in array).
     *
     * @return Length in bytes of encoded data
     */
    public int getEncodedLength() {
        return encodedLength;
    }

    // Set optional attributes
    /**
     * Sets the array length.
     *
     * @param arrayLength the new array length
     */
    public void setArrayLength(int arrayLength) {
        this.arrayLength = arrayLength;
    }

    /**
     * Sets the dataProcessing reference.
     *
     * @param dataProcessingRef the new dataProcessing reference
     */
    public void setDataProcessingRef(DataProcessing dataProcessingRef) {
        this.dataProcessingRef = dataProcessingRef;
    }

    /**
     * Checks if the binary data is double precision.
     *
     * @return true, if double precision cvParam is present, false otherwise
     */
    public boolean isDoublePrecision() {
        DataType dataType = getDataType();

        return dataType == DataTypeTransform.DataType.Double;
    }

    /**
     * Checks if the binary data is single precision floating point.
     *
     * @return true, if single precision cvParam is present, false otherwise
     */
    public boolean isSinglePrecision() {
        CVParam dataTypeCVParam = getCVParam(singlePrecisionID);

        return dataTypeCVParam != null;
    }

    /**
     * Checks if the binary data is 8-bit integer.
     *
     * @return true, if 8-bit integer cvParam is present, false otherwise
     */
    public boolean isSigned8BitInteger() {
        CVParam dataTypeCVParam = getCVParam(signed8bitIntegerID);

        return dataTypeCVParam != null;
    }

    /**
     * Checks if the binary data is 16-bit integer.
     *
     * @return true, if 16-bit integer cvParam is present, false otherwise
     */
    public boolean isSigned16BitInteger() {
        CVParam dataTypeCVParam = getCVParam(signed16bitIntegerID);

        return dataTypeCVParam != null;
    }

    /**
     * Checks if the binary data is 32-bit integer.
     *
     * @return true, if 32-bit integer cvParam is present, false otherwise
     */
    public boolean isSigned32BitInteger() {
        CVParam dataTypeCVParam = getCVParam(signed32bitIntegerID);
        CVParam imsDataType = getCVParam(imsSigned32bitIntegerID);

        return !(dataTypeCVParam == null && imsDataType == null);
    }

    /**
     * Checks if the binary data is 64-bit integer.
     *
     * @return true, if 64-bit integer cvParam is present, false otherwise
     */
    public boolean isSigned64BitInteger() {
        CVParam dataTypeCVParam = getCVParam(signed64bitIntegerID);
        CVParam imsDataType = getCVParam(imsSigned64bitIntegerID);

        return !(dataTypeCVParam == null && imsDataType == null);
    }

    /**
     * Checks if the cvParam for no compression is present.
     *
     * @return true, if the no compression cvParam cannot be found, false
     * otherwise
     */
    public boolean isCompressed() {
        CVParam compression = getCVParam(noCompressionID);

        return compression == null;
    }

    /**
     * Convert a CVParam (with MS or IMS OBO ontology terms) describing the data
     * type to the number of bytes for the type it describes.
     *
     * @param dataType CVParam to convert
     * @return Number of bytes for the data type
     */
    public static int getDataTypeInBytes(CVParam dataType) {
        String dataTypeTermID = dataType.getTerm().getID();

        if (dataTypeTermID.equals(doublePrecisionID)) {
            return 8;
        } else if (dataTypeTermID.equals(singlePrecisionID)) {
            return 4;
        } else if (dataTypeTermID.equals(signed8bitIntegerID)) {
            return 1;
        } else if (dataTypeTermID.equals(signed16bitIntegerID)) {
            return 2;
        } else if (dataTypeTermID.equals(signed32bitIntegerID)) {
            return 4;
        } else if (dataTypeTermID.equals(signed64bitIntegerID)) {
            return 8;
        }

        return 1;
    }

    /**
     * Set the location of the binary data.
     *
     * @param dataLocation Location of the binary data
     */
    public void setDataLocation(DataLocation dataLocation) {
        this.dataLocation = dataLocation;
    }

    /**
     * Get the location of the binary data.
     *
     * @return Location of the binary data
     */
    public DataLocation getDataLocation() {
        return dataLocation;
    }

    /**
     * Get the data array as double[], convert and decompress as necessary.
     * Calls getDataAsDouble(false); - does not keep in memory.
     *
     * @return Uncompressed data as double[]
     * @throws IOException On failure to read from data location
     */
    public double[] getDataAsDouble() throws IOException {
        return getDataAsDouble(false);
    }

    /**
     * Get the data array as double[], convert and decompress as necessary,
     * optionally keeping the data in memory.
     *
     * <p>TODO: Doesn't keep data in memory
     *
     * @param keepInMemory true if data should be kept in memory, false
     * otherwise
     * @return Uncompressed data as double[]
     * @throws IOException On failure to read from data location
     */
    public double[] getDataAsDouble(boolean keepInMemory) throws IOException {
        if(data != null)
            return data;

        // If there is no dataLocation stored for the BinaryDataArray then it is 
        // likely that the data storage is MzMLDataStorage and so needs to be converted 
        // to Base64Storage prior to being able to load any data
        if (dataLocation == null) {
            if(parent != null) {
                MzMLTag grandParent = parent.getParent();
                
                if(grandParent instanceof MzMLDataContainer) {
                    ((MzMLDataContainer) grandParent).convertMzMLDataStorageToBase64();
                }
            }
        }
         
        // If there is still no dataLocation after the conversion, then no data to load
        if (dataLocation == null) {
            return null;
        }

        double[] loadedData = null;

        try {
            loadedData = dataLocation.getData();
        } catch (DataFormatException ex) {
            Logger.getLogger(BinaryDataArray.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(keepInMemory) {
            data = loadedData;
        }

        return loadedData;
    }
    
    /**
     * Set the data internally. This does not update any metadata, and therefore
     * should only be used when metadata is also updated. Alternatively use
     * {@link Spectrum#updatemzArray(double[], DataProcessing)} or
     * {@link Spectrum#updateIntensityArray(double[], com.alanmrace.jimzmlparser.mzml.DataProcessing)}.
     * 
     * @param data New data for the data array
     */
    protected void setData(double[] data) {
        this.data = data;
    }

    /**
     * Get the data array as byte[]. No conversion or decompression is applied.
     *
     * @return Uncompressed data as byte[]
     * @throws IOException On failure to read from data location
     */
    protected byte[] getDataAsByte() throws IOException {
        if (dataLocation == null) {
            return null;
        }
        
        return dataLocation.getBytes();
    }

    /**
     * Create a {@link DataTransformation} based on the relevant CVParams 
     * included within this BinaryDataArray. This describes the forward transformation
     * from a double[] to a (optionally new data type, optionally compressed) byte[].
     * 
     * @return DataTransformation describing CVParams
     */
    public DataTransformation generateDataTransformation() {
        DataTransformation transformation = new DataTransformation();

        // Always add in the conversion to data type first
        if (!DataType.Double.equals(getDataType())) {
            transformation.addTransform(new DataTypeTransform(DataType.Double, getDataType()));
        }

        CVParam compressionCVParam = this.getCVParamOrChild(BinaryDataArray.compressionTypeID);

        // Add in any compression
        if (BinaryDataArray.zlibCompressionID.equals(compressionCVParam.getTerm().getID())) {
            transformation.addTransform(new ZlibDataTransform());
        } else if(BinaryDataArray.xyCompressionID.equals(compressionCVParam.getTerm().getID())) {
            transformation.addTransform(new XZDataTransform());
        }

        return transformation;
    }

    /**
     * Gets binary data type. (e.g. double or single)
     *
     * @return the binary data type
     */
    public DataType getDataType() {
        //return getCVParam(dataTypeID);

        CVParam dataTypeParam = this.getCVParamOrChild(dataTypeID);
        DataType dataType = null;
        
        if(dataTypeParam != null) {
            String term = dataTypeParam.getTerm().getID();

            if (term.equals(doublePrecisionID)) {
                dataType = DataTypeTransform.DataType.Double;
            } else if (term.equals(singlePrecisionID)) {
                dataType = DataTypeTransform.DataType.Float;
            } else if (term.equals(signed64bitIntegerID) || term.equals(imsSigned64bitIntegerID)) {
                dataType = DataTypeTransform.DataType.Integer64bit;
            } else if (term.equals(signed32bitIntegerID) || term.equals(imsSigned32bitIntegerID)) {
                dataType = DataTypeTransform.DataType.Integer32bit;
            } else if (term.equals(signed16bitIntegerID)) {
                dataType = DataTypeTransform.DataType.Integer16bit;
            } else if (term.equals(signed8bitIntegerID)) {
                dataType = DataTypeTransform.DataType.Integer8bit;
            }
        } else {
            System.out.println("BinaryDataArray#getDataType(): " + Arrays.toString(this.getCVParamList().toArray()));
        }

        return dataType;
    }

    /**
     * Gets the external array length.
     *
     * @return the external array length
     */
    public long getExternalArrayLength() {
        CVParam arrayLengthCVParam = getCVParam(externalArrayLengthID);

        if (arrayLengthCVParam == null) {
            return -1;
        }

        return arrayLengthCVParam.getValueAsLong();
    }

    /**
     * Gets the external encoded length.
     *
     * @return the external encoded length
     */
    public long getExternalEncodedLength() {
        CVParam encodedLengthCVParam = getCVParam(externalEncodedLengthID);

        if (encodedLengthCVParam == null) {
            return -1;
        }

        return encodedLengthCVParam.getValueAsLong();
    }

    /**
     * Gets the external offset.
     *
     * @return the external offset
     */
    public long getExternalOffset() {
        CVParam externalOffset = getCVParam(externalOffsetID);

        if (externalOffset == null) {
            return -1;
        }

        return externalOffset.getValueAsLong();
    }

    @Override
    public void addCVParam(CVParam cvParam) {
        // Check if the cvParam is a binaryDataType
        if (cvParam.getTerm().isChildOf(binaryDataArrayID)) {
            String term = cvParam.getTerm().getID();

            if (term.equals(mzArrayID)) {
                ismzArray = true;
            } else if (term.equals(intensityArrayID)) {
                isIntensityArray = true;
            }
        }

        super.addCVParam(cvParam);
    }

    @Override
    public void addReferenceableParamGroupRef(ReferenceableParamGroupRef rpgr) {
        CVParam child = rpgr.getReference().getCVParamOrChild(binaryDataArrayID);

        if (child != null) {
            if (child.getTerm().getID().equals(mzArrayID)) {
                ismzArray = true;
            } else if (child.getTerm().getID().equals(intensityArrayID)) {
                isIntensityArray = true;
            }
        }

        super.addReferenceableParamGroupRef(rpgr);
    }

    /**
     * Check if the binary data array is an m/z array.
     *
     * @return true if is m/z array, false otherwise
     */
    public boolean ismzArray() {
        return ismzArray;
    }

    /**
     * Check if the binary data array is an intensity array.
     *
     * @return true if is intensity array, false otherwise
     */
    public boolean isIntensityArray() {
        return isIntensityArray;
    }

    /**
     * Sets the compression (or none).
     * 
     * @param compression Compression
     */
    public void setCompression(BinaryDataArray.CompressionType compression) {
        String compressionID;
        
        switch(compression) {
            case Zlib:
                compressionID = BinaryDataArray.zlibCompressionID;
                break;
            case None:
            default:
                compressionID = BinaryDataArray.noCompressionID;
                break;
        }
        
        this.removeChildOfCVParam(BinaryDataArray.compressionTypeID);
        this.addCVParam(new EmptyCVParam(OBO.getOBO().getTerm(compressionID)));
    }
    
    /**
     * Sets the data type to be used when storing data. This only takes effect
     * when the data is written out.
     * 
     * @param dataType New data type
     */
    public void setDataType(DataTypeTransform.DataType dataType) {
        String newDataTypeID;
        
        switch(dataType) {
            case Double:
            default:
                newDataTypeID = BinaryDataArray.doublePrecisionID;
                break;
            case Float:
                newDataTypeID = BinaryDataArray.singlePrecisionID;
                break;
            case Integer64bit:
                newDataTypeID = BinaryDataArray.signed64bitIntegerID;
                break;
            case Integer32bit:
                newDataTypeID = BinaryDataArray.signed32bitIntegerID;
                break;
            case Integer16bit:
                newDataTypeID = BinaryDataArray.signed16bitIntegerID;
                break;
            case Integer8bit:
                newDataTypeID = BinaryDataArray.signed8bitIntegerID;
                break;
        }
        
        this.removeChildOfCVParam(BinaryDataArray.dataTypeID);
        this.addCVParam(new EmptyCVParam(OBO.getOBO().getTerm(newDataTypeID)));
    }
    
    /**
     * Gets the binary data array type. (e.g. m/z or intensity array)
     *
     * @return the binary data array type
     */
    public CVParam getDataArrayType() {
        return getCVParamOrChild(binaryDataArrayID);
    }

    @Override
    public String getXMLAttributeText() {
        String attributeText = "encodedLength=\"" + encodedLength + "\"";

        if (arrayLength != -1) {
            attributeText += " arrayLength=\"" + arrayLength + "\"";
        }
        if (dataProcessingRef != null) {
            attributeText += " dataProcessingRef=\"" + XMLHelper.ensureSafeXML(dataProcessingRef.getID()) + "\"";
        }

        return attributeText;
    }

    @Override
    public String toString() {
        return "binaryDataArray: " + dataLocation;
    }

    @Override
    public String getTagName() {
        return "binaryDataArray";
    }

    @Override
    public void addChildrenToCollection(Collection<MzMLTag> children) {
        super.addChildrenToCollection(children);
    }
}
