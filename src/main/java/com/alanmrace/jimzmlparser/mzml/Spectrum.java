package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.exceptions.InvalidXPathException;
import com.alanmrace.jimzmlparser.exceptions.UnfollowableXPathException;
import com.alanmrace.jimzmlparser.imzml.PixelLocation;
import com.alanmrace.jimzmlparser.obo.OBO;
import com.alanmrace.jimzmlparser.data.MzMLSpectrumDataStorage;
import com.alanmrace.jimzmlparser.util.XMLHelper;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Class describing a {@literal <spectrum>} tag, with additional methods for 
 * handling a pixel location for imaging data.
 * 
 * @author Alan Race
 */
public class Spectrum extends MzMLDataContainer implements Serializable {

    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Accession: scan polarity (MS:1000465).
     */
    public static final String scanPolarityID = "MS:1000465";

    /**
     * Accession: spectrum type (MS:1000559).
     */
    public static final String spectrumTypeID = "MS:1000559";

    /**
     * Accession: spectrum representation (MS:1000525).
     */
    public static final String spectrumRepresentationID = "MS:1000525";

    /**
     * Accession: spectrum attribute (MS:1000499).
     */
    public static final String spectrumAttributeID = "MS:1000499";

    /**
     * Accession: total ion current (MS:1000285).
     */
    public static final String totalIonCurrentID = "MS:1000285";

    /**
     * Accession: base peak m/z (MS:1000504).
     */
    public static final String basePeakMZID = "MS:1000504";

    /**
     * Accession: base peak intensity (MS:1000505).
     */
    public static final String basePeakIntensityID = "MS:1000505";

    /**
     * Accession: lowest observed m/z (MS:1000528).
     */
    public static final String lowestObservedmzID = "MS:1000528";

    /**
     * Accession: highest observed m/z (MS:1000527).
     */
    public static final String highestObservedmzID = "MS:1000527";

    /**
     * Accession: MS1 spectrum (MS:1000579).
     */
    public static final String MS1Spectrum = "MS:1000579"; // EmptyCVParam

    /**
     * Accession: positive scan (MS:1000130).
     */
    public static final String positiveScanID = "MS:1000130";	// EmptyCVParam

    /**
     * Accession: profile spectrum (MS:1000128).
     */
    public static final String profileSpectrumID = "MS:1000128"; // EmptyCVParam

    /**
     * SourceFileRef: SourceFile reference.
     */
    private SourceFile sourceFileRef;

    /**
     * XML attribute spotID.
     */
    private String spotID;

    /**
     * ScanList for the spectrum.
     */
    private ScanList scanList;

    /**
     * PrecursorList for the spectrum.
     */
    private PrecursorList precursorList;

    /**
     * ProductList for the spectrum.
     */
    private ProductList productList;

    /**
     * Relative pixel location of the spectrum with respect to the whole MzML file.
     */
    private PixelLocation pixelLocation;

    /**
     * Create Spectrum with required attributes from XML tag.
     * 
     * @param id Unique ID for the spectrum
     * @param defaultArrayLength The default length of the data array(s) that make up the spectrum
     */
    public Spectrum(String id, int defaultArrayLength) {
        super(id, defaultArrayLength);
    }

    /**
     * Copy constructor, requiring new versions of lists to match old references to.
     * 
     * @param spectrum Spectrum to copy
     * @param rpgList New ReferenceableParamGroupList
     * @param dpList New DataProcessingList
     * @param sourceFileList New SourceFileList
     * @param icList New InstrumentConfigurationList
     */
    public Spectrum(Spectrum spectrum, ReferenceableParamGroupList rpgList, DataProcessingList dpList,
            SourceFileList sourceFileList, InstrumentConfigurationList icList) {
        super(spectrum, rpgList);

        this.id = spectrum.id;
        this.defaultArrayLength = spectrum.defaultArrayLength;
        this.spotID = spectrum.spotID;

        if (spectrum.dataProcessingRef != null && dpList != null) {
            for (DataProcessing dp : dpList) {
                if (spectrum.dataProcessingRef.getID().equals(dp.getID())) {
                    dataProcessingRef = dp;
                }
            }
        }

        if (spectrum.sourceFileRef != null && sourceFileList != null) {
            for (SourceFile sourceFile : sourceFileList) {
                if (spectrum.sourceFileRef.getID().equals(sourceFile.getID())) {
                    sourceFileRef = sourceFile;

                    break;
                }
            }
        }

        if (spectrum.scanList != null) {
            scanList = new ScanList(spectrum.scanList, rpgList, sourceFileList, icList);
        }
        if (spectrum.precursorList != null) {
            precursorList = new PrecursorList(spectrum.precursorList, rpgList, sourceFileList);
        }
        if (spectrum.productList != null) {
            productList = new ProductList(spectrum.productList, rpgList);
        }
        if (spectrum.binaryDataArrayList != null) {
            binaryDataArrayList = new BinaryDataArrayList(spectrum.binaryDataArrayList, rpgList, dpList);
        }
    }

