package com.alanmrace.jimzmlparser.mzml;

public class SampleList extends MzMLIDContentList<Sample> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public SampleList(int count) {
        super(count);
    }

    public SampleList(SampleList sampleList, ReferenceableParamGroupList rpgList) {
        this(sampleList.size());

        for (Sample sample : sampleList) {
            add(new Sample(sample, rpgList));
        }
    }

    public Sample getSample(String id) {
        return get(id);
    }

    public Sample getSample(int index) {
        return get(index);
    }

    public void addSample(Sample sample) {
        add(sample);
    }

    public Sample removeSample(int index) {
        return remove(index);
    }

    @Override
    public String getTagName() {
        return "sampleList";
    }
}
