package com.alanmrace.jimzmlparser.mzML;

import com.alanmrace.jimzmlparser.util.XMLHelper;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class ChromatogramList extends MzMLContent  implements Serializable, Iterable<Chromatogram> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private ArrayList<Chromatogram> chromatogramList;
	private DataProcessing defaultDataProcessingRef;
	
	public ChromatogramList(int count, DataProcessing defaultDataProcessingRef) {
		this.defaultDataProcessingRef = defaultDataProcessingRef;
		chromatogramList = new ArrayList<Chromatogram>(count);
	}
	
	public ChromatogramList(ChromatogramList chromatogramList, ReferenceableParamGroupList rpgList, 
			DataProcessingList dpList, SourceFileList sourceFileList) {
		this.chromatogramList = new ArrayList<Chromatogram>(chromatogramList.size());
		
		if(chromatogramList.defaultDataProcessingRef != null && dpList != null) {
			for(DataProcessing dp : dpList) {
				if(chromatogramList.defaultDataProcessingRef.getID().equals(dp.getID())) {
					this.defaultDataProcessingRef = dp;
					
					break;
				}
			}
		}
		
		for(Chromatogram chromatogram : chromatogramList) {
			this.chromatogramList.add(new Chromatogram(chromatogram, rpgList, dpList, sourceFileList));
		}
	}
	
	public DataProcessing getDefaultDataProcessingRef() {
		return defaultDataProcessingRef;
	}
	
	public int size() {
		return chromatogramList.size();
	}
	
	public void addChromatogram(Chromatogram chromatogram) {
		chromatogram.setParent(this);
		
		chromatogramList.add(chromatogram);
	}
	
	public Chromatogram getChromatogram(int index) {
		return chromatogramList.get(index);
	}
        
        public Chromatogram getChromatogram(String id) {
            for(Chromatogram chromtogram : chromatogramList)
                if(chromtogram.getID().equals(id))
                    return chromtogram;
            
            return null;
        }
	
	public void outputXML(BufferedWriter output, int indent) throws IOException {
		MzMLContent.indent(output, indent);
		output.write("<chromatogramList");
		output.write(" count=\"" + chromatogramList.size() + "\"");
		output.write(" defaultDataProcessingRef=\"" + XMLHelper.ensureSafeXML(defaultDataProcessingRef.getID()) + "\"");
		output.write(">\n");
		
		int index = 0;
		
		for(Chromatogram chromatogram : chromatogramList)
			chromatogram.outputXML(output, indent+1, index++);
		
		MzMLContent.indent(output, indent);
		output.write("</chromatogramList>\n");
	}
	
	public String toString() {
		return "chromatogramList";
	}

	@Override
	public Iterator<Chromatogram> iterator() {
		return chromatogramList.iterator();
	}
	
//	@Override
//	public int getChildCount() {
//		// 
//		return size();
//	}
//	
//	@Override
//	public int getIndex(TreeNode childNode) {
//		return chromatogramList.indexOf(childNode);
//	}
//	
//	@Override
//	public TreeNode getChildAt(int index) {
//		return chromatogramList.get(index);
//	}
//	
//	@Override
//	public Enumeration<TreeNode> children() {
//		Vector<TreeNode> children = new Vector<TreeNode>();
//		children.addAll(chromatogramList);
//		
//		return children.elements();
//	}
}
