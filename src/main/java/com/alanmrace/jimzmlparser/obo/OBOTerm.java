package com.alanmrace.jimzmlparser.obo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Ontology term.
 * 
 * @author Alan Race
 */
public class OBOTerm implements Serializable {

    /** Class logger. */
    private static final Logger logger = Logger.getLogger(OBOTerm.class.getName());
    
    /**
     * Serialisation version.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Unique identifier for the ontology term. 
     */
    private final String id;

    /**
     * Ascribed name for the ontology term.
     */
    private String name;

    /**
     * The ontology namespace.
     */
    private String namespace;
        
    /**
     * List of all OBOTerms which were listed as relationship: has_units.
     */
    private final List<OBOTerm> has_units;
    
    /**
     * List of Strings containing all values which were listed as relationship: has_units.
     */
    protected final List<String> unitList;

    /**
     * List of Strings containing all values which were listed as relationship: is_a.
     */
    private final List<String> is_a;

    /**
     * List of Strings containing all values which were listed as relationship: part_of.
     */
    private final List<String> part_of;

    /**
     * List of all child terms of this ontology term. 
     * These are determined as the inverse of any relationship: is_a that is encountered.
     */
    private final List<OBOTerm> children;

    /**
     * List of all parent terms of this ontology term.
     */
    private final List<OBOTerm> parents;
    
    /**
     * Flag denoting whether the ontology term has been marked as obsolete.
     */
    private boolean is_obsolete = false;
    
    /**
     * Enum describing the valid value types of an ontology term value.
     * xref:value-type:xsd\\:
     */
    public enum XMLType {
        
        /** String. */
        String,
        
        /** Boolean. */
        Boolean,
        
        /** Any decimal number. */
        Decimal,
        
        /** Floating point precision number. */
        Float,
        
        /** Double precision floating point number. */
        Double,
        
        /** 
         * Duration in time. 
         * 
         * <p>The time interval is specified in the following form "PnYnMnDTnHnMnS" where:
         * <ul>
         *      <li>P indicates the period (required)
         *      <li>nY indicates the number of years
         *      <li>nM indicates the number of months
         *      <li>nD indicates the number of days
         *      <li>T indicates the start of a time section (required if you are going to specify hours, minutes, or seconds)
         *      <li>nH indicates the number of hours
         *      <li>nM indicates the number of minutes
         *      <li>nS indicates the number of seconds
         * </ul>
         * 
         * @see <a href="https://www.w3schools.com/xml/schema_dtypes_date.asp">https://www.w3schools.com/xml/schema_dtypes_date.asp</a>
         */
        Duration,
        
        /** Date and time.
         *  
         *  <p>Expected DateTime format is [-]CCYY-MM-DDThh:mm:ss[Z|(+|-)hh:mm]
         * 
         * @see <a href="http://books.xmlschemata.org/relaxng/ch19-77049.html">http://books.xmlschemata.org/relaxng/ch19-77049.html</a>
         * @see <a href="https://www.w3schools.com/xml/schema_dtypes_date.asp">https://www.w3schools.com/xml/schema_dtypes_date.asp</a>
         */
        DateTime,

        /**
         * Timestamp in the format hh:mm:ss.
         */
        Time,

        /**
         * Date in the format YYYY-MM-DD.
         */
        Date,

        /**
         * A period of one calendar month for a specific year, in the format YYYY-MM.
         */
        GYearMonth,

        /**
         * The period of one calendar year, in the format YYYY.
         */
        GYear,

        /**
         * The period of one calendar day recurring each calendar year, in the format --MM-DD (with an
         * optional timezone).
         */
        GMonthDay,

        /**
         * The period of one calendar day, recurring each calendar month, in the format ---DD (with
         * an optional timezone).
         */
        GDay,

        /**
         * The period of one calendar month recurring each year, in the format --MM (with an 
         * optional timezone).
         */
        GMonth,

        /**
         * Representation of binary data as a sequence of binary octets using hexadecimal encoding.
         * Each binary octet is a two-character hexadecimal number.
         */
        HexBinary,

        /**
         * Representation of binary data as a sequence of binary octets using base64 encoding 
         * as described in RFC 2045.
         * 
         * @see <a href="https://www.ietf.org/rfc/rfc2045.txt">RFC 2045</a>
         */
        Base64Binary,

        /**
         * Any uniform resource locator (URI) that conforms to RFC 2396 or 2732.
         * 
         * @see <a href="https://tools.ietf.org/html/rfc2396">RFC 2396</a>
         * @see <a href="https://www.ietf.org/rfc/rfc2732.txt">RFC 2732</a>
         */
        AnyURI,

