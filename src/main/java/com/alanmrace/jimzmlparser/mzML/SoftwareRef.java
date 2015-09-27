package com.alanmrace.jimzmlparser.mzML;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;

//import com.fasterxml.jackson.annotation.JsonIgnore;

public class SoftwareRef implements Serializable { //, MutableTreeNode {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Software ref;
	
//	private MutableTreeNode parentTreeNode;
	
	public SoftwareRef(Software ref) {
		this.ref = ref;
	}
	
	public Software getRef() {
		return ref;
	}
	
	public void outputXML(BufferedWriter output) throws IOException {
		output.write("<softwareRef");
		output.write(" ref=\"" + ref.getID() + "\"");
		output.write("/>\n");
	}
	
        @Override
	public String toString() {
		return "softwareRef: " + ref.getID();
	}

//	@Override
//	@JsonIgnore
//	public Enumeration<TreeNode> children() {
//		Vector<TreeNode> children = new Vector<TreeNode>();
//		children.add(ref);
//		
//		return children.elements();
//	}
//
//	@Override
//	@JsonIgnore
//	public boolean getAllowsChildren() {
//		return true;
//	}
//
//	@Override
//	@JsonIgnore
//	public TreeNode getChildAt(int childIndex) {
//		return ref;
//	}
//
//	@Override
//	@JsonIgnore
//	public int getChildCount() {
//		// Always 1 - the Software
//		return 1;
//	}
//
//	@Override
//	@JsonIgnore
//	public int getIndex(TreeNode node) {
//		return 0;
//	}
//
//	@Override
//	@JsonIgnore
//	public TreeNode getParent() {
//		return parentTreeNode;
//	}
//
//	@Override
//	@JsonIgnore
//	public boolean isLeaf() {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	@JsonIgnore
//	public void insert(MutableTreeNode child, int index) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	@JsonIgnore
//	public void remove(int index) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	@JsonIgnore
//	public void remove(MutableTreeNode node) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	@JsonIgnore
//	public void removeFromParent() {
//		parentTreeNode.remove(this);
//	}
//
//	@Override
//	@JsonIgnore
//	public void setParent(MutableTreeNode newParent) {
//		this.parentTreeNode = newParent;
//	}
//
//	@Override
//	@JsonIgnore
//	public void setUserObject(Object object) {
//		// TODO Auto-generated method stub
//		
//	}
}
