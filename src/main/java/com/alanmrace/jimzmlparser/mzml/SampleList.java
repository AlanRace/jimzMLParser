package com.alanmrace.jimzmlparser.mzml;

/**
 * Class describing {@literal <sampleList>} tag.
 * 
 * @author Alan Race
 */
public class SampleList extends MzMLIDContentList<Sample> {

    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Create an empty list with specified capacity.
     * 
     * @param count Capacity
     */
    public SampleList(int count) {
        super(count);
    }

    /**
     * Copy constructor.
     *
     * @param sampleList Old SampleList to copy
     * @param rpgList New ReferenceableParamGroupList to match references to
     */
    public SampleList(SampleList sampleList, ReferenceableParamGroupList rpgList) {
        this(sampleList.size());

        for (Sample sample : sampleList) {
            add(new Sample(sample, rpgList));
        }
    }

    /**
     * Add Sample. Helper method to retain API, calls 
     * {@link SampleList#add(com.alanmrace.jimzmlparser.mzml.MzMLTag)}.
     * 
     * @param sample Sample to add to list
     */
    public void addSample(Sample sample) {
        add(sample);
    }

    /**
     * Returns Sample at specified index in list. Helper method to retain 
     * API, calls {@link SampleList#get(int)}.
     * 
     * @param index Index in the list
     * @return Sample at index, or null if none exists
     */
    public Sample getSample(int index) {
        return get(index);
    }
    
    /**
     * Returns Sample in the list with the specified ID. Helper 
     * method to retain API, calls {@link SampleList#get(java.lang.String)}.
     * 
     * @param id Unique ID
     * @return Sample with ID, or null if none exists
     */
    public Sample getSample(String id) {
        return get(id);
    }

    /**
     * Removes Sample at specified index from the list. Helper method to retain 
     * API, calls {@link SampleList#remove(int)}.
     * 
     * @param index Index in the list
     * @return Removed Sample, or null if none exists
     */
    public Sample removeSample(int index) {
        return remove(index);
    }

    @Override
    public String getTagName() {
        return "sampleList";
    }
}
