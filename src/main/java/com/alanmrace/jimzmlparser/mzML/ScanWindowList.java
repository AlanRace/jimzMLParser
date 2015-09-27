package com.alanmrace.jimzmlparser.mzML;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.tree.TreeNode;

public class ScanWindowList extends MzMLContent  implements Serializable, Iterable<ScanWindow> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private ArrayList<ScanWindow> scanWindowList;
	
	public ScanWindowList(int count) {
		scanWindowList = new ArrayList<ScanWindow>(count);
	}
	
	public ScanWindowList(ScanWindowList scanWindowList, ReferenceableParamGroupList rpgList) {
		this(scanWindowList.size());
		
		for(ScanWindow scanWindow : scanWindowList) {
			this.scanWindowList.add(new ScanWindow(scanWindow, rpgList));
		}
	}
	
	public int size() {
		return scanWindowList.size();
	}
	
	public void addScanWindow(ScanWindow scanWindow) {
		scanWindow.setParent(this);
		
		scanWindowList.add(scanWindow);
	}
	
	public ScanWindow getScanWindow(int index) {
		return scanWindowList.get(index);
	}
	
	public void outputXML(BufferedWriter output, int indent) throws IOException {
		MzMLContent.indent(output, indent);
		output.write("<scanWindowList");
		output.write(" count=\"" + scanWindowList.size() + "\"");
		output.write(">\n");
		
		for(ScanWindow scanWindow : scanWindowList)
			scanWindow.outputXML(output, indent+1);
		
		MzMLContent.indent(output, indent);
		output.write("</scanWindowList>\n");
	}
	
	public String toString() {
		return "scanWindowList";
	}
	
//	@Override
//	public int getChildCount() {
//		// 
//		return size();
//	}
//	
//	@Override
//	public int getIndex(TreeNode childNode) {
//		return scanWindowList.indexOf(childNode);
//	}
//	
//	@Override
//	public TreeNode getChildAt(int index) {
//		return scanWindowList.get(index);
//	}
//	
//	@Override
//	public Enumeration<TreeNode> children() {
//		Vector<TreeNode> children = new Vector<TreeNode>();
//		children.addAll(scanWindowList);
//		
//		return children.elements();
//	}

	@Override
	public Iterator<ScanWindow> iterator() {
		return this.scanWindowList.iterator();
	}
}
