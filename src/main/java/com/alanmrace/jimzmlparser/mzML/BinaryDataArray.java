package com.alanmrace.jimzmlparser.mzML;

import com.alanmrace.jimzmlparser.obo.OBOTerm;
import com.alanmrace.jimzmlparser.parser.DataLocation;
import com.alanmrace.jimzmlparser.util.XMLHelper;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * BinaryDataArray tag.
 *
 * @author Alan Race
 */
public class BinaryDataArray extends MzMLContent implements Serializable {

    /**
     *
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

    public static final String ibdDataType = "IMS:1000014";

    /**
     * Accession: m/z array (MS:1000514).
     */
    public static final String mzArrayID = "MS:1000514";

    /**
     * Accession: Intensity array (MS:1000515).
     */
    public static final String intensityArrayID = "MS:1000515";

    /**
     * Accession: Double precision (MS:1000523).
     */
    public static final String doublePrecisionID = "MS:1000523";

    /**
     * Accession: Single precision (MS:1000521).
     */
    public static final String singlePrecisionID = "MS:1000521";

    // The IMS obo has copies of the data types found in the MS obo
    public static final String signed32bitIntegerID = "MS:1000519";
    public static final String imsSigned32bitIntegerID = "IMS:1000141";
    public static final String signed64bitIntegerID = "MS:1000522";
    public static final String imsSigned64bitIntegerID = "IMS:1000142";

//	public static final String unsigned32bitIntegerID		= "IMS:1000142";
    public static final String signed8bitIntegerID = "IMS:1100000";
    public static final String signed16bitIntegerID = "IMS:1100001";

    /**
     * Accession: No compression (MS:1000576).
     */
    public static final String noCompressionID = "MS:1000576";

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

    private Binary.DataType dataType;

    private boolean ismzArray;
    private boolean isIntensityArray;

    protected DataLocation dataLocation;

    /**
     * Instantiates a new binaryDataArray tag.
     *
     * @param encodedLength The encoded length of the binary data
     */
    public BinaryDataArray(int encodedLength) {
        this.encodedLength = encodedLength;
    }

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
     * @return true, if double precision cvParam is present
     */
    public boolean isDoublePrecision() {
        if (dataType != null) {
            return dataType == Binary.DataType.doublePrecision;
        }

        CVParam dataType = getCVParam(doublePrecisionID);

        if (dataType == null) {
            return false;
        }

        this.dataType = Binary.DataType.doublePrecision;

        return true;
    }

    public boolean isSinglePrecision() {
        CVParam dataType = getCVParam(singlePrecisionID);

        if (dataType == null) {
            return false;
        }

        return true;
    }

    public boolean isSigned8BitInteger() {
        CVParam dataType = getCVParam(signed8bitIntegerID);

        if (dataType == null) {
            return false;
        }

        return true;
    }

    public boolean isSigned16BitInteger() {
        CVParam dataType = getCVParam(signed16bitIntegerID);

        if (dataType == null) {
            return false;
        }

        return true;
    }

    public boolean isSigned32BitInteger() {
        CVParam dataType = getCVParam(signed32bitIntegerID);
        CVParam imsDataType = getCVParam(imsSigned32bitIntegerID);

        if (dataType == null && imsDataType == null) {
            return false;
        }

        return true;
    }

    public boolean isSigned64BitInteger() {
        CVParam dataType = getCVParam(signed64bitIntegerID);
        CVParam imsDataType = getCVParam(imsSigned64bitIntegerID);

        if (dataType == null && imsDataType == null) {
            return false;
        }

        return true;
    }

    public boolean isCompressed() {
        CVParam compression = getCVParam(noCompressionID);

        if (compression == null) {
            return true;
        }

        return false;
    }

    public static int getDataTypeInBytes(CVParam dataType) {
        String dataTypeID = dataType.getTerm().getID();

        if (dataTypeID.equals(doublePrecisionID)) {
            return 8;
        } else if (dataTypeID.equals(singlePrecisionID)) {
            return 4;
        } else if (dataTypeID.equals(signed8bitIntegerID)) {
            return 1;
        } else if (dataTypeID.equals(signed16bitIntegerID)) {
            return 2;
        } else if (dataTypeID.equals(signed32bitIntegerID)) {
            return 4;
        } else if (dataTypeID.equals(signed64bitIntegerID)) {
            return 8;
        }

        return 1;
    }

