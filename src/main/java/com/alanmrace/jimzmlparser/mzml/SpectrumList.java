package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.util.XMLHelper;
import java.util.HashMap;
import java.util.Map;

/**
 * Class describing the {@literal <spectrumList>} tag in MzML. Stores all Spectrum instances
 * which constitute the experiment.
 * 
 * @author Alan Race
 */
public class SpectrumList extends MzMLIDContentList<Spectrum> {

    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Store a map of all spectra to make recalling a spectrum from the ID much faster.
     */
    private Map<String, Spectrum> spectrumMap;

    /**
     * Default DataProcessing describing the process(es) used to generate the spectra
     * within the list.
     */
    private DataProcessing defaultDataProcessingRef;
    
    /**
     * DataProcessingList used to ensure that references are kept up to date.
     */
    private ReferenceList<DataProcessing> dataProcessingList;

    /**
     * Create a {@literal <spectrumList>} tag with the specified size.
     * 
     * @param count Number of spectra within the list.
     */
    protected SpectrumList(int count) {
        super(count);
        
        spectrumMap = new HashMap<String, Spectrum>(count);
    }
    
    /**
     * Create a {@literal <spectrumList>} tag with the specified size and default
     * {@link DataProcessing}.
     * 
     * @param count Number of spectra within the list.
     * @param defaultDataProcessingRef Default description of processing applied to the spectra in the list.
     */
    public SpectrumList(int count, DataProcessing defaultDataProcessingRef) {
        this(count);
        
        this.defaultDataProcessingRef = defaultDataProcessingRef;
    }

    /**
     * Copy constructor.
     * 
     * @param spectrumList SpectrumList to copy
     * @param rpgList New ReferenceableParamGroupList used to link old references to
     * @param dpList New DataProcessingList used to link old references to
     * @param sourceFileList New SourceFileList used to link old references to
     * @param icList New InstrumentConfigurationList used to link old references to
     */
    public SpectrumList(SpectrumList spectrumList, ReferenceableParamGroupList rpgList, DataProcessingList dpList,
            SourceFileList sourceFileList, InstrumentConfigurationList icList) {
        this(spectrumList.size());

        for (Spectrum spectrum : spectrumList) {
            Spectrum newSpectrum = new Spectrum(spectrum, rpgList, dpList, sourceFileList, icList);

            add(newSpectrum);
            this.spectrumMap.put(newSpectrum.getID(), newSpectrum);
        }

        if (spectrumList.defaultDataProcessingRef != null && dpList != null) {
            for (DataProcessing dp : dpList) {
                if (spectrumList.defaultDataProcessingRef.getID().equals(dp.getID())) {
                    defaultDataProcessingRef = dp;
                }
            }
        }
    }

    /**
     * Set the default DataProcessing description of applied processing to spectra
     * in the list.
     * 
     * @param dp Default DataProcessing
     */
    public void setDefaultDataProcessingRef(DataProcessing dp) {
        this.defaultDataProcessingRef = dp;
        
        ensureValidReferences();
    }

    /**
     * Returns current default DataProcessing.
     * 
     * @return Current default DataProcessing
     */
    public DataProcessing getDefaultDataProcessingRef() {
        return defaultDataProcessingRef;
    }
    
    /**
     * Sets the DataProcessingList which should be kept up to date with all DataProcessing
     * references. This also sets the DataProcessingList in each Spectrum in the list.
     * 
     * @param dataProcessingList List to be kept up to date
     */
    protected void setDataProcessingList(ReferenceList<DataProcessing> dataProcessingList) {
        this.dataProcessingList = dataProcessingList;
        
        for(Spectrum spectrum : this)
            spectrum.setDataProcessingList(dataProcessingList);
    }

    @Override
    public void add(Spectrum spectrum) {
        super.add(spectrum);
        
        if(dataProcessingList != null) {
            spectrum.setDataProcessingList(dataProcessingList);
        }
        
        spectrumMap.put(spectrum.getID(), spectrum);
    }
    
    /**
     * Calls add(Spectrum spectrum). Kept to retain previous API.
     * 
     * @param spectrum Spectrum to add
     */
    public void addSpectrum(Spectrum spectrum) {
        add(spectrum);
    }

    /**
     * Calls get(int index). Kept to retain previous API.
     * 
     * @param index Index of spectrum to retrieve
     * @return Spectrum if one exists at index, null otherwise
     */
    public Spectrum getSpectrum(int index) {
        return get(index);
    }

    @Override
    public Spectrum get(String id) {
        return spectrumMap.get(id);
    }
    
    /**
     * Calls get(String id). Kept to retain previous API.
     * 
     * @param id Unique ID for the spectrum to retrieve
     * @return Spectrum if one found, or null otherwise
     */
    public Spectrum getSpectrum(String id) {
        return get(id);
    }

    @Override
    public boolean remove(Spectrum spectrum) {
        boolean success = true;
        
        success &= list.remove(spectrum);
        
        Spectrum removedSpectrum = spectrumMap.remove(spectrum.getID());
        
        // Below method only included in Java 1.8
//        spectrumMap.remove(spectrum.getID(), spectrum);
        
        return success && (removedSpectrum.equals(spectrum));
    }

    /**
     * Calls remove(Spectrum spectrum). Kept to retain previous API.
     * 
     * @param spectrum Spectrum to remove from the list.
     * @return true if successful, false otherwise
     */
    public boolean removeSpectrum(Spectrum spectrum) {
        return remove(spectrum);
    }
    
    @Override
    public String getXMLAttributeText() {
        return super.getXMLAttributeText() + " defaultDataProcessingRef=\"" + XMLHelper.ensureSafeXML(defaultDataProcessingRef.getID()) + "\""; 
    }

    @Override
    public String getTagName() {
        return "spectrumList";
    }
    
    @Override
    public void ensureValidReferences() {
        if(dataProcessingList != null)
            defaultDataProcessingRef = dataProcessingList.getValidReference(defaultDataProcessingRef);
    }
}
