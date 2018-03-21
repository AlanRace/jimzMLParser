package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.exceptions.InvalidXPathException;
import com.alanmrace.jimzmlparser.exceptions.UnfollowableXPathException;
import java.util.Collection;

/**
 * Chromatogram tag ({@literal <chromatogram>}).
 *
 * @author Alan Race
 */
public class Chromatogram extends MzMLDataContainer {

    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Accession: chromatogram attribute (MS:1000808).
     */
    public static final String chromatogramAttributeID = "MS:1000808";

    /**
     * Accession: chromatogram type (MS:1000626).
     */
    public static final String chromatogramTypeID = "MS:1000626";

    /**
     * Child {@literal <precursor>} tag.
     */
    private Precursor precursor;

    /**
     * Child {@literal <product>} tag.
     */
    private Product product;

    /**
     * Create a {@literal <chromatogram>} tag with an ID and a default array length (in values).
     * 
     * @param id Unique identifier
     * @param defaultArrayLength Length of the chromatogram in data points
     */
    public Chromatogram(String id, int defaultArrayLength) {
        super(id, defaultArrayLength);
    }

    /**
     * Copy constructor, requiring new versions of lists to match old references to.
     * 
     * @param chromatogram      Chromatogram to copy
     * @param rpgList           New ReferenceableParamGroupList
     * @param dpList            New DataProcessingList
     * @param sourceFileList    New SourceFileList
     */
    public Chromatogram(Chromatogram chromatogram, ReferenceableParamGroupList rpgList, DataProcessingList dpList,
            SourceFileList sourceFileList) {
        super(chromatogram, rpgList);

        this.defaultArrayLength = chromatogram.defaultArrayLength;
        this.id = chromatogram.id;

        if (chromatogram.dataProcessingRef != null && dpList != null) {
            for (DataProcessing dp : dpList) {
                if (chromatogram.dataProcessingRef.getID().equals(dp.getID())) {
                    this.dataProcessingRef = dp;

                    break;
                }
            }
        }

        if (chromatogram.precursor != null) {
            this.precursor = new Precursor(chromatogram.precursor, rpgList, sourceFileList);
        }
        if (chromatogram.product != null) {
            this.product = new Product(chromatogram.product, rpgList);
        }
        if (chromatogram.binaryDataArrayList != null) {
            this.binaryDataArrayList = new BinaryDataArrayList(chromatogram.binaryDataArrayList, rpgList, dpList);
        }
    }

    /**
     * Set the child precursor tag.
     * 
     * @param precursor Precursor
     */
    public void setPrecursor(Precursor precursor) {
        precursor.setParent(this);

        this.precursor = precursor;
    }

    /**
     * Returns the precursor, or null if none exists.
     * 
     * @return Precursor
     */
    public Precursor getPrecursor() {
        return precursor;
    }

    /**
     * Set the child product tag associated with the chromatogram.
     * 
     * @param product Product
     */
    public void setProduct(Product product) {
        product.setParent(this);

        this.product = product;
    }

    /**
     * Returns the product, or null if none exists.
     * 
     * @return Product
     */
    public Product getProduct() {
        return product;
    }

    @Override
    protected void addTagSpecificElementsAtXPathToCollection(Collection<MzMLTag> elements, String fullXPath, String currentXPath) throws InvalidXPathException {
        if (currentXPath.startsWith("/precursor")) {
            if (precursor == null) {
                throw new UnfollowableXPathException("No precursor exists, so cannot go to " + fullXPath, fullXPath, currentXPath);
            }

            precursor.addElementsAtXPathToCollection(elements, fullXPath, currentXPath);
        } else if (currentXPath.startsWith("/product")) {
            if (product == null) {
                throw new UnfollowableXPathException("No product exists, so cannot go to " + fullXPath, fullXPath, currentXPath);
            }

            product.addElementsAtXPathToCollection(elements, fullXPath, currentXPath);
        } else if (currentXPath.startsWith("/binaryDataArrayList")) {
            if (binaryDataArrayList == null) {
                throw new UnfollowableXPathException("No binaryDataArrayList exists, so cannot go to " + fullXPath, fullXPath, currentXPath);
            }

            binaryDataArrayList.addElementsAtXPathToCollection(elements, fullXPath, currentXPath);
        }
    }

    @Override
    public String toString() {
        return "chromatogram: id=\"" + id + "\"";
    }

    @Override
    public String getTagName() {
        return "chromatogram";
    }

    @Override
    public void addChildrenToCollection(Collection<MzMLTag> children) {
        super.addChildrenToCollection(children);
        
        if (precursor != null) {
            children.add(precursor);
        }
        if (product != null) {
            children.add(product);
        }
        if (binaryDataArrayList != null) {
            children.add(binaryDataArrayList);
        }
    }
}
