package com.alanmrace.jimzmlparser.mzML;


import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.tree.TreeNode;

public class FileDescription extends MzMLContent  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	// Required
	protected FileContent fileContent;
	
	// Optional
	protected SourceFileList sourceFileList;
	protected ArrayList<Contact> contacts;
	
	public FileDescription() {
		contacts = new ArrayList<Contact>();
	}
	
	public FileDescription(FileDescription fileDescription, ReferenceableParamGroupList rpgList) {
		fileContent = new FileContent(fileDescription.fileContent, rpgList);
		
		if(fileDescription.sourceFileList != null)
			sourceFileList = new SourceFileList(fileDescription.sourceFileList, rpgList);
		
		contacts = new ArrayList<Contact>(fileDescription.contacts.size());
		
		for(Contact contact : fileDescription.contacts)
			contacts.add(new Contact(contact, rpgList));
	}

	public void setFileContent(FileContent fileContent) {
		fileContent.setParent(this);
		
		this.fileContent = fileContent;
	}
	
	public FileContent getFileContent() {
		return fileContent;
	}
	
	public SourceFileList getSourceFileList() {
		return sourceFileList;
	}
	
	public void setSourceFileList(SourceFileList sourceFileList) {
		sourceFileList.setParent(this);
		
		this.sourceFileList = sourceFileList;
	}
	
//	public void addSourceFile(SourceFile sf) {
//		sourceFileList.addSourceFile(sf);
//	}
	
	public void addContact(Contact contact) {
		contact.setParent(this);
		
		contacts.add(contact);
	}
	
	public void removeContact(int index) {
		Contact removed = contacts.remove(index);
		
		removed.setParent(null);
	}
	
	public Contact getContact(int index) {
		return contacts.get(index);
	}
	
	public int getNumberOfContacts() {
		return contacts.size();
	}
	
	public void outputXML(BufferedWriter output, int indent) throws IOException {
		MzMLContent.indent(output, indent);
		output.write("<fileDescription");
		output.write(">\n");
		
		fileContent.outputXML(output, indent+1);
		
		if(sourceFileList != null && sourceFileList.size() > 0)
			sourceFileList.outputXML(output, indent+1);
		
		for(Contact contact : contacts)
			contact.outputXML(output, indent+1);
		
		MzMLContent.indent(output, indent);
		output.write("</fileDescription>\n");
	}

	public String toString() {
		return "fileDescription";
	}
	
	private int getAdditionalChildrenCount() {
		int additionalChildren = ((fileContent != null)? 1 : 0) + 
				((sourceFileList != null)? 1 : 0) + 
				contacts.size();
		
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
//		if(fileContent != null)
//			children.add(fileContent);
//		if(sourceFileList != null)
//			children.add(sourceFileList);
//		children.addAll(contacts);
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
//			if(fileContent != null) {
//				if(counter == index)
//					return fileContent;
//				
//				counter++;
//			}
//			if(sourceFileList != null) {
//				if(counter == index)
//					return sourceFileList;
//				
//				counter++;
//			}
//			
//			return contacts.get(index - (getChildCount() - contacts.size()));
//		}
//		
//		return null;
//	}
//	
//	@Override
//	public int getIndex(TreeNode childNode) {
//		int counter = super.getChildCount();
//			
//		if(childNode instanceof FileContent)
//			return counter;
//		
//		if(fileContent != null)
//			counter++;
//		
//		if(childNode instanceof SourceFileList)
//			return counter;
//		
//		if(sourceFileList != null)
//			counter++;
//		
//		if(childNode instanceof Contact)
//			return counter + contacts.indexOf(contacts);
//		
//		return super.getIndex(childNode);
//	}
}
