package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.data.DataLocation;
import com.alanmrace.jimzmlparser.data.DataTransformation;
import com.alanmrace.jimzmlparser.data.DataTypeTransform;
import com.alanmrace.jimzmlparser.data.DataTypeTransform.DataType;
import com.alanmrace.jimzmlparser.data.LZ4DataTransform;
import com.alanmrace.jimzmlparser.data.MSNumpressDataTransform;
import com.alanmrace.jimzmlparser.data.MSNumpressDataTransform.NumpressAlgorithm;
import com.alanmrace.jimzmlparser.data.XZDataTransform;
import com.alanmrace.jimzmlparser.data.ZlibDataTransform;
import com.alanmrace.jimzmlparser.data.ZstdDataTransform;
import com.alanmrace.jimzmlparser.obo.OBO;
import com.alanmrace.jimzmlparser.obo.OBOTerm;
import com.alanmrace.jimzmlparser.util.XMLHelper;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;

/**
 * BinaryDataArray tag.
 *
 * @author Alan Race
 */
public class BinaryDataArray extends MzMLContentWithParams implements Serializable {
    
    private static final Logger LOGGER = Logger.getLogger(BinaryDataArray.class.getName());

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
        NONE,

        /**
         * ZLib compression.
         */
        ZLIB,
        
        /**
         * XZ compression.
         */
        XZ,
        
        /**
         * LZ4 compression
         */
        LZ4,
        
        /**
         * ZStandard compression
         */
        ZSTD,
        
        /**
         * MS Numpress linear prediction compression
         */
        MSNUMPRESS_LINEAR,
        
        /**
         * MS Numpress positive integer compression
         */
        MSNUMPRESS_POSITIVE,
        
        /**
         * MS Numpress short logged float compression
         */
        MSNUMPRESS_SLOF,
        
        MSNUMPRESS_LINEAR_ZLIB,
                
        MSNUMPRESS_POSITIVE_ZLIB,
        
        MSNUMPRESS_SLOF_ZLIB,
        
        MSNUMPRESS_LINEAR_XZ,
                
        MSNUMPRESS_POSITIVE_XZ,
        
        MSNUMPRESS_SLOF_XZ,
        
        MSNUMPRESS_LINEAR_LZ4,
                
        MSNUMPRESS_POSITIVE_LZ4,
        
        MSNUMPRESS_SLOF_LZ4,
        
        MSNUMPRESS_LINEAR_ZSTD,
                
        MSNUMPRESS_POSITIVE_ZSTD,
        
        MSNUMPRESS_SLOF_ZSTD;
        
