package com.alanmrace.jimzmlparser.mzml;

/**
 * Class for {@literal <sourceFileRef>} tag, which defines a reference to a 
 * SourceFile.
 * 
 * @author Alan Race
 */
public class SourceFileRef extends MzMLReference<SourceFile> {

    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Create a SourceFileRef with the supplied SourceFile as the reference.
     * 
     * @param ref SourceFile reference
     */
    public SourceFileRef(SourceFile ref) {
        super(ref);
    }

    @Override
    public String getTagName() {
        return "sourceFileRef";
    }
}
