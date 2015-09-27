package com.alanmrace.jimzmlparser.mzML;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.tree.TreeNode;

public class InstrumentConfigurationList extends MzMLContent implements Iterable<InstrumentConfiguration>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private ArrayList<InstrumentConfiguration> instrumentConfigurationList;
	
	public InstrumentConfigurationList(int count) {
		instrumentConfigurationList = new ArrayList<InstrumentConfiguration>(count);
	}
	
	public InstrumentConfigurationList(InstrumentConfigurationList icList, ReferenceableParamGroupList rpgList, ScanSettingsList ssList, SoftwareList softwareList) {
		this(icList.size());
		
		for(InstrumentConfiguration ic : icList)
			instrumentConfigurationList.add(new InstrumentConfiguration(ic, rpgList, ssList, softwareList));
	}
	
	public void addInstrumentConfiguration(InstrumentConfiguration instrumentConfiguration) {
		instrumentConfiguration.setParent(this);
		
		instrumentConfigurationList.add(instrumentConfiguration);
	}
	
	public void removeInstrumentConfiguration(int index) {
		InstrumentConfiguration removed = instrumentConfigurationList.remove(index);
		
		removed.setParent(null);
	}
	
	public InstrumentConfiguration getInstrumentConfiguration(String id) {
		for(InstrumentConfiguration instrumentConfiguration : instrumentConfigurationList)
			if(instrumentConfiguration.getID().equals(id))
				return instrumentConfiguration;
				
		return null;
	}
	
	public InstrumentConfiguration getInstrumentConfiguration(int index) {
		if(index >= instrumentConfigurationList.size())
			return null;
		
		return instrumentConfigurationList.get(index);
	}
	
	public int size() {
		return instrumentConfigurationList.size();
	}
	
	public void outputXML(BufferedWriter output, int indent) throws IOException {
		MzMLContent.indent(output, indent);
		output.write("<instrumentConfigurationList");
		output.write(" count=\"" + instrumentConfigurationList.size() + "\"");
		output.write(">\n");
		
		for(InstrumentConfiguration ic : instrumentConfigurationList)
			ic.outputXML(output, indent+1);
		
		MzMLContent.indent(output, indent);
		output.write("</instrumentConfigurationList>\n");
	}

	@Override
	public Iterator<InstrumentConfiguration> iterator() {
		return instrumentConfigurationList.iterator();
	}
	
	public String toString() {
		return "instrumentConfigurationList";
	}
	
//	@Override
//	public int getChildCount() {
//		// 
//		return size();
//	}
//	
//	@Override
//	public int getIndex(TreeNode childNode) {
//		return instrumentConfigurationList.indexOf(childNode);
//	}
//	
//	@Override
//	public TreeNode getChildAt(int index) {
//		return instrumentConfigurationList.get(index);
//	}
//	
//	@Override
//	public Enumeration<TreeNode> children() {
//		Vector<TreeNode> children = new Vector<TreeNode>();
//		children.addAll(instrumentConfigurationList);
//		
//		return children.elements();
//	}
}
