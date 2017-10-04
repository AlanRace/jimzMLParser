package com.alanmrace.jimzmlparser.mzml;

/**
 * Class for {@literal <softwareRef>} tag, which defines a reference to a 
 * Software.
 * 
 * @author Alan Race
 */
public class SoftwareRef extends MzMLReference<Software> { //, MutableTreeNode {

    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Create a SoftwareRef with the supplied Software as the reference.
     * 
     * @param ref Software reference
     */
    public SoftwareRef(Software ref) {
        super(ref);
    }

    @Override
    public String getTagName() {
        return "softwareRef";
    }
}
