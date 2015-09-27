package com.alanmrace.jimzmlparser.mzML;


import com.alanmrace.jimzmlparser.util.XMLHelper;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.tree.TreeNode;

public class DataProcessing extends MzMLContent  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	protected static int idNumber = 0;
	
	// Attributes
	protected String id;		// Required
	
	// Sub-elements
	protected ArrayList<ProcessingMethod> processingMethods;	
		
	public DataProcessing(String id) {
		this.id = id;
		
		processingMethods = new ArrayList<ProcessingMethod>();
	}
	
	public DataProcessing(DataProcessing dp, ReferenceableParamGroupList rpgList, SoftwareList softwareList) {
		this.id = dp.id;
		
		this.processingMethods = new ArrayList<ProcessingMethod>(dp.getProcessingMethodCount());
		
		for(ProcessingMethod pm : dp.processingMethods) {
			this.processingMethods.add(new ProcessingMethod(pm, rpgList, softwareList));
		}
	}
	
//	public DataProcessing(ArrayList<ProcessingMethod> processingMethods) {
//		id = "dp" + idNumber++;
//		
//		this.processingMethods = processingMethods;
//	}
	
	public void addProcessingMethod(ProcessingMethod pm) {
		pm.setParent(this);
		
		processingMethods.add(pm);
	}
	
	public ProcessingMethod getProcessingMethod(int index) {
		if(index < processingMethods.size() && index >= 0) {
			return processingMethods.get(index);
		}
		
		return null;
	}
	
	public int getProcessingMethodCount() {
		return processingMethods.size();
	}
	
	public String toString() {
		return "dataProcessing: " + id; // + " - " + processingMethods.get(0).toString();
	}
	
	public String getID() {
		return id;
	}
	
	public void outputXML(BufferedWriter output, int indent) throws IOException {
		MzMLContent.indent(output, indent);
		output.write("<dataProcessing");
		output.write(" id=\"" + XMLHelper.ensureSafeXML(id) + "\"");
		output.write(">\n");
		
		int order = 1;
		
		for(ProcessingMethod pm : processingMethods)
			pm.outputXML(output, indent+1, order++);
		
		MzMLContent.indent(output, indent);
		output.write("</dataProcessing>\n");
	}
	
//	@Override
//	public int getChildCount() {
//		// 
//		return getProcessingMethodCount();
//	}
//	
//	@Override
//	public int getIndex(TreeNode childNode) {
//		return processingMethods.indexOf(childNode);
//	}
//	
//	@Override
//	public TreeNode getChildAt(int index) {
//		return processingMethods.get(index);
//	}
//	
//	@Override
//	public Enumeration<TreeNode> children() {
//		Vector<TreeNode> children = new Vector<TreeNode>();
//		children.addAll(processingMethods);
//		
//		return children.elements();
//	}
}
