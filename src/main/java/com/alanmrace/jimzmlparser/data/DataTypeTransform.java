package com.alanmrace.jimzmlparser.data;

import com.alanmrace.jimzmlparser.mzml.BinaryDataArray;
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
 * this could be the conversion of a double -&lt; integer. Valid data types are 
 * included in {@link DataTypeTransform.DataType}.
 * 
 * @author Alan Race
 */
public class DataTypeTransform implements DataTransform {
    
    /**
     * Logger for the class.
     */
    private static final Logger LOGGER = Logger.getLogger(DataTypeTransform.class.getName());

    /**
     * Possible binary data types used to store data.
     */
    public enum DataType {

        /**
         * Double precision floating point.
         */
        DOUBLE,
        /**
         * Floating point.
         */
        FLOAT,
        /**
         * Signed 8-bit integer.
         */
        INTEGER_8BIT,
        /**
         * Signed 16-bit integer.
         */
        INTEGER_16BIT,
        /**
         * Signed 32-bit integer.
         */
        INTEGER_32BIT,
        /**
         * Signed 64-bit integer.
         */
        INTEGER_64BIT;
        
        /**
         * Convert DataType enum to OBOTerm, using ontology terms found in the 
         * MS ontology.
         * 
         * @param dataType DataType to convert
         * @return Ontology term which describes the DataType, or null if no match
         */
        public static OBOTerm toOBOTerm(DataType dataType) {
            switch(dataType) {
                case DOUBLE:
                    return OBO.getOBO().getTerm(BinaryDataArray.DOUBLE_PRECISION_ID);
                case FLOAT:
                    return OBO.getOBO().getTerm(BinaryDataArray.SINGLE_PRECISION_ID);
                case INTEGER_64BIT:
                    return OBO.getOBO().getTerm(BinaryDataArray.SIGNED_64BIT_INTEGER_ID);
                case INTEGER_32BIT:
                    return OBO.getOBO().getTerm(BinaryDataArray.SIGNED_32BIT_INTEGER_ID);
                case INTEGER_16BIT:
                    return OBO.getOBO().getTerm(BinaryDataArray.SIGNED_16BIT_INTEGER_ID);
                case INTEGER_8BIT:
                    return OBO.getOBO().getTerm(BinaryDataArray.SIGNED_8BIT_INTEGER_ID);
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
            
            if (accession.equals(BinaryDataArray.DOUBLE_PRECISION_ID)) {
                return DataTypeTransform.DataType.DOUBLE;
            } else if (accession.equals(BinaryDataArray.SINGLE_PRECISION_ID)) {
                return DataTypeTransform.DataType.FLOAT;
            } else if (accession.equals(BinaryDataArray.SIGNED_64BIT_INTEGER_ID) || accession.equals(BinaryDataArray.IMS_SIGNED_64BIT_INTEGER_ID)) {
                return DataTypeTransform.DataType.INTEGER_64BIT;
            } else if (accession.equals(BinaryDataArray.SIGNED_32BIT_INTEGER_ID) || accession.equals(BinaryDataArray.IMS_SIGNED_32BIT_INTEGER_ID)) {
                return DataTypeTransform.DataType.INTEGER_32BIT;
            } else if (accession.equals(BinaryDataArray.SIGNED_16BIT_INTEGER_ID)) {
                return DataTypeTransform.DataType.INTEGER_16BIT;
            } else if (accession.equals(BinaryDataArray.SIGNED_8BIT_INTEGER_ID)) {
                return DataTypeTransform.DataType.INTEGER_8BIT;
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

        for (double aData : data) {
            long doubleVal = Double.doubleToLongBits(aData);

            convertedData[j++] = (byte) (doubleVal);
            convertedData[j++] = (byte) (doubleVal >>> 8);
            convertedData[j++] = (byte) (doubleVal >>> 16);
            convertedData[j++] = (byte) (doubleVal >>> 24);
            convertedData[j++] = (byte) (doubleVal >>> 32);
            convertedData[j++] = (byte) (doubleVal >>> 40);
            convertedData[j++] = (byte) (doubleVal >>> 48);
            convertedData[j++] = (byte) (doubleVal >>> 56);
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
        double[] convertedData = new double[0];

        if (data == null) {
            return convertedData;
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        switch (dataType) {
            case DOUBLE:
                convertedData = new double[data.length / 8];

                for (int j = 0; j < convertedData.length; j++) {
                    convertedData[j] = buffer.getDouble();
                }

                break;
            case FLOAT:
                convertedData = new double[data.length / 4];

                for (int j = 0; j < convertedData.length; j++) {
                    convertedData[j] = buffer.getFloat();
                }

                break;
            case INTEGER_64BIT:
                convertedData = new double[data.length / 8];

                for (int j = 0; j < convertedData.length; j++) {
                    convertedData[j] = buffer.getLong();
                }

                break;
            case INTEGER_32BIT:
                convertedData = new double[data.length / 4];

                for (int j = 0; j < convertedData.length; j++) {
                    convertedData[j] = buffer.getInt();
                }

                break;
            case INTEGER_16BIT:
                convertedData = new double[data.length / 2];

                for (int j = 0; j < convertedData.length; j++) {
                    convertedData[j] = buffer.getShort();
                }

                break;
            case INTEGER_8BIT:
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
                case DOUBLE:
                    for (double dataPoint : doubleData) {
                        byteStream.writeLong(Long.reverseBytes(Double.doubleToLongBits(dataPoint)));
                    }

                    break;
                case FLOAT:
                    for (double dataPoint : doubleData) {
                        byteStream.writeInt(Integer.reverseBytes(Float.floatToIntBits((float) dataPoint)));
                    }

                    break;
                case INTEGER_64BIT:
                    for (double dataPoint : doubleData) {
                        byteStream.writeLong(Long.reverseBytes((long) dataPoint));
                    }

                    break;
                case INTEGER_32BIT:
                    for (double dataPoint : doubleData) {
                        byteStream.writeInt(Integer.reverseBytes((int) dataPoint));
                    }

                    break;
                case INTEGER_16BIT:
                    for (double dataPoint : doubleData) {
                        byteStream.writeShort(Short.reverseBytes((short) dataPoint));
                    }

                    break;
                case INTEGER_8BIT:
                    for (double dataPoint : doubleData) {
                        byteStream.writeByte((byte) dataPoint);
                    }

                    break;
                default:
                    throw new UnsupportedOperationException("Data type not supported: " + to);
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
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

    
    @Override
    public String toString() {
        return "DataTypeTransform from " + from + " to " + to;
    }
}