        /**
         * Any qualified name according to Namespaces in XML.
         * 
         * @see <a href="http://books.xmlschemata.org/relaxng/ch19-77287.html">http://books.xmlschemata.org/relaxng/ch19-77287.html</a>
         */
        QName,

        /**
         * Description of the format of non-XML data.
         */
        NOTATION,
        
        // Derived Types

        /**
         * Integer, derived from Decimal.
         */
        Integer,

        /**
         * Non-positive integer, derived from Integer with the constraint <= 0.
         */
        NonPositiveInteger,

        /**
         * Negative integer, derived from Integer with the constraint < 0.
         */
        NegativeInteger,

        /**
         * Long, derived from Integer.
         */
        Long,

        /**
         * Int, derived from Long.
         */
        Int,

        /**
         * Short (16-bit), derived from Int.
         */
        Short,

        /**
         * Byte.
         */
        Byte,

        /**
         * Non-negative integer, derived from Integer with the constraint >= 0.
         */
        NonNegativeInteger,

        /**
         * Derived from Long with the constraint >= 0.
         */
        UnsignedLong,

        /**
         * Derived from Int with the constraint >= 0.
         */
        UnsignedInt,

        /**
         * Derived from Short with the constraint >= 0.
         */
        UnsignedShort,

        /**
         * Derived from Byte with the constraint >= 0.
         */
        UnsignedByte,

        /**
         * Derived from Integer with the constraint > 0.
         */
        PositiveInteger,
        
        // Custom Derived Types

        /**
         * Derived from Float, with the constraint >= 0.0.
         */
        NonNegativeFloat,

        /**
         * Derived from Double, with the constraint >= 0.0.
         */
        NonNegativeDouble,
    }
    
    /**
     * The value type allowed for the ontology term, or null if no value expected.
     */
    protected XMLType valueType;
    
    /**
     * Synonym scope, as defined by the OBO file format.
     * 
     * @see <a href="http://owlcollab.github.io/oboformat/doc/obo-syntax.html">OBO File Format 1.4</a>
     */
    public enum Synonym {

        /**
         * A true synonym.
         */
        Exact,

        /**
         * A synonym for a class C is BROAD if it denotes a broader class than C.
         * 
         * <p>Here, broader than is an informal notion that encompasses both subsumption 
         * and possibly mereological and temporal containment. For example, "skull" 
         * could conceivably be a BROAD synonym for the class cranium. 
         * 
         * @see <a href="http://owlcollab.github.io/oboformat/doc/obo-syntax.html">OBO File Format 1.4</a>
         */
        Broad,
        
        /**
         * A synonym for a class C is NARROW if C denotes a broader class than the synonym.
         * 
         * <p>In contrast to BROAD. For example, the class cranium
         * could conceivably be a NARROW synonym for "skull". 
         * 
         * @see OBOTerm.Synonym#Broad
         * @see <a href="http://owlcollab.github.io/oboformat/doc/obo-syntax.html">OBO File Format 1.4</a>
         */
        Narrow,

        /**
         * If a synonym is neither EXACT, NARROW or BROAD, then it is RELATED.
         * 
         * @see OBOTerm.Synonym#Exact
         * @see OBOTerm.Synonym#Broad
         * @see OBOTerm.Synonym#Narrow
         * @see <a href="http://owlcollab.github.io/oboformat/doc/obo-syntax.html">OBO File Format 1.4</a>
         */
        Related
    }

    /**
     * Create an ontology term, with the ID id.
     * 
     * @param id Unique identifier for the ontology term
     */
    public OBOTerm(String id) {
        // TODO: Assign these only when necessary
        is_a = new ArrayList<String>();
        part_of = new ArrayList<String>();
        children = new ArrayList<OBOTerm>();
        parents = new ArrayList<OBOTerm>();
        has_units = new ArrayList<OBOTerm>();
        unitList = new ArrayList<String>();

        this.id = id;

        int indexOfColon = id.indexOf(":");
        namespace = id.substring(0, indexOfColon).trim();
    }

