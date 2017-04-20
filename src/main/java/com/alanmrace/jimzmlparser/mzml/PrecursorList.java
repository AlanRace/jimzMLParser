package com.alanmrace.jimzmlparser.mzml;

/**
 * Class describing {@literal <precursorList>} tag.
 * 
 * @author Alan Race
 */
public class PrecursorList extends MzMLContentList<Precursor> {

    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = 1L;


    /**
     * Create an empty list with specified capacity.
     * 
     * @param count Capacity
     */
    public PrecursorList(int count) {
        super(count);
    }

    /**
     * Copy constructor.
     *
     * @param precursorList Old PrecursorList to copy
     * @param rpgList New ReferenceableParamGroupList to match references to
     * @param sourceFileList New SourceFileList to match references to
     */
    public PrecursorList(PrecursorList precursorList, ReferenceableParamGroupList rpgList, SourceFileList sourceFileList) {
        this(precursorList.size());

        for (Precursor precursor : precursorList) {
            this.add(new Precursor(precursor, rpgList, sourceFileList));
        }
    }
    
    /**
     * Add Precursor. Helper method to retain API, calls 
     * {@link PrecursorList#add(com.alanmrace.jimzmlparser.mzml.MzMLTag)}.
     * 
     * @param precursor Precursor to add to list
     */
    public void addPrecursor(Precursor precursor) {
        add(precursor);
    }
    
    /**
     * Returns Precursor at specified index in list. Helper method to retain 
     * API, calls {@link PrecursorList#get(int)}.
     * 
     * @param index Index in the list
     * @return Precursor ion at index, or null if none exists
     */
    public Precursor getPrecursor(int index) {
        return get(index);
    }

    @Override
    public String getTagName() {
        return "precursorList";
    }
}
