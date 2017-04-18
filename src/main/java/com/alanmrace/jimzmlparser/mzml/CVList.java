package com.alanmrace.jimzmlparser.mzml;

/**
 * CVList tag ({@literal <cvList>}). A list of {@link CV} tags.
 * 
 * @author Alan Race
 * 
 * @see CV
 */
public class CVList extends MzMLIDContentList<CV> {

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
    
    public void addCV(CV cv) {
        add(cv);
    }
    
    public CV getCV(String id) {
        return get(id);
    }
    
    @Override
    public String getTagName() {
        return "cvList";
    }
    
    public static CVList create() {
        CVList cvList = new CVList(3);
        
        cvList.add(new CV(CV.IMS_URI, "Mass Spectrometry Imaging Ontology", "IMS"));
        cvList.add(new CV(CV.MS_URI, "Proteomics Standards Initiative Mass Spectrometry Ontology", "MS"));
        cvList.add(new CV(CV.UO_URI, "Unit Ontology", "UO"));
        
        return cvList;
    }
}
