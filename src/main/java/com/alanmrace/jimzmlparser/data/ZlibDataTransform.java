package com.alanmrace.jimzmlparser.data;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * DataTransform describing the compression (forward) and decompression (reverse)
 * of data using the zlib algorithm.
 * 
 * @author Alan Race
 */
public class ZlibDataTransform implements DataTransform {
    
    /**
     * Byte buffer size to use for temporary storage for (de)compression.
     */
    static protected final int BYTE_BUFFER_SIZE = 2 ^ 20;

    @Override
    public byte[] forwardTransform(byte[] data) throws DataFormatException {
        Deflater compressor = new Deflater();
        compressor.setInput(data);
        compressor.finish();

        ArrayList<Byte> compressedData = new ArrayList<Byte>();
        int compressed;

        do {
            byte[] temp = new byte[BYTE_BUFFER_SIZE]; // 2^20

            compressed = compressor.deflate(temp);

            for (int i = 0; i < compressed; i++) {
                compressedData.add(temp[i]);
            }
        } while (compressed != 0);

        byte[] compressedBytes = new byte[compressedData.size()];

        for (int i = 0; i < compressedData.size(); i++) {
            compressedBytes[i] = compressedData.get(i);
        }

        return compressedBytes;
    }

    @Override
    public byte[] reverseTransform(byte[] data) throws DataFormatException {
        Inflater decompressor = new Inflater();
        decompressor.setInput(data);

        List<Byte> uncompressedData = new ArrayList<Byte>(data.length);
        int uncompressed;

        do {
            byte[] temp = new byte[BYTE_BUFFER_SIZE]; // 2^20

            uncompressed = decompressor.inflate(temp);

            for (int i = 0; i < uncompressed; i++) {
                uncompressedData.add(temp[i]);
            }

        } while (uncompressed != 0);

        byte[] uncompressedBytes = new byte[uncompressedData.size()];

        for (int i = 0; i < uncompressedData.size(); i++) {
            uncompressedBytes[i] = uncompressedData.get(i);
        }

        decompressor.end();

        return uncompressedBytes;
    }

}
