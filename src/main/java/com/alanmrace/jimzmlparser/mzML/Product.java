package com.alanmrace.jimzmlparser.mzML;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.tree.TreeNode;

public class Product extends MzMLContent  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private IsolationWindow isolationWindow;
	
	public Product() {
	}
	
	public Product(Product product, ReferenceableParamGroupList rpgList) {
		if(product.isolationWindow != null)
			isolationWindow = new IsolationWindow(product.isolationWindow, rpgList);
	}
	
	public void setIsolationWindow(IsolationWindow isolationWindow) {
		isolationWindow.setParent(this);
		
		this.isolationWindow = isolationWindow;
	}
	
	public IsolationWindow getIsolationWindow() {
		return isolationWindow;
	}
	
	public void outputXML(BufferedWriter output, int indent) throws IOException {
		MzMLContent.indent(output, indent);
		output.write("<product>\n");
		
		if(isolationWindow != null)
			isolationWindow.outputXML(output, indent+1);
		
		MzMLContent.indent(output, indent);
		output.write("</product>\n");
	}
	
	public String toString() {
		return "product";
	}
	
	private int getAdditionalChildrenCount() {
		int additionalChildren = ((isolationWindow != null)? 1 : 0);
		
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
//		if(isolationWindow != null)
//			children.add(isolationWindow);
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
//			if(isolationWindow != null) {
//				if(counter == index)
//					return isolationWindow;
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
//		if(childNode instanceof IsolationWindow)
//			return counter;
//		
//		if(isolationWindow != null)
//			counter++;
//		
//		return super.getIndex(childNode);
//	}
}
