package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.exceptions.InvalidXPathException;
import com.alanmrace.jimzmlparser.exceptions.UnfollowableXPathException;
import com.alanmrace.jimzmlparser.imzml.PixelLocation;
import com.alanmrace.jimzmlparser.obo.OBO;
import com.alanmrace.jimzmlparser.util.XMLHelper;
import java.io.IOException;
import java.io.Serializable;
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
     * Static number used to generate a unique ID for any Spectrum generated
     * using the {@link Spectrum#createSpectrum(double[], double[])} or
     * {@link Spectrum#createSpectrum(double[], double[], com.alanmrace.jimzmlparser.mzml.DataProcessing)}
     * method(s).
     */
    protected static int spectrumNumber = 0;

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
     * Relative pixel location of the spectrum with respect to the whole MzML
     * file.
     */
    private PixelLocation pixelLocation;

    /**
     * Create Spectrum with required attributes from XML tag.
     *
     * @param id Unique ID for the spectrum
     * @param defaultArrayLength The default length of the data array(s) that
     * make up the spectrum
     */
    public Spectrum(String id, int defaultArrayLength) {
        super(id, defaultArrayLength);
    }

    /**
     * Copy constructor, requiring new versions of lists to match old references
     * to.
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

    /**
     * Set SourceFileRef.
     *
     * @param sourceFileRef New source file reference
     */
    public void setSourceFileRef(SourceFile sourceFileRef) {
        this.sourceFileRef = sourceFileRef;
    }

    /**
     * Set spotID attribute for spectrum XML tag.
     *
     * @param spotID Spot ID
     */
    public void setSpotID(String spotID) {
        this.spotID = spotID;
    }

    /**
     * Get ScanList.
     *
     * @return ScanList
     */
    public ScanList getScanList() {
        return scanList;
    }

    /**
     * Set ScanList.
     *
     * @param scanList ScanList
     */
    public void setScanList(ScanList scanList) {
        scanList.setParent(this);

        this.scanList = scanList;
    }

    /**
     * Set PrecursorList.
     *
     * @param precursorList PrecursorList
     */
    public void setPrecursorList(PrecursorList precursorList) {
        precursorList.setParent(this);

        this.precursorList = precursorList;
    }

    /**
     * Get PrecursorList.
     *
     * @return PrecursorList
     */
    public PrecursorList getPrecursorList() {
        return precursorList;
    }

    /**
     * Set ProductList.
     *
     * @param productList ProductList
     */
    public void setProductList(ProductList productList) {
        productList.setParent(this);

        this.productList = productList;
    }

    /**
     * Get ProductList.
     *
     * @return ProductList
     */
    public ProductList getProductList() {
        return productList;
    }

    /**
     * Get the pixel location of the spectrum, or null if one cannot be
     * determined. The first Scan within the ScanList which contains cvParams
     * for both x and y position determines the spectrum location.
     *
     * @return Location of the spectrum in coordinates
     */
    public PixelLocation getPixelLocation() {
        if (pixelLocation == null) {
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

    /**
     * Set the pixel location of the spectrum. If a ScanList does not 
     * exist then one will be created. Alternatively the first Scan in the ScanList
     * is updated with the new PixelLocation.
     * 
     * @param location New pixel location
     */
    public void setPixelLocation(PixelLocation location) {
        if (scanList == null) {
            scanList = ScanList.create();
        }

        Scan scan = scanList.get(0);

        scan.removeCVParam(Scan.positionXID);
        scan.removeCVParam(Scan.positionYID);
        scan.removeCVParam(Scan.positionZID);

        scan.addCVParam(new IntegerCVParam(OBO.getOBO().getTerm(Scan.positionXID), location.getX()));
        scan.addCVParam(new IntegerCVParam(OBO.getOBO().getTerm(Scan.positionYID), location.getY()));

        if (location.getZ() >= 1) {
            scan.addCVParam(new IntegerCVParam(OBO.getOBO().getTerm(Scan.positionZID), location.getZ()));
        }
        
        pixelLocation = location;
    }

    /**
     * Set the pixel location of the spectrum. If a ScanList does not 
     * exist then one will be created. Alternatively the first Scan in the ScanList
     * is updated with the new PixelLocation.
     * 
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public void setPixelLocation(int x, int y) {
        setPixelLocation(new PixelLocation(x, y, -1));
    }

    /**
     * Set the pixel location of the spectrum. If a ScanList does not 
     * exist then one will be created. Alternatively the first Scan in the ScanList
     * is updated with the new PixelLocation.
     * 
     * @param x x-coordinate
     * @param y y-coordinate
     * @param z z-coordinate
     */
    public void setPixelLocation(int x, int y, int z) {
        setPixelLocation(new PixelLocation(x, y, z));
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
    public String getXMLAttributeText() {
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

        if (scanList != null) {
            children.add(scanList);
        }
        if (precursorList != null) {
            children.add(precursorList);
        }
        if (productList != null) {
            children.add(productList);
        }
        if (binaryDataArrayList != null) {
            children.add(binaryDataArrayList);
        }
    }

    /**
     * Get the m/z array of the spectrum as a double[].
     *
     * @return m/z array
     * @throws IOException If an error occurred during file access
     * (DataLocation)
     */
    public double[] getmzArray() throws IOException {
        return getmzArray(false);
    }

    /**
     * Get the m/z array of the spectrum as a double[] and optionally keep the
     * array within memory.
     *
     * @param keepInMemory true to keep the data within memory, false otherwise
     * @return m/z array
     * @throws IOException If an error occurred during file access
     * (DataLocation)
     */
    public double[] getmzArray(boolean keepInMemory) throws IOException {
        if (binaryDataArrayList == null) {
            return null;
        }

        ensureLoadableData();

        return binaryDataArrayList.getmzArray().getDataAsDouble(keepInMemory);
    }

    /**
     * Set the m/z array to be equal to the supplied double[]. This does not
     * update any metadata, and therefore should only be used when creating new
     * spectra which have not had any processing applied. For all other cases,
     * use {@link Spectrum#updatemzArray(double[], DataProcessing)}.
     *
     * @param mzs New spectral data as double[]
     */
    protected void setmzArray(double[] mzs) {
        binaryDataArrayList.getmzArray().setData(mzs);
    }

    /**
     * Set the intensity array to be equal to the supplied double[]. This does
     * not update any metadata, and therefore should only be used when creating
     * new spectra which have not had any processing applied. For all other
     * cases, use
     * {@link Spectrum#updateIntesityArray(double[], DataProcessing)}.
     *
     * @param intensities New spectral data as double[]
     */
    protected void setIntensityArray(double[] intensities) {
        binaryDataArrayList.getIntensityArray().setData(intensities);
    }

    /**
     * Set the m/z and intensity arrays to be equal to the supplied double[]
     * arrays. This does not update any metadata, and therefore should only be
     * used when creating new spectra which have not had any processing applied.
     * For all other cases, use
     * {@link Spectrum#updateSpectralData(double[], double[], DataProcessing)}.
     *
     * @param mzs New m/z array as double[]
     * @param intensities New spectral data as double[]
     */
    protected void setSpectralData(double[] mzs, double[] intensities) {
        setmzArray(mzs);
        setIntensityArray(intensities);
    }

    /**
     * Update the description of the DataProcessing for the spectrum. This
     * combines all {@link ProcessingMethod}s in the old DataProcessing and the
     * supplied new DataProcessing into a new DataProcessing. The ID for the new
     * DataProcessing is the combination of the old and the new ID's separated
     * by a hyphen.
     *
     * @param processing New DataProcessing applied to modify spectrum
     */
    protected void updateDataProcessing(DataProcessing processing) {
        DataProcessing previousProcessing = this.getDataProcessingRef();
        DataProcessing newProcessing = new DataProcessing(previousProcessing.getID() + "-" + processing.getID());

        for (ProcessingMethod method : previousProcessing) {
            newProcessing.add(method);
        }
        for (ProcessingMethod method : processing) {
            newProcessing.add(method);
        }

        this.setDataProcessingRef(newProcessing);
    }

    /**
     * Set the m/z array to be equal to the supplied double[] and update the
     * metadata to include the description of the processing applied to create
     * the new spectrum.
     *
     * @param mzs New m/z array as double[]
     * @param processing Description of the processing applied to generate m/z
     * array
     */
    public void updatemzArray(double[] mzs, DataProcessing processing) {
        updateDataProcessing(processing);

        setmzArray(mzs);
    }

    /**
     * Set the intensity array to be equal to the supplied double[] and update
     * the metadata to include the description of the processing applied to
     * create the new spectrum.
     *
     * @param intensities New spectral data array as double[]
     * @param processing Description of the processing applied to generate
     * intensity array
     */
    public void updateIntensityArray(double[] intensities, DataProcessing processing) {
        updateDataProcessing(processing);

        setIntensityArray(intensities);
    }

    /**
     * Set the m/z and intensity array to be equal to the supplied double[]
     * arrays and update the metadata to include the description of the
     * processing applied to create the new spectrum.
     *
     * @param mzs New m/z array as double[]
     * @param intensities New spectral data array as double[]
     * @param processing Description of the processing applied to generate
     * intensity array
     */
    public void updateSpectralData(double[] mzs, double[] intensities, DataProcessing processing) {
        updateDataProcessing(processing);

        setmzArray(mzs);
        setIntensityArray(intensities);
    }

    /**
     * Default Spectrum parameters are: MS1 Spectrum & Profile Spectrum. These
     * MUST be changed if this is not the case. Data is stored in memory until
     * written out. Default DataProcessing is used to describe the spectrum, see
     * {@link DataProcessing#create()}.
     *
     * <p>
     * TODO: Consider having these as part of the create spectrum interface.
     *
     * <p>
     * Data defaults are no compression, 64-bit float representation.
     *
     * @param mzs m/z array as double[]
     * @param intensities Intensity array as double[]
     * @return Spectrum instance with default metadata
     */
    private static Spectrum createSpectrum(double[] mzs, double[] intensities) {
        return createSpectrum(mzs, intensities, DataProcessing.create());
    }

    /**
     * Default Spectrum parameters are: MS1 Spectrum & Profile Spectrum. These
     * MUST be changed if this is not the case. Data is stored in memory until
     * written out.
     *
     * <p>
     * TODO: Consider having these as part of the create spectrum interface.
     *
     * <p>
     * Data defaults are no compression, 64-bit float representation.
     *
     * @param mzs m/z array as double[]
     * @param intensities Intensity array as double[]
     * @param processing Description of the DataProcessing applied to create
     * spectrum
     * @return Spectrum instance with default metadata
     */
    private static Spectrum createSpectrum(double[] mzs, double[] intensities, DataProcessing processing) {
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

    public static Spectrum createSpectrum(double[] mzs, double[] intensities, int x, int y) {
        Spectrum spectrum = createSpectrum(mzs, intensities, DataProcessing.create());

        spectrum.setPixelLocation(x, y);

        return spectrum;
    }

    public static Spectrum createSpectrum(double[] mzs, double[] intensities, DataProcessing processing, int x, int y) {
        Spectrum spectrum = createSpectrum(mzs, intensities, processing);

        spectrum.setPixelLocation(x, y);

        return spectrum;
    }
}
