package com.alanmrace.jimzmlparser.mzML;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class CVList extends MzMLContent  implements Serializable, Iterable<CV> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private ArrayList<CV> cvList;
	
	public CVList(int count) {
		cvList = new ArrayList<CV>(count);
	}
	
	public CVList(CVList cvList) {
		this.cvList = new ArrayList<CV>(cvList.size());
		
		for(CV cv : cvList)
			this.cvList.add(cv);
	}
	
	public void addCV(CV cv) {
		cv.setParent(this);
		
		cvList.add(cv);
	}
	
	public CV getCV(int index) {
		return cvList.get(index);
	}
	
	public int getIndexOf(CV cv) {
		return cvList.indexOf(cv);
	}
	
	public int size() {
		return cvList.size();
	}
	
	public void outputXML(BufferedWriter output, int indent) throws IOException {
		MzMLContent.indent(output, indent);
		output.write("<cvList");
		output.write(" count=\"" + cvList.size() + "\"");
		output.write(">\n");
		
		for(CV cv : cvList)
			cv.outputXML(output, indent+1);
		
		MzMLContent.indent(output, indent);
		output.write("</cvList>\n");
	}
	
	public String toString() {
		return "cvList";
	}
	
//	@Override
//	public int getChildCount() {
//		return size();
//	}
//	
//	@Override
//	public int getIndex(TreeNode childNode) {
//		return cvList.indexOf(childNode);
//	}
//	
//	@Override
//	public TreeNode getChildAt(int index) {
//		return cvList.get(index);
//	}
//	
//	@Override
//	public Enumeration<TreeNode> children() {
//		Vector<TreeNode> children = new Vector<TreeNode>();
//		children.addAll(cvList);
//		
//		return children.elements();
//	}

	@Override
	public Iterator<CV> iterator() {
		return cvList.iterator();
	}
}
