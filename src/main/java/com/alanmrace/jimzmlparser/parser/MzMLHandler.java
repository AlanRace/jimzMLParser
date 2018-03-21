package com.alanmrace.jimzmlparser.parser;

import com.alanmrace.jimzmlparser.data.DataLocation;
import com.alanmrace.jimzmlparser.data.BinaryDataStorage;
import com.alanmrace.jimzmlparser.exceptions.FatalParseException;
import com.alanmrace.jimzmlparser.exceptions.MzMLParseException;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.alanmrace.jimzmlparser.mzml.MzML;
import com.alanmrace.jimzmlparser.obo.OBO;
import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class MzMLHandler extends MzMLHeaderHandler {

    private static final Logger logger = Logger.getLogger(MzMLHandler.class.getName());

    protected boolean processingBinary;

    protected File temporaryBinaryFile;

    protected DataOutputStream temporaryFileStream;
    protected StringBuffer binaryData;
    protected long offset = 0;

    private byte[] temp;
    private ArrayList<Byte> uncompressedData;

    public MzMLHandler(OBO obo, File temporaryBinaryFile) throws FileNotFoundException {
        super(obo);

        binaryData = new StringBuffer();
        this.temporaryBinaryFile = temporaryBinaryFile;
        this.dataStorage = new BinaryDataStorage(temporaryBinaryFile, true);

        temporaryFileStream = new DataOutputStream(new FileOutputStream(temporaryBinaryFile));
    }

    public static MzML parsemzML(String filename) throws FatalParseException {
        return parsemzML(filename, null);
    }

    public static MzML parsemzML(String filename, ParserListener listener) throws FatalParseException {
        try {
            //OBO obo = new OBO("imagingMS.obo");
            OBO obo = OBO.getOBO();

            // Necessary if the passed in filename is a resource
            //File resource = new File(filename);
            //String absolutePath = resource.getAbsolutePath();
            File tmpFile = new File(filename.substring(0, filename.lastIndexOf('.')) + ".tmp");
            tmpFile.deleteOnExit();

            // Parse mzML
            MzMLHandler handler = new MzMLHandler(obo, tmpFile);

            if (listener != null) {
                handler.registerParserListener(listener);
            }

            SAXParserFactory spf = SAXParserFactory.newInstance();

            //get a new instance of parser
            SAXParser sp = spf.newSAXParser();

            File file = new File(filename);

            //parse the file and also register this class for call backs
            sp.parse(file, handler);

            handler.getmzML().setOBO(obo);

            return handler.getmzML();
        } catch (SAXException ex) {
            logger.log(Level.SEVERE, null, ex);

            throw new MzMLParseException("SAXException: " + ex, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MzMLHandler.class.getName()).log(Level.SEVERE, null, ex);

            throw new MzMLParseException("File not found: " + ex, ex);
        } catch (IOException ex) {
            Logger.getLogger(MzMLHandler.class.getName()).log(Level.SEVERE, null, ex);

            throw new MzMLParseException("IOException: " + ex, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(MzMLHandler.class.getName()).log(Level.SEVERE, null, ex);

            throw new MzMLParseException("ParserConfigurationException: " + ex, ex);
        }
    }

    public boolean deleteTemporaryFile() {
        try {
            temporaryFileStream.close();
        } catch (IOException e) {
            logger.log(Level.SEVERE, null, e);
        }

        temporaryFileStream = null;
        return temporaryBinaryFile.delete();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if ("binary".equals(qName)) {
            binaryData.setLength(0);

            processingBinary = true;
        } else {
            super.startElement(uri, localName, qName, attributes);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (processingBinary) {
            binaryData.append(ch, start, length);
        } else {
            super.characters(ch, start, length);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if ("binary".equals(qName)) {

            try {
                // Convert the data from base 64
                byte[] processedData = Base64.decode(binaryData.toString().getBytes());

                int lengthToWrite = processedData.length;

//            if (currentBinaryDataArray.isCompressed()) {
//                Inflater decompressor = new Inflater();
//                decompressor.setInput(processedData);
//
//                if (uncompressedData == null) {
//                    uncompressedData = new ArrayList<Byte>(2 ^ 20);
//                }
//
//                lengthToWrite = 0;
//                int uncompressed = 0;
//
//                if (temp == null) {
//                    temp = new byte[1048576]; // 2^20
//                }
//                do {
//                    try {
//                        uncompressed = decompressor.inflate(temp);
//
//                        for (int i = 0; i < uncompressed; i++) {
//                            if (uncompressedData.size() <= lengthToWrite) {
//                                uncompressedData.add(temp[i]);
//                                lengthToWrite++;
//                            } else {
//                                uncompressedData.set(lengthToWrite++, temp[i]);
//                            }
//                        }
//                    } catch (DataFormatException ex) {
//                        logger.log(Level.SEVERE, null, ex);
//                    }
//
//                } while (uncompressed != 0);
//
//                if (processedData.length < lengthToWrite) {
//                    processedData = new byte[lengthToWrite];
//                }
//
//                for (int i = 0; i < lengthToWrite; i++) {
//                    processedData[i] = uncompressedData.get(i);
//                }
//                
//                decompressor.end();
//            }
                try {
                    temporaryFileStream.write(processedData, 0, lengthToWrite);
                } catch (IOException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }

                DataLocation location = new DataLocation(dataStorage, offset, lengthToWrite);
                currentBinaryDataArray.setDataLocation(location);
                location.setDataTransformation(currentBinaryDataArray.generateDataTransformation());

                offset += lengthToWrite;
            } catch (Base64DecodingException ex) {
                Logger.getLogger(MzMLHandler.class.getName()).log(Level.SEVERE, null, ex);
            }

            processingBinary = false;
        } else {
            super.endElement(uri, localName, qName);
        }
    }
}
