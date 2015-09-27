package com.alanmrace.jimzmlparser.mzML;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.tree.TreeNode;

public class SoftwareList extends MzMLContent implements Iterable<Software>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private ArrayList<Software> softwareList;
	
	public SoftwareList(int count) {
		softwareList = new ArrayList<Software>(count);
	}
	
	public SoftwareList(SoftwareList softwareList, ReferenceableParamGroupList rpgList) {
		this.softwareList = new ArrayList<Software>(softwareList.size());
		
		for(Software software : softwareList)
			this.softwareList.add(new Software(software, rpgList));
	}
	
	public void addSoftware(Software software) {
		software.setParent(this);
		
		softwareList.add(software);
	}
	
	public void removeSoftware(int index) {
		Software removed = softwareList.remove(index);
		
		removed.setParent(null);
	}
	
	public Software getSoftware(String id) {
		for(Software software : softwareList)
			if(software.getID().equals(id))
				return software;
				
		return null;
	}
	
	public Software getSoftware(int index) {
		return softwareList.get(index);
	}
	
	public int size() {
		return softwareList.size();
	}
	
	public void outputXML(BufferedWriter output, int indent) throws IOException {
		MzMLContent.indent(output, indent);
		output.write("<softwareList");
		output.write(" count=\"" + softwareList.size() + "\"");
		output.write(">\n");
		
		for(Software software : softwareList)
			software.outputXML(output, indent+1);
		
		MzMLContent.indent(output, indent);
		output.write("</softwareList>\n");
	}

	@Override
	public Iterator<Software> iterator() {
		return softwareList.iterator();
	}
	
	public String toString() {
		return "softwareList";
	}
	
//	@Override
//	public int getChildCount() {
//		// 
//		return size();
//	}
//	
//	@Override
//	public int getIndex(TreeNode childNode) {
//		return softwareList.indexOf(childNode);
//	}
//	
//	@Override
//	public TreeNode getChildAt(int index) {
//		return softwareList.get(index);
//	}
//	
//	@Override
//	public Enumeration<TreeNode> children() {
//		Vector<TreeNode> children = new Vector<TreeNode>();
//		children.addAll(softwareList);
//		
//		return children.elements();
//	}
}
