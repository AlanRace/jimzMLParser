package com.alanmrace.jimzmlparser.mzml;

public class SelectedIonList extends MzMLContentList<SelectedIon> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public SelectedIonList(int count) {
        super(count);
    }

    public SelectedIonList(SelectedIonList siList, ReferenceableParamGroupList rpgList) {
        this(siList.size());

        for (SelectedIon si : siList) {
            this.add(new SelectedIon(si, rpgList));
        }
    }
    
    public void addSelectedIon(SelectedIon selectedIon) {
        add(selectedIon);
    }

    public SelectedIon getSelectedIon(int index) {
        return get(index);
    }

    @Override
    public String getTagName() {
        return "selectedIonList";
    }
}
