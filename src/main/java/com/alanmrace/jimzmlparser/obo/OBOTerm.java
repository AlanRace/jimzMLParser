package com.alanmrace.jimzmlparser.obo;

import com.alanmrace.jimzmlparser.util.XMLHelper;
import java.io.BufferedWriter;
import java.io.IOException;
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

        if (tag.equals("name")) {
            this.name = value;
        } else if (tag.equals("namespace")) {
            this.namespace = value;
        } else if (tag.equals("def")) {
//			this.def = value;
        } else if (tag.equals("comment")) {
//			this.comment = value;
        } else if (tag.equals("relationship")) {
            int indexOfSpace = value.indexOf(" ");
            String relationshipTag = value.substring(0, indexOfSpace).trim();
            String relationshipValue = value.substring(indexOfSpace + 1).trim();

            if (relationshipTag.equals("is_a")) {
                is_a.add(relationshipValue);
            } else if (relationshipTag.equals("has_units")) {
                unitName = relationshipValue;
            } else if (relationshipTag.equals("part_of")) {
                part_of.add(relationshipValue);
            } else {
                //System.out.println("INFO: Relationship tag not implemented '" + relationshipTag + "'");
            }
        } else if (tag.equals("is_a")) {
            is_a.add(value);
        } else if (tag.equals("is_obsolete")) {
            is_obsolete = Boolean.parseBoolean(value);
        } else if (tag.equals("xref")) {
            if(value.contains("value-type:xsd\\:")) {
                String[] substrings = value.replace("value-type:xsd\\:", "").split("\\s");
                
                if(substrings[0].equals("string")) {
                    valueType = XMLType.String;
                } else if(substrings[0].equals("integer")) {
                    valueType = XMLType.Integer;
                } else if(substrings[0].equals("int")) {
                    valueType = XMLType.Int;
                } else if(substrings[0].equals("decimal")) {
                    valueType = XMLType.Decimal;
                } else if(substrings[0].equals("negativeInteger")) {
                    valueType = XMLType.NegativeInteger;
                } else if(substrings[0].equals("positiveInteger")) {
                    valueType = XMLType.PositiveInteger;
                } else if(substrings[0].equals("nonNegativeInteger")) {
                    valueType = XMLType.NonNegativeInteger;
                } else if(substrings[0].equals("boolean")) {
                    valueType = XMLType.Boolean;
                } else if(substrings[0].equals("date")) {
                    valueType = XMLType.Date;
                } else if(substrings[0].equals("float")) {
                    valueType = XMLType.Float; 
                } else if(substrings[0].equals("nonNegativeFloat")) {
                    valueType = XMLType.NonNegativeFloat;
                } else if(substrings[0].equals("nonNegativeDouble")) {
                    valueType = XMLType.NonNegativeDouble;
                } else if(substrings[0].equals("double")) {
                    valueType = XMLType.Double;  
                } else if(substrings[0].equals("anyURI")) {
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

//	private void addParentChildRelationship(OBO currentOBO, String id) {
//		// TODO: Implement is_a
//		OBOTerm term = currentOBO.getTerm(id);
//		
//		if(term == null) {
//			System.err.println("Haven't found " + id);
//		} else {
//			term.addChild(this);
//			is_a.add(term);
//		}
//	}
//	@JsonIgnore
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
    
//	@JsonIgnore
    public void addParent(OBOTerm parent) {
        parents.add(parent);
    }

//	@JsonIgnore
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

//	@JsonIgnore
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

//	@JsonIgnore
    public Collection<OBOTerm> getChildren() {
        return children;
    }

//	@JsonIgnore
    public Collection<OBOTerm> getAllChildren() {
        ArrayList<OBOTerm> allChildren = new ArrayList<OBOTerm>();

        for (OBOTerm child : children) {
            child.getAllChildren(allChildren);
        }

        return allChildren;
    }

//	@JsonIgnore
    private void getAllChildren(ArrayList<OBOTerm> allChildren) {
        allChildren.add(this);

        for (OBOTerm child : children) {
            child.getAllChildren(allChildren);
        }
    }

//	@JsonIgnore
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

//	@JsonIgnore
    private void getAllParents(ArrayList<OBOTerm> allParents) {
        allParents.add(this);

        for (OBOTerm parent : parents) {
            parent.getAllParents(allParents);
        }
    }

//	@JsonIgnore
    public Collection<String> getAllChildNames() {
        ArrayList<String> allChildren = new ArrayList<String>();

        for (OBOTerm child : children) {
            child.getAllChildNames(allChildren, 0);
        }

        return allChildren;
    }

//	@JsonIgnore
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

//	@JsonIgnore
    public Collection<String> getIsA() {
        return is_a;
    }

//	@JsonIgnore
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
