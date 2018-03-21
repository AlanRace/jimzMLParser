package com.alanmrace.jimzmlparser.util;

/**
 *
 * @author Alan Race
 */
public class HexHelper {
       
	private HexHelper() {
		// No constructor required for this class.
	}
	
    /**
     * Convert hex string to byte[].
     * 
     * @param s Hex string to convert
     * @return  Converted byte[]
     */
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];

        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }

        return data;
    }

    /**
     * Convert byte[] to hex string.
     * 
     * @param byteArray byte[] to convert
     * @return          Converted string
     */
    public static String byteArrayToHexString(byte[] byteArray) {
        StringBuilder sb = new StringBuilder(2 * byteArray.length);

        byte[] Hexhars = {
            '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b',
            'c', 'd', 'e', 'f'
        };

        for (int i = 0; i < byteArray.length; i++) {

            int v = byteArray[i] & 0xff;

            sb.append((char) Hexhars[v >> 4]);
            sb.append((char) Hexhars[v & 0xf]);
        }

        return sb.toString();
    }
}
