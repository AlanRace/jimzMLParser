package com.alanmrace.jimzmlparser.mzML;

import com.alanmrace.jimzmlparser.exceptions.InvalidXPathException;
import com.alanmrace.jimzmlparser.exceptions.UnfollowableXPathException;
import com.alanmrace.jimzmlparser.util.XMLHelper;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SpectrumList extends MzMLContent implements Iterable<Spectrum>, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    // Store a map of all spectra to make recalling a spectrum from the ID much faster
    private Map<String, Spectrum> spectrumMap;
    private ArrayList<Spectrum> spectrumList;
    private DataProcessing defaultDataProcessingRef;

    public SpectrumList(int count, DataProcessing defaultDataProcessingRef) {
        spectrumList = new ArrayList<Spectrum>(count);
        spectrumMap = new HashMap<String, Spectrum>(spectrumList.size());
        this.defaultDataProcessingRef = defaultDataProcessingRef;
    }

    public SpectrumList(SpectrumList spectrumList, ReferenceableParamGroupList rpgList, DataProcessingList dpList,
            SourceFileList sourceFileList, InstrumentConfigurationList icList) {
        this.spectrumList = new ArrayList<Spectrum>(spectrumList.size());
        this.spectrumMap = new HashMap<String, Spectrum>(spectrumList.size());

        for (Spectrum spectrum : spectrumList) {
            Spectrum newSpectrum = new Spectrum(spectrum, rpgList, dpList, sourceFileList, icList);

            this.spectrumList.add(newSpectrum);
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

    public int size() {
        return spectrumList.size();
    }

    public void addSpectrum(Spectrum spectrum) {
        spectrum.setParent(this);

        spectrumList.add(spectrum);
        spectrumMap.put(spectrum.getID(), spectrum);
    }

    public Spectrum getSpectrum(int index) {
        if (index >= spectrumList.size()) {
            return null;
        }

        return spectrumList.get(index);
    }

    public Spectrum getSpectrum(String id) {
//            for(Spectrum spectrum : spectrumList)
//                if (spectrum.getID().equals(id)) {
//                    return spectrum;
//                }
//            
//            return null;
        return spectrumMap.get(id);
    }

    public void removeSpectrum(Spectrum spectrum) {
        spectrumList.remove(spectrum);
        spectrumMap.remove(spectrum.getID(), spectrum);
    }

    @Override
    protected Collection<MzMLContent> getTagSpecificElementsAtXPath(String fullXPath, String currentXPath) throws InvalidXPathException {
        ArrayList<MzMLContent> elements = new ArrayList<MzMLContent>();

        if (currentXPath.startsWith("/spectrum")) {
            if (spectrumList == null) {
                throw new UnfollowableXPathException("No spectrumList exists, so cannot go to " + fullXPath);
            }

            for (Spectrum spectrum : spectrumList) {
                elements.addAll(spectrum.getElementsAtXPath(fullXPath, currentXPath));
            }

            return elements;
        }

        return elements;
    }

    @Override
    public void outputXML(BufferedWriter output, int indent) throws IOException {
        MzMLContent.indent(output, indent);
        output.write("<spectrumList");
        output.write(" count=\"" + spectrumList.size() + "\"");
        output.write(" defaultDataProcessingRef=\"" + XMLHelper.ensureSafeXML(defaultDataProcessingRef.getID()) + "\"");
        output.write(">\n");

        int index = 0;

        for (Spectrum spectrum : spectrumList) {
            spectrum.outputXML(output, indent + 1, index++);
        }

        MzMLContent.indent(output, indent);
        output.write("</spectrumList>\n");
    }

    @Override
    public Iterator<Spectrum> iterator() {
        return spectrumList.iterator();
    }

    @Override
    public String toString() {
        return "spectrumList: defaultDataProcessingRef=\"" + defaultDataProcessingRef.getID() + "\"";
    }

    @Override
    public String getTagName() {
        return "spectrumList";
    }
}
