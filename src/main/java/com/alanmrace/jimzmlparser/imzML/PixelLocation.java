package com.alanmrace.jimzmlparser.imzML;

import java.io.Serializable;

public class PixelLocation  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int x;
	private int y;
	private int z;
	
	public PixelLocation(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getZ() {
		return z;
	}
}
