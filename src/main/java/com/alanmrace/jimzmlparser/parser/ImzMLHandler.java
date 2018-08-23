package com.alanmrace.jimzmlparser.parser;

import com.alanmrace.jimzmlparser.data.DataLocation;
import com.alanmrace.jimzmlparser.data.BinaryDataStorage;
import com.alanmrace.jimzmlparser.exceptions.FatalParseException;
import com.alanmrace.jimzmlparser.exceptions.ImzMLParseException;
import com.alanmrace.jimzmlparser.exceptions.InvalidExternalOffset;
import com.alanmrace.jimzmlparser.exceptions.InvalidImzML;
import com.alanmrace.jimzmlparser.imzml.ImzML;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.alanmrace.jimzmlparser.mzml.BinaryDataArray;
import com.alanmrace.jimzmlparser.obo.OBO;
import com.alanmrace.jimzmlparser.exceptions.InvalidMzML;
import com.alanmrace.jimzmlparser.mzml.MzMLDataContainer;
import com.alanmrace.jimzmlparser.mzml.Scan;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import net.jpountz.lz4.LZ4BlockInputStream;
import org.tukaani.xz.XZInputStream;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * SAX parser for imzML files.
 *
 * @author Alan Race
 */
public class ImzMLHandler extends MzMLHeaderHandler {

    /**
     * Logger for ImzMLHandler.
     */
    private static final Logger logger = Logger.getLogger(ImzMLHandler.class.getName());

    /**
     * IBD file containing the binary data for the imzML file.
     */
    private File ibdFile;

    /**
     * Used to store the current spectrum/chromatogram's externalOffset attribute
     * value for use when creating a {@link DataLocation} for the binary data.
     */
    private long currentOffset;

    /**
     * Used to store the current spectrum/chromatogram's externalEncodedLength attribute
     * value for use when creating a {@link DataLocation} for the binary data.
     */
    private long currentNumBytes;

    /**
     * Boolean indicating whether 3D data exported from SCiLS has been detected. This 
     * is determined by detecting a userParam with the name '3DPositionZ';
     */
    private boolean processingSCiLS3DData = false;

    /**
     * Updated at the end of each {@literal <scan>} tag if processing SCiLS exported 
     * 3D data (see {@link ImzMLHandler#processingSCiLS3DData}) to keep track of the largest
     * x-coordinate for a spectrum stored within the imzML file.
     */
    private int imageMaxX;
    
    /**
     * Updated at the end of each {@literal <scan>} tag to keep track of the largest
     * y-coordinate for a spectrum stored within the imzML file.
     */
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

    /**
     * Set up a SAX parser for imzML metadata only with the specified ontology dictionary.
     * 
     * @param obo Ontology database
     */
    public ImzMLHandler(OBO obo) {
        super(obo);
    }

    /**
     * Set up a SAX parser for imzML with the specified ontology dictionary.
     * 
     * @param obo               Ontology database
     * @param ibdFile           IBD file containing the binary data for the imzML file
     * @param openDataStorage   if true, open the binary data file, otherwise just process metadata
     * @throws FileNotFoundException    If no IBD file could be found
     */
    public ImzMLHandler(OBO obo, File ibdFile, boolean openDataStorage) throws FileNotFoundException {
        super(obo);

        this.ibdFile = ibdFile;

        if (openDataStorage) {
            this.dataStorage = new BinaryDataStorage(ibdFile, false);
        }
    }

    /**
     * Set up an ImzMLHandler, perform the parsing and return the ImzML representation.
     * Calls parseimzML(filename, true); defaulting to opening the binary IBD storage.
     * 
     * @param filename  Location of the imzML file
     * @return          ImzML representation of the imzML file
     * @throws ImzMLParseException  If a fatal parse error occurs
     */
    public static ImzML parseimzML(String filename) {
        return parseimzML(filename, true);
    }

    /**
     * Set up an ImzMLHandler, perform the parsing and return the ImzML representation,
     * optionally opening the IBD binary data storage for reading.
     * Calls parseimzML(filename, openDataStorage, null); defaulting to no ParserListener
     * 
     * @param filename          Location of the imzML file
     * @param openDataStorage   true to open the IBD binary data storage, false to only parse metadata
     * @return                  ImzML representation of the imzML file
     * @throws ImzMLParseException  If a fatal parse error occurs
     */
    public static ImzML parseimzML(String filename, boolean openDataStorage) {
        return parseimzML(filename, openDataStorage, null);
    }
    