    /**
     * Parse a single, pre-white character stripped, line from an OBO file.
     * 
     * @param strippedLine Single line from an OBO file with all preceeding and proceeding white characters removed.
     */
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
        } else if ("relationship".equals(tag)) {
            int indexOfSpace = value.indexOf(" ");
            String relationshipTag = value.substring(0, indexOfSpace).trim();
            String relationshipValue = value.substring(indexOfSpace + 1).trim();

            if ("is_a".equals(relationshipTag)) {
                is_a.add(relationshipValue);
            } else if ("has_units".equals(relationshipTag)) {
                unitList.add(relationshipValue);
            } else if ("part_of".equals(relationshipTag)) {
                part_of.add(relationshipValue);
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
//            } else {
                //logger.log(Level.INFO, "INFO: Unknown xref encountered ''{0}'' @ {1}", new Object[] {value, id});
            }
//        } else {
            //logger.log(Level.INFO, "INFO: Tag not implemented ''{0}''", tag);
        }
    }

    /**
     * Add an ontology term as a child to the current term.
     * 
     * @param child Ontology term to add as a child.
     */
    public void addChild(OBOTerm child) {
//		System.out.println("INFO: Adding child " + child.getID() + " to " + getID());
        children.add(child);

    }

    /**
     * Get the expected value type for the ontology term, or null if no value expected.
     * 
     * @return Value type
     */
    public XMLType getValueType() {
        return this.valueType;
    }
    
    /**
     * Get whether the ontology term has been marked as obsolete.
     * 
     * @return true if obsolete, false otherwise
     */
    public boolean isObsolete() {
        return is_obsolete;
    }
    
    /**
     * Add an ontology term as a parent to the current term.
     * 
     * @param parent Ontology term to add as parent
     */
    public void addParent(OBOTerm parent) {
        parents.add(parent);
    }

    /**
     * Check whether a given id is that of any parent to the current ontology term.
     * 
     * @param id ID of the potential parent term
     * @return true if a parent term is found to have the specified id, false otherwise
     */
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

    /**
     * Check whether a given id is that of any child to the current ontology term.
     * 
     * @param id ID of the potential child term
     * @return true if a child term is found to have the specified id, false otherwise
     */
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

    /**
     * Get all terms which have been previously added as child terms.
     * 
     * @return List of child terms
     */
    public List<OBOTerm> getChildren() {
        return children;
    }

    /**
     * Get all terms which have been previously added as child terms, as well as 
     * all of their child terms (repeated recursively).
     * 
     * @return List of all child terms
     */
    public List<OBOTerm> getAllChildren() {
        ArrayList<OBOTerm> allChildren = new ArrayList<OBOTerm>();

        for (OBOTerm child : children) {
            child.getAllChildren(allChildren);
        }

        return allChildren;
    }

    /**
     * Get all terms which have been previously added as child terms, as well as 
     * all of their child terms (repeated recursively) and add to the specified 
     * allChildren list.
     * 
     * @param allChildren list to add all children to
     */
    private void getAllChildren(List<OBOTerm> allChildren) {
        allChildren.add(this);

        for (OBOTerm child : children) {
            child.getAllChildren(allChildren);
        }
    }

    /**
     * Get all terms which have been previously added as parent terms, as well as 
     * all of their parent terms (repeated recursively).
     * 
     * @return List of all parent terms
     */
    public List<OBOTerm> getAllParents() {
        ArrayList<OBOTerm> allParents = new ArrayList<OBOTerm>();

        for (OBOTerm parent : parents) {
            parent.getAllParents(allParents);
        }

        return allParents;
    }
    
    /**
     * Check whether any of the parents, or their parents (checked recursively), is 
     * the specified term.
     * 
     * @param term OBOTerm to check if is a parent
     * @return true if the parent term was found, false otherwise
     */
    public boolean hasParent(OBOTerm term) {
        return getAllParents().contains(term);
    }

    /**
     * Get all terms which have been previously added as parent terms, as well as 
     * all of their parent terms (repeated recursively) and add to the specified 
     * allParents list.
     * 
     * @param allParents list to add all parents to
     */
    private void getAllParents(List<OBOTerm> allParents) {
        allParents.add(this);

        for (OBOTerm parent : parents) {
            parent.getAllParents(allParents);
        }
    }

    /**
     * Get list of is_a relationships.
     * 
     * @return List of is_a relationships
     */
    public List<String> getIsA() {
        return is_a;
    }

    /**
     * Get list of part_of relationships.
     * 
     * @return List of part_of relationships
     */
    public List<String> getPartOf() {
        return part_of;
    }

    /**
     * Get the ID or accession for the current ontology term.
     *
     * @return id (accession) of the ontology term
     */
    public String getID() {
        return id;
    }

    /**
     * Get the name of the ontology term.
     * 
     * @return Ontology term name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the list of has_units relationships
     * 
     * @return List of has_units relationships
     */
    public List<OBOTerm> getUnits() {
        return has_units;
    }

    /**
     * Get the namespace of the ontology term.
     * 
     * @return Ontology term namespace
     */
    public String getNamespace() {
        return namespace;
    }

    /**
     * Add a unit ontology term which describes the units for the value of this ontology term. 
     * Determined by has_units relationship in the OBO file.
     * 
     * @param unit Ontology term that describes the value units
     */
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
