package com.alanmrace.jimzmlparser.mzML;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.Enumeration;

//import javax.swing.tree.MutableTreeNode;
//import javax.swing.tree.TreeNode;

import com.alanmrace.jimzmlparser.obo.OBOTerm;
import com.alanmrace.jimzmlparser.util.XMLHelper;

public class UserParam implements Serializable { //, MutableTreeNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;
	private String type;
	private OBOTerm units;
	private String value;

//	private MutableTreeNode parentTreeNode;
	
	public UserParam(String name) {
		this.name = name;
	}
	
	public UserParam(String name, String value) {
		this(name);
		
		this.value = value;
	}
	
	public UserParam(String name, String value, OBOTerm units) {
		this(name, value);
		
		this.units = units;
	}
	
	public UserParam(UserParam userParam) {
		this.name = userParam.name;
		this.type = userParam.type;
		this.units = userParam.units;
		this.value = userParam.value;
	}
	
	public String getName() {
		return name;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
	
	public void setUnits(OBOTerm units) {
		this.units = units;
	}
	
	public OBOTerm getUnits() {
		return units;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	public void outputXML(BufferedWriter output) throws IOException {
		output.write("<userParam");
		
		output.write(" name=\"" + XMLHelper.ensureSafeXML(name) + "\"");
		
		if(type != null)
			output.write(" type=\"" + XMLHelper.ensureSafeXML(type) + "\"");
		
		if(value != null)
			output.write(" value=\"" + XMLHelper.ensureSafeXML(value) + "\"");
		
		if(units != null) {
			output.write(" unitCvRef=\"" + XMLHelper.ensureSafeXML(units.getNamespace()) + "\"");
			output.write(" unitAccession=\"" + XMLHelper.ensureSafeXML(units.getID()) + "\"");
			output.write(" unitName=\"" + XMLHelper.ensureSafeXML(units.getName()) + "\"");
		}
		
		output.write("/>\n");
	}
	
	public String toString() {
		return "userParam: " + name + ((value != null && !value.isEmpty()) ? (" - " + value) : "" );
	}
	
//	@Override
//	public Enumeration<TreeNode> children() {
//		// userParams are always leaves
//		return null;
//	}
//
//	@Override
//	public boolean getAllowsChildren() {
//		// userParams are always leaves
//		return false;
//	}
//
//	@Override
//	public TreeNode getChildAt(int index) {
//		// userParams are always leaves
//		return null;
//	}
//
//	@Override
//	public int getChildCount() {
//		// userParams are always leaves
//		return 0;
//	}
//
//	@Override
//	public int getIndex(TreeNode childNode) {
//		// Has no children, so can be ignored
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
//		// userParams are always leaves
//		return true;
//	}
//
//	@Override
//	public void insert(MutableTreeNode arg0, int arg1) {
//		// Doesn't have children, so insert should do nothing
//	}
//
//	@Override
//	public void remove(int arg0) {
//		// Has no children, so shouldn't require removing
//		
//	}
//
//	@Override
//	public void remove(MutableTreeNode child) {
//		// Has no children, so shouldn't require removing
//	}
//
//	@Override
//	public void removeFromParent() {
//		this.parentTreeNode.remove(this);
//	}
//
//	@Override
//	public void setParent(MutableTreeNode parent) {
//		this.parentTreeNode = parent;
//	}
//
//	@Override
//	public void setUserObject(Object arg0) {
//		// TODO Auto-generated method stub
//		
//	}
}
