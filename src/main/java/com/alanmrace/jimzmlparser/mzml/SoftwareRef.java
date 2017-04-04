package com.alanmrace.jimzmlparser.mzml;

public class SoftwareRef extends MzMLReference<Software> { //, MutableTreeNode {

    /**
     *
     */
    private static final long serialVersionUID = 1L;


    public SoftwareRef(Software ref) {
        super(ref);
    }

    @Override
    public String getTagName() {
        return "softwareRef";
    }

    @Override
    public void setParent(MzMLTag parent) {
        // This is a dummy function only included to allow the removal
    }
}