    public void setDataLocation(DataLocation dataLocation) {
        this.dataLocation = dataLocation;
    }

    public DataLocation getDataLocation() {
        return dataLocation;
    }

    public double[] getDataAsDouble() throws IOException {
        return getDataAsDouble(false);
    }

    public byte[] getDataAsByte() throws IOException {
        return getDataAsByte(false);
    }

    public byte[] getDataAsByte(boolean keepInMemory) throws IOException {
        if (dataLocation == null) {
            return null;
        }

        byte[] data = dataLocation.getData();

        // Check if the data is compressed, if so decompress
        try {
            data = decompress(data);
        } catch (DataFormatException ex) {
            Logger.getLogger(BinaryDataArray.class.getName()).log(Level.SEVERE, "" + data, ex);
        }

        return data;
    }

    public double[] getDataAsDouble(boolean keepInMemory) throws IOException {
        byte[] data = getDataAsByte(keepInMemory);

        CVParam dataType = this.getCVParamOrChild(BinaryDataArray.dataTypeID);

//	    System.out.println("DataType: " + dataType);
        double[] convertedData = convertDataToDouble(data, dataType);

        return convertedData;
    }

    public static double[] convertDataToDouble(byte[] data, CVParam dataType) {
        double[] convertedData = null;

        if (data == null) {
            return convertedData;
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        // Check on the data type and convert to double if necessary
        if (dataType.getTerm().getID().equals(BinaryDataArray.doublePrecisionID)) {
            convertedData = new double[data.length / 8];

            // Convert to double
            for (int j = 0; j < convertedData.length; j++) {
                convertedData[j] = buffer.getDouble();
            }
        } else if (dataType.getTerm().getID().equals(BinaryDataArray.singlePrecisionID)) {
            convertedData = new double[data.length / 4];

            // Convert to double
            for (int j = 0; j < convertedData.length; j++) {
                convertedData[j] = buffer.getFloat();
            }

        } else if (dataType.getTerm().getID().equals(BinaryDataArray.imsSigned64bitIntegerID)
                || dataType.getTerm().getID().equals(BinaryDataArray.signed64bitIntegerID)) {
            convertedData = new double[data.length / 8];

            // Convert to double
            for (int j = 0; j < convertedData.length; j++) {
                convertedData[j] = buffer.getLong();
            }
        } else if (dataType.getTerm().getID().equals(BinaryDataArray.imsSigned32bitIntegerID)
                || dataType.getTerm().getID().equals(BinaryDataArray.signed32bitIntegerID)) {
            convertedData = new double[data.length / 4];

            // Convert to double
            for (int j = 0; j < convertedData.length; j++) {
                convertedData[j] = buffer.getInt();
            }
        } else if (dataType.getTerm().getID().equals(BinaryDataArray.signed16bitIntegerID)) {
            convertedData = new double[data.length / 2];

            // Convert to double
            for (int j = 0; j < convertedData.length; j++) {
                convertedData[j] = buffer.getShort();
            }
        } else if (dataType.getTerm().getID().equals(BinaryDataArray.signed8bitIntegerID)) {
            convertedData = new double[data.length];

            // Convert to double
            for (int j = 0; j < convertedData.length; j++) {
                convertedData[j] = buffer.get();
            }
        } else {
            throw new UnsupportedOperationException("Data type not supported: " + dataType);
        }

        return convertedData;
    }

    public static byte[] convertDataType(byte[] data, Binary.DataType dataType, CVParam newDataType) {
        if (dataType == null) {
            return convertDataType(data, new EmptyCVParam(new OBOTerm(BinaryDataArray.doublePrecisionID)), newDataType);
        }

        switch (dataType) {
            case singlePrecision:
                return convertDataType(data, new EmptyCVParam(new OBOTerm(BinaryDataArray.singlePrecisionID)), newDataType);
            case signed64bitInteger:
                return convertDataType(data, new EmptyCVParam(new OBOTerm(BinaryDataArray.signed64bitIntegerID)), newDataType);
            case signed32bitInteger:
                return convertDataType(data, new EmptyCVParam(new OBOTerm(BinaryDataArray.signed32bitIntegerID)), newDataType);
            case signed16bitInteger:
                return convertDataType(data, new EmptyCVParam(new OBOTerm(BinaryDataArray.signed16bitIntegerID)), newDataType);
            case signed8bitInteger:
                return convertDataType(data, new EmptyCVParam(new OBOTerm(BinaryDataArray.signed8bitIntegerID)), newDataType);
            case doublePrecision:
            default:
                return convertDataType(data, new EmptyCVParam(new OBOTerm(BinaryDataArray.doublePrecisionID)), newDataType);
        }
    }

    public static byte[] convertDataType(byte[] data, CVParam originalDataType, CVParam newDataType) {
        if (originalDataType.getTerm().getID().equals(newDataType.getTerm().getID())) {
            return data;
        }

        double[] doubleData = convertDataToDouble(data, originalDataType);

        ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
        DataOutputStream byteStream = new DataOutputStream(byteArrayStream);

        try {
            if (newDataType.getTerm().getID().equals(BinaryDataArray.doublePrecisionID)) {
                for (double dataPoint : doubleData) {
                    byteStream.writeLong(Long.reverseBytes(Double.doubleToLongBits(dataPoint)));
                }

            } else if (newDataType.getTerm().getID().equals(BinaryDataArray.singlePrecisionID)) {
                for (double dataPoint : doubleData) {
                    byteStream.writeInt(Integer.reverseBytes(Float.floatToIntBits((float) dataPoint)));
                }

            } else if (newDataType.getTerm().getID().equals(BinaryDataArray.signed64bitIntegerID)) {
                for (double dataPoint : doubleData) {
                    byteStream.writeLong(Long.reverseBytes((long) dataPoint));
                }

            } else if (newDataType.getTerm().getID().equals(BinaryDataArray.signed32bitIntegerID)) {
                for (double dataPoint : doubleData) {
                    byteStream.writeInt(Integer.reverseBytes((int) dataPoint));
                }
            } else if (newDataType.getTerm().getID().equals(BinaryDataArray.signed16bitIntegerID)) {
                for (double dataPoint : doubleData) {
                    byteStream.writeShort(Short.reverseBytes((short) dataPoint));
                }

            } else if (newDataType.getTerm().getID().equals(BinaryDataArray.signed8bitIntegerID)) {
                for (double dataPoint : doubleData) {
                    byteStream.writeByte((byte) dataPoint);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(BinaryDataArray.class.getName()).log(Level.SEVERE, null, ex);
        }

        return byteArrayStream.toByteArray();
    }

    public static byte[] compressZLib(byte[] data) {
        Deflater compressor = new Deflater();
        compressor.setInput(data);
        compressor.finish();

        ArrayList<Byte> compressedData = new ArrayList<Byte>();
//            int length = 0;
        int compressed = 0;

        do {
            byte[] temp = new byte[BYTE_BUFFER_SIZE]; // 2^20

            compressed = compressor.deflate(temp);

            for (int i = 0; i < compressed; i++) {
                compressedData.add(temp[i]);
            }

//                length += compressed;
        } while (compressed != 0);

        byte[] compressedBytes = new byte[compressedData.size()];

        for (int i = 0; i < compressedData.size(); i++) {
            compressedBytes[i] = compressedData.get(i);
        }

        return compressedBytes;
    }

    protected byte[] decompressZLib(byte[] data) throws DataFormatException {
        Inflater decompressor = new Inflater();
        decompressor.setInput(data);

        ArrayList<Byte> uncompressedData = new ArrayList<Byte>();
//				int length = 0;
        int uncompressed = 0;

        do {
            byte[] temp = new byte[BYTE_BUFFER_SIZE]; // 2^20

//					try {
            uncompressed = decompressor.inflate(temp);

            for (int i = 0; i < uncompressed; i++) {
                uncompressedData.add(temp[i]);
            }
//					} catch (DataFormatException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}

//					length += uncompressed;
        } while (uncompressed != 0);

        data = new byte[uncompressedData.size()];

        for (int i = 0; i < uncompressedData.size(); i++) {
            data[i] = uncompressedData.get(i);
        }

        decompressor.end();

        return data;
    }

    public static byte[] compress(byte[] data, CVParam compressionCVParam) {
        // Check the decompression type needed

        byte[] compressedData = data;

        if (compressionCVParam.getTerm().getID().equals(BinaryDataArray.noCompressionID)) {
        } else if (compressionCVParam.getTerm().getID().equals(BinaryDataArray.zlibCompressionID)) {
            compressedData = compressZLib(data);
        } else {
            throw new UnsupportedOperationException("Compression not supported: " + compressionCVParam);
        }

        return compressedData;
    }

    protected byte[] compress(byte[] data) {
        CVParam compressionCVParam = this.getCVParamOrChild(BinaryDataArray.compressionTypeID);

        return compress(data, compressionCVParam);
    }

    protected byte[] decompress(byte[] data) throws DataFormatException {
        // Check the decompression type needed
        CVParam compressionCVParam = this.getCVParamOrChild(BinaryDataArray.compressionTypeID);

        byte[] uncompressedData = data;

        if (compressionCVParam.getTerm().getID().equals(BinaryDataArray.noCompressionID)) {
        } else if (compressionCVParam.getTerm().getID().equals(BinaryDataArray.zlibCompressionID)) {
            uncompressedData = decompressZLib(data);
        } else {
            throw new UnsupportedOperationException("Compression not supported: " + compressionCVParam);
        }

        return uncompressedData;
    }

    /**
     * Gets binary data type. (e.g. double or single)
     *
     * @return the binary data type
     */
    public Binary.DataType getDataType() {
        //return getCVParam(dataTypeID);
        return dataType;
    }

    /**
     * Gets the external array length.
     *
     * @return the external array length
     */
    public long getExternalArrayLength() {
        CVParam arrayLength = getCVParam(externalArrayLengthID);

        if (arrayLength == null) {
            return -1;
        }

        return arrayLength.getValueAsLong();
    }

    /**
     * Gets the external encoded length.
     *
     * @return the external encoded length
     */
    public long getExternalEncodedLength() {
        CVParam encodedLength = getCVParam(externalEncodedLengthID);

        if (encodedLength == null) {
            return -1;
        }

        return encodedLength.getValueAsLong();
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
        if (cvParam.getTerm().isChildOf(dataTypeID)) {
            String term = cvParam.getTerm().getID();

            if (term.equals(doublePrecisionID)) {
                dataType = Binary.DataType.doublePrecision;
            } else if (term.equals(singlePrecisionID)) {
                dataType = Binary.DataType.singlePrecision;
            } else if (term.equals(signed64bitIntegerID) || term.equals(imsSigned64bitIntegerID)) {
                dataType = Binary.DataType.signed64bitInteger;
            } else if (term.equals(signed32bitIntegerID) || term.equals(imsSigned32bitIntegerID)) {
                dataType = Binary.DataType.signed32bitInteger;
            } else if (term.equals(signed16bitIntegerID)) {
                dataType = Binary.DataType.signed16bitInteger;
            } else if (term.equals(signed8bitIntegerID)) {
                dataType = Binary.DataType.signed8bitInteger;
            }
        } else if (cvParam.getTerm().isChildOf(binaryDataArrayID)) {
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

    public boolean ismzArray() {
        return ismzArray;
    }

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

    /* (non-Javadoc)
     * @see mzML.MzMLContent#outputXML(java.io.BufferedWriter, int)
     */
    public void outputXML(BufferedWriter output, int indent) throws IOException {
        MzMLContent.indent(output, indent);
        output.write("<binaryDataArray");
        output.write(" encodedLength=\"" + encodedLength + "\"");
        if (arrayLength != -1) {
            output.write(" arrayLength=\"" + arrayLength + "\"");
        }
        if (dataProcessingRef != null) {
            output.write(" dataProcessingRef=\"" + XMLHelper.ensureSafeXML(dataProcessingRef.getID()) + "\"");
        }
        output.write(">\n");

        super.outputXML(output, indent + 1);

        if (binary != null) {
            binary.outputXML(output, indent + 1);
        } else {
            MzMLContent.indent(output, indent + 1);
            output.write("<binary />\n");
        }

        MzMLContent.indent(output, indent);
        output.write("</binaryDataArray>\n");
    }

    public String toString() {
        return "binaryDataArray: " + dataLocation;
    }
}
