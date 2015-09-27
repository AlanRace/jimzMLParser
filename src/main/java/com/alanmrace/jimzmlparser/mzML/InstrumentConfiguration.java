package com.alanmrace.jimzmlparser.mzML;

import com.alanmrace.jimzmlparser.util.XMLHelper;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.tree.TreeNode;



public class InstrumentConfiguration extends MzMLContent  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public static String instrumentModelID 		= "MS:1000031"; // Required child (1)
	public static String instrumentAttributeID 	= "MS:1000496"; // Optional child (1+)
	public static String ionOpticsTypeID 		= "MS:1000597"; // Optional child (1)
	public static String ionOpticsAttributeID 	= "MS:1000487"; // Optional child (1+)
	
	protected static int idNumber = 0;
	
	// Attributes
	private String id;						// Required
	private ScanSettings scanSettingsRef;	// Optional
	
	// Sub-elements
	private ComponentList componentList;
	private SoftwareRef softwareRef;
	
	
	public InstrumentConfiguration(String id) {
		this.id = id;
	}
	
	public InstrumentConfiguration(InstrumentConfiguration ic, ReferenceableParamGroupList rpgList, ScanSettingsList ssList, SoftwareList softwareList) {
		super(ic, rpgList);
		
		this.id = ic.id;
		
		if(ic.scanSettingsRef != null && ssList != null) {
			for(ScanSettings ss : ssList) {
				if(ic.scanSettingsRef.getID().equals(ss.getID())) {
					scanSettingsRef = ss;
					
					break;
				}
			}
		}
		
		if(ic.componentList != null) {
			componentList = new ComponentList(ic.componentList, rpgList);
		}
		
		if(ic.softwareRef != null && softwareList != null){
			for(Software software : softwareList) {
				if(ic.softwareRef.getRef().getID().equals(software.id)) {
					softwareRef = new SoftwareRef(software);
					 break;
				}
			}
		}
			
	}
	
	public InstrumentConfiguration(String id, ScanSettings scanSettingsRef) {
		this.id = id;
		this.scanSettingsRef = scanSettingsRef;
	}
	
	public ArrayList<OBOTermInclusion> getListOfRequiredCVParams() {
		ArrayList<OBOTermInclusion> required = new ArrayList<OBOTermInclusion>();
		required.add(new OBOTermInclusion(instrumentModelID, true, true, true));
				
		return required; 
	}
	
	public ArrayList<OBOTermInclusion> getListOfOptionalCVParams() {
		ArrayList<OBOTermInclusion> optional = new ArrayList<OBOTermInclusion>();
		optional.add(new OBOTermInclusion(instrumentAttributeID, false, true, false));
		optional.add(new OBOTermInclusion(ionOpticsTypeID, true, true, false));
		optional.add(new OBOTermInclusion(ionOpticsAttributeID, false, true, false));
		
		return optional;
	}
	
	public void setScanSettingsRef(ScanSettings scanSettingsRef) {
		this.scanSettingsRef = scanSettingsRef;
	}
	
	public ScanSettings getScanSettingsRef() {
		return scanSettingsRef;
	}
	
	public void setComponentList(ComponentList componentList) {
		componentList.setParent(this);
		
		this.componentList = componentList;
	}
	
	public ComponentList getComponentList() {
		if(componentList == null)
			componentList = new ComponentList();
		
		return componentList;
	}
	
//	public void addSource(Source source) {
//		componentList.addSource(source);
//	}
//	
//	public void addAnalyser(Analyser analyser) {
//		componentList.addAnalyser(analyser);
//	}
//	
//	public void addDetector(Detector detector) {
//		componentList.addDetector(detector);
//	}

	public void setSoftwareRef(SoftwareRef software) {
//		software.setParent(this);
		
		softwareRef = software;
	}
	
	public SoftwareRef getSoftwareRef() {
		return softwareRef;
	}
	
	public String getID() {
		return id;
	}
	
	public void outputXML(BufferedWriter output, int indent) throws IOException {
		MzMLContent.indent(output, indent);
		output.write("<instrumentConfiguration");
		output.write(" id=\"" + XMLHelper.ensureSafeXML(id) + "\"");
		
		if(scanSettingsRef != null)
			output.write(" scanSettingsRef=\"" + XMLHelper.ensureSafeXML(scanSettingsRef.getID()) + "\"");
		
		output.write(">\n");
		
		super.outputXML(output, indent+1);
		
		if(componentList != null && componentList.size() > 0)
			componentList.outputXML(output, indent+1);
		
		// Create softwareRef
		if(softwareRef != null) {
			MzMLContent.indent(output, indent+1);
			softwareRef.outputXML(output);
		}
		
		MzMLContent.indent(output, indent);
		output.write("</instrumentConfiguration>\n");
	}
	
	public String toString() {
		return "instrumentConfiguration: " + id;
	}
	
	private int getAdditionalChildrenCount() {
		int additionalChildren = ((componentList != null)? 1 : 0) + 
				((softwareRef != null)? 1 : 0);
		
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
//		if(componentList != null)
//			children.add(componentList);
//		if(softwareRef != null)
//			children.add(softwareRef);
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
//			if(componentList != null) {
//				if(counter == index)
//					return componentList;
//				
//				counter++;
//			}
//			if(softwareRef != null) {
//				if(counter == index)
//					return softwareRef;
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
//		if(childNode instanceof ComponentList)
//			return counter;
//		
//		if(componentList != null)
//			counter++;
//		
//		if(childNode instanceof Software)
//			return counter;
//		
//		if(softwareRef != null)
//			counter++;
//		
//		return super.getIndex(childNode);
//	}
}
