package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.util.XMLHelper;
import java.io.Serializable;
import java.util.Collection;

public class Precursor extends MzMLContentWithParams implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    // Attributes
    private String externalSpectrumID; 	// Optional
    private SourceFile sourceFileRef; 	// Optional
    private String spectrumRef;		// Optional

    // Sub-elements
    private IsolationWindow isolationWindow;
    private SelectedIonList selectedIonList;
    private Activation activation;

    public Precursor() {
        super();
    }

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

    public void setExternalSpectrumID(String externalSpectrumID) {
        this.externalSpectrumID = externalSpectrumID;
    }

    public void setSourceFileRef(SourceFile sourceFileRef) {
        this.sourceFileRef = sourceFileRef;
    }

    public void setSpectrumRef(String spectrumRef) {
        this.spectrumRef = spectrumRef;
    }

    public void setIsolationWindow(IsolationWindow isolationWindow) {
        isolationWindow.setParent(this);

        this.isolationWindow = isolationWindow;
    }

    public IsolationWindow getIsolationWindow() {
        return isolationWindow;
    }

    public void setSelectedIonList(SelectedIonList selectedIonList) {
        selectedIonList.setParent(this);

        this.selectedIonList = selectedIonList;
    }

    public SelectedIonList getSelectedIonList() {
        return selectedIonList;
    }

    public void setActivation(Activation activation) {
        activation.setParent(this);

        this.activation = activation;
    }

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
            attributeText += " spectrumRef=\"" + XMLHelper.ensureSafeXML(spectrumRef) + "\"";
        }
        
        if(attributeText.startsWith(" "))
            attributeText = attributeText.substring(1);
        
        return attributeText;
    }

    @Override
    public String toString() {
        return "precursor: "
                + ((spectrumRef != null && !spectrumRef.isEmpty()) ? " spectrumRef=\"" + spectrumRef + "\"" : "")
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
