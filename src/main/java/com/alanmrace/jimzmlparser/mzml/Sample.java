package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.util.XMLHelper;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;

public class Sample extends MzMLContent implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private static int idNumber = 0;

    private String id;		// Required
    private String name;	// Optional

//	protected ArrayList<OBOTermValue> attributes;
    public Sample() {
        this("sample" + idNumber++);
    }

    public Sample(String id) {
        this(id, null);
    }

    public Sample(String id, String name) {
        this.id = id;
        this.name = name;

//		attributes = new ArrayList<OBOTermValue>();
    }

    public Sample(Sample sample, ReferenceableParamGroupList rpgList) {
        super(sample, rpgList);

        this.id = sample.id;
        this.name = sample.name;
    }

//	public void addAttributes(ArrayList<OBOTermValue> attributes) {
//		this.attributes.addAll(attributes);
//	}	
//	
    public String getID() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void outputXML(BufferedWriter output, int indent) throws IOException {
        MzMLContent.indent(output, indent);
        output.write("<sample");
        output.write(" id=\"" + XMLHelper.ensureSafeXML(id) + "\"");

        if (name != null) {
            output.write(" name=\"" + XMLHelper.ensureSafeXML(name) + "\"");
        }
        output.write(">\n");

        super.outputXML(output, indent + 1);

        MzMLContent.indent(output, indent);
        output.write("</sample>\n");
    }

    @Override
    public String toString() {
        return "sample: id=\"" + id + "\""
                + ((name != null && !name.isEmpty()) ? " name=\"" + name + "\"" : "");
    }

    @Override
    public String getTagName() {
        return "sample";
    }
}