    @Override
    public ArrayList<OBOTermInclusion> getListOfRequiredCVParams() {
        ArrayList<OBOTermInclusion> required = new ArrayList<OBOTermInclusion>();
        required.add(new OBOTermInclusion(spectrumTypeID, true, true, false));
        required.add(new OBOTermInclusion(spectrumRepresentationID, true, true, true));

        return required;
    }

    @Override
    public ArrayList<OBOTermInclusion> getListOfOptionalCVParams() {
        ArrayList<OBOTermInclusion> optional = new ArrayList<OBOTermInclusion>();
        optional.add(new OBOTermInclusion(scanPolarityID, true, true, false));
        optional.add(new OBOTermInclusion(spectrumAttributeID, false, true, false));

        return optional;
    }

    /**
     * Set SourceFileRef.
     * 
     * @param sourceFileRef
     */
    public void setSourceFileRef(SourceFile sourceFileRef) {
        this.sourceFileRef = sourceFileRef;
    }

    /**
     * Set spotID attribute for spectrum XML tag.
     * 
     * @param spotID
     */
    public void setSpotID(String spotID) {
        this.spotID = spotID;
    }

    /**
     * Get ScanList.
     * 
     * @return
     */
    public ScanList getScanList() {
        return scanList;
    }

    /**
     * Set ScanList.
     * 
     * @param scanList
     */
    public void setScanList(ScanList scanList) {
        scanList.setParent(this);

        this.scanList = scanList;
    }

    /**
     * Set PrecursorList.
     * 
     * @param precursorList
     */
    public void setPrecursorList(PrecursorList precursorList) {
        precursorList.setParent(this);

        this.precursorList = precursorList;
    }

    /**
     * Get PrecursorList.
     * 
     * @return
     */
    public PrecursorList getPrecursorList() {
        return precursorList;
    }

    /**
     * Set ProductList.
     * 
     * @param productList
     */
    public void setProductList(ProductList productList) {
        productList.setParent(this);

        this.productList = productList;
    }

    /**
     * Get ProductList.
     * 
     * @return
     */
    public ProductList getProductList() {
        return productList;
    }

