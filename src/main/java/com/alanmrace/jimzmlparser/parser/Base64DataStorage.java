package com.alanmrace.jimzmlparser.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author Alan Race
 */
public class Base64DataStorage extends DataStorage {
    public Base64DataStorage(File dataFile) throws FileNotFoundException {
        super(dataFile);
    }

    @Override
    public byte[] getData(long offset, int length) throws IOException {
	byte[] buffer = super.getData(offset, length);
	
	return DatatypeConverter.parseBase64Binary(new String(buffer));
    }
}
