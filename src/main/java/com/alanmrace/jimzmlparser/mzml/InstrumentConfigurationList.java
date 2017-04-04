package com.alanmrace.jimzmlparser.mzml;

public class InstrumentConfigurationList extends MzMLIDContentList<InstrumentConfiguration> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;


    public InstrumentConfigurationList(int count) {
        super(count);
    }

    public InstrumentConfigurationList(InstrumentConfigurationList icList, ReferenceableParamGroupList rpgList, ScanSettingsList ssList, SoftwareList softwareList) {
        this(icList.size());

        for (InstrumentConfiguration ic : icList) {
            this.add(new InstrumentConfiguration(ic, rpgList, ssList, softwareList));
        }
    }
    
    public void addInstrumentConfiguration(InstrumentConfiguration ic) {
        add(ic);
    }
    
    public InstrumentConfiguration getInstrumentConfiguration(int index) {
        return get(index);
    }
    
    public InstrumentConfiguration getInstrumentConfiguration(String id) {
        return get(id);
    }

    @Override
    public String getTagName() {
        return "instrumentConfigurationList";
    }
}
