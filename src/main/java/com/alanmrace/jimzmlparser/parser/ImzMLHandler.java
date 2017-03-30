package com.alanmrace.jimzmlparser.parser;

import com.alanmrace.jimzmlparser.exceptions.ImzMLParseException;
import com.alanmrace.jimzmlparser.exceptions.ImzMLWriteException;
import com.alanmrace.jimzmlparser.imzML.ImzML;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.alanmrace.jimzmlparser.mzML.BinaryDataArray;
import com.alanmrace.jimzmlparser.obo.OBO;
import com.alanmrace.jimzmlparser.exceptions.InvalidImzML;
import com.alanmrace.jimzmlparser.exceptions.InvalidMzML;
import com.alanmrace.jimzmlparser.mzML.Scan;
import com.alanmrace.jimzmlparser.mzML.Spectrum;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ImzMLHandler extends MzMLHeaderHandler {

    private static final Logger logger = Logger.getLogger(ImzMLHandler.class.getName());

    private File ibdFile;
//    private BinaryDataStorage dataStorage;
    private long currentOffset;
    private long currentNumBytes;

    private boolean processingSCiLS3DData = false;
    private int imageMaxX;
    private int imageMaxY;
    private int datasetMaxX;
    private int datasetMaxY;

    private int currentZ = 0;
    private double current3DPositionZ = Double.POSITIVE_INFINITY;

    private boolean haveDoneCheck = false;

    private int numImagesX = 0;
    private int numImagesY = 0;

    private int maxImagesX;

    private int previousMaxX = 0;
    private int previousMaxY = 0;
    private int currentMaxX = 0;
    private int currentMaxY = 0;

    private int count = 0;

    public ImzMLHandler(OBO obo) {
        super(obo);
    }

    public ImzMLHandler(OBO obo, File ibdFile, boolean openDataStorage) throws FileNotFoundException {
        super(obo);

        this.ibdFile = ibdFile;

        if (openDataStorage) {
            this.dataStorage = new BinaryDataStorage(ibdFile);
        }
    }

    public static ImzML parseimzML(String filename) throws ImzMLParseException {
        return parseimzML(filename, true);
    }

    public static ImzML parseimzML(String filename, boolean openDataStorage) throws ImzMLParseException {
        return parseimzML(filename, openDataStorage, null);
    }
    
    public static ImzML parseimzML(String filename, boolean openDataStorage, ParserListener listener) throws ImzMLParseException {
        try {
            OBO obo = new OBO("imagingMS.obo");

            File ibdFile = new File(filename.substring(0, filename.lastIndexOf('.')) + ".ibd");

            // Convert mzML header information -> imzML
            ImzMLHandler handler = new ImzMLHandler(obo, ibdFile, openDataStorage);

            if(listener != null)
                handler.registerParserListener(listener);
            
            SAXParserFactory spf = SAXParserFactory.newInstance();

            //get a new instance of parser
            SAXParser sp = spf.newSAXParser();

            File file = new File(filename);

            //parse the file and also register this class for call backs
            sp.parse(file, handler);

            handler.getimzML().setOBO(obo);

            return handler.getimzML();
        } catch (SAXException ex) {
            logger.log(Level.SEVERE, null, ex);

            throw new ImzMLParseException("SAXException: " + ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ImzMLHandler.class.getName()).log(Level.SEVERE, null, ex);

            throw new ImzMLParseException("File not found: " + filename);
        } catch (IOException ex) {
            Logger.getLogger(ImzMLHandler.class.getName()).log(Level.SEVERE, null, ex);

            throw new ImzMLParseException("IOException: " + ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ImzMLHandler.class.getName()).log(Level.SEVERE, null, ex);

            throw new ImzMLParseException("ParserConfigurationException: " + ex);
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (ibdFile != null && qName.equals("cvParam")) {
            String accession = attributes.getValue("accession");

            if (accession.equals(BinaryDataArray.externalEncodedLengthID)) {
                try {
                    currentNumBytes = Long.parseLong(attributes.getValue("value"));
                } catch (NumberFormatException ex) {
                    currentNumBytes = (long) Double.parseDouble(attributes.getValue("value"));
                }
            } else if (accession.equals(BinaryDataArray.externalOffsetID)) {
                try {
                    currentOffset = Long.parseLong(attributes.getValue("value"));
                } catch (NumberFormatException ex) {
                    currentNumBytes = (long) Double.parseDouble(attributes.getValue("value"));
                }
            }
        }

        if ("userParam".equals(qName)) {
            String name = attributes.getValue("name");

            if ("3DPositionZ".equals(name)) {
                processingSCiLS3DData = true;

                double z = Double.parseDouble(attributes.getValue("value"));

                if (z != current3DPositionZ) {
                    if (current3DPositionZ != Double.POSITIVE_INFINITY && !haveDoneCheck) {
                        int imageSize = imageMaxX * imageMaxY;
                        int numImagesGuess = (int) Math.ceil((numberOfSpectra * 1.0) / spectrumList.size());

                        maxImagesX = (int) Math.ceil(Math.sqrt(numImagesGuess));

                        Logger.getLogger(ImzMLHandler.class.getName()).log(Level.FINER, "Found image size {0} ({1}, {2})", new Object[]{imageSize, imageMaxX, imageMaxY});
                        Logger.getLogger(ImzMLHandler.class.getName()).log(Level.FINER, "Guessing we have {0} images based on {1} spectra", new Object[]{numImagesGuess, spectrumList.size()});
                        Logger.getLogger(ImzMLHandler.class.getName()).log(Level.FINER, "Putting {0} images in x", maxImagesX);

                        haveDoneCheck = true;
                    }

                    numImagesX++;
                    previousMaxX = currentMaxX;

                    Logger.getLogger(ImzMLHandler.class.getName()).log(Level.FINEST, "Changing previousMaxX to {0}", currentMaxX);

                    if (numImagesX > maxImagesX) {
                        Logger.getLogger(ImzMLHandler.class.getName()).log(Level.FINEST, "Moving to next line");

                        numImagesX = 0;
                        numImagesY++;

                        currentMaxX = 0;
                        previousMaxX = 0;
                        previousMaxY = currentMaxY;
                    }

                    current3DPositionZ = z;
                    currentZ++;
                }

            }
        }

        if ("mzML".equals(qName)) {
            mzML = new ImzML(attributes.getValue("version"));

            // Add optional attributes
            if (attributes.getValue("accession") != null) {
                mzML.setAccession(attributes.getValue("accession"));
            }

            if (attributes.getValue("id") != null) {
                mzML.setID(attributes.getValue("id"));
            }
        } else {
            try {
                super.startElement(uri, localName, qName, attributes);
            } catch (InvalidMzML ex) {
                throw new InvalidImzML(ex.getLocalizedMessage());
            }
        }

//        if (processingSCiLS3DData) {
//            // Modify the x, y, z coordinates to 
//            if (qName.equals("cvParam")) {
//                String accession = attributes.getValue("accession");
//
//                if (accession.equals(Scan.positionXID)) {
//
//                    //System.out.println("x = " + currentScan.getCVParam(Scan.positionXID).getValueAsString());
//                } else if (accession.equals(Scan.positionYID)) {
//
//                    //System.out.println("y = " + currentScan.getCVParam(Scan.positionYID).getValueAsString());
//                }
//            }
//        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (ibdFile != null && qName.equals("binaryDataArray")) {
//			CVParam dataType = currentBinaryDataArray.getCVParamOrChild(BinaryDataArray.dataTypeID);

//			if(dataType == null)
//				dataType = currentBinaryDataArray.getCVParamOrChild(BinaryDataArray.ibdDataType);
            currentBinaryDataArray.setDataLocation(new DataLocation(this.dataStorage, currentOffset, (int) this.currentNumBytes));

//                        if(true)
//                            throw new RuntimeException("Removed code - won't work");
//			currentBinaryDataArray.setBinary(new Binary(dataStorage, currentOffset, currentNumBytes, dataType));
        }

        if ("scan".equals(qName) && processingSCiLS3DData) {
            int x = currentScan.getCVParam(Scan.positionXID).getValueAsInteger();

            if (x > imageMaxX) {
                imageMaxX = x;
            }

            int newX = (previousMaxX + x);

            if (newX > currentMaxX) {
                currentMaxX = newX;
            }
            if (newX > datasetMaxX) {
                datasetMaxX = newX;
            }

            if (currentScan.getCVParam(Scan.positionXID).getValueAsInteger() != x) {
                System.out.println("BAD TIME");
            }

            currentScan.getCVParam(Scan.positionXID).setValueAsString("" + newX);

            int y = currentScan.getCVParam(Scan.positionYID).getValueAsInteger();

            if (y > imageMaxY) {
                imageMaxY = y;
            }

            int newY = (previousMaxY + y);

            if (newY > currentMaxY) {
                currentMaxY = newY;
            }
            if (newY > datasetMaxY) {
                datasetMaxY = newY;
            }

            if (currentScan.getCVParam(Scan.positionYID).getValueAsInteger() != y) {
                System.out.println("BAD TIME");
            }

            currentScan.getCVParam(Scan.positionYID).setValueAsString("" + newY);
            
//            count++;
        }

        if ("run".equals(qName) && processingSCiLS3DData) {
            this.currentScanSettings.getCVParam("IMS:1000042").setValueAsString("" + this.datasetMaxX);
            this.currentScanSettings.getCVParam("IMS:1000043").setValueAsString("" + this.datasetMaxY);

//            System.out.println("Count = " + count);
        }

        super.endElement(uri, localName, qName);
    }

    public ImzML getimzML() {
        ImzML imzML = (ImzML) mzML;

        imzML.setibdFile(ibdFile);
        imzML.setDataStorage(dataStorage);

        return imzML;
    }

    public static void main(String args[]) {
        try {
            ImzML imzML = ImzMLHandler.parseimzML("F:\\Sample3D\\Microbe_Interaction_3D_Timecourse_LP.imzML");

            try {
                imzML.write("F:\\Sample3D\\test.imzML");
            } catch (ImzMLWriteException ex) {
                Logger.getLogger(ImzMLHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            System.out.println("Size: " + imzML.getWidth() + " x " + imzML.getHeight());

            double[] mzs;
            try {
                Spectrum spectrum = imzML.getSpectrum(1, 1);

                if (spectrum != null) {
                    mzs = spectrum.getmzArray();
                    System.out.println(mzs[0]);
                }
            } catch (IOException ex) {
                Logger.getLogger(ImzMLHandler.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (ImzMLParseException ex) {
            Logger.getLogger(ImzMLHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
