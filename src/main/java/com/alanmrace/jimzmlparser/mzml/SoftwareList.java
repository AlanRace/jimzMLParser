package com.alanmrace.jimzmlparser.mzml;

public class SoftwareList extends MzMLIDContentList<Software> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public SoftwareList(int count) {
        super(count);
    }

    public SoftwareList(SoftwareList softwareList, ReferenceableParamGroupList rpgList) {
        this(softwareList.size());

        for (Software software : softwareList) {
            this.add(new Software(software, rpgList));
        }
    }

    public void addSoftware(Software software) {
        add(software);
    }

    public Software getSoftware(String id) {
        return get(id);
    }

    public Software getSoftware(int index) {
        return get(index);
    }

    public void removeSoftware(int index) {
        remove(index);
    }

    @Override
    public String getTagName() {
        return "softwareList";
    }
    
    public static SoftwareList create() {
        SoftwareList softwareList = new SoftwareList(1);
        
        softwareList.add(Software.create());
        
        return softwareList;
    }
}