        /**
         * Convert CompressionType enum to OBOTerm, using ontology terms found in the 
         * MS ontology.
         * 
         * @param compressionType CompressionType to convert
         * @return Ontology term which describes the CompressionType, or null if no match
         */
        public static OBOTerm toOBOTerm(CompressionType compressionType) {
            switch(compressionType) {
                case NONE:
                    return OBO.getOBO().getTerm(NO_COMPRESSION_ID);
                case ZLIB:
                    return OBO.getOBO().getTerm(ZLIB_COMPRESSION_ID);
                case XZ:
                    return OBO.getOBO().getTerm(XZ_COMPRESSION_ID);
                case LZ4:
                    return OBO.getOBO().getTerm(LZ4_COMPRESSION_ID);
                case ZSTD:
                    return OBO.getOBO().getTerm(ZSTD_COMPRESSION_ID);
                case MSNUMPRESS_LINEAR:
                    return OBO.getOBO().getTerm(MSNUMPRESS_LINEAR_ID);
                case MSNUMPRESS_POSITIVE:
                    return OBO.getOBO().getTerm(MSNUMPRESS_POSITIVE_ID);
                case MSNUMPRESS_SLOF:
                    return OBO.getOBO().getTerm(MSNUMPRESS_SLOF_ID);
                case MSNUMPRESS_LINEAR_ZLIB:
                    return OBO.getOBO().getTerm(MSNUMPRESS_LINEAR_ZLIB_ID);
                case MSNUMPRESS_POSITIVE_ZLIB:
                    return OBO.getOBO().getTerm(MSNUMPRESS_POSITIVE_ZLIB_ID);
                case MSNUMPRESS_SLOF_ZLIB:
                    return OBO.getOBO().getTerm(MSNUMPRESS_SLOF_ZLIB_ID);
                case MSNUMPRESS_LINEAR_XZ:
                    return OBO.getOBO().getTerm(MSNUMPRESS_LINEAR_XZ_ID);
                case MSNUMPRESS_POSITIVE_XZ:
                    return OBO.getOBO().getTerm(MSNUMPRESS_POSITIVE_XZ_ID);
                case MSNUMPRESS_SLOF_XZ:
                    return OBO.getOBO().getTerm(MSNUMPRESS_SLOF_XZ_ID);
                case MSNUMPRESS_LINEAR_LZ4:
                    return OBO.getOBO().getTerm(MSNUMPRESS_LINEAR_LZ4_ID);
                case MSNUMPRESS_POSITIVE_LZ4:
                    return OBO.getOBO().getTerm(MSNUMPRESS_POSITIVE_LZ4_ID);
                case MSNUMPRESS_SLOF_LZ4:
                    return OBO.getOBO().getTerm(MSNUMPRESS_SLOF_LZ4_ID);
                case MSNUMPRESS_LINEAR_ZSTD:
                    return OBO.getOBO().getTerm(MSNUMPRESS_LINEAR_ZSTD_ID);
                case MSNUMPRESS_POSITIVE_ZSTD:
                    return OBO.getOBO().getTerm(MSNUMPRESS_POSITIVE_ZSTD_ID);
                case MSNUMPRESS_SLOF_ZSTD:
                    return OBO.getOBO().getTerm(MSNUMPRESS_SLOF_ZSTD_ID);
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
    public static final String COMPRESSION_TYPE_ID = "MS:1000572"; // Required child (1)

    /**
     * Accession: Binary data array (MS:1000513). MUST supply a child only once
     */
    public static final String BINARY_DATA_ARRAY_ID = "MS:1000513";	// Required child (1)

    /**
     * Accession: Binary data type (MS:1000518). MUST supply a child only once
     */
    public static final String BINARY_DATA_TYPE_ID = "MS:1000518";	// Required child (1)

    /**
     * Accession: IBD binary data type (IMS:1000014).
     */
    public static final String IBD_BINARY_DATA_TYPE_ID = "IMS:1000014";

    /**
     * Accession: m/z array (MS:1000514).
     */
    public static final String MZ_ARRAY_ID = "MS:1000514";

    /**
     * Accession: m/z units (MS:1000040).
     */
    public static final String MZ_ARRAY_UNITS_ID = "MS:1000040";

    /**
     * Accession: Intensity array (MS:1000515).
     */
    public static final String INTENSITY_ARRAY_ID = "MS:1000515";

    /**
     * Accession: Intensity array units: number of counts (MS:1000131).
     */
    public static final String INTENSITY_ARRAY_UNITS_NUMBER_OF_COUNTS_ID = "MS:1000131";

    /**
     * Accession: Intensity array units: percentage of basepeak (MS:1000132).
     */
    public static final String INTENSITY_ARRAY_UNITS_PERCENTAGE_OF_BASEPEAK_ID = "MS:1000132";

    /**
     * Accession: Intensity array units: counts per second (MS:1000814).
     */
    public static final String INTENSITY_ARRAY_UNITS_COUNTS_PER_SECOND_ID = "MS:1000814";

    /**
     * Accession: Intensity array units: percent of basepeak x 100 (MS:1000905).
     */
    public static final String INTENSITY_ARRAY_UNITS_PERCENTAGE_OF_BASEPEAK_TIMES_100_ID = "MS:1000905";

    /**
     * Accession: Double precision (MS:1000523).
     */
    public static final String DOUBLE_PRECISION_ID = "MS:1000523";

    /**
     * Accession: Single precision (MS:1000521).
     */
    public static final String SINGLE_PRECISION_ID = "MS:1000521";

    // The IMS obo has copies of the data types found in the MS obo
    /**
     * Accession: Signed 32-bit integer (MS:1000519).
     */
    public static final String SIGNED_32BIT_INTEGER_ID = "MS:1000519";

    /**
     * Accession: Signed 32-bit integer (IMS:1000141). Copy of MS:1000519.
     *
     * @see BinaryDataArray#signed32bitIntegerID
     */
    public static final String IMS_SIGNED_32BIT_INTEGER_ID = "IMS:1000141";

    /**
     * Accession: Signed 64-bit integer (MS:1000522).
     */
    public static final String SIGNED_64BIT_INTEGER_ID = "MS:1000522";

    /**
     * Accession: Signed 64-bit integer (IMS:1000142). Copy of MS:1000522.
     *
     * @see BinaryDataArray#signed64bitIntegerID
     */
    public static final String IMS_SIGNED_64BIT_INTEGER_ID = "IMS:1000142";

    /**
     * Accession: Signed 8-bit integer (IMS:1100000).
     */
    public static final String SIGNED_8BIT_INTEGER_ID = "IMS:1100000";

    /**
     * Accession: Signed 16-bit integer (IMS:1100000).
     */
    public static final String SIGNED_16BIT_INTEGER_ID = "IMS:1100001";

    /**
     * Accession: No compression (MS:1000576).
     */
    public static final String NO_COMPRESSION_ID = "MS:1000576";

    /**
     * Accession: zlib compression (MS:1000574).
     */
    public static final String ZLIB_COMPRESSION_ID = "MS:1000574";

    /**
     * Accession: xz compression (IMS:1005001).
     */
    public static final String XZ_COMPRESSION_ID = "IMS:1005001";
    
    /**
     * Accession: lz4 compression (IMS:1005001).
     */
    public static final String LZ4_COMPRESSION_ID = "IMS:1005002";
    
    /**
     * Accession: zstd compression (IMS:1005001).
     */
    public static final String ZSTD_COMPRESSION_ID = "IMS:1005003";
    
    public static final String MSNUMPRESS_POSITIVE_ID = "MS:1002313";
    
    public static final String MSNUMPRESS_LINEAR_ID = "MS:1002312";
    
    public static final String MSNUMPRESS_SLOF_ID = "MS:1002314";
    
    public static final String MSNUMPRESS_LINEAR_ZLIB_ID = "IMS:1005013";
    
    public static final String MSNUMPRESS_POSITIVE_ZLIB_ID = "IMS:1005014";
    
    public static final String MSNUMPRESS_SLOF_ZLIB_ID = "IMS:1005015";
    
    public static final String MSNUMPRESS_LINEAR_XZ_ID = "IMS:1005004";
    
    public static final String MSNUMPRESS_POSITIVE_XZ_ID = "IMS:1005005";
    
    public static final String MSNUMPRESS_SLOF_XZ_ID = "IMS:1005006";
    
    public static final String MSNUMPRESS_LINEAR_LZ4_ID = "IMS:1005007";
    
    public static final String MSNUMPRESS_POSITIVE_LZ4_ID = "IMS:1005008";
    
    public static final String MSNUMPRESS_SLOF_LZ4_ID = "IMS:1005009";
    
    public static final String MSNUMPRESS_LINEAR_ZSTD_ID = "IMS:1005010";
    
    public static final String MSNUMPRESS_POSITIVE_ZSTD_ID = "IMS:1005011";
    
    public static final String MSNUMPRESS_SLOF_ZSTD_ID = "IMS:1005012";
    
    /**
     * Accession: External array length (IMS:1000103). MUST supply once
     */
    public static final String EXTERNAL_ARRAY_LENGTH_ID = "IMS:1000103";

    /**
     * Accession: External data (IMS:1000101). MUST supply once
     */
    public static final String EXTERNAL_DATA_ID = "IMS:1000101";

    /**
     * Accession: External encoded length (IMS:1000104). MUST supply once
     */
    public static final String EXTERNAL_ENCODED_LENGTH_ID = "IMS:1000104";

    /**
     * Accession: External offset (IMS:1000102). MUST supply once
     */
    public static final String EXTERNAL_OFFSET_ID = "IMS:1000102";

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
        this.dataLocation = bda.dataLocation;
        
        this.ismzArray = bda.ismzArray;
        this.isIntensityArray = bda.isIntensityArray;
        this.data = bda.data;

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

        return dataType == DataTypeTransform.DataType.DOUBLE;
    }

    /**
     * Checks if the binary data is single precision floating point.
     *
     * @return true, if single precision cvParam is present, false otherwise
     */
    public boolean isSinglePrecision() {
        CVParam dataTypeCVParam = getCVParam(SINGLE_PRECISION_ID);

        return dataTypeCVParam != null;
    }

    /**
     * Checks if the binary data is 8-bit integer.
     *
     * @return true, if 8-bit integer cvParam is present, false otherwise
     */
    public boolean isSigned8BitInteger() {
        CVParam dataTypeCVParam = getCVParam(SIGNED_8BIT_INTEGER_ID);

        return dataTypeCVParam != null;
    }

    /**
     * Checks if the binary data is 16-bit integer.
     *
     * @return true, if 16-bit integer cvParam is present, false otherwise
     */
    public boolean isSigned16BitInteger() {
        CVParam dataTypeCVParam = getCVParam(SIGNED_16BIT_INTEGER_ID);

        return dataTypeCVParam != null;
    }

    /**
     * Checks if the binary data is 32-bit integer.
     *
     * @return true, if 32-bit integer cvParam is present, false otherwise
     */
    public boolean isSigned32BitInteger() {
        CVParam dataTypeCVParam = getCVParam(SIGNED_32BIT_INTEGER_ID);
        CVParam imsDataType = getCVParam(IMS_SIGNED_32BIT_INTEGER_ID);

        return !(dataTypeCVParam == null && imsDataType == null);
    }

    /**
     * Checks if the binary data is 64-bit integer.
     *
     * @return true, if 64-bit integer cvParam is present, false otherwise
     */
    public boolean isSigned64BitInteger() {
        CVParam dataTypeCVParam = getCVParam(SIGNED_64BIT_INTEGER_ID);
        CVParam imsDataType = getCVParam(IMS_SIGNED_64BIT_INTEGER_ID);

        return !(dataTypeCVParam == null && imsDataType == null);
    }

    /**
     * Checks if the cvParam for no compression is present.
     *
     * @return true, if the no compression cvParam cannot be found, false
     * otherwise
     */
    public boolean isCompressed() {
        CVParam compression = getCVParam(NO_COMPRESSION_ID);

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

        if (dataTypeTermID.equals(DOUBLE_PRECISION_ID)) {
            return 8;
        } else if (dataTypeTermID.equals(SINGLE_PRECISION_ID)) {
            return 4;
        } else if (dataTypeTermID.equals(SIGNED_8BIT_INTEGER_ID)) {
            return 1;
        } else if (dataTypeTermID.equals(SIGNED_16BIT_INTEGER_ID)) {
            return 2;
        } else if (dataTypeTermID.equals(SIGNED_32BIT_INTEGER_ID)) {
            return 4;
        } else if (dataTypeTermID.equals(SIGNED_64BIT_INTEGER_ID)) {
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
        if (dataLocation == null && parent != null) {
            MzMLTag grandParent = parent.getParent();

            if (grandParent instanceof MzMLDataContainer) {
                ((MzMLDataContainer) grandParent).convertMzMLDataStorageToBase64();
            }
        }
         
        // If there is still no dataLocation after the conversion, then no data to load
        if (dataLocation == null) {
            return new double[0];
        }

        double[] loadedData = new double[0];

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
            return new byte[0];
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
        if (!DataType.DOUBLE.equals(getDataType())) {
            transformation.addTransform(new DataTypeTransform(DataType.DOUBLE, getDataType()));
        }

        CVParam compressionCVParam = this.getCVParamOrChild(BinaryDataArray.COMPRESSION_TYPE_ID);
        
        // Add in any compression
        if (BinaryDataArray.ZLIB_COMPRESSION_ID.equals(compressionCVParam.getTerm().getID())) {
            transformation.addTransform(new ZlibDataTransform());
        } else if(BinaryDataArray.XZ_COMPRESSION_ID.equals(compressionCVParam.getTerm().getID())) {
            transformation.addTransform(new XZDataTransform());
        } else if(BinaryDataArray.LZ4_COMPRESSION_ID.equals(compressionCVParam.getTerm().getID())) {
            transformation.addTransform(new LZ4DataTransform((int)(this.getExternalArrayLength() * getDataTypeInBytes(getCVParamOrChild(BINARY_DATA_TYPE_ID)))));
        } else if(BinaryDataArray.ZSTD_COMPRESSION_ID.equals(compressionCVParam.getTerm().getID())) {
            transformation.addTransform(new ZstdDataTransform((int)(this.getExternalArrayLength() * getDataTypeInBytes(getCVParamOrChild(BINARY_DATA_TYPE_ID)))));
        } else if(BinaryDataArray.MSNUMPRESS_LINEAR_ID.equals(compressionCVParam.getTerm().getID())) {
            transformation.addTransform(new MSNumpressDataTransform(NumpressAlgorithm.LINEAR));
        } else if(BinaryDataArray.MSNUMPRESS_POSITIVE_ID.equals(compressionCVParam.getTerm().getID())) {
            transformation.addTransform(new MSNumpressDataTransform(NumpressAlgorithm.PIC));
        } else if(BinaryDataArray.MSNUMPRESS_SLOF_ID.equals(compressionCVParam.getTerm().getID())) {
            transformation.addTransform(new MSNumpressDataTransform(NumpressAlgorithm.SLOF));
        } else if(BinaryDataArray.MSNUMPRESS_LINEAR_LZ4_ID.equals(compressionCVParam.getTerm().getID())) {
            transformation.addTransform(new MSNumpressDataTransform(NumpressAlgorithm.LINEAR));
            
            int decompressedSize = (int)(this.getExternalArrayLength() * getDataTypeInBytes(getCVParamOrChild(BINARY_DATA_TYPE_ID)));
            UserParam userParam = this.getUserParam("LZ4 decompression size");
            
            if(userParam != null) {
                decompressedSize = Integer.parseInt(this.getUserParam("LZ4 decompression size").getValue());
            }
            
            transformation.addTransform(new LZ4DataTransform(decompressedSize));
        } else if(BinaryDataArray.MSNUMPRESS_LINEAR_XZ_ID.equals(compressionCVParam.getTerm().getID())) {
            transformation.addTransform(new MSNumpressDataTransform(NumpressAlgorithm.LINEAR));
            transformation.addTransform(new XZDataTransform());
        } else if(BinaryDataArray.MSNUMPRESS_LINEAR_ZLIB_ID.equals(compressionCVParam.getTerm().getID())) {
            transformation.addTransform(new MSNumpressDataTransform(NumpressAlgorithm.LINEAR));
            transformation.addTransform(new ZlibDataTransform());
        } else if(BinaryDataArray.MSNUMPRESS_LINEAR_ZSTD_ID.equals(compressionCVParam.getTerm().getID())) {
            transformation.addTransform(new MSNumpressDataTransform(NumpressAlgorithm.LINEAR));
            transformation.addTransform(new ZstdDataTransform((int)(this.getExternalArrayLength() * getDataTypeInBytes(getCVParamOrChild(BINARY_DATA_TYPE_ID)))));
        } else if(BinaryDataArray.MSNUMPRESS_POSITIVE_LZ4_ID.equals(compressionCVParam.getTerm().getID())) {
            transformation.addTransform(new MSNumpressDataTransform(NumpressAlgorithm.PIC));
            
            int decompressedSize = (int)(this.getExternalArrayLength() * getDataTypeInBytes(getCVParamOrChild(BINARY_DATA_TYPE_ID)));
            UserParam userParam = this.getUserParam("LZ4 decompression size");
            
            if(userParam != null) {
                decompressedSize = Integer.parseInt(this.getUserParam("LZ4 decompression size").getValue());
            }
            
            transformation.addTransform(new LZ4DataTransform(decompressedSize));
        } else if(BinaryDataArray.MSNUMPRESS_POSITIVE_XZ_ID.equals(compressionCVParam.getTerm().getID())) {
            transformation.addTransform(new MSNumpressDataTransform(NumpressAlgorithm.PIC));
            transformation.addTransform(new XZDataTransform());
        } else if(BinaryDataArray.MSNUMPRESS_POSITIVE_ZLIB_ID.equals(compressionCVParam.getTerm().getID())) {
            transformation.addTransform(new MSNumpressDataTransform(NumpressAlgorithm.PIC));
            transformation.addTransform(new ZlibDataTransform());
        } else if(BinaryDataArray.MSNUMPRESS_POSITIVE_ZSTD_ID.equals(compressionCVParam.getTerm().getID())) {
            transformation.addTransform(new MSNumpressDataTransform(NumpressAlgorithm.PIC));
            transformation.addTransform(new ZstdDataTransform((int)(this.getExternalArrayLength() * getDataTypeInBytes(getCVParamOrChild(BINARY_DATA_TYPE_ID)))));
        } else if(BinaryDataArray.MSNUMPRESS_SLOF_LZ4_ID.equals(compressionCVParam.getTerm().getID())) {
            transformation.addTransform(new MSNumpressDataTransform(NumpressAlgorithm.SLOF));
            
            int decompressedSize = (int)(this.getExternalArrayLength() * getDataTypeInBytes(getCVParamOrChild(BINARY_DATA_TYPE_ID)));
            UserParam userParam = this.getUserParam("LZ4 decompression size");
            
            if(userParam != null) {
                decompressedSize = Integer.parseInt(this.getUserParam("LZ4 decompression size").getValue());
            }
            
            transformation.addTransform(new LZ4DataTransform(decompressedSize));
        } else if(BinaryDataArray.MSNUMPRESS_SLOF_XZ_ID.equals(compressionCVParam.getTerm().getID())) {
            transformation.addTransform(new MSNumpressDataTransform(NumpressAlgorithm.SLOF));
            transformation.addTransform(new XZDataTransform());
        } else if(BinaryDataArray.MSNUMPRESS_SLOF_ZLIB_ID.equals(compressionCVParam.getTerm().getID())) {
            transformation.addTransform(new MSNumpressDataTransform(NumpressAlgorithm.SLOF));
            transformation.addTransform(new ZlibDataTransform());
        } else if(BinaryDataArray.MSNUMPRESS_SLOF_ZSTD_ID.equals(compressionCVParam.getTerm().getID())) {
            transformation.addTransform(new MSNumpressDataTransform(NumpressAlgorithm.SLOF));
            transformation.addTransform(new ZstdDataTransform((int)(this.getExternalArrayLength() * getDataTypeInBytes(getCVParamOrChild(BINARY_DATA_TYPE_ID)))));
        }
        
        return transformation;
    }

    /**
     * Gets binary data type. (e.g. double or single)
     *
     * @return the binary data type
     */
    public DataType getDataType() {
        CVParam dataTypeParam = this.getCVParamOrChild(BINARY_DATA_TYPE_ID);
        DataType dataType = null;
        
        if(dataTypeParam != null) {
            String term = dataTypeParam.getTerm().getID();

            if (term.equals(DOUBLE_PRECISION_ID)) {
                dataType = DataTypeTransform.DataType.DOUBLE;
            } else if (term.equals(SINGLE_PRECISION_ID)) {
                dataType = DataTypeTransform.DataType.FLOAT;
            } else if (term.equals(SIGNED_64BIT_INTEGER_ID) || term.equals(IMS_SIGNED_64BIT_INTEGER_ID)) {
                dataType = DataTypeTransform.DataType.INTEGER_64BIT;
            } else if (term.equals(SIGNED_32BIT_INTEGER_ID) || term.equals(IMS_SIGNED_32BIT_INTEGER_ID)) {
                dataType = DataTypeTransform.DataType.INTEGER_32BIT;
            } else if (term.equals(SIGNED_16BIT_INTEGER_ID)) {
                dataType = DataTypeTransform.DataType.INTEGER_16BIT;
            } else if (term.equals(SIGNED_8BIT_INTEGER_ID)) {
                dataType = DataTypeTransform.DataType.INTEGER_8BIT;
            }
        } else {
            LOGGER.log(Level.INFO, "BinaryDataArray#getDataType(): {0}", Arrays.toString(this.getCVParamList().toArray()));
        }

        return dataType;
    }

    /**
     * Gets the external array length.
     *
     * @return the external array length
     */
    public long getExternalArrayLength() {
        CVParam arrayLengthCVParam = getCVParam(EXTERNAL_ARRAY_LENGTH_ID);

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
        CVParam encodedLengthCVParam = getCVParam(EXTERNAL_ENCODED_LENGTH_ID);

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
        CVParam externalOffset = getCVParam(EXTERNAL_OFFSET_ID);

        if (externalOffset == null) {
            return -1;
        }

        return externalOffset.getValueAsLong();
    }

    @Override
    public void addCVParam(CVParam cvParam) {
        LOGGER.log(Level.FINEST, "Adding CVParam to BinaryDataArray {0}", cvParam);
        
        // Check if the cvParam is a binaryDataType
        if (cvParam.getTerm().isChildOf(BINARY_DATA_ARRAY_ID)) {
            String term = cvParam.getTerm().getID();

            if (term.equals(MZ_ARRAY_ID)) {
                LOGGER.log(Level.FINEST, "Found m/z array");
                
                ismzArray = true;
            } else if (term.equals(INTENSITY_ARRAY_ID)) {
                isIntensityArray = true;
            }
        }

        super.addCVParam(cvParam);
    }

    @Override
    public void addReferenceableParamGroupRef(ReferenceableParamGroupRef rpgr) {
        CVParam child = rpgr.getReference().getCVParamOrChild(BINARY_DATA_ARRAY_ID);

        if (child != null) {
            if (child.getTerm().getID().equals(MZ_ARRAY_ID)) {
                ismzArray = true;
            } else if (child.getTerm().getID().equals(INTENSITY_ARRAY_ID)) {
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
        this.removeChildrenOfCVParam(BinaryDataArray.COMPRESSION_TYPE_ID, false);
        this.addCVParam(new EmptyCVParam(BinaryDataArray.CompressionType.toOBOTerm(compression)));
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
            case FLOAT:
                newDataTypeID = BinaryDataArray.SINGLE_PRECISION_ID;
                break;
            case INTEGER_64BIT:
                newDataTypeID = BinaryDataArray.SIGNED_64BIT_INTEGER_ID;
                break;
            case INTEGER_32BIT:
                newDataTypeID = BinaryDataArray.SIGNED_32BIT_INTEGER_ID;
                break;
            case INTEGER_16BIT:
                newDataTypeID = BinaryDataArray.SIGNED_16BIT_INTEGER_ID;
                break;
            case INTEGER_8BIT:
                newDataTypeID = BinaryDataArray.SIGNED_8BIT_INTEGER_ID;
                break;
            case DOUBLE:
            default:
                newDataTypeID = BinaryDataArray.DOUBLE_PRECISION_ID;
                break;
        }
        
        this.removeChildrenOfCVParam(BinaryDataArray.BINARY_DATA_TYPE_ID, false);
        this.addCVParam(new EmptyCVParam(OBO.getOBO().getTerm(newDataTypeID)));
    }
    
    /**
     * Gets the binary data array type. (e.g. m/z or intensity array)
     *
     * @return the binary data array type
     */
    public CVParam getDataArrayType() {
        return getCVParamOrChild(BINARY_DATA_ARRAY_ID);
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
}