    /**
     * Get the pixel location of the spectrum, or null if one cannot be determined.
     * The first Scan within the ScanList which contains cvParams for both x and y 
     * position determines the spectrum location.
     * 
     * @return
     */
    public PixelLocation getPixelLocation() {
        if (pixelLocation == null) {
            // 
            for (Scan scan : scanList) {
                CVParam xValue = scan.getCVParam(Scan.positionXID);
                CVParam yValue = scan.getCVParam(Scan.positionYID);
                CVParam zValue = scan.getCVParam(Scan.positionZID);

                if (xValue != null && yValue != null) {
                    int x = xValue.getValueAsInteger();
                    int y = yValue.getValueAsInteger();

                    int z = (zValue != null) ? zValue.getValueAsInteger() : 1;

                    pixelLocation = new PixelLocation(x, y, z);

                    break;
                }
            }
        }

        return pixelLocation;
    }
    @Override
    protected void addTagSpecificElementsAtXPathToCollection(Collection<MzMLTag> elements, String fullXPath, String currentXPath) throws InvalidXPathException {
        if (currentXPath.startsWith("/scanList")) {
            if (scanList == null) {
                throw new UnfollowableXPathException("No scanList exists, so cannot go to " + fullXPath, fullXPath, currentXPath);
            }

            scanList.addElementsAtXPathToCollection(elements, fullXPath, currentXPath);
        } else if (currentXPath.startsWith("/precursorList")) {
            if (precursorList == null) {
                throw new UnfollowableXPathException("No precursorList exists, so cannot go to " + fullXPath, fullXPath, currentXPath);
            }

            precursorList.addElementsAtXPathToCollection(elements, fullXPath, currentXPath);
        } else if (currentXPath.startsWith("/productList")) {
            if (productList == null) {
                throw new UnfollowableXPathException("No productList exists, so cannot go to " + fullXPath, fullXPath, currentXPath);
            }

            productList.addElementsAtXPathToCollection(elements, fullXPath, currentXPath);
        } else if (currentXPath.startsWith("/binaryDataArrayList")) {
            if (binaryDataArrayList == null) {
                throw new UnfollowableXPathException("No binaryDataArrayList exists, so cannot go to " + fullXPath, fullXPath, currentXPath);
            }

            binaryDataArrayList.addElementsAtXPathToCollection(elements, fullXPath, currentXPath);
        }
    }

    @Override
    protected String getXMLAttributeText() {
        String attributeText = super.getXMLAttributeText();
                
        if (sourceFileRef != null) {
            attributeText += " sourceFileRef=\"" + XMLHelper.ensureSafeXML(sourceFileRef.getID()) + "\"";
        }
        if (spotID != null) {
            attributeText += " spotID=\"" + XMLHelper.ensureSafeXML(spotID) + "\"";
        }
        
        return attributeText;
    }

    @Override
    public String toString() {
        return "spectrum:"
                + " id=\"" + id + "\""
                + ((dataProcessingRef != null) ? (" dataProcessingRef=\"" + dataProcessingRef.getID() + "\"") : ""
                        + " defaultArrayLength=\"" + defaultArrayLength + "\""
                        + ((sourceFileRef != null) ? (" sourceFileRef=\"" + sourceFileRef.getID() + "\"") : "")
                        + ((spotID != null && !spotID.isEmpty()) ? (" spotID=\"" + spotID + "\"") : ""));
    }

    @Override
    public String getTagName() {
        return "spectrum";
    }
    
    @Override
    public void addChildrenToCollection(Collection<MzMLTag> children) {
        super.addChildrenToCollection(children);
        
        if(scanList != null)
            children.add(scanList);
        if(precursorList != null)
            children.add(precursorList);
        if(productList != null)
            children.add(productList);
        if(binaryDataArrayList != null)
            children.add(binaryDataArrayList);
    }
    
    /**
     * Get the m/z array of the spectrum as a double[].
     * 
     * @return m/z array
     * @throws IOException
     */
    public double[] getmzArray() throws IOException {
        return getmzArray(false);
    }

    /**
     * Get the m/z array of the spectrum as a double[] and optionally keep the 
     * array within memory.
     * 
     * @param keepInMemory true to keep the data within memory, false otherwise
     * @return
     * @throws IOException
     */
    public double[] getmzArray(boolean keepInMemory) throws IOException {
        if (binaryDataArrayList == null) {
            return null;
        }

        if (dataLocation != null && dataLocation.getDataStorage() instanceof MzMLSpectrumDataStorage) {
            convertMzMLDataStorageToBase64();
        }

        return binaryDataArrayList.getmzArray().getDataAsDouble(keepInMemory);
    }
    
    /**
     * Set the m/z array to be equal to the supplied double[]. This does not update
     * any metadata, and therefore should only be used when creating new spectra
     * which have not had any processing applied. For all other cases, 
     * use {@link Spectrum#updatemzArray(double[], DataProcessing)}.
     * 
     * @param mzs New spectral data as double[]
     */
    protected void setmzArray(double[] mzs) {
        Binary binary = new Binary(mzs);
        
        binaryDataArrayList.getmzArray().setBinary(binary);
    }
    
