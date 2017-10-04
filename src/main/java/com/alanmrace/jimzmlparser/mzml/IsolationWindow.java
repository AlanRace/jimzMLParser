package com.alanmrace.jimzmlparser.mzml;

/**
 * Class describing the isolation (or selection) window configuration used to 
 * isolated one or more ions.
 * 
 * @author Alan Race
 */
public class IsolationWindow extends MzMLContentWithParams {

    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Accession: Isolation window (MS:1000792). MUST supply a child
     */
    public static String isolationWindowAttributeID = "MS:1000792"; 

    /**
     * Create empty IsolationWindow.
     */
    public IsolationWindow() {
        super();
    }

    /**
     * Copy constructor.
     *
     * @param isolationWindow Old IsolationWindow to copy
     * @param rpgList New ReferenceableParamGroupList to match references to
     */
    public IsolationWindow(IsolationWindow isolationWindow, ReferenceableParamGroupList rpgList) {
        super(isolationWindow, rpgList);
    }

    @Override
    public String getTagName() {
        return "isolationWindow";
    }
}
