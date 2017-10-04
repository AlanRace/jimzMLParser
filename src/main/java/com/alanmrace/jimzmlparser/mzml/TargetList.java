package com.alanmrace.jimzmlparser.mzml;

/**
 * Class describing {@literal <targetList>} tag, an inclusion (or target) list.
 * 
 * @author Alan Race
 */
public class TargetList extends MzMLContentList<Target> {

    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Create a TargetList with the specified capacity.
     * 
     * @param count List capacity
     */
    public TargetList(int count) {
        super(count);
    }

    /**
     * Copy constructor.
     * 
     * @param targetList TargetList to copy
     * @param rpgList New ReferenceableParamGroupList to match references to
     */
    public TargetList(TargetList targetList, ReferenceableParamGroupList rpgList) {
        this(targetList.size());

        for (Target target : targetList) {
            add(new Target(target, rpgList));
        }
    }

    /**
     * Add Target. Helper method to retain API, calls 
     * {@link TargetList#add(com.alanmrace.jimzmlparser.mzml.MzMLTag)}.
     * 
     * @param target Target to add to list
     */
    public void addTarget(Target target) {
        add(target);
    }

    @Override
    public String getTagName() {
        return "targetList";
    }
}
