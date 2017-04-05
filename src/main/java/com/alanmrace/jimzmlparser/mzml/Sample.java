package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.util.XMLHelper;

public class Sample extends MzMLContentWithParams implements ReferenceableTag {

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
    @Override
    public String getID() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    protected String getXMLAttributeText() {
        String attributeText = "id=\"" + XMLHelper.ensureSafeXML(id) + "\"";
        
        if (name != null) {
            attributeText += " name=\"" + XMLHelper.ensureSafeXML(name) + "\"";
        }
        
        return attributeText;
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

    @Override
    public void setID(String id) {
        this.id = id;
    }
}
