package com.alanmrace.jimzmlparser.mzML;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

public class SourceFileRef implements Serializable { //, MutableTreeNode {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private SourceFile ref;
	
	private MutableTreeNode parentTreeNode;
	
	public SourceFileRef(SourceFile ref) {
		this.ref = ref;
	}
	
	public SourceFile getRef() {
		return ref;
	}
	
	public void outputXML(BufferedWriter output) throws IOException {
		output.write("<sourceFileRef");
		output.write(" ref=\"" + ref.getID() + "\"");
		output.write("/>\n");
	}
	
	public String toString() {
		return "sourceFileRef: " + ref.getID();
	}

//	@Override
//	public Enumeration<TreeNode> children() {
//		Vector<TreeNode> children = new Vector<TreeNode>();
//		children.add(ref);
//		
//		return children.elements();
//	}
//
//	@Override
//	public boolean getAllowsChildren() {
//		return true;
//	}
//
//	@Override
//	public TreeNode getChildAt(int childIndex) {
//		return ref;
//	}
//
//	@Override
//	public int getChildCount() {
//		// Always 1 - the SourceFile
//		return 1;
//	}
//
//	@Override
//	public int getIndex(TreeNode node) {
//		return 0;
//	}
//
//	@Override
//	public TreeNode getParent() {
//		return parentTreeNode;
//	}
//
//	@Override
//	public boolean isLeaf() {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public void insert(MutableTreeNode child, int index) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void remove(int index) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void remove(MutableTreeNode node) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void removeFromParent() {
//		parentTreeNode.remove(this);
//	}
//
//	@Override
//	public void setParent(MutableTreeNode newParent) {
//		this.parentTreeNode = newParent;
//	}
//
//	@Override
//	public void setUserObject(Object object) {
//		// TODO Auto-generated method stub
//		
//	}
}
