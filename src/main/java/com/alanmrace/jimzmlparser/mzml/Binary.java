package com.alanmrace.jimzmlparser.mzml;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import com.sun.org.apache.xml.internal.security.utils.Base64;

/**
 * Binary tag. When an mzML file is parsed the data is output into a temporary
 * file. This object allows access to the temporary file.
 *
 * @author Alan Race
 */
public class Binary extends MzMLContent {

    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * Possible binary data types used to store data.
     */
    public enum DataType {

        /**
         * Double precision floating point.
         */
        doublePrecision,

        /**
         * Floating point.
         */
        singlePrecision,

        /**
         * Signed 8-bit integer.
         */
        signed8bitInteger,

        /**
         * Signed 16-bit integer.
         */
        signed16bitInteger,

        /**
         * Signed 32-bit integer.
         */
        signed32bitInteger,

        /**
         * Signed 64-bit integer.
         */
        signed64bitInteger;
    }

    /**
     * Possible compression methods for compressing binary data.
     */
    public enum CompressionType {

        /**
         * No compression.
         */
        noCompression,

        /**
         * ZLib compression.
         */
        zlibCompresion;
    }

    /**
     * Binary data type used to store the data.
     */
    private DataType dataType;

    /**
     * CVParam describing the binary data type used to store the data.
     */
    private CVParam cvDataType;

    /**
     * Compression (if any) used to compress the binary data.
     */
    private CompressionType compression;

    /**
     * Uncompressed data, stored as double precision.
     */
    private double[] data;

    /**
     * Constructor for use when writing out mzML.
     *
     * @param data          Data associated with this binary tag
     * @param dataType      Data type that should be used to store data
     * @param compression   Compression (if any) to use on the data
     */
    public Binary(double[] data, DataType dataType, CompressionType compression) {
        this.data = data;

        this.dataType = dataType;
        this.compression = compression;
    }

    /**
     * Constructor for use when writing out mzML. Default to double precision 
     * and no compression.
     *
     * @param data Data associated with this binary tag
     */
    public Binary(double[] data) {
        this(data, DataType.doublePrecision, CompressionType.noCompression);
    }
    
    @Override
    public String getTagName() {
        return "binary";
    }
    
    /**
     * Convert a CVParam (with MS or IMS OBO ontology terms) describing the data 
     * type to a {@link DataType}.
     * 
     * @param cvParam   CVParam to convert
     * @return          DataType if conversion possible, otherwise null
     */
    public static DataType getDataTypeFromCV(CVParam cvParam) {
        String term = cvParam.getTerm().getID();

        if (term.equals(BinaryDataArray.doublePrecisionID)) {
            return DataType.doublePrecision;
        } else if (term.equals(BinaryDataArray.singlePrecisionID)) {
            return Binary.DataType.singlePrecision;
        } else if (term.equals(BinaryDataArray.signed64bitIntegerID) || term.equals(BinaryDataArray.imsSigned64bitIntegerID)) {
            return Binary.DataType.signed64bitInteger;
        } else if (term.equals(BinaryDataArray.signed32bitIntegerID) || term.equals(BinaryDataArray.imsSigned32bitIntegerID)) {
            return Binary.DataType.signed32bitInteger;
        } else if (term.equals(BinaryDataArray.signed16bitIntegerID)) {
            return Binary.DataType.signed16bitInteger;
        } else if (term.equals(BinaryDataArray.signed8bitIntegerID)) {
            return Binary.DataType.signed8bitInteger;
        }

        return null;
    }

    /**
     * Get the CVParam that describes the binary data type.
     * 
     * @return Data type CVParam
     */
    public CVParam getDataTypeCVParam() {
        return cvDataType;
    }

    /**
     * Get the DataType used to store binary data.
     * 
     * @return DataType
     */
    public DataType getDataType() {
        return dataType;
    }

    /**
     * Get the compression method used to store binary data.
     * 
     * @return Compression method
     */
    public CompressionType getCompressionType() {
        return compression;
    }

    @Override
    public void outputXML(BufferedWriter output, int indent) throws IOException {
        MzMLContent.indent(output, indent);

        if (this.data != null) {
            output.write("<binary>");

            byte[] bytes = new byte[8 * this.data.length];

            for (int i = 0; i < data.length; i++) {
                ByteBuffer buffer = ByteBuffer.wrap(bytes).putDouble(i * 8, data[i]);
                buffer.order(ByteOrder.LITTLE_ENDIAN);
                buffer.putDouble(i * 8, data[i]);
            }

            output.write(Base64.encode(bytes));

            MzMLContent.indent(output, indent);
            output.write("</binary>\n");
        } else {
            output.write("<binary />\n");
        }
    }

    @Override
    public void setParent(MzMLTag parent) {
        // This is a dummy function only included to allow the removal
    }
}
