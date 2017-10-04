package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.obo.OBO;

/**
 * Class describing {@literal <instrumentConfigurationList>} tag.
 * 
 * @author Alan Race
 */
public class InstrumentConfigurationList extends MzMLIDContentList<InstrumentConfiguration> {

    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Create an empty list with specified capacity.
     * 
     * @param count Capacity
     */
    public InstrumentConfigurationList(int count) {
        super(count);
    }

    /**
     * Copy constructor.
     *
     * @param icList Old InstrumentConfigurationList to copy
     * @param rpgList New ReferenceableParamGroupList to match references to
     * @param ssList New ScanSettingsList to match references to
     * @param softwareList New SoftwareList to match references to
     */
    public InstrumentConfigurationList(InstrumentConfigurationList icList, ReferenceableParamGroupList rpgList, ScanSettingsList ssList, SoftwareList softwareList) {
        this(icList.size());

        for (InstrumentConfiguration ic : icList) {
            this.add(new InstrumentConfiguration(ic, rpgList, ssList, softwareList));
        }
    }
    
    /**
     * Add InstrumentConfiguration. Helper method to retain API, calls 
     * {@link InstrumentConfigurationList#add(com.alanmrace.jimzmlparser.mzml.MzMLTag)}.
     * 
     * @param ic InstrumentConfiguration to add to list
     */
    public void addInstrumentConfiguration(InstrumentConfiguration ic) {
        add(ic);
    }
    
    /**
     * Returns InstrumentConfiguration at specified index in list. Helper method to retain 
     * API, calls {@link InstrumentConfigurationList#get(int)}.
     * 
     * @param index Index in the list
     * @return InstrumentConfiguration at index, or null if none exists
     */
    public InstrumentConfiguration getInstrumentConfiguration(int index) {
        return get(index);
    }
    
    /**
     * Returns InstrumentConfiguration in the list with the specified ID. Helper 
     * method to retain API, calls {@link InstrumentConfigurationList#get(java.lang.String)}.
     * 
     * @param id Unique ID
     * @return InstrumentConfiguration with ID, or null if none exists
     */
    public InstrumentConfiguration getInstrumentConfiguration(String id) {
        return get(id);
    }

    @Override
    public String getTagName() {
        return "instrumentConfigurationList";
    }
    
    /**
     * Create default valid InstrumentConfiguration List. This contains a single
     * InstrumentConfiguration with the reference 'instrumentConfiguration', which
     * has solely the term {@link InstrumentConfiguration#instrumentModelID}.
     * 
     * @return Valid InstrumentConfigurationList
     */
    public static InstrumentConfigurationList create() {
        InstrumentConfigurationList icList = new InstrumentConfigurationList(1);
        
        InstrumentConfiguration ic = new InstrumentConfiguration("instrumentConfiguration");
        icList.add(ic);
        
        ic.addCVParam(new EmptyCVParam(OBO.getOBO().getTerm(InstrumentConfiguration.instrumentModelID)));
        
        return icList;
    }
}
