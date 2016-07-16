/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alanmrace.jimzmlparser.imzML;

import com.alanmrace.jimzmlparser.mzML.Spectrum;
import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author amr1
 */
public class ImzMLContainer implements MassSpectrometryImagingData {

    protected int width;
    protected int height;
    protected int depth;
    
    protected ImzML[][][] pixelImzML;
    
    protected ArrayList<ImzML> imzMLFiles;
    protected ArrayList<File> ibdFiles;
    
    public ImzMLContainer(int width, int height) {
        this(width, height, 1);
    }
    
    
    public ImzMLContainer(int width, int height, int depth) {
        this.width = width;
        this.height = height;
        this.depth = depth;
        
        imzMLFiles = new ArrayList<ImzML>();
        ibdFiles = new ArrayList<File>();
        
        pixelImzML = new ImzML[width][height][depth];
    }
    
    public void addImzML(ImzML imzML, File ibdFile, int startX, int endX, int startY, int endY) {
        addImzML(imzML, ibdFile, startX, endX, startY, endY, 1, 1);
    }
    
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
