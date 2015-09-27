package com.alanmrace.jimzmlparser.obo;

import com.alanmrace.jimzmlparser.util.XMLHelper;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Vector;

//import javax.swing.tree.MutableTreeNode;
//import javax.swing.tree.TreeNode;

//import com.fasterxml.jackson.annotation.JsonIgnore;

//import com.alanmrace.jimzmlparser.mzML.MzMLContent;

public class OBOTerm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private String id;
	private String name;
//	private String def;
	private String namespace;
//	private String comment;
	
	private String unitName = null;
	private OBOTerm units;
	
	private ArrayList<String> is_a;
	private ArrayList<String> part_of;
	private ArrayList<OBOTerm> children;
	private ArrayList<OBOTerm> parents;
	
	public OBOTerm(String id) {
		is_a = new ArrayList<String>();
		part_of = new ArrayList<String>();
		children = new ArrayList<OBOTerm>();
		parents = new ArrayList<OBOTerm>();
		
		this.id = id;
		
		int indexOfColon = id.indexOf(":");
		namespace = id.substring(0, indexOfColon).trim();
	}
	
	public void parse(String strippedLine) {
		int indexOfColon = strippedLine.indexOf(":");
		String tag = strippedLine.substring(0, indexOfColon).trim();
		String value = strippedLine.substring(indexOfColon+1).trim();
		
		// Strip comments
		int indexOfExclaimation = value.indexOf("!");
		
		if(indexOfExclaimation > -1)
			value = value.substring(0, indexOfExclaimation).trim();
		
		if(tag.equals("name")) {
			this.name = value;
		} else if(tag.equals("namespace")) {
			this.namespace = value;
		} else if(tag.equals("def")) {
//			this.def = value;
		} else if(tag.equals("comment")) {
//			this.comment = value;
		} else if(tag.equals("relationship")) {
			int indexOfSpace = value.indexOf(" ");
			String relationshipTag = value.substring(0, indexOfSpace).trim();
			String relationshipValue = value.substring(indexOfSpace+1).trim();
			
			if(relationshipTag.equals("is_a")) {
				is_a.add(relationshipValue);
			} else if(relationshipTag.equals("has_units")) {
				unitName = relationshipValue;
			} else if(relationshipTag.equals("part_of")) {
				part_of.add(relationshipValue);
			} else {
				//System.out.println("INFO: Relationship tag not implemented '" + relationshipTag + "'");
			}
		} else if(tag.equals("is_a")) {
			is_a.add(value);
		} else {
			//System.out.println("INFO: Tag not implemented '" + tag + "'");
		}
	}
	
//	private void addParentChildRelationship(OBO currentOBO, String id) {
//		// TODO: Implement is_a
//		OBOTerm term = currentOBO.getTerm(id);
//		
//		if(term == null) {
//			System.err.println("Haven't found " + id);
//		} else {
//			term.addChild(this);
//			is_a.add(term);
//		}
//	}
	
//	@JsonIgnore
	public void addChild(OBOTerm child) {
//		System.out.println("INFO: Adding child " + child.getID() + " to " + getID());
		children.add(child);
		
	}
	
//	@JsonIgnore
	public void addParent(OBOTerm parent) {
		parents.add(parent);
	}
	
//	@JsonIgnore
	public boolean isParentOf(String id) {
		if(this.id.equals(id))
			return true;
		
		for(OBOTerm child : getAllChildren())
			if(child.getID().equals(id))
				return true;
		
		return false;
	}
	
//	@JsonIgnore
	public boolean isChildOf(String id) {
		if(this.id.equals(id))
			return true;
		
		for(OBOTerm parent : getAllParents())
			if(parent.getID().equals(id))
				return true;
		
		return false;
	}
	
//	@JsonIgnore
	public Collection<OBOTerm> getChildren() {
		return children;
	}
	
//	@JsonIgnore
	public Collection<OBOTerm> getAllChildren() {
		ArrayList<OBOTerm> allChildren = new ArrayList<OBOTerm>();
		
		for(OBOTerm child : children)
			child.getAllChildren(allChildren);
		
		return allChildren;
	}
	
//	@JsonIgnore
	private void getAllChildren(ArrayList<OBOTerm> allChildren) {
		allChildren.add(this);
		
		for(OBOTerm child : children)
			child.getAllChildren(allChildren);
	}
	
