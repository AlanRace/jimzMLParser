package com.alanmrace.jimzmlparser.imzml;

import java.io.Serializable;

public class PixelLocation implements Serializable {

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

    public boolean canEqual(Object other) {
        return (other instanceof PixelLocation);
    }
    
}
