package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.exceptions.InvalidXPathException;
import com.alanmrace.jimzmlparser.exceptions.UnfollowableXPathException;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class ComponentList extends MzMLContent implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private ArrayList<Source> sources;
    private ArrayList<Analyser> analysers;
    private ArrayList<Detector> detectors;

    public ComponentList() {
        sources = new ArrayList<Source>();
        analysers = new ArrayList<Analyser>();
        detectors = new ArrayList<Detector>();
    }

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

    public void addSource(Source source) {
        source.setParent(this);

        sources.add(source);
    }

    public void addAnalyser(Analyser analyser) {
        analyser.setParent(this);

        analysers.add(analyser);
    }

    public void addDetector(Detector detector) {
        detector.setParent(this);

        detectors.add(detector);
    }

    public int getSourceCount() {
        return sources.size();
    }

    public int getAnalyserCount() {
        return analysers.size();
    }

    public int getDetectorCount() {
        return detectors.size();
    }

    public Source getSource(int index) {
        return sources.get(index);
    }

    public Analyser getAnalyser(int index) {
        return analysers.get(index);
    }

    public Detector getDetector(int index) {
        return detectors.get(index);
    }

    public int size() {
        return sources.size() + analysers.size() + detectors.size();
    }

    @Override
    protected void addTagSpecificElementsAtXPathToCollection(Collection<MzMLContent> elements, String fullXPath, String currentXPath) throws InvalidXPathException {
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

    @Override
    public void outputXML(BufferedWriter output, int indent) throws IOException {
        MzMLContent.indent(output, indent);
        output.write("<" + getTagName());
        output.write(" count=\"" + size() + "\"");
        output.write(">\n");

        int order = 1;
        for (Source source : sources) {
            source.outputXML(output, indent + 1, order++);
        }

        for (Analyser analyser : analysers) {
            analyser.outputXML(output, indent + 1, order++);
        }

        for (Detector detector : detectors) {
            detector.outputXML(output, indent + 1, order++);
        }

        MzMLContent.indent(output, indent);
        output.write("</" + getTagName() + ">\n");
    }

    @Override
    public String toString() {
        return "componentList";
    }

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
        
        super.addChildrenToCollection(children);
    }
}
