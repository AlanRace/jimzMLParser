package com.alanmrace.jimzmlparser.mzml;

import java.io.IOException;

import com.alanmrace.jimzmlparser.obo.OBOTerm;
import com.alanmrace.jimzmlparser.util.XMLHelper;
import java.io.RandomAccessFile;
import com.alanmrace.jimzmlparser.writer.MzMLWritable;

public class UserParam extends MzMLContent {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String name;
    private String type;
    private OBOTerm units;
    private String value;

    public UserParam(String name) {
        this.name = name;
    }

    public UserParam(String name, String value) {
        this(name);

        this.value = value;
    }

    public UserParam(String name, String value, OBOTerm units) {
        this(name, value);

        this.units = units;
    }

    public UserParam(UserParam userParam) {
        this.name = userParam.name;
        this.type = userParam.type;
        this.units = userParam.units;
        this.value = userParam.value;
    }

    public String getName() {
        return name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setUnits(OBOTerm units) {
        this.units = units;
    }

    public OBOTerm getUnits() {
        return units;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public void outputXML(MzMLWritable output, int indent) throws IOException {
        MzMLContent.indent(output, indent);
        
        output.write("<userParam");

        output.write(" name=\"" + XMLHelper.ensureSafeXML(name) + "\"");

        if (type != null) {
            output.write(" type=\"" + XMLHelper.ensureSafeXML(type) + "\"");
        }

        if (value != null) {
            output.write(" value=\"" + XMLHelper.ensureSafeXML(value) + "\"");
        }

        if (units != null) {
            output.write(" unitCvRef=\"" + XMLHelper.ensureSafeXML(units.getNamespace()) + "\"");
            output.write(" unitAccession=\"" + XMLHelper.ensureSafeXML(units.getID()) + "\"");
            output.write(" unitName=\"" + XMLHelper.ensureSafeXML(units.getName()) + "\"");
        }

        output.write("/>\n");
    }

    @Override
    public String toString() {
        return "userParam: " + name + ((value != null && !value.isEmpty()) ? (" - " + value) : "");
    }

    @Override
    public String getTagName() {
        return "userParam";
    }
    
}
