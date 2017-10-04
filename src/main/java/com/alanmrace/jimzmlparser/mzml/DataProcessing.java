package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.obo.OBO;
import com.alanmrace.jimzmlparser.util.XMLHelper;

/**
 * Class describing a data processing workflow.
 * 
 * @author Alan Race
 */
public class DataProcessing extends MzMLContentList<ProcessingMethod> implements ReferenceableTag {

    /**
     * Serial version ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Unique identifier for the data processing [Required].
     */
    protected String id;

    /**
     * Create an empty DataProcessing with specified unique ID.
     * 
     * @param id Unique identifier.
     */
    public DataProcessing(String id) {
        super(0);
        
        this.id = id;
    }

    /**
     * Copy constructor.
     * 
     * @param dp Old DataProcessing to copy
     * @param rpgList New ReferenceableParamGroupList to match references to
     * @param softwareList New SoftwareList to match references to
     */
    public DataProcessing(DataProcessing dp, ReferenceableParamGroupList rpgList, SoftwareList softwareList) {
        this(dp.id);

        for (ProcessingMethod pm : dp) {
            this.add(new ProcessingMethod(pm, rpgList, softwareList));
        }
    }

    /**
     * Add ProcessingMethod. Helper method to retain API, calls 
     * {@link DataProcessing#add(com.alanmrace.jimzmlparser.mzml.MzMLTag)}.
     * 
     * @param preprocessingMethod ProcessingMethod to add to list
     */
    public void addProcessingMethod(ProcessingMethod preprocessingMethod) {
        add(preprocessingMethod);
    }

    /**
     * Returns ProcessingMethod at specified index in list. Helper method to retain 
     * API, calls {@link DataProcessing#get(int)}.
     * 
     * @param index Index in the list
     * @return ProcessingMethod at index, or null if none exists
     */
    public ProcessingMethod getProcessingMethod(int index) {
        return get(index);
    }

    /**
     * Returns number of PreprocessingMethod items added to DataProcessing workflow.
     * 
     * @return Number of preprocessing methods
     */
    public int getProcessingMethodCount() {
        return size();
    }

    @Override
    public String toString() {
        return "dataProcessing: " + id; // + " - " + processingMethods.get(0).toString();
    }

    @Override
    public String getID() {
        return id;
    }
    
    @Override
    public void setID(String id) {
        this.id = id;
    }
    
    /**
     * DataProcessing does not include a count attribute, despite being a list, so
     * this method is overridden to only output the ID.
     * 
     * @return Attribute text
     */
    @Override
    public String getXMLAttributeText() {
        return "id=\"" + XMLHelper.ensureSafeXML(id) + "\"";
    }

    @Override
    public String getTagName() {
        return "dataProcessing";
    }
    
    public static DataProcessing create() {
        return create(Software.create());
    }
    
    public static DataProcessing create(Software software) {
        DataProcessing dp = new DataProcessing("imzML-creation");
        
        ProcessingMethod pm = new ProcessingMethod(software);
        dp.add(pm);
        
        pm.addCVParam(new EmptyCVParam(OBO.getOBO().getTerm(ProcessingMethod.conversionTomzMLID)));
        
        return dp;
    }
}
