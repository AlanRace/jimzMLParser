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
     * Temporary file used to store the binary data.
     */
    private DataStorage dataStorage;

    /**
     * Offset in the temporary file where the data starts.
     */
    private long offset;

    /**
     * Number of bytes of data.
     */
    private long numBytes;

    @Override
    public String getTagName() {
        return "binary";
    }

    @Override
    public void addChildrenToCollection(Collection<MzMLTag> children) {
        // No children
    }

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
     * Instantiates a new binary tag.
     *
     * @param dataStorage The means of accessing the data
     * @param offset Offset in the temporary file where the data starts
     * @param numBytes Number of bytes of data
     * @param dataType
     */
    public Binary(DataStorage dataStorage, long offset, long numBytes) { //, CVParam dataType) {
        this.dataStorage = dataStorage;
        this.offset = offset;
        this.numBytes = numBytes;

//		this.cvDataType = dataType;
//		this.dataType = getDataTypeFromCV(dataType);
    }

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

    public byte[] getData() {
        System.err.println("Binary.getData()");

        return null;
    }

//	public byte[] getData(RandomAccessFile reader, boolean compress) throws DataFormatException {
//		byte[] inputBytes = new byte[(int) numBytes];
//		
//		try {
//			
//			boolean closeStream = false;
//		
//			if(reader == null) {
//				closeStream = true;
//				
//				reader = new RandomAccessFile(temporaryBinaryFile, "r");
//			}
//
//			synchronized(reader) {
//				reader.seek(this.offset);
//				
//				reader.read(inputBytes);
//			}
//		
//			if(compress) {
//				Inflater decompressor = new Inflater();
//				decompressor.setInput(inputBytes);
//				
//				ArrayList<Byte> uncompressedData = new ArrayList<Byte>();
////				int length = 0;
//				int uncompressed = 0;
//				
//				do {
//					byte[] temp = new byte[1048576]; // 2^20
//					
////					try {
//						uncompressed = decompressor.inflate(temp);
//		
//						for(int i = 0; i < uncompressed; i++)
//							uncompressedData.add(temp[i]);
////					} catch (DataFormatException e1) {
////						// TODO Auto-generated catch block
////						e1.printStackTrace();
////					}
//					
////					length += uncompressed;
//				} while(uncompressed != 0);
//				
//				inputBytes = new byte[uncompressedData.size()];
//				
//				for(int i = 0; i < uncompressedData.size(); i++)
//					inputBytes[i] = uncompressedData.get(i);
//
//				decompressor.end();
//			}
//		
//			if(closeStream)
//				reader.close();
//		} catch (IOException io) {
//			io.printStackTrace();
//		}
//		
//		return inputBytes;
//	}
    /**
     * Copy the binary data to a new data stream.
     *
     * @param binaryDataStream The new stream to output the binary data to
     * @param offset Offset of the new file before copying the data
     * @return The new offset after copying the data
     */
