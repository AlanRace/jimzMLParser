package com.alanmrace.jimzmlparser.mzML;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.tree.TreeNode;

public class DataProcessingList extends MzMLContent implements Iterable<DataProcessing>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public ArrayList<DataProcessing> dataProcessingList;
	
	public DataProcessingList(int count) {
		dataProcessingList = new ArrayList<DataProcessing>(count);
	}
	
	public DataProcessingList(DataProcessingList dpList, ReferenceableParamGroupList rpgList, SoftwareList softwareList) {
		this(dpList.size());
		
		for(DataProcessing dp : dpList)
			dataProcessingList.add(new DataProcessing(dp, rpgList, softwareList));
	}
	
	public DataProcessing getDataProcessing(String dataProcessingRef) {
		for(DataProcessing dp : dataProcessingList)
			if(dp.getID().equals(dataProcessingRef))
				return dp;
		
		return null;
	}
	
	public DataProcessing getDataProcessing(int index) {
		return dataProcessingList.get(index);
	}
	
	public void addDataProcessing(DataProcessing dataProcessing) {
		dataProcessing.setParent(this);
		
		dataProcessingList.add(dataProcessing);
	}
	
	public void removeDataProcessing(int index) {
		DataProcessing removed = dataProcessingList.remove(index);
		removed.setParent(null);
	}
	
	public int size() { 
		return dataProcessingList.size();
	}
	
	public void outputXML(BufferedWriter output, int indent) throws IOException {
		MzMLContent.indent(output, indent);
		output.write("<dataProcessingList");
		output.write(" count=\"" + dataProcessingList.size() + "\"");
		output.write(">\n");
		
		for(DataProcessing dp : dataProcessingList)
			dp.outputXML(output, indent+1);
		
		MzMLContent.indent(output, indent);
		output.write("</dataProcessingList>\n");
	}

	@Override
	public Iterator<DataProcessing> iterator() {
		return dataProcessingList.iterator();
	}
	
	public String toString() {
		return "dataProcessingList";
	}
	
//	@Override
//	public int getChildCount() {
//		// 
//		return size();
//	}
//	
//	@Override
//	public int getIndex(TreeNode childNode) {
//		return dataProcessingList.indexOf(childNode);
//	}
//	
//	@Override
//	public TreeNode getChildAt(int index) {
//		return dataProcessingList.get(index);
//	}
//	
//	@Override
//	public Enumeration<TreeNode> children() {
//		Vector<TreeNode> children = new Vector<TreeNode>();
//		children.addAll(dataProcessingList);
//		
//		return children.elements();
//	}
}
