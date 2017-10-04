package com.alanmrace.jimzmlparser.imzml;

import com.alanmrace.jimzmlparser.mzml.Spectrum;
import java.io.File;
import java.util.ArrayList;

/**
 * Class to handle multiple imzML images within a single view.
 * 
 * @author Alan Race
 */
public class ImzMLContainer implements MassSpectrometryImagingData {

    /**
     * Width in pixels of the total container.
     */
    protected int width;
    
    /**
     * Height in pixels of the container.
     */
    protected int height;

    /**
     * Depth in pixels of the container.
     */
    protected int depth;
    
    /**
     * Array containing the imzML at each pixel coordinate.
     */
    protected ImzML[][][] pixelImzML;
    
    /**
     * List of imzML files, should be kept in sync with {@link ImzMLContainer#ibdFiles}.
     */
    protected ArrayList<ImzML> imzMLFiles;

    /**
     * List of IBD files, should be kept in sync with {@link ImzMLContainer#imzMLFiles}.
     */
    protected ArrayList<File> ibdFiles;
    
    /**
     * Create an imzML container with a specific size in pixels.
     * 
     * @param width     Number of pixels wide.
     * @param height    Number of pixels hight.
     */
    public ImzMLContainer(int width, int height) {
        this(width, height, 1);
    }
    
    /**
     * Create an imzML container with specific size in pixels.
     * 
     * @param width     Number of pixels wide.
     * @param height    Number of pixels hight.
     * @param depth     Number of pixels deep.
     */
    public ImzMLContainer(int width, int height, int depth) {
        this.width = width;
        this.height = height;
        this.depth = depth;
        
        imzMLFiles = new ArrayList<ImzML>();
        ibdFiles = new ArrayList<File>();
        
        pixelImzML = new ImzML[width][height][depth];
    }
    
    /**
     * Add imzML to the all pixels that fall within the bounding box specified by
     * startX, endX, startY, endY.
     * 
     * @param imzML     ImzML file to add
     * @param ibdFile   Corresponding IBD file
     * @param startX    Top left x-coordinate in the container
     * @param endX      Bottom right x-coordinate in the container
     * @param startY    Top left y-coordinate in the container
     * @param endY      Bottom right y-coordinate in the container
     */
    public void addImzML(ImzML imzML, File ibdFile, int startX, int endX, int startY, int endY) {
        addImzML(imzML, ibdFile, startX, endX, startY, endY, 1, 1);
    }
    
    /**
     * Add imzML to the all pixels that fall within the bounding cube specified by
     * startX, endX, startY, endY, startZ, endZ.
     * 
     * @param imzML     ImzML file to add
     * @param ibdFile   Corresponding IBD file
     * @param startX    Top left front x-coordinate in the container
     * @param endX      Bottom right back x-coordinate in the container
     * @param startY    Top left front y-coordinate in the container
     * @param endY      Bottom right back y-coordinate in the container
     * @param startZ    Top left front z-coordinate in the container
     * @param endZ      Bottom right back z-coordinate in the container
     */
    public void addImzML(ImzML imzML, File ibdFile, int startX, int endX, int startY, int endY, int startZ, int endZ) {
        imzMLFiles.add(imzML);
        ibdFiles.add(ibdFile);
        
        for(int z = startZ-1; z < endZ; z++) {
            for(int y = startY-1; y < endY; y++) {
                for(int x = startX-1; x < endX; x++) {
                    pixelImzML[x][y][z] = imzML;
                }
            }
        }
    }
    
    @Override
    public double[] getFullmzList() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getSpatialDimensionality() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getDimensionality() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getNumberOfSpectraPerPixel() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Spectrum getSpectrum(int x, int y) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Spectrum getSpectrum(int x, int y, int z) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getWidth() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getHeight() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getDepth() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getMinimumDetectedmz() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getMaximumDetectedmz() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isProcessed() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isContinuous() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double[][] generateTICImage() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
