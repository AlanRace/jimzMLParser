package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.exceptions.InvalidXPathException;
import com.alanmrace.jimzmlparser.exceptions.UnfollowableXPathException;
import com.alanmrace.jimzmlparser.util.XMLHelper;
import java.io.Serializable;
import java.util.Collection;

/**
 * Class describing a {@literal <scan>} tag.
 *
 * <p>TODO: Look at the methods setInstrumentConfigurationRef,
 * getInstrumentConfigurationRef, setSourceFileRef, setSpectrumRef as they do
 * not match some of the getter methods. Decide on consistent API. Also check the
 * setExternalSpectrumID and setSourceFile - these should probably be joined like
 * in Precursor.
 *
 * @author Alan Race
 */
public class Scan extends MzMLContentWithParams implements Serializable {

    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Accession: scan attribute (MS:1000503).
     */
    public static final String scanAttributeID = "MS:1000503";

    /**
     * Accession: scan direction (MS:1000018).
     */
    public static final String scanDirectionID = "MS:1000018";

    /**
     * Accession: scan law (MS:1000019).
     */
    public static final String scanLawID = "MS:1000019";

    /**
     * Accession: spectrum position x (IMS:1000050).
     */
    public static final String positionXID = "IMS:1000050";

    /**
     * Accession: spectrum position y (IMS:1000051).
     */
    public static final String positionYID = "IMS:1000051";

    /**
     * Accession: spectrum position z (IMS:1000052).
     */
    public static final String positionZID = "IMS:1000052";

    /**
     * Accession: elution time (MS:1000826).
     */
    public static final String elutionTimeID = "MS:1000826";

    /**
     * Accession: scan start time (MS:1000016).
     */
    public static final String scanStartTimeID = "MS:1000016";

    /**
     * Accession: ion mobility time (MS:1002476).
     */
    public static final String ionMobilityDriftTimeID = "MS:1002476";

    /**
     * External spectrum ID (attribute from spectrum tag).
     */
    private String externalSpectrumID;

    /**
     * InstrumentConfiguration (attribute from spectrum tag:
     * instrumentConfigurationRef).
     */
    private InstrumentConfiguration instrumentConfigurationRef;

    /**
     * SourceFile (attribute from spectrum tag: sourceFileRef).
     */
    private SourceFile sourceFileRef;

    /**
     * Spectrum reference.
     */
    private String spectrumRef;

    /**
     * ScanWindowList.
     */
    private ScanWindowList scanWindowList;

    /**
     * Scan constructor.
     */
    public Scan() {
        super();
    }

    /**
     * Copy constructor, requiring new versions of lists to match old references
     * to.
     *
     * @param scan Old Scan to copy
     * @param rpgList New ReferenceableParamGroupList
     * @param icList New InstrumentConfigurationList
     * @param sourceFileList New SourceFileList
     */
    public Scan(Scan scan, ReferenceableParamGroupList rpgList, InstrumentConfigurationList icList, SourceFileList sourceFileList) {
        super(scan, rpgList);

        this.externalSpectrumID = scan.externalSpectrumID;
        this.spectrumRef = scan.spectrumRef;

        if (scan.instrumentConfigurationRef != null && icList != null) {
            for (InstrumentConfiguration ic : icList) {
                if (scan.instrumentConfigurationRef.getID().equals(ic.getID())) {
                    this.instrumentConfigurationRef = ic;

                    break;
                }
            }
        }

        if (scan.sourceFileRef != null && sourceFileList != null) {
            for (SourceFile sourceFile : sourceFileList) {
                if (scan.sourceFileRef.getID().equals(sourceFile.getID())) {
                    this.sourceFileRef = sourceFile;

                    break;
                }
            }
        }

        if (scan.scanWindowList != null) {
            scanWindowList = new ScanWindowList(scan.scanWindowList, rpgList);
        }
    }

    /**
     * Set externalSpectrumID for use as attribute when exporting to XML.
     *
     * @param externalSpectrumID External spectrum ID
     */
    public void setExternalSpectrumID(String externalSpectrumID) {
        this.externalSpectrumID = externalSpectrumID;
    }

    /**
     * Get external spectrum ID.
     *
     * @return External spectrum ID
     */
    public String getExternalSpectrumID() {
        return externalSpectrumID;
    }

