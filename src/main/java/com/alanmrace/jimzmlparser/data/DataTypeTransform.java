package com.alanmrace.jimzmlparser.data;

import com.alanmrace.jimzmlparser.mzml.BinaryDataArray;
import static com.alanmrace.jimzmlparser.mzml.BinaryDataArray.doublePrecisionID;
import static com.alanmrace.jimzmlparser.mzml.BinaryDataArray.imsSigned32bitIntegerID;
import static com.alanmrace.jimzmlparser.mzml.BinaryDataArray.imsSigned64bitIntegerID;
import static com.alanmrace.jimzmlparser.mzml.BinaryDataArray.signed16bitIntegerID;
import static com.alanmrace.jimzmlparser.mzml.BinaryDataArray.signed32bitIntegerID;
import static com.alanmrace.jimzmlparser.mzml.BinaryDataArray.signed64bitIntegerID;
import static com.alanmrace.jimzmlparser.mzml.BinaryDataArray.signed8bitIntegerID;
import static com.alanmrace.jimzmlparser.mzml.BinaryDataArray.singlePrecisionID;
import com.alanmrace.jimzmlparser.obo.OBO;
import com.alanmrace.jimzmlparser.obo.OBOTerm;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;

/**
 * DataTransformation where data stored in a byte[] representation of one data 
 * type is converted to a byte[] representation of another data type. For example
 * this could be the conversion of a double -> integer. Valid data types are 
 * included in {@link DataTypeTransform.DataType}.
 * 
 * @author Alan Race
 */
public class DataTypeTransform implements DataTransform {

    /**
     * Possible binary data types used to store data.
     */
    public enum DataType {

        /**
         * Double precision floating point.
         */
        Double,
        /**
         * Floating point.
         */
        Float,
        /**
         * Signed 8-bit integer.
         */
        Integer8bit,
        /**
         * Signed 16-bit integer.
         */
        Integer16bit,
        /**
         * Signed 32-bit integer.
         */
        Integer32bit,
        /**
         * Signed 64-bit integer.
         */
        Integer64bit;
        
        /**
         * Convert DataType enum to OBOTerm, using ontology terms found in the 
         * MS ontology.
         * 
         * @param dataType DataType to convert
         * @return Ontology term which describes the DataType, or null if no match
         */
        public static OBOTerm toOBOTerm(DataType dataType) {
            switch(dataType) {
                case Double:
                    return OBO.getOBO().getTerm(doublePrecisionID);
                case Float:
                    return OBO.getOBO().getTerm(singlePrecisionID);
                case Integer64bit:
                    return OBO.getOBO().getTerm(signed64bitIntegerID);
                case Integer32bit:
                    return OBO.getOBO().getTerm(signed32bitIntegerID);
                case Integer16bit:
                    return OBO.getOBO().getTerm(signed16bitIntegerID);
                case Integer8bit:
                    return OBO.getOBO().getTerm(signed8bitIntegerID);
                default:
                    return null;
            }
        }
        
        /**
         * Convert OBOTerm ontology term to DataType enum, based on ontology terms
         * found within the MS ontology.
         * 
         * @param term Ontology term to convert
         * @return DataType which is described by the ontology term, or null if no match
         */
        public static DataType fromOBOTerm(OBOTerm term) {
            String accession = term.getID();
            
            if (accession.equals(doublePrecisionID)) {
                return DataTypeTransform.DataType.Double;
            } else if (accession.equals(singlePrecisionID)) {
                return DataTypeTransform.DataType.Float;
            } else if (accession.equals(signed64bitIntegerID) || accession.equals(imsSigned64bitIntegerID)) {
                return DataTypeTransform.DataType.Integer64bit;
            } else if (accession.equals(signed32bitIntegerID) || accession.equals(imsSigned32bitIntegerID)) {
                return DataTypeTransform.DataType.Integer32bit;
            } else if (accession.equals(signed16bitIntegerID)) {
                return DataTypeTransform.DataType.Integer16bit;
            } else if (accession.equals(signed8bitIntegerID)) {
                return DataTypeTransform.DataType.Integer8bit;
            }
            
            return null;
        }
    }

    /**
     * Original data type to convert from.
     */
    protected DataType from;

    /**
     * New data type to convert to.
     */
    protected DataType to;

    /**
     * Set up a DataTypeTransform from one data type to another.
     * 
     * @param from Original data type
     * @param to New data type
     */
    public DataTypeTransform(DataType from, DataType to) {
        this.from = from;
        this.to = to;
    }

