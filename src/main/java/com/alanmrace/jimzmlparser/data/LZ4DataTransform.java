package com.alanmrace.jimzmlparser.data;

import java.util.Arrays;
import java.util.zip.DataFormatException;
import net.jpountz.lz4.LZ4Compressor;
import net.jpountz.lz4.LZ4Factory;
import net.jpountz.lz4.LZ4FastDecompressor;

/**
 *
 * @author Alan Race
 */
public class LZ4DataTransform implements DataTransform {
    public static final int MAX_COMPRESSION_FACTOR = 10;
    
    private final transient LZ4Factory factory = LZ4Factory.fastestInstance();
    private final int arrayLengthInBytes;
    
    public LZ4DataTransform(int arrayLengthInBytes) {
        this.arrayLengthInBytes = arrayLengthInBytes;
    }
    
    @Override
    public byte[] forwardTransform(byte[] data) throws DataFormatException {
        LZ4Compressor compressor = factory.fastCompressor();
        
        // For some reason the compression of m/z arrays using LZ4 results in larger data than the input
        int maxCompressedLength = compressor.maxCompressedLength(data.length) * 2;
        byte[] compressed = new byte[maxCompressedLength];
        
        int compressedLength = compressor.compress(data, 0, data.length, compressed, 0, maxCompressedLength);
        
        
        if(compressedLength != compressed.length)
            return Arrays.copyOf(compressed, compressedLength);
        
        return compressed;
    }

    @Override
    public byte[] reverseTransform(byte[] data) throws DataFormatException {
        byte[] decompressed = new byte[arrayLengthInBytes];
        
        LZ4FastDecompressor decompressor = factory.fastDecompressor();
        decompressor.decompress(data, 0, decompressed, 0, arrayLengthInBytes);
        
        return decompressed;
    }
}
