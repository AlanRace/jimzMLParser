package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.exceptions.InvalidXPathException;
import com.alanmrace.jimzmlparser.exceptions.UnfollowableXPathException;
import com.alanmrace.jimzmlparser.util.XMLHelper;
import java.util.Collection;

/**
 * Class describing an mass spectrometer.
 * 
 * @author Alan Race
 */
public class InstrumentConfiguration extends MzMLContentWithParams implements ReferenceableTag {

    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Accession: Instrument model (MS:1000031). [Required child]
     */
    public static final String instrumentModelID = "MS:1000031"; // Required child (1)

    /**
     * Accession: Instrument attribute (MS:1000496). [Optional child]
     */
    public static final String instrumentAttributeID = "MS:1000496"; // Optional child (1+)

    /**
     * Accession: Ion optics type (MS:1000597). [Optional child]
     */
    public static final String ionOpticsTypeID = "MS:1000597"; // Optional child (1)

    /**
     * Accession: Ion optics attribute (MS:1000487). [Optional child]
     */
    public static final String ionOpticsAttributeID = "MS:1000487"; // Optional child (1+)

    // Attributes

    /**
     * Unique ID for the instrument configuration.
     */
    private String id;				// Required

    /**
     * Scan settings for the instrument.
     */
    private ScanSettings scanSettingsRef;	// Optional

    // Sub-elements

    /**
     * Components that make up the instrument.
     */
    private ComponentList componentList;

    /**
     * Software used to control the instrument.
     */
    private SoftwareRef softwareRef;

    /**
     * Create instrument configuration with the specified unique identifier.
     * 
     * @param id Unique identifier
     */
    public InstrumentConfiguration(String id) {
        this.id = id;
    }

    /**
     * Create instrument configuration with specified unique identifier and scan 
     * settings.
     * 
     * @param id Unique identifier
     * @param scanSettingsRef Scan settings
     */
    public InstrumentConfiguration(String id, ScanSettings scanSettingsRef) {
        this.id = id;
        this.scanSettingsRef = scanSettingsRef;
    }

    public InstrumentConfiguration(InstrumentConfiguration ic, ReferenceableParamGroupList rpgList, ScanSettingsList ssList, SoftwareList softwareList) {
        super(ic, rpgList);

        this.id = ic.id;

        if (ic.scanSettingsRef != null && ssList != null) {
            for (ScanSettings ss : ssList) {
                if (ic.scanSettingsRef.getID().equals(ss.getID())) {
                    scanSettingsRef = ss;

                    break;
                }
            }
        }

        if (ic.componentList != null) {
            componentList = new ComponentList(ic.componentList, rpgList);
        }

        if (ic.softwareRef != null && softwareList != null) {
            for (Software software : softwareList) {
                if (ic.softwareRef.getReference().getID().equals(software.id)) {
                    softwareRef = new SoftwareRef(software);
                    break;
                }
            }
        }

    }

    public void setScanSettingsRef(ScanSettings scanSettingsRef) {
        this.scanSettingsRef = scanSettingsRef;
    }

    public ScanSettings getScanSettingsRef() {
        return scanSettingsRef;
    }

    public void setComponentList(ComponentList componentList) {
        componentList.setParent(this);

        this.componentList = componentList;
    }

    public ComponentList getComponentList() {
        if (componentList == null) {
            componentList = new ComponentList();
        }

        return componentList;
    }
    
    public void setSoftwareRef(SoftwareRef software) {
        softwareRef = software;
    }

    public SoftwareRef getSoftwareRef() {
        return softwareRef;
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    protected void addTagSpecificElementsAtXPathToCollection(Collection<MzMLTag> elements, String fullXPath, String currentXPath) throws InvalidXPathException {
        if (currentXPath.startsWith("/componentList")) {
            if (componentList == null) {
                throw new UnfollowableXPathException("No componentList exists, so cannot go to " + fullXPath, fullXPath, currentXPath);
            }

            componentList.addElementsAtXPathToCollection(elements, fullXPath, currentXPath);
        }
    }

    @Override
    public String getXMLAttributeText() {
        String attributeText = super.getXMLAttributeText();
        
        if (scanSettingsRef != null) {
            attributeText += " scanSettingsRef=\"" + XMLHelper.ensureSafeXML(scanSettingsRef.getID()) + "\"";
        }
        
        return attributeText;
    }

    @Override
    public String toString() {
        return "instrumentConfiguration: " + id;
    }

    @Override
    public String getTagName() {
        return "instrumentConfiguration";
    }
    
    @Override
    public void addChildrenToCollection(Collection<MzMLTag> children) {
        super.addChildrenToCollection(children);
        
        if(componentList != null)
            children.add(componentList);
        if(softwareRef != null)
            children.add(softwareRef);
    }

    @Override
    public void setID(String id) {
        this.id = id;
    }
}
