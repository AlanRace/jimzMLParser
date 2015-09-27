package com.alanmrace.jimzmlparser.mzML;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.tree.TreeNode;

public class SelectedIonList extends MzMLContent  implements Serializable, Iterable<SelectedIon> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private ArrayList<SelectedIon> selectedIonList;
	
	public SelectedIonList(int count) {
		selectedIonList = new ArrayList<SelectedIon>(count);
	}
	
	public SelectedIonList(SelectedIonList siList, ReferenceableParamGroupList rpgList) {
		this(siList.size());
		
		for(SelectedIon si : siList)
			this.selectedIonList.add(new SelectedIon(si, rpgList));
	}
	
	public int size() {
		return selectedIonList.size();
	}
	
	public void addSelectedIon(SelectedIon selectedIon) {
		selectedIon.setParent(this);
		
		selectedIonList.add(selectedIon);
	}
	
	public SelectedIon getSelectedIon(int index) {
		return selectedIonList.get(index);
	}
	
	public void outputXML(BufferedWriter output, int indent) throws IOException {
		MzMLContent.indent(output, indent);
		output.write("<selectedIonList");
		output.write(" count=\"" + selectedIonList.size() + "\"");
		output.write(">\n");
		
		for(SelectedIon selectedIon : selectedIonList)
			selectedIon.outputXML(output, indent+1);
	
		MzMLContent.indent(output, indent);
		output.write("</selectedIonList>\n");
	}
		
	public String toString() {
		return "selectedIonList";
	}
	
//	@Override
//	public int getChildCount() {
//		// 
//		return size();
//	}
//	
//	@Override
//	public int getIndex(TreeNode childNode) {
//		return selectedIonList.indexOf(childNode);
//	}
//	
//	@Override
//	public TreeNode getChildAt(int index) {
//		return selectedIonList.get(index);
//	}
//	
//	@Override
//	public Enumeration<TreeNode> children() {
//		Vector<TreeNode> children = new Vector<TreeNode>();
//		children.addAll(selectedIonList);
//		
//		return children.elements();
//	}

	@Override
	public Iterator<SelectedIon> iterator() {
		return selectedIonList.iterator();
	}
}
