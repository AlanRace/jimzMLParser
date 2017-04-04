package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.util.XMLHelper;
import java.io.BufferedWriter;
import java.io.IOException;

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

    @Override
    public void outputXML(BufferedWriter output, int indent) throws IOException {
        MzMLContent.indent(output, indent);
        output.write("<dataProcessing");
        output.write(" id=\"" + XMLHelper.ensureSafeXML(id) + "\"");
        output.write(">\n");

        int order = 1;

        for (ProcessingMethod pm : this) {
            pm.outputXML(output, indent + 1, order++);
        }

        MzMLContent.indent(output, indent);
        output.write("</dataProcessing>\n");
    }

    @Override
    public String getTagName() {
        return "dataProcessing";
    }
}
