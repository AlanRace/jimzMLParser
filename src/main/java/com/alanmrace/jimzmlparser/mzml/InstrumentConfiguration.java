package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.exceptions.InvalidXPathException;
import com.alanmrace.jimzmlparser.exceptions.UnfollowableXPathException;
import com.alanmrace.jimzmlparser.util.XMLHelper;
import java.util.ArrayList;
import java.util.Collection;

public class InstrumentConfiguration extends MzMLContentWithParams implements ReferenceableTag {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static String instrumentModelID = "MS:1000031"; // Required child (1)
    public static String instrumentAttributeID = "MS:1000496"; // Optional child (1+)
    public static String ionOpticsTypeID = "MS:1000597"; // Optional child (1)
    public static String ionOpticsAttributeID = "MS:1000487"; // Optional child (1+)

    protected static int idNumber = 0;

    // Attributes
    private String id;						// Required
    private ScanSettings scanSettingsRef;	// Optional

    // Sub-elements
    private ComponentList componentList;
    private SoftwareRef softwareRef;

    public InstrumentConfiguration(String id) {
        this.id = id;
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

    public InstrumentConfiguration(String id, ScanSettings scanSettingsRef) {
        this.id = id;
        this.scanSettingsRef = scanSettingsRef;
    }

    @Override
    public ArrayList<OBOTermInclusion> getListOfRequiredCVParams() {
        ArrayList<OBOTermInclusion> required = new ArrayList<OBOTermInclusion>();
        required.add(new OBOTermInclusion(instrumentModelID, true, true, true));

        return required;
    }

    @Override
    public ArrayList<OBOTermInclusion> getListOfOptionalCVParams() {
        ArrayList<OBOTermInclusion> optional = new ArrayList<OBOTermInclusion>();
        optional.add(new OBOTermInclusion(instrumentAttributeID, false, true, false));
        optional.add(new OBOTermInclusion(ionOpticsTypeID, true, true, false));
        optional.add(new OBOTermInclusion(ionOpticsAttributeID, false, true, false));

        return optional;
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
    protected String getXMLAttributeText() {
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
