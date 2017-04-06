package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.exceptions.InvalidXPathException;
import com.alanmrace.jimzmlparser.exceptions.UnfollowableXPathException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class Chromatogram extends MzMLDataContainer implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static String chromatogramAttributeID = "MS:1000808";
    public static String chromatogramTypeID = "MS:1000626";

//	private DataProcessing dataProcessingRef;	// Optional
//	private int defaultArrayLength;				// Required
//	private String id;							// Required
//	private int index;							// Required
    private Precursor precursor;
    private Product product;
//	private BinaryDataArrayList binaryDataArrayList;

    public Chromatogram(String id, int defaultArrayLength) {
        super(id, defaultArrayLength);
    }

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

    @Override
    public ArrayList<OBOTermInclusion> getListOfRequiredCVParams() {
        ArrayList<OBOTermInclusion> required = new ArrayList<OBOTermInclusion>();
        required.add(new OBOTermInclusion(chromatogramTypeID, true, true, false));

        return required;
    }

    @Override
    public ArrayList<OBOTermInclusion> getListOfOptionalCVParams() {
        ArrayList<OBOTermInclusion> optional = new ArrayList<OBOTermInclusion>();
        optional.add(new OBOTermInclusion(chromatogramAttributeID, false, true, false));

        return optional;
    }

    // Set optional attributes
//    public void setDataProcessingRef(DataProcessing dataProcessingRef) {
//        this.dataProcessingRef = dataProcessingRef;
//    }
    
    public DataProcessing getDataProcessingRef() {
        return dataProcessingRef;
    }

    public void setPrecursor(Precursor precursor) {
        precursor.setParent(this);

        this.precursor = precursor;
    }

    public Precursor getPrecursor() {
        return precursor;
    }

    public void setProduct(Product product) {
        product.setParent(this);

        this.product = product;
    }

    public Product getProduct() {
        return product;
    }

//    public void setBinaryDataArrayList(BinaryDataArrayList binaryDataArrayList) {
//        binaryDataArrayList.setParent(this);
//
//        this.binaryDataArrayList = binaryDataArrayList;
//    }
//
//    public BinaryDataArrayList getBinaryDataArrayList() {
//        return binaryDataArrayList;
//    }
    
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
        if(precursor != null)
            children.add(precursor);
        if(product != null)
            children.add(product);
        if(binaryDataArrayList != null)
            children.add(binaryDataArrayList);
        
        super.addChildrenToCollection(children);
    }
}
