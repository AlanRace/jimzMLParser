package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.exceptions.InvalidXPathException;
import com.alanmrace.jimzmlparser.exceptions.UnfollowableXPathException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Class describing the components within a mass spectrometer.
 * 
 * <p>TODO: Change this to use the base component list rather than the 3 lists within
 * this class.
 * 
 * @author Alan Race
 */
public class ComponentList extends MzMLContentList<Component> {

    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Ionisation source list.
     */
    private final List<Source> sources;

    /**
     * Mass analyser list.
     */
    private final List<Analyser> analysers;

    /**
     * Detector list.
     */
    private final List<Detector> detectors;

    /**
     * Create default component list with all component lists empty.
     */
    public ComponentList() {
        sources = new ArrayList<Source>();
        analysers = new ArrayList<Analyser>();
        detectors = new ArrayList<Detector>();
    }

    /**
     * Copy constructor.
     * 
     * @param componentList Component list to copy
     * @param rpgList ReferenceableParamGroupList to match references to
     */
    public ComponentList(ComponentList componentList, ReferenceableParamGroupList rpgList) {
        sources = new ArrayList<Source>(componentList.sources.size());

        for (Source source : componentList.sources) {
            sources.add(new Source(source, rpgList));
        }

        analysers = new ArrayList<Analyser>(componentList.analysers.size());

        for (Analyser analyser : componentList.analysers) {
            analysers.add(new Analyser(analyser, rpgList));
        }

        detectors = new ArrayList<Detector>(componentList.detectors.size());

        for (Detector detector : componentList.detectors) {
            detectors.add(new Detector(detector, rpgList));
        }
    }

    @Override
    public void add(Component component) {
        if(component instanceof Source)
            addSource((Source) component);
        else if(component instanceof Analyser)
            addAnalyser((Analyser) component);
        else if(component instanceof Detector)
            addDetector((Detector) component);
    }
    
    /**
     * Add a source to the source list.
     *
     * @param source Source to add to the list
     */
    public void addSource(Source source) {
        source.setParent(this);

        sources.add(source);
    }
    
    /**
     * Add an analyser to the analyser list.
     *
     * @param analyser Analyser to add to the list
     */
    public void addAnalyser(Analyser analyser) {
        analyser.setParent(this);

        analysers.add(analyser);
    }

    /**
     * Add a detector to the detector list.
     *
     * @param detector Detector to add to the list
     */
    public void addDetector(Detector detector) {
        detector.setParent(this);

        detectors.add(detector);
    }
    
    /**
     * Returns the number of sources in the list.
     * 
     * @return Number of sources
     */
    public int getSourceCount() {
        return sources.size();
    }

    /**
     * Returns the number of analysers in the list.
     * 
     * @return Number of analysers
     */
    public int getAnalyserCount() {
        return analysers.size();
    }

    /**
     * Returns the number of detectors in the list.
     * 
     * @return Number of detectors
     */
    public int getDetectorCount() {
        return detectors.size();
    }

    /**
     * Returns the source at the specified index, null otherwise.
     * 
     * @param index Index in the source list
     * @return Source at the index, null if does not exist
     */
    public Source getSource(int index) {
        return sources.get(index);
    }

    /**
     * Returns the analyser at the specified index, null otherwise.
     * 
     * @param index Index in the analyser list
     * @return Analyser at the index, null if does not exist
     */
    public Analyser getAnalyser(int index) {
        return analysers.get(index);
    }

    /**
     * Returns the detector at the specified index, null otherwise.
     * 
     * @param index Index in the detector list
     * @return Detector at the index, null if does not exist
     */
    public Detector getDetector(int index) {
        return detectors.get(index);
    }

    @Override
    public int size() {
        return sources.size() + analysers.size() + detectors.size();
    }

    @Override
    protected void addTagSpecificElementsAtXPathToCollection(Collection<MzMLTag> elements, String fullXPath, String currentXPath) throws InvalidXPathException {
        if (currentXPath.startsWith("/source")) {
            if (sources == null) {
                throw new UnfollowableXPathException("No source exists, so cannot go to " + fullXPath, fullXPath, currentXPath);
            }

            for (Source source : sources) {
                source.addElementsAtXPathToCollection(elements, fullXPath, currentXPath);
            }
        } else if (currentXPath.startsWith("/analyzer")) {
            if (analysers == null) {
                throw new UnfollowableXPathException("No analyzer exists, so cannot go to " + fullXPath, fullXPath, currentXPath);
            }

            for (Analyser analyser : analysers) {
                analyser.addElementsAtXPathToCollection(elements, fullXPath, currentXPath);
            }
        } else if (currentXPath.startsWith("/detector")) {
            if (detectors == null) {
                throw new UnfollowableXPathException("No detector exists, so cannot go to " + fullXPath, fullXPath, currentXPath);
            }

            for (Detector detector : detectors) {
                detector.addElementsAtXPathToCollection(elements, fullXPath, currentXPath);
            }
        }
    }

//    @Override
//    protected void outputXMLContent(MzMLWritable output, int indent) throws IOException {
//        int order = 1;
//        for (Source source : sources) {
//            source.outputXML(output, indent + 1, order++);
//        }
//
//        for (Analyser analyser : analysers) {
//            analyser.outputXML(output, indent + 1, order++);
//        }
//
//        for (Detector detector : detectors) {
//            detector.outputXML(output, indent + 1, order++);
//        }
//    }

    @Override
    public String getTagName() {
        return "componentList";
    }
    
    @Override
    public void addChildrenToCollection(Collection<MzMLTag> children) {
        if(sources != null)
            children.addAll(sources);
        if(analysers != null)
            children.addAll(analysers);
        if(detectors != null)
            children.addAll(detectors);
    }
}
