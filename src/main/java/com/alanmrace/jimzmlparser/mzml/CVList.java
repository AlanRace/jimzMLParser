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
    
    /**
     * Add CV. Helper method to retain API, calls 
     * {@link CVList#add(com.alanmrace.jimzmlparser.mzml.MzMLTag)}.
     * 
     * @param cv CV to add to list
     */
    public void addCV(CV cv) {
        add(cv);
    }
    
    /**
     * Returns a CV with a specific unique ID. Helper method to retain API, calls 
     * {@link CVList#get(java.lang.String) }.
     * 
     * @param id Unique ID attribute of the CV
     * @return CV if one exists in the list with the given id, null otherwise.
     */
    public CV getCV(String id) {
        return get(id);
    }
    
    @Override
    public String getTagName() {
        return "cvList";
    }
    
    /**
     * Generate the default content for a CVList. This will include CV elements 
     * which describe the MSI, MS and units ontologies.
     * 
     * @return Default CVList
     */
    public static CVList create() {
        CVList cvList = new CVList(3);
        
        cvList.add(new CV(CV.IMS_URI, "Mass Spectrometry Imaging Ontology", "IMS"));
        cvList.add(new CV(CV.MS_URI, "Proteomics Standards Initiative Mass Spectrometry Ontology", "MS"));
        cvList.add(new CV(CV.UO_URI, "Unit Ontology", "UO"));
        
        return cvList;
    }
}
