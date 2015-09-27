package com.alanmrace.jimzmlparser.mzML;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.tree.TreeNode;

public class PrecursorList extends MzMLContent  implements Serializable, Iterable<Precursor> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private ArrayList<Precursor> precursorList;
	
	public PrecursorList(int count) {
		precursorList = new ArrayList<Precursor>(count);
	}
	
	public PrecursorList(PrecursorList precursorList, ReferenceableParamGroupList rpgList, SourceFileList sourceFileList) {
		this(precursorList.size());
		
		for(Precursor precursor : precursorList)
			this.precursorList.add(new Precursor(precursor, rpgList, sourceFileList));
	}
	
	public int size() {
		return precursorList.size();
	}
	
	public void addPrecursor(Precursor precursor) {
		precursor.setParent(this);
		
		precursorList.add(precursor);
	}
	
	public Precursor getPrecursor(int index) {
		return precursorList.get(index);
	}
	
	public void outputXML(BufferedWriter output, int indent) throws IOException {
		MzMLContent.indent(output, indent);
		output.write("<precursorList");
		output.write(" count=\"" + precursorList.size() + "\"");
		output.write(">\n");
		
		for(Precursor precursor : precursorList)
			precursor.outputXML(output, indent+1);
		
		MzMLContent.indent(output, indent);
		output.write("</precursorList>\n");
	}
	
	public String toString() {
		return "precursorList";
	}
	
//	@Override
//	public int getChildCount() {
//		// 
//		return size();
//	}
//	
//	@Override
//	public int getIndex(TreeNode childNode) {
//		return precursorList.indexOf(childNode);
//	}
//	
//	@Override
//	public TreeNode getChildAt(int index) {
//		return precursorList.get(index);
//	}
//	
//	@Override
//	public Enumeration<TreeNode> children() {
//		Vector<TreeNode> children = new Vector<TreeNode>();
//		children.addAll(precursorList);
//		
//		return children.elements();
//	}

	@Override
	public Iterator<Precursor> iterator() {
		return precursorList.iterator();
	}
}