//	public long copyDataToDataStream(DataOutputStream binaryDataStream, long offset, boolean compress) {
//		return copyDataToDataStream(binaryDataStream, offset, compress, cvDataType);
//	}
    private void convertDataType(byte[] inputBytes, CVParam newDataType, CVParam oldDataType) throws IOException {
        if (!newDataType.equals(oldDataType)) {
            ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
            DataOutputStream byteStream = new DataOutputStream(byteArrayStream);

            if (oldDataType.getTerm().getID().equals(BinaryDataArray.doublePrecisionID)) {
                double[] convertedData = new double[inputBytes.length / 8];
                ByteBuffer buffer = ByteBuffer.wrap(inputBytes);
                buffer.order(ByteOrder.LITTLE_ENDIAN);

                // Convert to double
                for (int j = 0; j < convertedData.length; j++) {
                    convertedData[j] = buffer.getDouble();
                }

                if (newDataType.getTerm().getID().equals(BinaryDataArray.singlePrecisionID)) {
                    for (int j = 0; j < convertedData.length; j++) {
                        byteStream.writeInt(Integer.reverseBytes(Float.floatToIntBits((float) convertedData[j])));
                    }
//					System.out.println("Single");
                    // Change the number of bytes to the new format
                    numBytes = numBytes / 2;
                } else if (newDataType.getTerm().getID().equals(BinaryDataArray.signed64bitIntegerID)) {
                    for (int j = 0; j < convertedData.length; j++) {
                        byteStream.writeLong(Long.reverseBytes((long) convertedData[j]));
                    }

                    // Number of bytes unchanged
                } else if (newDataType.getTerm().getID().equals(BinaryDataArray.signed32bitIntegerID)) {
                    for (int j = 0; j < convertedData.length; j++) {
                        byteStream.writeInt(Integer.reverseBytes((int) convertedData[j]));
                    }

                    numBytes = numBytes / 2;
                } else if (newDataType.getTerm().getID().equals(BinaryDataArray.signed16bitIntegerID)) {
                    for (int j = 0; j < convertedData.length; j++) {
                        byteStream.writeShort(Short.reverseBytes((short) convertedData[j]));
                    }

                    numBytes = numBytes / 4;
                } else if (newDataType.getTerm().getID().equals(BinaryDataArray.signed8bitIntegerID)) {
                    for (int j = 0; j < convertedData.length; j++) {
                        byteStream.writeByte((byte) convertedData[j]);
                    }

                    numBytes = numBytes / 8;
                } else if (newDataType.getTerm().getID().equals(BinaryDataArray.doublePrecisionID)) {
                    for (int j = 0; j < convertedData.length; j++) {
                        byteStream.writeLong(Long.reverseBytes(Double.doubleToLongBits(convertedData[j])));
                    }
                }

            } else if (oldDataType.getTerm().getID().equals(BinaryDataArray.singlePrecisionID)) {
                float[] convertedData = new float[inputBytes.length / 4];
                ByteBuffer buffer = ByteBuffer.wrap(inputBytes);
                buffer.order(ByteOrder.LITTLE_ENDIAN);

                for (int j = 0; j < convertedData.length; j++) {
                    convertedData[j] = buffer.getFloat();
                }

                if (newDataType.getTerm().getID().equals(BinaryDataArray.doublePrecisionID)) {
                    for (int j = 0; j < convertedData.length; j++) {
                        byteStream.writeLong(Long.reverseBytes(Double.doubleToLongBits(convertedData[j])));
                    }

                    numBytes = numBytes * 2;
                } else if (newDataType.getTerm().getID().equals(BinaryDataArray.signed64bitIntegerID)) {
                    for (int j = 0; j < convertedData.length; j++) {
                        byteStream.writeLong(Long.reverseBytes((long) convertedData[j]));
                    }

                    numBytes = numBytes * 2;
                } else if (newDataType.getTerm().getID().equals(BinaryDataArray.signed32bitIntegerID)) {
                    for (int j = 0; j < convertedData.length; j++) {
                        byteStream.writeInt(Integer.reverseBytes((int) convertedData[j]));
                    }

                    // Number of bytes unchanged
                } else if (newDataType.getTerm().getID().equals(BinaryDataArray.signed16bitIntegerID)) {
                    for (int j = 0; j < convertedData.length; j++) {
                        byteStream.writeShort(Short.reverseBytes((short) convertedData[j]));
                    }

                    numBytes = numBytes / 2;
                } else if (newDataType.getTerm().getID().equals(BinaryDataArray.signed8bitIntegerID)) {
                    for (int j = 0; j < convertedData.length; j++) {
                        byteStream.writeByte((byte) convertedData[j]);
                    }

                    numBytes = numBytes / 4;
                } else if (newDataType.getTerm().getID().equals(BinaryDataArray.singlePrecisionID)) {
                    for (int j = 0; j < convertedData.length; j++) {
                        byteStream.writeInt(Integer.reverseBytes(Float.floatToIntBits((float) convertedData[j])));
                    }
                }
            }

            inputBytes = byteArrayStream.toByteArray();
        }
    }

