package com.alanmrace.jimzmlparser.mzML;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.tree.TreeNode;

/**
 * BinaryDataArrayList tag.
 * 
 * @author Alan Race
 */
public class BinaryDataArrayList extends MzMLContent implements Iterable<BinaryDataArray>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BinaryDataArray mzArray;
	private BinaryDataArray intensityArray;

	/** The binaryDataArray list. */
	private ArrayList<BinaryDataArray> binaryDataArrayList;
	
	/**
	 * Instantiates a new binaryDataArrayList tag.
	 *
	 * @param count the size of the list
	 */
	public BinaryDataArrayList(int count) {
		binaryDataArrayList = new ArrayList<BinaryDataArray>(count);
	}
	
	public BinaryDataArrayList(BinaryDataArrayList bdaList, ReferenceableParamGroupList rpgList, DataProcessingList dpList) {
		this(bdaList.size());
		
		for(BinaryDataArray bda : bdaList) {
			this.binaryDataArrayList.add(new BinaryDataArray(bda, rpgList, dpList));
		}
	}
	
	/**
	 * Get the number of binaryDataArrayList sub-elements.
	 *
	 * @return the size of the list
	 */
	public int size() {
		return binaryDataArrayList.size();
	}
	
	/**
	 * Adds a binaryDataArray tag.
	 *
	 * @param binaryDataArray the binaryDataArray tag
	 */
	public void addBinaryDataArray(BinaryDataArray binaryDataArray) {
		binaryDataArray.setParent(this);
		
		binaryDataArrayList.add(binaryDataArray);
	}
	
	public BinaryDataArray getmzArray() {
		if(mzArray == null) {
			for(BinaryDataArray binaryDataArray : binaryDataArrayList) {
				if(binaryDataArray.ismzArray()) {
					mzArray = binaryDataArray;
					break;
				}
			}
		}
		
		return mzArray;
	}
	
	public BinaryDataArray getIntensityArray() {
		if(intensityArray == null) {
			for(BinaryDataArray binaryDataArray : binaryDataArrayList) {
				if(binaryDataArray.isIntensityArray()) {
					intensityArray = binaryDataArray;
					break;
				}
			}
		}
		
		return intensityArray;
	}
	
	public void updatemzAndIntensityArray() {
		for(BinaryDataArray binaryDataArray : binaryDataArrayList) {
			if(binaryDataArray.ismzArray())
				mzArray = binaryDataArray;
			else if(binaryDataArray.isIntensityArray()) 
				intensityArray = binaryDataArray;
		}
	}
	
	/**
	 * Gets the binaryDataArray.
	 *
	 * @param index the index
	 * @return the binaryDataArray
	 */
	public BinaryDataArray getBinaryDataArray(int index) {
		return binaryDataArrayList.get(index);
	}
	
	/* (non-Javadoc)
	 * @see mzML.MzMLContent#outputXML(java.io.BufferedWriter, int)
	 */
	public void outputXML(BufferedWriter output, int indent) throws IOException {
		MzMLContent.indent(output, indent);
		output.write("<binaryDataArrayList");
		output.write(" count=\"" + binaryDataArrayList.size() + "\"");
		output.write(">\n");
		
		for(BinaryDataArray binaryDataArray : binaryDataArrayList)
			binaryDataArray.outputXML(output, indent+1);
		
		MzMLContent.indent(output, indent);
		output.write("</binaryDataArrayList>\n");
	}

	@Override
	public Iterator<BinaryDataArray> iterator() {
		return binaryDataArrayList.iterator();
	}
	
	public String toString() {
		return "binaryDataArrayList";
	}
	
//	@Override
//	public int getChildCount() {
//		// 
//		return size();
//	}
//	
//	@Override
//	public int getIndex(TreeNode childNode) {
//		return binaryDataArrayList.indexOf(childNode);
//	}
//	
//	@Override
//	public TreeNode getChildAt(int index) {
//		return binaryDataArrayList.get(index);
//	}
//	
//	@Override
//	public Enumeration<TreeNode> children() {
//		Vector<TreeNode> children = new Vector<TreeNode>();
//		children.addAll(binaryDataArrayList);
//		
//		return children.elements();
//	}
}