    protected void setIntensityArray(double[] intensities) {
        Binary binary = new Binary(intensities);
        
        binaryDataArrayList.getIntensityArray().setBinary(binary);
    }
    
    protected void setSpectralData(double[] mzs, double[] intensities) {
        setmzArray(mzs);
        setIntensityArray(intensities);
    }
    
    /**
     * Set the m/z array to be equal to the supplied double[] and update the metadata
     * to include the description of the processing applied to create the new spectrum.
     * 
     * @param mzs
     * @param processing
     */
    public void updatemzArray(double[] mzs, DataProcessing processing) {
        // TODO: 
        
        DataProcessing previousProcessing = this.getDataProcessingRef();
        DataProcessing newProcessing = new DataProcessing(previousProcessing.getID() + "-" + processing.getID());
        
        for(ProcessingMethod method : previousProcessing)
            newProcessing.add(method);
        for(ProcessingMethod method : processing)
            newProcessing.add(method);
        
        this.setDataProcessingRef(newProcessing);
        
        setmzArray(mzs);
    }
    
    public void updateIntensityArray(double[] mzs, DataProcessing processing) {
        
    }
    
    public void updateSpectralData(double[] mzs, double[] intensities, DataProcessing processing) {
        
    }
    
    protected static int spectrumNumber = 0;
    
    /**
     *
     * Default Spectrum parameters are: MS1 Spectrum & Profile Spectrum. These MUST be 
     * changed if this is not the case.
     * TODO: Consider having these as part of the create spectrum interface.
     * 
     * Data defaults are no compression, 64-bit float representation.
     * 
     * @param mzs
     * @param intensities
     * @return
     */
    public static Spectrum createSpectrum(double[] mzs, double[] intensities) {
        return createSpectrum(mzs, intensities, DataProcessing.create());
    }
    
    /**
     *
     * Default Spectrum parameters are: MS1 Spectrum & Profile Spectrum. These MUST be 
     * changed if this is not the case.
     * TODO: Consider having these as part of the create spectrum interface.
     * 
     * Data defaults are no compression, 64-bit float representation.
     * 
     * @param mzs
     * @param intensities
     * @param processing
     * @return
     */
    public static Spectrum createSpectrum(double[] mzs, double[] intensities, DataProcessing processing) {
        String id = "spectrum=" + spectrumNumber++;
        
        Spectrum spectrum = new Spectrum(id, intensities.length);
        spectrum.setDataProcessingRef(processing);
        
        spectrum.addCVParam(new EmptyCVParam(OBO.getOBO().getTerm(Spectrum.MS1Spectrum)));
        spectrum.addCVParam(new EmptyCVParam(OBO.getOBO().getTerm(Spectrum.profileSpectrumID)));
        
        BinaryDataArray mzsDataArray = new BinaryDataArray(mzs.length);
        mzsDataArray.addCVParam(new EmptyCVParam(OBO.getOBO().getTerm(BinaryDataArray.mzArrayID), 
                OBO.getOBO().getTerm(BinaryDataArray.mzArrayUnitsID)));
        mzsDataArray.addCVParam(new EmptyCVParam(OBO.getOBO().getTerm(BinaryDataArray.noCompressionID)));
        mzsDataArray.addCVParam(new EmptyCVParam(OBO.getOBO().getTerm(BinaryDataArray.doublePrecisionID)));
        
        BinaryDataArray intensitiesDataArray = new BinaryDataArray(intensities.length);
        intensitiesDataArray.addCVParam(new EmptyCVParam(OBO.getOBO().getTerm(BinaryDataArray.intensityArrayID)));
        intensitiesDataArray.addCVParam(new EmptyCVParam(OBO.getOBO().getTerm(BinaryDataArray.noCompressionID)));
        intensitiesDataArray.addCVParam(new EmptyCVParam(OBO.getOBO().getTerm(BinaryDataArray.doublePrecisionID)));
        
        spectrum.getBinaryDataArrayList().addBinaryDataArray(mzsDataArray);
        spectrum.getBinaryDataArrayList().addBinaryDataArray(intensitiesDataArray);
        
        spectrum.setSpectralData(mzs, intensities);
        
        return spectrum;
    }
}
