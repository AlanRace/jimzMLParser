package com.alanmrace.jimzmlparser.exceptions;

public class CVParamAccessionNotFoundException extends RuntimeException {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1470705281628394244L;

	public CVParamAccessionNotFoundException(String message) {
		super(message + " not found in OBO.");
	}

}
