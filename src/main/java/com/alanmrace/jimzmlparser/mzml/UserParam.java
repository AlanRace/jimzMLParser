package com.alanmrace.jimzmlparser.mzml;


import com.alanmrace.jimzmlparser.obo.OBOTerm;
import com.alanmrace.jimzmlparser.util.XMLHelper;

/**
 * Parameters which are not part of any controlled vocabulary. 
 * 
 * @author Alan Race
 */
public class UserParam extends MzMLContent {

    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Name of the parameter [Required].
     */
    private String name;

    /**
     * Datatype of the parameter (if appropriate) e.g. xsd:float [Optional].
     */
    private String type;

    /**
     * The ontology term describing the units of the term [Optional].
     */
    private OBOTerm units;

    /**
     * The value of the parameter (if appropriate) [Optional].
     */
    private String value;

    /**
     * Create a UserParam from only a name, all other attributes are empty.
     * 
     * @param name Name of parameter
     */
    public UserParam(String name) {
        this.name = name;
    }

    /**
     * Create a UserParam with a name and value, all other attributes are empty.
     * 
     * @param name Name of UserParam
     * @param value Parameter value
     */
    public UserParam(String name, String value) {
        this(name);

        this.value = value;
    }

    /**
     * Create a UserParam with a name and value with specific units.
     * 
     * @param name Name of UserParam
     * @param value Parameter value
     * @param units Units as ontology term
     */
    public UserParam(String name, String value, OBOTerm units) {
        this(name, value);

        this.units = units;
    }

    /**
     * Copy constructor for UserParam.
     * 
     * @param userParam Parameter to copy
     */
    public UserParam(UserParam userParam) {
        this.name = userParam.name;
        this.type = userParam.type;
        this.units = userParam.units;
        this.value = userParam.value;
    }

    /**
     * Returns the name of the parameter.
     * 
     * @return Name of the parameter
     */
    public String getName() {
        return name;
    }

    /**
     * Set the type of the user parameter as a string (e.g. xsd:float).
     * 
     * @param type Type of the parameter
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Get the type of the user parameter (e.g. xsd:float).
     * 
     * @return Type of parameter in String representation
     */
    public String getType() {
        return type;
    }

    /**
     * Set the units of the parameter as an ontology term.
     * 
     * @param units Units.
     */
    public void setUnits(OBOTerm units) {
        this.units = units;
    }

    /**
     * Get the units of the parameter.
     * 
     * @return Units as OBOTerm
     */
    public OBOTerm getUnits() {
        return units;
    }

    /**
     * Set the value of the user parameter.
     * 
     * @param value Value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Returns the value of the parameter, or null if one does not exist.
     * 
     * @return Value or null if no value
     */
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "userParam: " + name + ((value != null && !value.isEmpty()) ? (" - " + value) : "");
    }

    @Override
    public String getTagName() {
        return "userParam";
    }
    
    @Override
    public String getXMLAttributeText() {
        String attributes = " name=\"" + XMLHelper.ensureSafeXML(getName()) + "\"";
        
        if(getType() != null && !getType().isEmpty())
            attributes += " type=\"" + XMLHelper.ensureSafeXML(getType()) + "\"";
        
        String value = getValue();
        
        if (value != null && !value.equals("null")) {
            attributes += " value=\"" + XMLHelper.ensureSafeXML(value) + "\"";
        }

        if (units != null) {
            attributes += " unitCvRef=\"" + XMLHelper.ensureSafeXML(units.getNamespace()) + "\"";
            attributes += " unitAccession=\"" + XMLHelper.ensureSafeXML(units.getID()) + "\"";
            attributes += " unitName=\"" + XMLHelper.ensureSafeXML(units.getName()) + "\"";
        }
        
        return attributes;
    }
    
}
