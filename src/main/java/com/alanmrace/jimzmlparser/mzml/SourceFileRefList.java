package com.alanmrace.jimzmlparser.mzml;

public class SourceFileRefList extends MzMLContentList<SourceFileRef> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public SourceFileRefList(int count) {
        super(count);
    }

    public SourceFileRefList(SourceFileRefList sourceFileRefList, SourceFileList sourceFileList) {
        this(sourceFileRefList.size());

        for (SourceFileRef ref : sourceFileRefList) {
            for (SourceFile sourceFile : sourceFileList) {
                if (ref.getReference().getID().equals(sourceFile.getID())) {
                    add(new SourceFileRef(sourceFile));
                }
            }
        }
    }
    public void addSourceFileRef(SourceFileRef sourceFileRef) {
        add(sourceFileRef);
    }

    @Override
    public String getTagName() {
        return "sourceFileRefList";
    }
}
