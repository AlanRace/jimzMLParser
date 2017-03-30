package com.alanmrace.jimzmlparser.mzML;

import com.alanmrace.jimzmlparser.parser.DataStorage;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.zip.Deflater;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import java.util.Collection;

/**
 * Binary tag. When an mzML file is parsed the data is output into a temporary
 * file. This object allows access to the temporary file.
 *
 * @author Alan Race
 */
public class Binary implements Serializable, MzMLTag {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * Number of bytes of data.
     */
    private long numBytes;
    
    public enum DataType {
        doublePrecision,
        singlePrecision,
        signed8bitInteger,
        signed16bitInteger,
        signed32bitInteger,
        signed64bitInteger;
    }

    public enum CompressionType {
        noCompression,
        zlibCompresion;
    }

    private DataType dataType;
    private CVParam cvDataType;

    private CompressionType compression;

    private double[] data;

    /**
     * Constructor for use when writing out mzML
     *
     * @param data
     * @param dataType
     * @param compression
     */
    public Binary(double[] data, DataType dataType, CompressionType compression) {
        this.data = data;

        this.dataType = dataType;
        this.compression = compression;
    }

    public Binary(double[] data) {
        this(data, DataType.doublePrecision, CompressionType.noCompression);
    }
    
    @Override
    public String getTagName() {
        return "binary";
    }

    @Override
    public void addChildrenToCollection(Collection<MzMLTag> children) {
        // No children
    }
    
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

    public CVParam getDataTypeCVParam() {
        return cvDataType;
    }

    public DataType getDataType() {
        return dataType;
    }

    public CompressionType getCompressionType() {
        return compression;
    }

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

}
