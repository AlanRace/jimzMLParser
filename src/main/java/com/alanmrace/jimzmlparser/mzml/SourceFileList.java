package com.alanmrace.jimzmlparser.mzml;

public class SourceFileList extends MzMLIDContentList<SourceFile> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public SourceFileList(int count) {
        super(count);
    }

    public SourceFileList(SourceFileList sourceFileList, ReferenceableParamGroupList rpgList) {
        this(sourceFileList.size());

        for (SourceFile sourceFile : sourceFileList) {
            this.add(new SourceFile(sourceFile, rpgList));
        }
    }

    public void addSourceFile(SourceFile sourceFile) {
        add(sourceFile);
    }

    public SourceFile getSourceFile(String id) {
        return get(id);
    }

    public SourceFile getSourceFile(int index) {
        return get(index);
    }

    @Override
    public String getTagName() {
        return "sourceFileList";
    }
}
