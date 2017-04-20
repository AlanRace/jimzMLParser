package com.alanmrace.jimzmlparser.mzml;

/**
 * Class describing a list of selected ions.
 * 
 * @author Alan Race
 */
public class SelectedIonList extends MzMLContentList<SelectedIon> {

    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Create an empty list with specified capacity.
     * 
     * @param count Capacity
     */
    public SelectedIonList(int count) {
        super(count);
    }

    /**
     * Copy constructor.
     *
     * @param siList Old SelectedIonList to copy
     * @param rpgList New ReferenceableParamGroupList to match references to
     */
    public SelectedIonList(SelectedIonList siList, ReferenceableParamGroupList rpgList) {
        this(siList.size());

        for (SelectedIon si : siList) {
            this.add(new SelectedIon(si, rpgList));
        }
    }
    
    /**
     * Add SelectedIon. Helper method to retain API, calls 
     * {@link SelectedIonList#add(com.alanmrace.jimzmlparser.mzml.MzMLTag)}.
     * 
     * @param selectedIon SelectedIon to add to list
     */
    public void addSelectedIon(SelectedIon selectedIon) {
        add(selectedIon);
    }

    /**
     * Returns SelectedIon at specified index in list. Helper method to retain 
     * API, calls {@link SelectedIonList#get(int)}.
     * 
     * @param index Index in the list
     * @return Selected ion at index, or null if none exists
     */
    public SelectedIon getSelectedIon(int index) {
        return get(index);
    }

    @Override
    public String getTagName() {
        return "selectedIonList";
    }
}
