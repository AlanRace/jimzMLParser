package com.alanmrace.jimzmlparser.mzml;

public class TargetList extends MzMLContentList<Target> {

    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = 1L;

    public TargetList(int count) {
        super(count);
    }

    public TargetList(TargetList targetList, ReferenceableParamGroupList rpgList) {
        this(targetList.size());

        for (Target target : targetList) {
            add(new Target(target, rpgList));
        }
    }

    public void addTarget(Target target) {
        add(target);
    }

    @Override
    public String getTagName() {
        return "targetList";
    }
}
