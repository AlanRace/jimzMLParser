package com.alanmrace.jimzmlparser.mzML;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.tree.TreeNode;

public class ScanList extends MzMLContent implements Iterable<Scan>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static String spectraCombinationID		= "MS:1000570";

	protected ArrayList<Scan> scanList;
	
	public ScanList(int count) {
		scanList = new ArrayList<Scan>(count);
	}
	
	public ScanList(ScanList scanList, ReferenceableParamGroupList rpgList, SourceFileList sourceFileList, 
			InstrumentConfigurationList icList) {
		super(scanList, rpgList);
		
		this.scanList = new ArrayList<Scan>(scanList.size());
		
		for(Scan scan : scanList) {
			this.scanList.add(new Scan(scan, rpgList, icList, sourceFileList));
		}
	}
	
	public ArrayList<OBOTermInclusion> getListOfRequiredCVParams() {
		ArrayList<OBOTermInclusion> required = new ArrayList<OBOTermInclusion>();
		required.add(new OBOTermInclusion(spectraCombinationID, true, true, false));
				
		return required; 
	}
	
	public void addScan(Scan scan) {
		scan.setParent(this);
		
		scanList.add(scan);
	}
	
	public int size() {
		return scanList.size();
	}
	
	public Scan getScan(int index) {
		return scanList.get(index);
	}
	
//	public double getScanStartTime() {
//		// TODO: Take into account multiple scans
//		if(scanList.size() > 0)
//			return scanList.get(0).getScanStartTime();
//		
//		return -1;
//	}
	
	public void outputXML(BufferedWriter output, int indent) throws IOException {
		MzMLContent.indent(output, indent);
		output.write("<scanList");
		output.write(" count=\"" + scanList.size() + "\"");
		output.write(">\n");
		
		super.outputXML(output, indent+1);
		
		for(Scan scan : scanList)
			scan.outputXML(output, indent+1);
		
		MzMLContent.indent(output, indent);
		output.write("</scanList>\n");
	}

	@Override
	public Iterator<Scan> iterator() {
		return scanList.iterator();
	}
	
	public String toString() {
		return "scanList";
	}
	
	private int getAdditionalChildrenCount() {
		int additionalChildren = ((scanList != null)? scanList.size() : 0);
		
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
//		if(scanList != null)
//			children.addAll(scanList);
//		
//		return children.elements();
//	}
//	
//	@Override
//	public TreeNode getChildAt(int index) {
//		if(index < super.getChildCount()) {
//			return super.getChildAt(index);
//		} else if(index < getChildCount()) {			
//			return scanList.get(index - super.getChildCount());
//		}
//		
//		return null;
//	}
//	
//	@Override
//	public int getIndex(TreeNode childNode) {			
//		if(childNode instanceof Scan)
//			return scanList.indexOf(childNode);
//		
//		return super.getIndex(childNode);
//	}
}
