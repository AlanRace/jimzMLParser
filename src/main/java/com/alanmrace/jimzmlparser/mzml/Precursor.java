package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.util.XMLHelper;
import java.util.Collection;

/**
 * Class describing {@literal <precursor>} tag. This describes the method of 
 * precursor ion selection and activation.
 * 
 * @author Alan Race
 */
public class Precursor extends MzMLContentWithParams {

    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = 1L;

    // Attributes

    /**
     * Spectrum ID of a precursor spectrum which is stored in an external document 
     * (see {@link Precursor#sourceFileRef}) [Optional].
     */
    private String externalSpectrumID; 	// Optional

    /**
     * External document containing precursor spectrum [Optional].
     */
    private SourceFile sourceFileRef; 	// Optional

    /**
     * Precursor spectrum [Optional].
     */
    private Spectrum spectrumRef;		// Optional

    // Sub-elements

    /**
     * Isolation window configuration used to isolate one or more ions.
     */
    private IsolationWindow isolationWindow;

    /**
     * List of selected ions.
     */
    private SelectedIonList selectedIonList;

    /**
     * Type and energy level used for activation.
     */
    private Activation activation;

    /**
     * Create empty {@literal <precusor>} tag.
     */
    public Precursor() {
        super();
    }

    /**
     * Copy constructor, requiring new versions of lists to match old references
     * to.
     * 
     * @param precursor Old Precursor to copy
     * @param rpgList New ReferenceableParamGroupList
     * @param sourceFileList New SourceFileList
     */
    public Precursor(Precursor precursor, ReferenceableParamGroupList rpgList, SourceFileList sourceFileList) {
        this.externalSpectrumID = precursor.externalSpectrumID;
        this.spectrumRef = precursor.spectrumRef;

        if (precursor.sourceFileRef != null && sourceFileList != null) {
            for (SourceFile sourceFile : sourceFileList) {
                if (precursor.sourceFileRef.getID().equals(sourceFile.getID())) {
                    this.sourceFileRef = sourceFile;

                    break;
                }
            }
        }

        if (precursor.isolationWindow != null) {
            isolationWindow = new IsolationWindow(precursor.isolationWindow, rpgList);
        }
        if (precursor.selectedIonList != null) {
            selectedIonList = new SelectedIonList(precursor.selectedIonList, rpgList);
        }
        if (precursor.activation != null) {
            activation = new Activation(precursor.activation, rpgList);
        }
    }

    /**
     * Set the details of the location of the spectrum describing the precursor
     * in an external SourceFile [Optional]. The spectrum ID of a precursor spectrum which 
     * is stored in the external document (see {@link Precursor#setSourceFileRef}) 
     * must also be supplied.
     * 
     * @param sourceFileRef Reference to the SourceFile containing the spectrum
     * @param externalSpectrumID Unique spectrum ID in external file
     */
    public void setExternalSpectrum(SourceFile sourceFileRef, String externalSpectrumID) {
        this.sourceFileRef = sourceFileRef;
        this.externalSpectrumID = externalSpectrumID;
    }

    /**
     * Set the Spectrum which describes the precursor spectrum.
     * 
     * @param spectrumRef Precursor spectrum.
     */
    public void setSpectrumRef(Spectrum spectrumRef) {
        this.spectrumRef = spectrumRef;
    }

    /**
     * Set the IsolationWindow configuration used to isolate one or more ions.
     * 
     * @param isolationWindow IsolationWindow
     */
    public void setIsolationWindow(IsolationWindow isolationWindow) {
        isolationWindow.setParent(this);

        this.isolationWindow = isolationWindow;
    }

    /**
     * Return the IsolationWindow configuration used to isolate one or more ions.
     * 
     * @return IsolationWindow
     */
    public IsolationWindow getIsolationWindow() {
        return isolationWindow;
    }

    /**
     * Set the list of selected ions.
     * 
     * @param selectedIonList SelectedIonList
     */
    public void setSelectedIonList(SelectedIonList selectedIonList) {
        selectedIonList.setParent(this);

        this.selectedIonList = selectedIonList;
    }

    /**
     * Return the list of selected ions.
     * 
     * @return SelectedIonList
     */
    public SelectedIonList getSelectedIonList() {
        return selectedIonList;
    }

    /**
     * Set the Activation describing the type and energy level used for activation.
     * 
     * @param activation Activation
     */
    public void setActivation(Activation activation) {
        activation.setParent(this);

        this.activation = activation;
    }

    /**
     * Returns the Activation describing the type and energy level used for activation.
     * 
     * @return Activation
     */
    public Activation getActivation() {
        return activation;
    }

    @Override
    public String getXMLAttributeText() {
        String attributeText = super.getXMLAttributeText();
        
        if (externalSpectrumID != null) {
            attributeText += " externalSpectrumID=\"" + XMLHelper.ensureSafeXML(externalSpectrumID) + "\"";
        }
        if (sourceFileRef != null) {
            attributeText += " sourceFileRef=\"" + XMLHelper.ensureSafeXML(sourceFileRef.getID()) + "\"";
        }
        if (spectrumRef != null) {
            attributeText += " spectrumRef=\"" + XMLHelper.ensureSafeXML(spectrumRef.getID()) + "\"";
        }
        
        if(attributeText.startsWith(" "))
            attributeText = attributeText.substring(1);
        
        return attributeText;
    }

    @Override
    public String toString() {
        return "precursor: "
                + ((spectrumRef != null) ? " spectrumRef=\"" + spectrumRef.getID() + "\"" : "")
                + ((externalSpectrumID != null && !externalSpectrumID.isEmpty()) ? " externalSpectrumID=\"" + externalSpectrumID + "\"" : "")
                + ((sourceFileRef != null) ? " sourceFileRef=\"" + sourceFileRef.getID() + "\"" : "");
    }

    @Override
    public String getTagName() {
        return "precursor";
    }
    
    @Override
    public void addChildrenToCollection(Collection<MzMLTag> children) {
        if(isolationWindow != null)
            children.add(isolationWindow);
        if(selectedIonList != null)
            children.add(selectedIonList);
        if(activation != null)
            children.add(activation);
        
        super.addChildrenToCollection(children);
    }
}