    /**
     * Set the instrument configuration that was used to when performing this
     * Scan.
     *
     * @param instrumentConfigurationRef Instrument configuration
     */
    public void setInstrumentConfigurationRef(InstrumentConfiguration instrumentConfigurationRef) {
        this.instrumentConfigurationRef = instrumentConfigurationRef;
    }

    /**
     * Get instrument configuration used to perform this scan.
     *
     * @return Instrument configuration
     */
    public InstrumentConfiguration getInstrumentConfigurationRef() {
        return instrumentConfigurationRef;
    }

    /**
     * Set the source file that this scan was previously stored in.
     *
     * @param sourceFileRef Source file
     */
    public void setSourceFileRef(SourceFile sourceFileRef) {
        this.sourceFileRef = sourceFileRef;
    }

    /**
     * Get the source file that this scan was previously stored in.
     *
     * @return SourceFile
     */
    public SourceFile getSourceFileRef() {
        return sourceFileRef;
    }

    /**
     * Set the spectrum reference that corresponds to this scan.
     *
     * @param spectrumRef Spectrum reference
     */
    public void setSpectrumRef(String spectrumRef) {
        this.spectrumRef = spectrumRef;
    }

    /**
     * Set the scan window list.
     *
     * @param scanWindowList Scan window list
     */
    public void setScanWindowList(ScanWindowList scanWindowList) {
        scanWindowList.setParent(this);

        this.scanWindowList = scanWindowList;
    }

    /**
     * Get the scan window list.
     *
     * @return Scan window list
     */
    public ScanWindowList getScanWindowList() {
        return scanWindowList;
    }

    /**
     * Get the instrument configuration used to perform this scan.
     *
     * @return Instrument configuration
     */
    public InstrumentConfiguration getInstrumentConfiguration() {
        return instrumentConfigurationRef;
    }

    /**
     * Get the source file that this scan was previously stored in.
     *
     * @return Source file
     */
    public SourceFile getSourceFile() {
        return sourceFileRef;
    }

    @Override
    protected void addTagSpecificElementsAtXPathToCollection(Collection<MzMLTag> elements, String fullXPath, String currentXPath) throws InvalidXPathException {
        if (currentXPath.startsWith("/scanWindowList")) {
            if (scanWindowList == null) {
                throw new UnfollowableXPathException("No scanWindowList exists, so cannot go to " + fullXPath, fullXPath, currentXPath);
            }

            scanWindowList.addElementsAtXPathToCollection(elements, fullXPath, currentXPath);
        }
    }

    @Override
    public String getXMLAttributeText() {
        String attributeText = super.getXMLAttributeText();

        if (externalSpectrumID != null) {
            attributeText += " externalSpectrumID=\"" + XMLHelper.ensureSafeXML(externalSpectrumID) + "\"";
        }
        if (instrumentConfigurationRef != null) {
            attributeText += " instrumentConfigurationRef=\"" + XMLHelper.ensureSafeXML(instrumentConfigurationRef.getID()) + "\"";
        }
        if (sourceFileRef != null) {
            attributeText += " sourceFileRef=\"" + XMLHelper.ensureSafeXML(sourceFileRef.getID()) + "\"";
        }
        if (spectrumRef != null) {
            attributeText += " spectrumRef=\"" + XMLHelper.ensureSafeXML(spectrumRef) + "\"";
        }

        if (attributeText.startsWith(" ")) {
            attributeText = attributeText.substring(1);
        }

        return attributeText;
    }

    @Override
    public String toString() {
        String description = "scan: ";

        if (externalSpectrumID != null && !externalSpectrumID.isEmpty()) {
            description += " externalSpectrumID=\"" + externalSpectrumID + "\"";
        }

        if (instrumentConfigurationRef != null) {
            description += " instrumentConfigurationRef=\"" + instrumentConfigurationRef.getID() + "\"";
        }

        if (sourceFileRef != null) {
            description += " sourceFileRef=\"" + sourceFileRef.getID() + "\"";
        }

        if (spectrumRef != null && !spectrumRef.isEmpty()) {
            description += " spectrumRef=\"" + spectrumRef + "\"";
        }

        return description;
    }

    @Override
    public String getTagName() {
        return "scan";
    }

    @Override
    public void addChildrenToCollection(Collection<MzMLTag> children) {
        super.addChildrenToCollection(children);

        if (scanWindowList != null) {
            children.add(scanWindowList);
        }
    }
    
    public static Scan create() {
        Scan scan = new Scan();
        
        return scan;
    }
}
