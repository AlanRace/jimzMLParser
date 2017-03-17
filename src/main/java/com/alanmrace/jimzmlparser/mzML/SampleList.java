package com.alanmrace.jimzmlparser.mzML;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class SampleList extends MzMLContent implements Iterable<Sample>, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private ArrayList<Sample> sampleList;

    public SampleList(int count) {
        sampleList = new ArrayList<Sample>(count);
    }

    public SampleList(SampleList sampleList, ReferenceableParamGroupList rpgList) {
        this.sampleList = new ArrayList<Sample>(sampleList.size());

        for (Sample sample : sampleList) {
            this.sampleList.add(new Sample(sample, rpgList));
        }
    }

    public int size() {
        return sampleList.size();
    }

    public Sample getSample(String id) {
        for (Sample sample : sampleList) {
            if (sample.getID().equals(id)) {
                return sample;
            }
        }

        return null;
    }

    public Sample getSample(int index) {
        return sampleList.get(index);
    }

    public void addSample(Sample sample) {
        sample.setParent(this);

        sampleList.add(sample);
    }

    public void removeSample(int index) {
        Sample removed = sampleList.remove(index);
        removed.setParent(null);
    }

    @Override
    public void outputXML(BufferedWriter output, int indent) throws IOException {
        MzMLContent.indent(output, indent);
        output.write("<sampleList");
        output.write(" count=\"" + sampleList.size() + "\"");
        output.write(">\n");

        for (Sample sample : sampleList) {
            sample.outputXML(output, indent + 1);
        }

        MzMLContent.indent(output, indent);
        output.write("</sampleList>\n");
    }

    @Override
    public Iterator<Sample> iterator() {
        return sampleList.iterator();
    }

    @Override
    public String toString() {
        return "sampleList";
    }

    @Override
    public String getTagName() {
        return "sampleList";
    }
    
    @Override
    public void addChildrenToCollection(Collection<MzMLTag> children) {
        if(sampleList != null)
            children.addAll(sampleList);
        
        super.addChildrenToCollection(children);
    }
}
