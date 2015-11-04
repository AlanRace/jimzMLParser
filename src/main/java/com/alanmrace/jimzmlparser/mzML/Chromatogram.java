package com.alanmrace.jimzmlparser.mzML;

import com.alanmrace.jimzmlparser.util.XMLHelper;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

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

    public Chromatogram(String id, int defaultArrayLength, int index) {
        super(id, defaultArrayLength, index);
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

    public ArrayList<OBOTermInclusion> getListOfRequiredCVParams() {
        ArrayList<OBOTermInclusion> required = new ArrayList<OBOTermInclusion>();
        required.add(new OBOTermInclusion(chromatogramTypeID, true, true, false));

        return required;
    }

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

    public void outputXML(BufferedWriter output, int indent, int index) throws IOException {
        MzMLContent.indent(output, indent);
        output.write("<chromatogram");
        output.write(" defaultArrayLength=\"" + defaultArrayLength + "\"");
        output.write(" id=\"" + XMLHelper.ensureSafeXML(id) + "\"");
        output.write(" index=\"" + index + "\"");
        if (dataProcessingRef != null) {
            output.write(" dataProcessingRef=\"" + XMLHelper.ensureSafeXML(dataProcessingRef.getID()) + "\"");
        }
        output.write(">\n");

        super.outputXML(output, indent + 1);

        if (precursor != null) {
            precursor.outputXML(output, indent + 1);
        }

        if (product != null) {
            product.outputXML(output, indent + 1);
        }

        if (binaryDataArrayList != null && binaryDataArrayList.size() > 0) {
            binaryDataArrayList.outputXML(output, indent + 1);
        }

        MzMLContent.indent(output, indent);
        output.write("</chromatogram>\n");
    }

    public String toString() {
        return "chromatogram: id=\"" + id + "\"";
    }

    private int getAdditionalChildrenCount() {
        int additionalChildren = ((precursor != null) ? 1 : 0)
                + ((product != null) ? 1 : 0)
                + ((binaryDataArrayList != null) ? 1 : 0);

        return additionalChildren;
    }

//	@Override
//	public int getChildCount() {
//		return super.getChildCount() + getAdditionalChildrenCount();
//	}
//	
//	@Override
//	public Enumeration<TreeNode> children() {
//		Vector<TreeNode> children = new Vector<TreeNode>();		
//		Enumeration<TreeNode> superChildren = super.children();
//		
//		while(superChildren.hasMoreElements())
//			children.add(superChildren.nextElement());
//		
//		if(precursor != null)
//			children.add(precursor);
//		if(product != null)
//			children.add(product);
//		if(binaryDataArrayList != null)
//			children.add(binaryDataArrayList);
//		
//		return children.elements();
//	}
//	
//	@Override
//	public TreeNode getChildAt(int index) {
//		if(index < super.getChildCount()) {
//			return super.getChildAt(index);
//		} else if(index < getChildCount()) {			
//			int counter = super.getChildCount();
//			
//			if(precursor != null) {
//				if(counter == index)
//					return precursor;
//				
//				counter++;
//			}
//			if(product != null) {
//				if(counter == index)
//					return product;
//				
//				counter++;
//			}
//			if(binaryDataArrayList != null) {
//				if(counter == index)
//					return binaryDataArrayList;
//				
//				counter++;
//			}
//		}
//		
//		return null;
//	}
//	
//	@Override
//	public int getIndex(TreeNode childNode) {
//		int counter = super.getChildCount();
//			
//		if(childNode instanceof Precursor)
//			return counter;
//		
//		if(precursor != null)
//			counter++;
//		
//		if(childNode instanceof Product)
//			return counter;
//		
//		if(product != null)
//			counter++;
//		
//		if(childNode instanceof BinaryDataArrayList)
//			return counter;
//		
//		if(binaryDataArrayList != null)
//			counter++;
//		
//		return super.getIndex(childNode);
//	}
}
