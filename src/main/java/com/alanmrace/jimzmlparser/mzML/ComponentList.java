package com.alanmrace.jimzmlparser.mzML;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class ComponentList extends MzMLContent  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	
	private ArrayList<Source> sources;
	private ArrayList<Analyser> analysers;
	private ArrayList<Detector> detectors;
	
	public ComponentList() {
		sources = new ArrayList<Source>();
		analysers = new ArrayList<Analyser>();
		detectors = new ArrayList<Detector>();
	}
	
	public ComponentList(ComponentList componentList, ReferenceableParamGroupList rpgList) {
		sources = new ArrayList<Source>(componentList.sources.size());
		
		for(Source source : componentList.sources)
			sources.add(new Source(source, rpgList));
		
		analysers = new ArrayList<Analyser>(componentList.analysers.size());
		
		for(Analyser analyser : componentList.analysers)
			analysers.add(new Analyser(analyser, rpgList));
		
		detectors = new ArrayList<Detector>(componentList.detectors.size());
		
		for(Detector detector : componentList.detectors)
			detectors.add(new Detector(detector, rpgList));
	}
	
	public void addSource(Source source) {
		source.setParent(this);
		
		sources.add(source);
	}
	
	public void addAnalyser(Analyser analyser) {
		analyser.setParent(this);
		
		analysers.add(analyser);
	}
	
	public void addDetector(Detector detector) {
		detector.setParent(this);
		
		detectors.add(detector);
	}
	
	public int getSourceCount() {
		return sources.size();
	}
	
	public int getAnalyserCount() {
		return analysers.size();
	}
	
	public int getDetectorCount() {
		return detectors.size();
	}
	
	public Source getSource(int index) {
		return sources.get(index);
	}
	
	public Analyser getAnalyser(int index) {
		return analysers.get(index);
	}
	
	public Detector getDetector(int index) {
		return detectors.get(index);
	}
	
	public int size() {
		return sources.size() + analysers.size() + detectors.size();
	}
	
	public void outputXML(BufferedWriter output, int indent) throws IOException {
		MzMLContent.indent(output, indent);
		output.write("<componentList");
		output.write(" count=\"" + size() + "\"");
		output.write(">\n");
		
		int order = 1;
		for(Source source : sources)
			source.outputXML(output, indent+1, order++);
		
		for(Analyser analyser : analysers)
			analyser.outputXML(output, indent+1, order++);
				
		for(Detector detector : detectors)
			detector.outputXML(output, indent+1, order++);
				
		
		MzMLContent.indent(output, indent);
		output.write("</componentList>\n");
	}
	
	public String toString() {
		return "componentList";
	}
	
//	@Override
//	public int getChildCount() {
//		// 
//		return size();
//	}
//	
//	@Override
//	public int getIndex(TreeNode childNode) {
//		if(childNode instanceof Source) {
//			return sources.indexOf(childNode);
//		} else if(childNode instanceof Analyser) {
//			return analysers.indexOf(childNode) + sources.size();
//		} else if(childNode instanceof Detector) {
//			return detectors.indexOf(childNode) + sources.size() + analysers.size();
//		}
//		
//		return 0;
//	}
//	
//	@Override
//	public TreeNode getChildAt(int index) {
//		if(index < sources.size())
//			return sources.get(index);
//		if(index < (sources.size() + analysers.size()))
//			return analysers.get(index - sources.size());
//		if(index < size())
//			return detectors.get(index - (sources.size() + analysers.size()));
//		
//		return null;
//	}
//	
//	@Override
//	public Enumeration<TreeNode> children() {
//		Vector<TreeNode> children = new Vector<TreeNode>();
//		children.addAll(sources);
//		children.addAll(analysers);
//		children.addAll(detectors);
//		
//		return children.elements();
//	}
}
