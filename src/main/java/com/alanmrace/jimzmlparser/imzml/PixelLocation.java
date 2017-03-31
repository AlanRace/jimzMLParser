package com.alanmrace.jimzmlparser.imzml;

import java.io.Serializable;

/**
 * Pixel 3D (x, y, z) integer coordinate.
 * 
 * @author Alan Race
 */
public class PixelLocation implements Serializable {

    /** Serial version ID */
    private static final long serialVersionUID = 1L;

    /** x-coordinate */
    private final int x;
    /** y-coordinate */
    private final int y;
    /** z-coordinate */
    private final int z;

    /**
     * Construct PixelLocation (x, y, z).
     * 
     * @param x x-coordinate
     * @param y y-coordinate
     * @param z z-coordinate
     */
    public PixelLocation(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Get x-coordinate.
     * 
     * @return x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Get y-coordinate.
     * 
     * @return y-coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Get z-coordinate.
     * 
     * @return z-coordinate
     */
    public int getZ() {
        return z;
    }
    
    /**
     * Test whether an object can equal this class (i.e. is of type PixelLocation).
     * 
     * @param other Object to test
     * @return true if is of type PixelLocation, false if not
     */
    public boolean canEqual(Object other) {
        return (other instanceof PixelLocation);
    }
    
    @Override 
    public boolean equals(Object other) {
        boolean result = false;
        if (other instanceof PixelLocation) {
            PixelLocation that = (PixelLocation) other;
            result = (that.canEqual(this) && this.getX() == that.getX() && this.getY() == that.getY() && this.getZ() == that.getZ());
        }
        return result;
    }

    @Override public int hashCode() {
        return (41 * (41 * (41 + getX()) + getY()) + getZ());
    }
}
