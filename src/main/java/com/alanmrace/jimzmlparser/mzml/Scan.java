package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.exceptions.InvalidXPathException;
import com.alanmrace.jimzmlparser.exceptions.UnfollowableXPathException;
import com.alanmrace.jimzmlparser.util.XMLHelper;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Class describing a {@literal <scan>} tag.
 * 
 * <p>TODO: Look at the methods setInstrumentConfigurationRef, 
 * getInstrumentConfigurationRef, setSourceFileRef, setSpectrumRef as they
 * do not match some of the getter methods. Decide on consistent API.
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
    public static String scanAttributeID = "MS:1000503";

    /**
     * Accession: scan direction (MS:1000018).
     */
    public static String scanDirectionID = "MS:1000018";

    /**
     * Accession: scan law (MS:1000019).
     */
    public static String scanLawID = "MS:1000019";

    /**
     * Accession: spectrum position x (IMS:1000050).
     */
    public static String positionXID = "IMS:1000050";

    /**
     * Accession: spectrum position y (IMS:1000051).
     */
    public static String positionYID = "IMS:1000051";

    /**
     * Accession: spectrum position z (IMS:1000052).
     */
    public static String positionZID = "IMS:1000052";

    /**
     * Accession: elution time (MS:1000826).
     */
    public static String elutionTimeID = "MS:1000826";

    /**
     * Accession: scan start time (MS:1000016).
     */
    public static String scanStartTimeID = "MS:1000016";

    /**
     * Accession: ion mobility time (MS:1002476).
     */
    public static String ionMobilityDriftTimeID = "MS:1002476";

    /**
     * External spectrum ID (attribute from spectrum tag).
     */
    private String externalSpectrumID;

    /**
     * InstrumentConfiguration (attribute from spectrum tag: instrumentConfigurationRef).
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
     * Copy constructor, requiring new versions of lists to match old references to.
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

    @Override
    public ArrayList<OBOTermInclusion> getListOfRequiredCVParams() {
        ArrayList<OBOTermInclusion> required = new ArrayList<OBOTermInclusion>();
        required.add(new OBOTermInclusion(positionXID, true, false, true));
        required.add(new OBOTermInclusion(positionYID, true, false, true));

        return required;
    }

    @Override
    public ArrayList<OBOTermInclusion> getListOfOptionalCVParams() {
        ArrayList<OBOTermInclusion> optional = new ArrayList<OBOTermInclusion>();
        optional.add(new OBOTermInclusion(positionZID, true, false, true));
        optional.add(new OBOTermInclusion(scanAttributeID, false, true, false));
        optional.add(new OBOTermInclusion(scanDirectionID, false, true, false));
        optional.add(new OBOTermInclusion(scanLawID, true, true, false));

        return optional;
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
     * Get external spectrum ID
     * 
     * @return External spectrum ID
     */
    public String getExternalSpectrumID() {
        return externalSpectrumID;
    }

    /**
     * Set the instrument configuration that was used to when performing this Scan.
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
     * @return
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
    public void outputXML(BufferedWriter output, int indent) throws IOException {
        MzMLContent.indent(output, indent);
        output.write("<scan");
        if (externalSpectrumID != null) {
            output.write(" externalSpectrumID=\"" + XMLHelper.ensureSafeXML(externalSpectrumID) + "\"");
        }
        if (instrumentConfigurationRef != null) {
            output.write(" instrumentConfigurationRef=\"" + XMLHelper.ensureSafeXML(instrumentConfigurationRef.getID()) + "\"");
        }
        if (sourceFileRef != null) {
            output.write(" sourceFileRef=\"" + XMLHelper.ensureSafeXML(sourceFileRef.getID()) + "\"");
        }
        if (spectrumRef != null) {
            output.write(" spectrumRef=\"" + XMLHelper.ensureSafeXML(spectrumRef) + "\"");
        }
        output.write(">\n");

        super.outputXMLContent(output, indent + 1);

        if (scanWindowList != null && scanWindowList.size() > 0) {
            scanWindowList.outputXML(output, indent + 1);
        }

        MzMLContent.indent(output, indent);
        output.write("</scan>\n");
    }

    @Override
    public String toString() {
        return "scan: "
                + ((externalSpectrumID != null && !externalSpectrumID.isEmpty()) ? (" externalSpectrumID=\"" + externalSpectrumID + "\"") : "")
                + ((instrumentConfigurationRef != null) ? (" instrumentConfigurationRef=\"" + instrumentConfigurationRef.getID() + "\"") : "")
                + ((sourceFileRef != null) ? (" sourceFileRef=\"" + sourceFileRef.getID() + "\"") : "")
                + ((spectrumRef != null && !spectrumRef.isEmpty()) ? (" spectrumRef=\"" + spectrumRef + "\"") : "");
    }

    @Override
    public String getTagName() {
        return "scan";
    }
    
    @Override
    public void addChildrenToCollection(Collection<MzMLTag> children) {
        if(scanWindowList != null)
            children.add(scanWindowList);
        
        super.addChildrenToCollection(children);
    }
}
