package com.alanmrace.jimzmlparser.mzml;

public class SoftwareRef extends MzMLReference<Software> { //, MutableTreeNode {

    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = 1L;


    public SoftwareRef(Software ref) {
        super(ref);
    }

    @Override
    public String getTagName() {
        return "softwareRef";
    }
}
