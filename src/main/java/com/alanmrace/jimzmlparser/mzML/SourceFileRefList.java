package com.alanmrace.jimzmlparser.mzML;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.tree.TreeNode;

public class SourceFileRefList extends MzMLContent  implements Serializable, Iterable<SourceFileRef> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private ArrayList<SourceFileRef> sourceFileRefList;
	
	public SourceFileRefList(int count) {
		sourceFileRefList = new ArrayList<SourceFileRef>(count);
	}
	
	public SourceFileRefList(SourceFileRefList sourceFileRefList, SourceFileList sourceFileList) {
		this.sourceFileRefList = new ArrayList<SourceFileRef>(sourceFileRefList.size());
		
		for(SourceFileRef ref : sourceFileRefList) {
			for(SourceFile sourceFile : sourceFileList)
				if(ref.getRef().getID().equals(sourceFile.getID()))
					this.sourceFileRefList.add(new SourceFileRef(sourceFile));
		}
	}
	
	public int size() {
		return sourceFileRefList.size();
	}
	
	public void addSourceFileRef(SourceFileRef sourceFileRef) {
//		sourceFileRef.setParent(this);
		
		sourceFileRefList.add(sourceFileRef);
	}
	
	public void outputXML(BufferedWriter output, int indent) throws IOException {
		MzMLContent.indent(output, indent);
		output.write("<sourceFileRefList");
		output.write(" count=\"" + sourceFileRefList.size() + "\"");
		output.write(">\n");
		
		for(SourceFileRef sourceFileRef : sourceFileRefList) {
			MzMLContent.indent(output, indent+1);
			sourceFileRef.outputXML(output);
		}
		
		MzMLContent.indent(output, indent);
		output.write("</sourceFileRefList>\n");
	}
	
	public String toString() {
		return "sourceFileRefList";
	}
	
//	@Override
//	public int getChildCount() {
//		// 
//		return size();
//	}
//	
//	@Override
//	public int getIndex(TreeNode childNode) {
//		return sourceFileRefList.indexOf(childNode);
//	}
//	
//	@Override
//	public TreeNode getChildAt(int index) {
//		return sourceFileRefList.get(index);
//	}
//	
//	@Override
//	public Enumeration<TreeNode> children() {
//		Vector<TreeNode> children = new Vector<TreeNode>();
//		children.addAll(sourceFileRefList);
//		
//		return children.elements();
//	}

	@Override
	public Iterator<SourceFileRef> iterator() {
		return sourceFileRefList.iterator();
	}
}
