package com.alanmrace.jimzmlparser.mzml;

/**
 * Class describing {@literal <sourceFileList>} tag, a list of SourceFiles which
 * make up the (i)mzML file.
 * 
 * @author Alan Race
 */
public class SourceFileList extends MzMLIDContentList<SourceFile> {

    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Create a SourceFileList with the specified capacity.
     * 
     * @param count List capacity
     */
    public SourceFileList(int count) {
        super(count);
    }

    /**
     * Copy constructor.
     * 
     * @param sourceFileList SourceFileList to copy
     * @param rpgList New ReferenceableParamGroupList to match references to
     */
    public SourceFileList(SourceFileList sourceFileList, ReferenceableParamGroupList rpgList) {
        this(sourceFileList.size());

        for (SourceFile sourceFile : sourceFileList) {
            this.add(new SourceFile(sourceFile, rpgList));
        }
    }

    /**
     * Add SourceFile. Helper method to retain API, calls 
     * {@link SourceFileList#add(com.alanmrace.jimzmlparser.mzml.MzMLTag)}.
     * 
     * @param sourceFile SourceFile to add to list
     */
    public void addSourceFile(SourceFile sourceFile) {
        add(sourceFile);
    }

    /**
     * Returns SourceFile with specified ID, or null if none exists. Helper method 
     * to retain API, calls {@link SourceFileList#get(java.lang.String)}.
     * 
     * @param id ID of the SourceFile
     * @return SourceFile if an ID match is found, null otherwise
     */
    public SourceFile getSourceFile(String id) {
        return get(id);
    }

    /**
     * Returns SourceFile at specified index in the list, or null otherwise. 
     * Helper method to retain API, calls {@link SourceFileList#get(int)}.
     * 
     * @param index Index of the SourceFile in the list
     * @return SourceFile if index is valid, null otherwise
     */
    public SourceFile getSourceFile(int index) {
        return get(index);
    }

    @Override
    public String getTagName() {
        return "sourceFileList";
    }
}
