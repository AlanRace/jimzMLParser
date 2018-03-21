package com.alanmrace.jimzmlparser.util;

import java.nio.ByteBuffer;
import java.util.UUID;

/**
 *
 * @author Alan Race
 */
public class UUIDHelper {
    
	private UUIDHelper() {
		// No constructor required for this class.
	}
	
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