//	private void skipOffset(BufferedInputStream reader) throws IOException {
//		reader.skip(this.offset);
//	}
    private void compressData(boolean compress, byte[] inputBytes) {
        // Compress data if needed
        if (compress) {
            Deflater compressor = new Deflater();
            compressor.setInput(inputBytes);
            compressor.finish();

            ArrayList<Byte> compressedData = new ArrayList<Byte>();
            int length = 0;
            int compressed = 0;

            do {
                byte[] temp = new byte[1048576]; // 2^20

                compressed = compressor.deflate(temp);

                for (int i = 0; i < compressed; i++) {
                    compressedData.add(temp[i]);
                }

                length += compressed;
            } while (compressed != 0);

            inputBytes = new byte[compressedData.size()];

            for (int i = 0; i < compressedData.size(); i++) {
                inputBytes[i] = compressedData.get(i);
            }

            numBytes = length;
        }
    }

//	private void readData(BufferedInputStream reader, byte[] inputBytes) throws IOException {
//		reader.read(inputBytes);
//	}
//	
//	private void writeData(DataOutputStream binaryDataStream, byte[] inputBytes) throws IOException {
//		// Write out the data
//		binaryDataStream.write(inputBytes);
//	}
//	private BufferedInputStream createInputStream() throws FileNotFoundException {
//		return new BufferedInputStream(createFileInputStream());
//	}
//	private FileInputStream createFileInputStream() throws FileNotFoundException {
//		return new FileInputStream(temporaryBinaryFile);
//	}
//	private void closeInputStream(BufferedInputStream reader) throws IOException {
//		reader.close();
//	}
//	
//	private byte[] createByteArray() {
//		return new byte[(int) numBytes];
//	}
//	public long copyDataToDataStream(DataOutputStream binaryDataStream, long offset, boolean compress, CVParam dataType) {
//		if(numBytes == 0)
//			return offset;
//		
//		try {
//			BufferedInputStream reader = createInputStream();
//			
//			skipOffset(reader);
//			
//			byte[] inputBytes = createByteArray();
//			readData(reader, inputBytes);
//			
////			System.out.println("Trying to read " + numBytes + " bytes");
//			
//			// Change data type if necessary
//			CVParam newDataType = dataType;
//			CVParam oldDataType = cvDataType;
//			
//			convertDataType(inputBytes, newDataType, oldDataType);
//
//			compressData(compress, inputBytes);
//			
//			writeData(binaryDataStream, inputBytes);
//			
////			System.out.println("Should have written " + inputBytes.length + " bytes");
//			
//			//reader.close();
//			closeInputStream(reader);
//		} catch (IOException io) {
//			io.printStackTrace();
//		}
//		
//		return offset + numBytes;
//	}
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

    public static void main(String args[]) throws Base64DecodingException {
        double[] data = {1.0, 2.54};

        byte[] bytes = new byte[8 * data.length];

        for (int i = 0; i < data.length; i++) {
            ByteBuffer buffer = ByteBuffer.wrap(bytes);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            buffer.putDouble(i * 8, data[i]);
        }

        String encoded = Base64.encode(bytes);

        System.out.println(encoded);

        byte[] revBytes = Base64.decode(encoded);

        double[] convertedData = new double[revBytes.length / 8];
        ByteBuffer buffer = ByteBuffer.wrap(revBytes);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        // Convert to double
        for (int j = 0; j < convertedData.length; j++) {
            convertedData[j] = buffer.getDouble();
        }

        System.out.println(convertedData[0]);
        System.out.println(convertedData[1]);

        //byte[] processedData = DatatypeConverter.parseBase64Binary(binaryData.toString());
    }

}
