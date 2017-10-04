package com.alanmrace.jimzmlparser.mzml;

/**
 * Class for {@literal <sourceFileRefList>} tag, which defines a list of 
 * references to SourceFiles.
 * 
 * @author Alan Race
 */
public class SourceFileRefList extends MzMLContentList<SourceFileRef> {

    /**
     * Serialisation version id.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Create a SourceFileRefList with the specified capacity.
     * 
     * @param count List capacity
     */
    public SourceFileRefList(int count) {
        super(count);
    }

    /**
     * Copy constructor.
     * 
     * @param sourceFileRefList SourceFileRefList to copy
     * @param sourceFileList New SourceFileList to match references to
     */
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

    /**
     * Add SourceFileRef. Helper method to retain API, calls 
     * {@link SourceFileRefList#add(com.alanmrace.jimzmlparser.mzml.MzMLTag)}.
     * 
     * @param sourceFileRef SourceFileRef to add
     */
    public void addSourceFileRef(SourceFileRef sourceFileRef) {
        add(sourceFileRef);
    }

    @Override
    public String getTagName() {
        return "sourceFileRefList";
    }
}
