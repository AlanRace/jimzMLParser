package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.obo.OBO;
import com.alanmrace.jimzmlparser.util.XMLHelper;

public class DataProcessing extends MzMLContentList<ProcessingMethod> implements ReferenceableTag {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    protected static int idNumber = 0;

    // Attributes
    protected String id;		// Required


    public DataProcessing(String id) {
        super(0);
        
        this.id = id;
    }

    public DataProcessing(DataProcessing dp, ReferenceableParamGroupList rpgList, SoftwareList softwareList) {
        this(dp.id);

        for (ProcessingMethod pm : dp) {
            this.add(new ProcessingMethod(pm, rpgList, softwareList));
        }
    }

    public void addProcessingMethod(ProcessingMethod preprocessingMethod) {
        add(preprocessingMethod);
    }

    public ProcessingMethod getProcessingMethod(int index) {
        return get(index);
    }

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
    protected String getXMLAttributeText() {
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