    /**
     * Convert a double[] to the corresponding byte[] representation of the same
     * array.
     * 
     * @param data Data as double[] to convert
     * @return byte[] representation of the double[]
     */
    public static byte[] convertDoublesToBytes(double[] data) {
        byte[] convertedData = new byte[data.length*8];
        int j = 0;
        
        for (int i = 0; i < data.length; i++) {
          long doubleVal = Double.doubleToLongBits(data[i]);
          
          convertedData[j++] = (byte)(doubleVal);
          convertedData[j++] = (byte)(doubleVal>>>8);
          convertedData[j++] = (byte)(doubleVal>>>16);
          convertedData[j++] = (byte)(doubleVal>>>24);
          convertedData[j++] = (byte)(doubleVal>>>32);
          convertedData[j++] = (byte)(doubleVal>>>40);
          convertedData[j++] = (byte)(doubleVal>>>48);
          convertedData[j++] = (byte)(doubleVal>>>56);
        }
        
        return convertedData;
    }
    
    /**
     * Convert data from uncompressed byte[] with the data type defined by the
     * dataType to a double[].
     *
     * @param data Data as byte[]
     * @param dataType DataType of the byte[]
     * @return Data as double[]
     */
    public static double[] convertDataToDouble(byte[] data, DataType dataType) {
        double[] convertedData = null;

        if (data == null) {
            return convertedData;
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        switch (dataType) {
            case Double:
                convertedData = new double[data.length / 8];

                for (int j = 0; j < convertedData.length; j++) {
                    convertedData[j] = buffer.getDouble();
                }

                break;
            case Float:
                convertedData = new double[data.length / 4];

                for (int j = 0; j < convertedData.length; j++) {
                    convertedData[j] = buffer.getFloat();
                }

                break;
            case Integer64bit:
                convertedData = new double[data.length / 8];

                for (int j = 0; j < convertedData.length; j++) {
                    convertedData[j] = buffer.getLong();
                }

                break;
            case Integer32bit:
                convertedData = new double[data.length / 4];

                for (int j = 0; j < convertedData.length; j++) {
                    convertedData[j] = buffer.getInt();
                }

                break;
            case Integer16bit:
                convertedData = new double[data.length / 2];

                for (int j = 0; j < convertedData.length; j++) {
                    convertedData[j] = buffer.getShort();
                }

                break;
            case Integer8bit:
                convertedData = new double[data.length];

                for (int j = 0; j < convertedData.length; j++) {
                    convertedData[j] = buffer.get();
                }

                break;
            default:
                throw new UnsupportedOperationException("Data type not supported: " + dataType);
        }

        return convertedData;
    }

    /**
     * Convert data from uncompressed byte[] with the data type 'from' to the 
     * data type 'to'.
     *
     * @param data Data as byte[] in data type 'from'
     * @param from Data type of the input byte[]
     * @param to Data type to convert the input data to
     * @return Data as byte[] in the data type 'to'
     */
    public static byte[] convertData(byte[] data, DataType from, DataType to) {
        if (from.equals(to)) {
            return data;
        }

        double[] doubleData = convertDataToDouble(data, from);

        ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
        DataOutputStream byteStream = new DataOutputStream(byteArrayStream);

        try {
            switch (to) {
                case Double:
                    for (double dataPoint : doubleData) {
                        byteStream.writeLong(Long.reverseBytes(Double.doubleToLongBits(dataPoint)));
                    }

                    break;
                case Float:
                    for (double dataPoint : doubleData) {
                        byteStream.writeInt(Integer.reverseBytes(Float.floatToIntBits((float) dataPoint)));
                    }

                    break;
                case Integer64bit:
                    for (double dataPoint : doubleData) {
                        byteStream.writeLong(Long.reverseBytes((long) dataPoint));
                    }

                    break;
                case Integer32bit:
                    for (double dataPoint : doubleData) {
                        byteStream.writeInt(Integer.reverseBytes((int) dataPoint));
                    }

                    break;
                case Integer16bit:
                    for (double dataPoint : doubleData) {
                        byteStream.writeShort(Short.reverseBytes((short) dataPoint));
                    }

                    break;
                case Integer8bit:
                    for (double dataPoint : doubleData) {
                        byteStream.writeByte((byte) dataPoint);
                    }

                    break;
                default:
                    throw new UnsupportedOperationException("Data type not supported: " + to);
            }
        } catch (IOException ex) {
            Logger.getLogger(BinaryDataArray.class.getName()).log(Level.SEVERE, null, ex);
        }

        return byteArrayStream.toByteArray();
    }

    @Override
    public byte[] forwardTransform(byte[] data) throws DataFormatException {        
        return convertData(data, from, to);
    }

    @Override
    public byte[] reverseTransform(byte[] data) throws DataFormatException {
        return convertData(data, to, from);
    }

}
