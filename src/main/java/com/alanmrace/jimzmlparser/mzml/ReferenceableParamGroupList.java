package com.alanmrace.jimzmlparser.mzml;

public class ReferenceableParamGroupList extends MzMLContentList<ReferenceableParamGroup> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public ReferenceableParamGroupList(int count) {
        super(count);
    }

    public ReferenceableParamGroupList(ReferenceableParamGroupList rpgList) {
        this(rpgList.size());

        for (ReferenceableParamGroup rpg : rpgList) {
            add(new ReferenceableParamGroup(rpg));
        }
    }

    public void addReferenceableParamGroup(ReferenceableParamGroup rpg) {
        add(rpg);
    }
    
    @Override
    public void add(ReferenceableParamGroup rpg) {
        boolean exists = false;

        for (ReferenceableParamGroup currentRPG : list) {
            if (currentRPG.getID().equals(rpg.getID())) {
                exists = true;
                break;
            }
        }

        // TODO: Consider throwing an error if we try and add the same one twice?
        if (!exists) {
            rpg.setParent(this);
            list.add(rpg);
        }
    }

    public ReferenceableParamGroup getReferenceableParamGroup(String id) {
        return get(id);
    }
    
    public ReferenceableParamGroup get(String id) {
        for (ReferenceableParamGroup rpg : list) {
            if (rpg.getID().equals(id)) {
                return rpg;
            }
        }

        return null;
    }

    public ReferenceableParamGroup getReferenceableParamGroup(int index) {
        return get(index);
    }

    @Override
    public String getTagName() {
        return "referenceableParamGroupList";
    }
}