//	@JsonIgnore
	public Collection<OBOTerm> getAllParents() {
		ArrayList<OBOTerm> allParents = new ArrayList<OBOTerm>();
		
		for(OBOTerm parent : parents)
			parent.getAllParents(allParents);
		
		return allParents;
	}
	
//	@JsonIgnore
	private void getAllParents(ArrayList<OBOTerm> allParents) {
		allParents.add(this);
		
		for(OBOTerm parent : parents)
			parent.getAllParents(allParents);
	}
	
//	@JsonIgnore
	public Collection<String> getAllChildNames() {
		ArrayList<String> allChildren = new ArrayList<String>();
		
		for(OBOTerm child : children)
			child.getAllChildNames(allChildren, 0);
		
		return allChildren;
	}
	
//	@JsonIgnore
	private void getAllChildNames(ArrayList<String> allChildren, int indent) {
		String indented = "";
		
		for(int i = 0; i < indent; i++)
			indented += "-";
		
		allChildren.add(indented + toString());
		
		for(OBOTerm child : children)
			child.getAllChildNames(allChildren, indent + 1);
	}
	
//	@JsonIgnore
	public Collection<String> getIsA() {
		return is_a;
	}
	
//	@JsonIgnore
	public Collection<String> getPartOf() {
		return part_of;
	}
	
	public String getID() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getUnitName() {
		return unitName;
	}
	
	public OBOTerm getUnits() {
		return units;
	}
	
	public String getNamespace() {
		return namespace;
	}
	
	public void setUnit(OBOTerm units) {
//		System.out.println(unitName + " " + units.getID() + " " + units.getName());
		this.units = units;
	}
	
	public String toString() {
		return "(" + id + ") " + name;
	}
	
	public void outputXML(BufferedWriter output, String value) throws IOException {
		output.write("<cvParam");
		
		output.write(" cvRef=\"" + XMLHelper.ensureSafeXML(namespace) + "\"");
		output.write(" accession=\"" + XMLHelper.ensureSafeXML(id) + "\"");
		output.write(" name=\"" + XMLHelper.ensureSafeXML(name) + "\"");
		
		if(value != null)
			output.write(" value=\"" + XMLHelper.ensureSafeXML(value) + "\"");
		
		if(getUnits() != null) {
			output.write(" unitCvRef=\"" + getUnits().getNamespace() + "\"");
			output.write(" unitAccession=\"" + getUnits().getID() + "\"");
			output.write(" unitName=\"" + getUnits().getName() + "\"");
		}
		
		output.write("/>\n");
	}
	
	public boolean equals(Object o) {
		if(o == this)
			return true;
		
		if(!(o instanceof OBOTerm))
			return false;
		
		OBOTerm term = (OBOTerm)o;
		
		return term.getID().equals(id) && term.getNamespace().equals(namespace);
	}

//	@Override
//	@JsonIgnore
//	public Enumeration<TreeNode> children() {
//		Vector<TreeNode> child = new Vector<TreeNode>();
//		child.addAll(children);
//		
//		return child.elements();
//	}
//
//	@Override
////	@JsonIgnore
//	public boolean getAllowsChildren() {
//		return true;
//	}
//
//	@Override
////	@JsonIgnore
//	public TreeNode getChildAt(int index) {
//		return children.get(index);
//	}
//
//	@Override
////	@JsonIgnore
//	public int getChildCount() {
//		return children.size();
//	}
//
//	@Override
////	@JsonIgnore
//	public int getIndex(TreeNode child) {
//		return children.indexOf(child);
//	}
//
//	@Override
////	@JsonIgnore
//	public TreeNode getParent() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
////	@JsonIgnore
//	public boolean isLeaf() {
//		return (children.size() == 0);
//	}
//
//	@Override
////	@JsonIgnore
//	public void insert(MutableTreeNode child, int index) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
////	@JsonIgnore
//	public void remove(int index) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
////	@JsonIgnore
//	public void remove(MutableTreeNode node) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
////	@JsonIgnore
//	public void removeFromParent() {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
////	@JsonIgnore
//	public void setParent(MutableTreeNode newParent) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
////	@JsonIgnore
//	public void setUserObject(Object object) {
//		// TODO Auto-generated method stub
//		
//	}

}
