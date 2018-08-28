package com.alanmrace.jimzmlparser.parser;

import com.alanmrace.jimzmlparser.data.DataLocation;
import com.alanmrace.jimzmlparser.data.BinaryDataStorage;
import com.alanmrace.jimzmlparser.exceptions.FatalParseIssue;
import com.alanmrace.jimzmlparser.exceptions.MzMLParseException;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.alanmrace.jimzmlparser.mzml.MzML;
import com.alanmrace.jimzmlparser.obo.OBO;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.binary.Base64;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class MzMLHandler extends MzMLHeaderHandler {

    private static final Logger LOGGER = Logger.getLogger(MzMLHandler.class.getName());

    protected boolean processingBinary;

    protected File temporaryBinaryFile;

    protected DataOutputStream temporaryFileStream;
    protected StringBuilder binaryData;
    protected long offset = 0;

    public MzMLHandler(OBO obo, File temporaryBinaryFile) throws FileNotFoundException {
        super(obo);

        binaryData = new StringBuilder();
        this.temporaryBinaryFile = temporaryBinaryFile;
        this.dataStorage = new BinaryDataStorage(temporaryBinaryFile, true);

        temporaryFileStream = new DataOutputStream(new FileOutputStream(temporaryBinaryFile));
    }

    public static MzML parsemzML(String filename) throws MzMLParseException {
        return parsemzML(filename, null);
    }

    public static MzML parsemzML(String filename, ParserListener listener) throws MzMLParseException {
        try {
            OBO obo = OBO.getOBO();

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
            LOGGER.log(Level.SEVERE, null, ex);

            
            throw new MzMLParseException(new FatalParseIssue("SAXException: " + ex, ex.getLocalizedMessage()), ex);
        } catch (FileNotFoundException ex) {
            LOGGER.log(Level.SEVERE, null, ex);

            throw new MzMLParseException(new FatalParseIssue("File not found: " + ex, ex.getLocalizedMessage()), ex);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);

            throw new MzMLParseException(new FatalParseIssue("IOException: " + ex, ex.getLocalizedMessage()), ex);
        } catch (ParserConfigurationException ex) {
            LOGGER.log(Level.SEVERE, null, ex);

            throw new MzMLParseException(new FatalParseIssue("ParserConfigurationException: " + ex, ex.getLocalizedMessage()), ex);
        }
    }

    public boolean deleteTemporaryFile() {
        try {
            temporaryFileStream.close();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, null, e);
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
            // Convert the data from base 64
            byte[] processedData = Base64.decodeBase64(binaryData.toString().getBytes());

            int lengthToWrite = processedData.length;

            try {
                temporaryFileStream.write(processedData, 0, lengthToWrite);
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }

            DataLocation location = new DataLocation(dataStorage, offset, lengthToWrite);
            currentBinaryDataArray.setDataLocation(location);
            location.setDataTransformation(currentBinaryDataArray.generateDataTransformation());

            offset += lengthToWrite;

            processingBinary = false;
        } else {
            super.endElement(uri, localName, qName);
        }
    }
}
