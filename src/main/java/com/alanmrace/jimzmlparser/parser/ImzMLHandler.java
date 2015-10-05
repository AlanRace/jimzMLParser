package com.alanmrace.jimzmlparser.parser;

import com.alanmrace.jimzmlparser.imzML.ImzML;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.alanmrace.jimzmlparser.mzML.Binary;
import com.alanmrace.jimzmlparser.mzML.BinaryDataArray;
import com.alanmrace.jimzmlparser.mzML.CVParam;
import com.alanmrace.jimzmlparser.obo.OBO;
import com.alanmrace.jimzmlparser.exceptions.InvalidImzML;
import com.alanmrace.jimzmlparser.exceptions.InvalidMzML;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ImzMLHandler extends MzMLHeaderHandler {
	
	private File ibdFile;
        private BinaryDataStorage dataStorage;
	private long currentOffset;
	private long currentNumBytes;
	
	public ImzMLHandler(OBO obo) {
		super(obo);
	}
	
	public ImzMLHandler(OBO obo, File ibdFile) {
		super(obo);
		
		this.ibdFile = ibdFile;
                this.dataStorage = new BinaryDataStorage(ibdFile);
	}
	
	public static ImzML parseimzML(String filename) {
		OBO obo = new OBO("imagingMS.obo");
		
		File ibdFile = new File(filename.substring(0, filename.lastIndexOf('.')) + ".ibd");
		
		// Convert mzML header information -> imzML
        ImzMLHandler handler = new ImzMLHandler(obo, ibdFile);
        		
        SAXParserFactory spf = SAXParserFactory.newInstance();
        try {
        	//get a new instance of parser
        	SAXParser sp = spf.newSAXParser();
        			
        	File file = new File(filename);
        	
        	//parse the file and also register this class for call backs
        	sp.parse(file, handler);
        			
        }catch(SAXException se) {
        	se.printStackTrace();
        }catch(ParserConfigurationException pce) {
        	pce.printStackTrace();
        }catch (IOException ie) {
        	ie.printStackTrace();
        }

        handler.getimzML().setOBO(obo);
        
        return handler.getimzML();
	}

        @Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if(ibdFile != null && qName.equals("cvParam")) {
			String accession = attributes.getValue("accession");
			
			if(accession.equals(BinaryDataArray.externalEncodedLengthID))
				currentNumBytes = Long.parseLong(attributes.getValue("value"));
			else if(accession.equals(BinaryDataArray.externalOffsetID))
				currentOffset = Long.parseLong(attributes.getValue("value"));
		}
			
		if(qName.equals("mzML")) {
			mzML = new ImzML(attributes.getValue("version"));
			
			// Add optional attributes
			if(attributes.getValue("accession") != null)
				mzML.setAccession(attributes.getValue("accession"));
			
			if(attributes.getValue("id") != null)
				mzML.setID(attributes.getValue("id"));
		} else {
			try {
				super.startElement(uri, localName, qName, attributes);
			} catch(InvalidMzML ex) {
				throw new InvalidImzML(ex.getLocalizedMessage());
			}
		}
	}
	
        @Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if(ibdFile != null && qName.equals("binaryDataArray")) {
//			CVParam dataType = currentBinaryDataArray.getCVParamOrChild(BinaryDataArray.dataTypeID);
			
//			if(dataType == null)
//				dataType = currentBinaryDataArray.getCVParamOrChild(BinaryDataArray.ibdDataType);

                        currentBinaryDataArray.setDataLocation(new DataLocation(this.dataStorage, currentOffset, (int)this.currentNumBytes));
                        
//                        if(true)
//                            throw new RuntimeException("Removed code - won't work");
//			currentBinaryDataArray.setBinary(new Binary(dataStorage, currentOffset, currentNumBytes, dataType));
		}
		
		super.endElement(uri, localName, qName);
	}
	
	public ImzML getimzML() {
		ImzML imzML = (ImzML)mzML;
		
		imzML.setibdFile(ibdFile);
		
		return imzML;
	}

        public static void main(String args[]) {
            ImzML imzML = ImzMLHandler.parseimzML("D:\\2012_5_2_medium(120502,20h18m)_23.898, 29.745, 30.898, 41.766, 7.000, 0.100, 1_1.imzML");
            
            double[] mzs;
            try {
                mzs = imzML.getSpectrum(1, 1).getmzArray();
                System.out.println(mzs[0]);
            } catch (IOException ex) {
                Logger.getLogger(ImzMLHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
}
