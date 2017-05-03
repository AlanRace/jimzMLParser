/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alanmrace.jimzmlparser.util;

import java.nio.ByteBuffer;
import java.util.UUID;

/**
 *
 * @author Alan Race
 */
public class UUIDHelper {
    
    
    /**
     * Convert byte[] to UUID.
     * 
     * @param bytes byte[] to convert
     * @return      UUID
     */
    public static UUID byteArrayToUuid(byte[] bytes) {
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        long firstLong = bb.getLong();
        long secondLong = bb.getLong();
        return new UUID(firstLong, secondLong);
    }

    /**
     * Convert UUID to byte[].
     * 
     * @param uuid  UUID to convert
     * @return      byte[]
     */
    public static byte[] uuidToByteArray(UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }
}
