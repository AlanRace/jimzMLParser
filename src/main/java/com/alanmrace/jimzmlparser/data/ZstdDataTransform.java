package com.alanmrace.jimzmlparser.data;

import com.github.luben.zstd.Zstd;
import java.util.zip.DataFormatException;

/**
 *
 * @author Alan Race
 */
public class ZstdDataTransform implements DataTransform {

    protected int arrayLengthInBytes;
    protected int compressionLevel;
    
    
    public ZstdDataTransform(int arrayLengthInBytes) {
        this(arrayLengthInBytes, 3);
    }
    
    public ZstdDataTransform(int arrayLengthInBytes, int compressionLevel) {
        this.arrayLengthInBytes = arrayLengthInBytes;
        this.compressionLevel = compressionLevel;
    }
    
    @Override
    public byte[] forwardTransform(byte[] data) throws DataFormatException {
        return Zstd.compress(data, compressionLevel);
    }

    @Override
    public byte[] reverseTransform(byte[] data) throws DataFormatException {
        return Zstd.decompress(data, arrayLengthInBytes);
    }
    
}

