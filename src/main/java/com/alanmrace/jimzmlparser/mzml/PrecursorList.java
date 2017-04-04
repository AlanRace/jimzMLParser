package com.alanmrace.jimzmlparser.mzml;

public class PrecursorList extends MzMLContentList<Precursor> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;


    public PrecursorList(int count) {
        super(count);
    }

    public PrecursorList(PrecursorList precursorList, ReferenceableParamGroupList rpgList, SourceFileList sourceFileList) {
        this(precursorList.size());

        for (Precursor precursor : precursorList) {
            this.add(new Precursor(precursor, rpgList, sourceFileList));
        }
    }
    
    public void addPrecursor(Precursor ic) {
        add(ic);
    }
    
    public Precursor getPrecursor(int index) {
        return get(index);
    }

    @Override
    public String getTagName() {
        return "precursorList";
    }
}
