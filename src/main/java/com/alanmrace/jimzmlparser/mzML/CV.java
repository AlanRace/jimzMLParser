package com.alanmrace.jimzmlparser.mzML;

import com.alanmrace.jimzmlparser.util.XMLHelper;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;

public class CV extends MzMLContent  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

        public static final String IMS_URI = "http://www.maldi-msi.org/download/imzml/imagingMS.obo";
        
	private String uri; 		// Required
	private String fullName; 	// Required
	private String id;			// Required
	private String version;		// Optional
	
	public CV(String uri, String fullName, String id) {
		this(uri, fullName, id, null);
	}
	
	public CV(String uri, String fullName, String id, String version) {
		this.uri = uri;
		this.fullName = fullName;
		this.id = id;
		this.version = version;
	}
	
	public String getURI() {
		return uri;
	}
	
	public String getFullName() {
		return fullName;
	}
	
	public String getID() {
		return id;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}
	
	public String getVersion() {
		return version;
	}
	
        @Override
	public void outputXML(BufferedWriter output, int indent) throws IOException {
		MzMLContent.indent(output, indent);
		output.write("<cv");
		output.write(" URI=\"" + XMLHelper.ensureSafeXML(uri) + "\"");
		output.write(" fullName=\"" + XMLHelper.ensureSafeXML(fullName) + "\"");
		output.write(" id=\"" + XMLHelper.ensureSafeXML(id) + "\"");
		
		if(version != null)
			output.write(" version=\"" + XMLHelper.ensureSafeXML(version) + "\"");
		
		output.write("/>\n");
	}
	
	public String toString() {
		return "cv : uri=\"" + uri + "\" fullName=\"" + fullName + "\" id=\"" + id + "\"" + ((version != null)? " version=\"" + version + "\"" : ""); 
	}
}
