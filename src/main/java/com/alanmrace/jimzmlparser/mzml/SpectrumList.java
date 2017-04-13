package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.listener.DataProcessingListener;
import com.alanmrace.jimzmlparser.util.XMLHelper;
import java.util.HashMap;
import java.util.Map;

public class SpectrumList extends MzMLIDContentList<Spectrum> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    // Store a map of all spectra to make recalling a spectrum from the ID much faster
    private Map<String, Spectrum> spectrumMap;
    private DataProcessing defaultDataProcessingRef;
    
    private DataProcessingListener dataProcessingListener;

    protected SpectrumList(int count) {
        super(count);
        
        spectrumMap = new HashMap<String, Spectrum>(count);
    }
    
    public SpectrumList(int count, DataProcessing defaultDataProcessingRef) {
        this(count);
        
        this.defaultDataProcessingRef = defaultDataProcessingRef;
    }

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

    public void setDefaultDataProcessingRef(DataProcessing dp) {
        this.defaultDataProcessingRef = dp;
    }

    public DataProcessing getDefaultDataProcessingRef() {
        return defaultDataProcessingRef;
    }
    
    protected void setDataProcessingListener(DataProcessingListener dataProcessingListener) {
        this.dataProcessingListener = dataProcessingListener;
        
        for(Spectrum spectrum : this)
            spectrum.setDataProcessingListener(dataProcessingListener);
    }

    @Override
    public void add(Spectrum spectrum) {
        super.add(spectrum);
        
        if(dataProcessingListener != null) {
            spectrum.setDataProcessingListener(dataProcessingListener);
            dataProcessingListener.referenceCheck(spectrum.getDataProcessingRef());
        }
        
        spectrumMap.put(spectrum.getID(), spectrum);
    }
    
    public void addSpectrum(Spectrum spectrum) {
        add(spectrum);
    }

    public Spectrum getSpectrum(int index) {
        return get(index);
    }

    public Spectrum getSpectrum(String id) {
        return spectrumMap.get(id);
    }

    public void removeSpectrum(Spectrum spectrum) {
        list.remove(spectrum);
        
        spectrumMap.remove(spectrum.getID());
        // Below method only included in Java 1.8
//        spectrumMap.remove(spectrum.getID(), spectrum);
    }

    
    @Override
    protected String getXMLAttributeText() {
        return super.getXMLAttributeText() + " defaultDataProcessingRef=\"" + XMLHelper.ensureSafeXML(defaultDataProcessingRef.getID()) + "\""; 
    }

    @Override
    public String getTagName() {
        return "spectrumList";
    }
}
