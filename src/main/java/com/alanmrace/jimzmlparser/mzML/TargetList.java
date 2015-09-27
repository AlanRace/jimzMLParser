package com.alanmrace.jimzmlparser.mzML;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.tree.TreeNode;

public class TargetList extends MzMLContent  implements Serializable, Iterable<Target> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private ArrayList<Target> targetList;
	
	public TargetList(int count) {
		targetList = new ArrayList<Target>(count);
	}
	
	public TargetList(TargetList targetList, ReferenceableParamGroupList rpgList) {
		this(targetList.size());
		
		for(Target target : targetList)
			this.targetList.add(new Target(target, rpgList));
	}
	
	public int size() {
		return targetList.size();
	}
	
	public void addTarget(Target target) {
		target.setParent(this);
		
		targetList.add(target);
	}
	
	public void outputXML(BufferedWriter output, int indent) throws IOException {
		MzMLContent.indent(output, indent);
		output.write("<targetList");
		output.write(" count=\"" + targetList.size() + "\"");
		output.write(">\n");
		
		for(Target target : targetList)
			target.outputXML(output, indent+1);
		
		MzMLContent.indent(output, indent);
		output.write("</targetList>\n");
	}
	
	public String toString() {
		return "targetList";
	}
	
//	@Override
//	public int getChildCount() {
//		// 
//		return size();
//	}
//	
//	@Override
//	public int getIndex(TreeNode childNode) {
//		return targetList.indexOf(childNode);
//	}
//	
//	@Override
//	public TreeNode getChildAt(int index) {
//		return targetList.get(index);
//	}
//	
//	@Override
//	public Enumeration<TreeNode> children() {
//		Vector<TreeNode> children = new Vector<TreeNode>();
//		children.addAll(targetList);
//		
//		return children.elements();
//	}

	@Override
	public Iterator<Target> iterator() {
		return this.targetList.iterator();
	}
}
