package com.alanmrace.jimzmlparser.obo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OBOTerm implements Serializable {

    private static final Logger logger = Logger.getLogger(OBOTerm.class.getName());
    
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
//	private String def;
    private String namespace;
//	private String comment;

    private String unitName = null;
    
    private List<OBOTerm> has_units;
    private List<String> is_a;
    private List<String> part_of;
    private List<OBOTerm> children;
    private List<OBOTerm> parents;
    
    private boolean is_obsolete = false;
    
    public enum XMLType {
        String,
        Boolean,
        Decimal,
        Float,
        Double,
        Duration,
        DateTime,
        Time,
        Date,
        GYearMonth,
        GYear,
        GMonthDay,
        GDay,
        GMonth,
        HexBinary,
        Base64Binary,
        AnyURI,
        QName,
        NOTATION,
        
        // Derived Types
        Integer,
        NonPositiveInteger,
        NegativeInteger,
        Long,
        Int,
        Short,
        Byte,
        NonNegativeInteger,
        UnsignedLong,
        UnsignedInt,
        UnsignedShort,
        UnsignedByte,
        PositiveInteger,
        
        // Custom Derived Types
        NonNegativeFloat,
        NonNegativeDouble,
    }
    
    protected XMLType valueType;
    
    public enum Synonym {
        Exact,
        Narrow,
        Broad,
        Related
    }

    public OBOTerm(String id) {
        is_a = new ArrayList<String>();
        part_of = new ArrayList<String>();
        children = new ArrayList<OBOTerm>();
        parents = new ArrayList<OBOTerm>();
        has_units = new ArrayList<OBOTerm>();

        this.id = id;

        int indexOfColon = id.indexOf(":");
        namespace = id.substring(0, indexOfColon).trim();
    }

    public void parse(String strippedLine) {
        int indexOfColon = strippedLine.indexOf(":");
        String tag = strippedLine.substring(0, indexOfColon).trim();
        String value = strippedLine.substring(indexOfColon + 1).trim();

        // Strip comments
        int indexOfExclaimation = value.indexOf("!");

        if (indexOfExclaimation > -1) {
            value = value.substring(0, indexOfExclaimation).trim();
        }

        if ("name".equals(tag)) {
            this.name = value;
        } else if ("namespace".equals(tag)) {
            this.namespace = value;
        } else if ("def".equals(tag)) {
//			this.def = value;
        } else if ("comment".equals(tag)) {
//			this.comment = value;
        } else if ("relationship".equals(tag)) {
            int indexOfSpace = value.indexOf(" ");
            String relationshipTag = value.substring(0, indexOfSpace).trim();
            String relationshipValue = value.substring(indexOfSpace + 1).trim();

            if ("is_a".equals(relationshipTag)) {
                is_a.add(relationshipValue);
            } else if ("has_units".equals(relationshipTag)) {
                unitName = relationshipValue;
            } else if ("part_of".equals(relationshipTag)) {
                part_of.add(relationshipValue);
            } else {
                //System.out.println("INFO: Relationship tag not implemented '" + relationshipTag + "'");
            }
        } else if ("is_a".equals(tag)) {
            is_a.add(value);
        } else if ("is_obsolete".equals(tag)) {
            is_obsolete = Boolean.parseBoolean(value);
        } else if ("xref".equals(tag)) {
            if(value.contains("value-type:xsd\\:")) {
                String[] substrings = value.replace("value-type:xsd\\:", "").split("\\s");
                
                if("string".equals(substrings[0])) {
                    valueType = XMLType.String;
                } else if("integer".equals(substrings[0])) {
                    valueType = XMLType.Integer;
                } else if("int".equals(substrings[0])) {
                    valueType = XMLType.Int;
                } else if("decimal".equals(substrings[0])) {
                    valueType = XMLType.Decimal;
                } else if("negativeInteger".equals(substrings[0])) {
                    valueType = XMLType.NegativeInteger;
                } else if("positiveInteger".equals(substrings[0])) {
                    valueType = XMLType.PositiveInteger;
                } else if("nonNegativeInteger".equals(substrings[0])) {
                    valueType = XMLType.NonNegativeInteger;
                } else if("boolean".equals(substrings[0])) {
                    valueType = XMLType.Boolean;
                } else if("date".equals(substrings[0])) {
                    valueType = XMLType.Date;
                } else if("float".equals(substrings[0])) {
                    valueType = XMLType.Float; 
                } else if("nonNegativeFloat".equals(substrings[0])) {
                    valueType = XMLType.NonNegativeFloat;
                } else if("nonNegativeDouble".equals(substrings[0])) {
                    valueType = XMLType.NonNegativeDouble;
                } else if("double".equals(substrings[0])) {
                    valueType = XMLType.Double;  
                } else if("anyURI".equals(substrings[0])) {
                    valueType = XMLType.AnyURI;
                } else {
                    logger.log(Level.INFO, "INFO: Unknown value-type encountered ''{0}'' @ {1}", new Object[] {value, id});
                }
            } else {
                //logger.log(Level.INFO, "INFO: Unknown xref encountered ''{0}'' @ {1}", new Object[] {value, id});
            }
        } else {
            //logger.log(Level.INFO, "INFO: Tag not implemented ''{0}''", tag);
        }
    }

    public void addChild(OBOTerm child) {
//		System.out.println("INFO: Adding child " + child.getID() + " to " + getID());
        children.add(child);

    }

    public XMLType getValueType() {
        return this.valueType;
    }
    
    public boolean isObsolete() {
        return is_obsolete;
    }
    
    public void addParent(OBOTerm parent) {
        parents.add(parent);
    }

    public boolean isParentOf(String id) {
        if (this.id.equals(id)) {
            return true;
        }

        for (OBOTerm child : getAllChildren()) {
            if (child.getID().equals(id)) {
                return true;
            }
        }

        return false;
    }

    public boolean isChildOf(String id) {
        if (this.id.equals(id)) {
            return true;
        }

        for (OBOTerm parent : getAllParents()) {
            if (parent.getID().equals(id)) {
                return true;
            }
        }

        return false;
    }

    public Collection<OBOTerm> getChildren() {
        return children;
    }

    public Collection<OBOTerm> getAllChildren() {
        ArrayList<OBOTerm> allChildren = new ArrayList<OBOTerm>();

        for (OBOTerm child : children) {
            child.getAllChildren(allChildren);
        }

        return allChildren;
    }

    private void getAllChildren(ArrayList<OBOTerm> allChildren) {
        allChildren.add(this);

        for (OBOTerm child : children) {
            child.getAllChildren(allChildren);
        }
    }

    public Collection<OBOTerm> getAllParents() {
        ArrayList<OBOTerm> allParents = new ArrayList<OBOTerm>();

        for (OBOTerm parent : parents) {
            parent.getAllParents(allParents);
        }

        return allParents;
    }
    
    public boolean hasParent(OBOTerm term) {
        return getAllParents().contains(term);
    }

    private void getAllParents(ArrayList<OBOTerm> allParents) {
        allParents.add(this);

        for (OBOTerm parent : parents) {
            parent.getAllParents(allParents);
        }
    }

    public Collection<String> getAllChildNames() {
        ArrayList<String> allChildren = new ArrayList<String>();

        for (OBOTerm child : children) {
            child.getAllChildNames(allChildren, 0);
        }

        return allChildren;
    }

    private void getAllChildNames(ArrayList<String> allChildren, int indent) {
        String indented = "";

        for (int i = 0; i < indent; i++) {
            indented += "-";
        }

        allChildren.add(indented + toString());

        for (OBOTerm child : children) {
            child.getAllChildNames(allChildren, indent + 1);
        }
    }

    public Collection<String> getIsA() {
        return is_a;
    }

    public Collection<String> getPartOf() {
        return part_of;
    }

    /**
     * Get the ID or accession for the current ontology term
     *
     * @return id (accession) of the ontology term
     */
    public String getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUnitName() {
        return unitName;
    }

    public List<OBOTerm> getUnits() {
        return has_units;
    }

    public String getNamespace() {
        return namespace;
    }

    public void addUnit(OBOTerm unit) {
//		System.out.println(unitName + " " + units.getID() + " " + units.getName());
        this.has_units.add(unit);
    }

    @Override
    public String toString() {
        return "(" + id + ") " + name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof OBOTerm)) {
            return false;
        }

        OBOTerm term = (OBOTerm) o;

        return term.getID().equals(id) && term.getNamespace().equals(namespace);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }
}
