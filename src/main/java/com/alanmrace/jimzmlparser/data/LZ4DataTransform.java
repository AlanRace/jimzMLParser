/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alanmrace.jimzmlparser.data;

import java.util.Arrays;
import java.util.zip.DataFormatException;
import net.jpountz.lz4.LZ4Compressor;
import net.jpountz.lz4.LZ4Factory;
import net.jpountz.lz4.LZ4FastDecompressor;

/**
 *
 * @author Alan
 */
public class LZ4DataTransform implements DataTransform {
    public static int MAX_COMPRESSION_FACTOR = 10;
    
    protected final LZ4Factory factory = LZ4Factory.fastestInstance();
    protected int arrayLengthInBytes;
    
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
        
//        System.out.println("Compressed " + data.length + " to " + compressedLength);
        
        if(compressedLength != compressed.length)
            return Arrays.copyOf(compressed, compressedLength); // .copyOfRange(compressed, 0, compressedLength);
        
        return compressed;
    }

    @Override
    public byte[] reverseTransform(byte[] data) throws DataFormatException {
        byte[] decompressed = new byte[arrayLengthInBytes];
        
        LZ4FastDecompressor decompressor = factory.fastDecompressor();
        decompressor.decompress(data, 0, decompressed, 0, arrayLengthInBytes);
        //decompressed = decompressor.decompress(data, 0, arrayLengthInBytes);
        
        //LZ4SafeDecompressor decompressor = factory.safeDecompressor();
        //int decompressedLength = decompressor.decompress(data, 0, data.length, decompressed, 0, arrayLengthInBytes);
        
        return decompressed;
    }
        
//        int decompressedLength = decompressor.decompress(data, 0, data.length, restored, 0);
//        
//        if(decompressedLength != restored.length)
//            return Arrays.copyOfRange(restored, 0, decompressedLength-1);
//        
//        return restored;
//    }
//    
//    public byte[] reverseTransform(byte[] data, int uncompressedLength) {
//        LZ4FastDecompressor decompressor = factory.fastDecompressor();
//        byte[] decompressed = new byte[uncompressedLength];
//        
//        decompressor.decompress(data, 0, decompressed, 0, uncompressedLength);
//        
//        return decompressed;
//    }
}
