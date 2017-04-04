package com.alanmrace.jimzmlparser.mzml;

public class SourceFileRef extends MzMLReference<SourceFile> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;


    public SourceFileRef(SourceFile ref) {
        super(ref);
    }

    @Override
    public String getTagName() {
        return "sourceFileRef";
    }
    
    @Override
    public void setParent(MzMLTag parent) {
        // This is a dummy function only included to allow the removal
    }
}