    /**
     * Set up an ImzMLHandler, perform the parsing and return the ImzML representation,
     * optionally opening the IBD binary data storage for reading. Optional inclusion
     * of a ParserListener which will be notified of any non-fatal parsing issues.
     * 
     * @param filename          Location of the imzML file
     * @param openDataStorage   true to open the IBD binary data storage, false to only parse metadata
     * @param listener          ParserListener which will be notified of any non-fatal parsing issues
     * @return                  ImzML representation of the imzML file
     * @throws ImzMLParseException  If a fatal parse error occurs
     */
    public static ImzML parseimzML(String filename, boolean openDataStorage, ParserListener listener) {
        ImzMLHandler handler;
        InputStream inputStream = null;
        
        try {
            //OBO obo = new OBO("imagingMS.obo");
            OBO obo = OBO.getOBO();
            
            File ibdFile = new File(filename.substring(0, filename.toLowerCase().lastIndexOf(".imzml")) + ".ibd");
            
            // Convert mzML header information -> imzML
            handler = new ImzMLHandler(obo, ibdFile, openDataStorage);

            if(listener != null)
                handler.registerParserListener(listener);
            
            SAXParserFactory spf = SAXParserFactory.newInstance();

            //get a new instance of parser
            SAXParser sp = spf.newSAXParser();

            File file = new File(filename);
            inputStream = new FileInputStream(file);
            
            if(filename.endsWith(".lz4")) {
                inputStream = new LZ4BlockInputStream(inputStream);
            } else if(filename.endsWith(".gz")) {
                inputStream = new GZIPInputStream(inputStream);
            } else if(filename.endsWith(".xz")) {
                inputStream = new XZInputStream(inputStream);
            }

            //parse the file and also register this class for call backs
            sp.parse(inputStream, handler);

            handler.getimzML().setOBO(obo);
        } catch (SAXException ex) {
            logger.log(Level.SEVERE, null, ex);

            throw new ImzMLParseException("SAXException: " + ex, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ImzMLHandler.class.getName()).log(Level.SEVERE, null, ex);

            throw new ImzMLParseException(ex.getLocalizedMessage(), ex);
        } catch (IOException ex) {
            Logger.getLogger(ImzMLHandler.class.getName()).log(Level.SEVERE, null, ex);

            throw new ImzMLParseException("IOException: " + ex, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ImzMLHandler.class.getName()).log(Level.SEVERE, null, ex);

            throw new ImzMLParseException("ParserConfigurationException: " + ex, ex);
        } finally {
            if(inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ex) {
                    Logger.getLogger(ImzMLHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        return handler.getimzML();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (ibdFile != null && qName.equals("cvParam")) {
            String accession = attributes.getValue("accession");

            if (accession.equals(BinaryDataArray.externalEncodedLengthID)) {
                try {
                    currentNumBytes = Long.parseLong(attributes.getValue("value"));
                } catch (NumberFormatException ex) {
                    // TODO: Add in a parser issue notification here
                    
                    currentNumBytes = (long) Double.parseDouble(attributes.getValue("value"));
                }
            } else if (accession.equals(BinaryDataArray.externalOffsetID)) {
                try {
                    currentOffset = Long.parseLong(attributes.getValue("value"));
                } catch (NumberFormatException ex) {
                    // TODO: Add in a parser issue notification here
                    
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
                throw new InvalidImzML(ex.getLocalizedMessage(), ex);
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
            if(currentOffset < 0) {
                InvalidExternalOffset issue = new InvalidExternalOffset((MzMLDataContainer)currentBinaryDataArray.getParent().getParent(), currentOffset);

                notifyParserListeners(issue);
                
                currentOffset += DataLocation.EXTENDED_OFFSET;
                currentBinaryDataArray.getCVParam(BinaryDataArray.externalOffsetID).setValueAsString("" + currentOffset);
            }
            
            DataLocation location = new DataLocation(this.dataStorage, currentOffset, (int) this.currentNumBytes);
            currentBinaryDataArray.setDataLocation(location);
            location.setDataTransformation(currentBinaryDataArray.generateDataTransformation());
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
                logger.log(Level.SEVERE, "Mismatch between the X value in the currentScan ({0}) and the local variable x ({1})", 
                        new Object[]{currentScan.getCVParam(Scan.positionXID).getValueAsInteger(), x});
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
                logger.log(Level.SEVERE, "Mismatch between the Y value in the currentScan ({0}) and the local variable y ({1})", 
                        new Object[]{currentScan.getCVParam(Scan.positionYID).getValueAsInteger(), y});
            }

            currentScan.getCVParam(Scan.positionYID).setValueAsString("" + newY);
        }

        if ("run".equals(qName) && processingSCiLS3DData) {
            this.currentScanSettings.getCVParam("IMS:1000042").setValueAsString("" + this.datasetMaxX);
            this.currentScanSettings.getCVParam("IMS:1000043").setValueAsString("" + this.datasetMaxY);
        }

        super.endElement(uri, localName, qName);
    }

    /**
     * Get the ImzML created in the SAX parser process.
     * 
     * @return
     */
    public ImzML getimzML() {
        ImzML imzML = (ImzML) mzML;

        imzML.setibdFile(ibdFile);
        imzML.setDataStorage(dataStorage);

        return imzML;
    }
}
