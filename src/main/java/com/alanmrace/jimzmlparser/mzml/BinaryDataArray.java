package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.data.Base64DataTransform;
import com.alanmrace.jimzmlparser.data.DataLocation;
import com.alanmrace.jimzmlparser.data.DataTransformation;
import com.alanmrace.jimzmlparser.data.DataTypeTransform;
import com.alanmrace.jimzmlparser.data.DataTypeTransform.DataType;
import com.alanmrace.jimzmlparser.data.ZlibDataTransform;
import com.alanmrace.jimzmlparser.util.XMLHelper;
import com.alanmrace.jimzmlparser.writer.MzMLWriteable;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;

/**
 * BinaryDataArray tag.
 *
 * <p>
 * TODO: Reassess the variables and the getter methods as the getter methods
 * mainly search the CVParams.
 *
 * @author Alan Race
 */
public class BinaryDataArray extends MzMLContentWithParams implements Serializable {

    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = 1L;

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

    /**
     * Buffer size to use when reading in data. Set to 2^20 bytes = 1 MB.
     */
    protected static final int BYTE_BUFFER_SIZE = 2 ^ 20;

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
     * Sub-element: Binary tag.
     */
    private Binary binary;

    /**
     * Binary data type of the stored data array.
     */
//    private DataTypeTransform.DataType dataType;

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

    @Override
    public ArrayList<OBOTermInclusion> getListOfRequiredCVParams() {
        ArrayList<OBOTermInclusion> required = new ArrayList<OBOTermInclusion>();
        required.add(new OBOTermInclusion(compressionTypeID, true, true, false));
        required.add(new OBOTermInclusion(binaryDataArrayID, true, true, false));
        required.add(new OBOTermInclusion(dataTypeID, true, true, false));
        required.add(new OBOTermInclusion(externalArrayLengthID, true, false, true));
        required.add(new OBOTermInclusion(externalDataID, true, false, true));
        required.add(new OBOTermInclusion(externalEncodedLengthID, true, false, true));
        required.add(new OBOTermInclusion(externalOffsetID, true, false, true));

        return required;
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
     * Sets the binary tag.
     *
     * @param binary the new binary tag
     */
    public void setBinary(Binary binary) {
        this.binary = binary;
    }

    /**
     * Gets the binary tag.
     *
     * @return the binary tag
     */
    public Binary getBinary() {
        return binary;
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
     * <p>
     * TODO: Doesn't keep data in memory
     *
     * @param keepInMemory true if data should be kept in memory, false
     * otherwise
     * @return Uncompressed data as double[]
     * @throws IOException On failure to read from data location
     */
    public double[] getDataAsDouble(boolean keepInMemory) throws IOException {
        if (binary != null && binary.getData() != null) {
            return binary.getData();
        }

//        byte[] data = getDataAsByte(keepInMemory);
//
//        CVParam dataTypeCVParam = this.getCVParamOrChild(BinaryDataArray.dataTypeID);
//
//        double[] convertedData = convertDataToDouble(data, dataTypeCVParam);
//
//        if (keepInMemory) {
//            binary = new Binary(convertedData);
//        }
        double[] data = null;

        try {
            data = dataLocation.getData();
        } catch (DataFormatException ex) {
            Logger.getLogger(BinaryDataArray.class.getName()).log(Level.SEVERE, null, ex);
        }

        return data;
    }

    /**
     * Get the data array as byte[], decompressing as necessary. Calls
     * getDataAsByte(false); - does not keep in memory.
     *
     * @return Uncompressed data as byte[]
     * @throws IOException On failure to read from data location
     */
    public byte[] getDataAsByte() throws IOException {
        return getDataAsByte(false);
    }

    /**
     * Get the data array as byte[], decompressing as necessary, optionally
     * keeping the data in memory.
     *
     * <p>
     * TODO: Doesn't keep data in memory
     *
     * @param keepInMemory true if data should be kept in memory, false
     * otherwise
     * @return Uncompressed data as byte[]
     * @throws IOException On failure to read from data location
     */
    public byte[] getDataAsByte(boolean keepInMemory) throws IOException {
        // Check to see if already loaded data in memory
        if (binary != null && binary.getData() != null) {

        }

        if (dataLocation == null) {
            return null;
        }

//        byte[] data = dataLocation.getData();
//
//        // Check if the data is compressed, if so decompress
//        try {
//            data = decompress(data);
//        } catch (DataFormatException ex) {
//            Logger.getLogger(BinaryDataArray.class.getName()).log(Level.SEVERE, "" + Arrays.toString(data), ex);
//        }
        return dataLocation.getBytes();
    }

    public DataTransformation generateDataTransformation() {
        DataTransformation transformation = new DataTransformation();

        // Always add in the conversion to data type first
        if (!getDataType().equals(DataType.Double)) {
            transformation.addTransform(new DataTypeTransform(DataType.Double, getDataType()));
        }

        CVParam compressionCVParam = this.getCVParamOrChild(BinaryDataArray.compressionTypeID);

        // Add in any compression
        if (compressionCVParam.getTerm().getID().equals(BinaryDataArray.zlibCompressionID)) {
            transformation.addTransform(new ZlibDataTransform());
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

        String term = dataTypeParam.getTerm().getID();
        DataType dataType = null;

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
        CVParam child = rpgr.getRef().getCVParamOrChild(binaryDataArrayID);

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
     * Gets the binary data array type. (e.g. m/z or intensity array)
     *
     * @return the binary data array type
     */
    public CVParam getDataArrayType() {
        return getCVParamOrChild(binaryDataArrayID);
    }

    @Override
    protected String getXMLAttributeText() {
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
    protected void outputXMLContent(MzMLWriteable output, int indent) throws IOException {
        super.outputXMLContent(output, indent);

        MzMLContent.indent(output, indent);
        
        if (binary == null) {
            output.write("<binary />\n");
        } else {
            output.write("<binary>");
            
            try {
                double[] data = getDataAsDouble();
                DataTransformation transformation = this.generateDataTransformation();
                //transformation.addTransform(new Base64DataTransform());
                                
                output.writeData(transformation.performForwardTransform(data));

            } catch (DataFormatException ex) {
                Logger.getLogger(BinaryDataArray.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            //            output.writeData(convertDataToBytes());
            output.write("</binary>\n");
        }
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

//        if(binary != null)
//            children.add(binary);
    }
}
