package com.alanmrace.jimzmlparser.mzml;

/**
 * CVList tag ({@literal <cvList>}). A list of {@link CV} tags.
 * 
 * @author Alan Race
 * 
 * @see CV
 */
public class CVList extends MzMLContentList<CV> {

    /**
     * Create an empty CVList with the specified initial capacity.
     *
     * @param count Initial capacity
     */
    public CVList(int count) {
        super(count);
    }

    /**
     * Copy constructor for list, shallow copy.
     *
     * @param cvList CVList to copy
     */
    public CVList(CVList cvList) {
        super(cvList);
    }
    
    @Override
    public String getTagName() {
        return "cvList";
    }
}
