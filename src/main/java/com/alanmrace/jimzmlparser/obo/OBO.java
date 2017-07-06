package com.alanmrace.jimzmlparser.obo;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class to store an ontology database (including dependents) loaded from OBO format.
 * 
 * @author Alan Race
 */
public class OBO implements Serializable {

    /** Class logger. */
    private static final Logger logger = Logger.getLogger(OBO.class.getName());

    /** Serial version ID. */
    private static final long serialVersionUID = 1L;

    /** Path to the OBO file. */
    private String path;

    /** List of imported ontologies. */
    private List<OBO> imports;

    /** Dictionary of ontology terms, using their ID as the key. */
    private Map<String, OBOTerm> terms;

    /**
     * Generate ontology database from the specified .obo file. 
     * If the obo file specifies imports, then load those imports from resources
     * associated with the project. The path of the import is ignored, with only 
     * the final name considered as the location of the resource.
     * 
     * @param path Location of the .obo file to load the ontology from.
     */
    public OBO(String path) {
        this.path = path;

        imports = new ArrayList<OBO>();
        terms = new HashMap<String, OBOTerm>();

        String resourcePath = path;
        
        // Strip off the URL details if they exist
        if (resourcePath.contains("http://")) {
            resourcePath = resourcePath.substring(resourcePath.lastIndexOf("/") + 1).toLowerCase();
        }

        logger.log(Level.FINER, "Parsing OBO /obo/{0}", resourcePath);

        InputStream is = OBO.class.getResourceAsStream("/obo/" + resourcePath);

        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader in = new BufferedReader(isr);

        String curLine;
        OBOTerm curTerm = null;
        boolean processingTerms = false;

        try {
            while ((curLine = in.readLine()) != null) {
                // Skip empty lines
                if (curLine.trim().isEmpty()) {
                    continue;
                }

                if (curLine.trim().equals("[Term]")) {
                    // Process a term
                    processingTerms = true;

                    // Get the ID
                    curLine = in.readLine();
                    // TODO: Requires that the first tag in the term is the ID tag 
                    int indexOfColon = curLine.indexOf(":");
                    String id = curLine.substring(indexOfColon + 1).trim();

                    curTerm = new OBOTerm(id);

                    terms.put(id, curTerm);
                } else if (curLine.trim().equals("[Typedef]")) {
                    processingTerms = false;
                } else if (curTerm != null && processingTerms) {
                    curTerm.parse(curLine);
                } else {
                    // TODO: Add in header information
                    int locationOfColon = curLine.indexOf(":");
                    String tag = curLine.substring(0, locationOfColon).trim();
                    String value = curLine.substring(locationOfColon + 1).trim().toLowerCase();

                    if (tag != null && value != null
                            && "import".equals(tag)) {

                        imports.add(new OBO(value));
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(OBO.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Process relationships
        for (OBOTerm term : terms.values()) {
            Collection<String> is_a = term.getIsA();

            for (String id : is_a) {
                OBOTerm parentTerm = getTerm(id);

                if (parentTerm == null) {
                    System.err.println("Haven't found " + id);
                } else {
                    parentTerm.addChild(term);
                    term.addParent(parentTerm);
                }
            }

            Collection<String> part_of = term.getPartOf();

            for (String id : part_of) {
                OBOTerm parentTerm = getTerm(id);

                if (parentTerm == null) {
                    System.err.println("Haven't found " + id);
                } else {
                    parentTerm.addChild(term);
                    term.addParent(parentTerm);
                }
            }

            // Units
            if (term.getUnitName() != null) {
                term.addUnit(getTerm(term.getUnitName()));
            }
        }
    }

    protected static OBO obo = new OBO("imagingMS.obo");
    
    /**
     * Static function to load in the imagingMS.obo file stored as a project resource.
     * 
     * @return Loaded ontology
     */
    public static OBO getOBO() {
        return obo;
    }

    /**
     * Get the term from the ontology with the ID id.
     * If no term is found with exactly the id specified, then return null.
     * 
     * @param id ID of the ontology term
     * @return Ontology term if found, null otherwise
     */
    public OBOTerm getTerm(String id) {
        if (id == null) {
            return null;
        }

        OBOTerm term = terms.get(id);

        if (term == null) {
            for (OBO parent : imports) {
                term = parent.getTerm(id);

                if (term != null) {
                    break;
                }
            }
        }

        return term;
    }

    @Override
    public String toString() {
        return path;
    }
}
