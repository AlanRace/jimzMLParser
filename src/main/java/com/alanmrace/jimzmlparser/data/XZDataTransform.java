/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alanmrace.jimzmlparser.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;
import org.tukaani.xz.LZMA2Options;
import org.tukaani.xz.XZInputStream;
import org.tukaani.xz.XZOutputStream;

/**
 *
 * @author alan.race
 */
public class XZDataTransform implements DataTransform {

    @Override
    public byte[] forwardTransform(byte[] data) throws DataFormatException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        XZOutputStream gzos = null;
        
        try {
            gzos = new XZOutputStream(outputStream, new LZMA2Options());
            gzos.write(data, 0, data.length);
            gzos.finish();
        } catch (IOException ex) {
            Logger.getLogger(XZDataTransform.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                gzos.close();
            } catch (IOException ex) {
                Logger.getLogger(XZDataTransform.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        if(gzos != null) {
            return outputStream.toByteArray();
        }
        
        return null;
    }

    @Override
    public byte[] reverseTransform(byte[] data) throws DataFormatException {
        try {
            XZInputStream xzInputStream = new XZInputStream(
                    new ByteArrayInputStream(data));
            
            byte firstByte = (byte) xzInputStream.read();
            byte[] buffer = new byte[xzInputStream.available() + 1];
            buffer[0] = firstByte;
            xzInputStream.read(buffer, 1, buffer.length-1);
            
            xzInputStream.close();
            
            return buffer;
        } catch (IOException ex) {
            Logger.getLogger(XZDataTransform.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }

}
