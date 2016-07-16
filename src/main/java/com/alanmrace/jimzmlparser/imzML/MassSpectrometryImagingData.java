/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alanmrace.jimzmlparser.imzML;

import com.alanmrace.jimzmlparser.mzML.Spectrum;

/**
 *
 * @author amr1
 */
public interface MassSpectrometryImagingData {
    public double[] getFullmzList();
    public int getSpatialDimensionality();
    public int getDimensionality();
    public int getNumberOfSpectraPerPixel();
    public Spectrum getSpectrum(int x, int y);
    public Spectrum getSpectrum(int x, int y, int z);
    public int getWidth();
    public int getHeight();
    public int getDepth();
    public double getMinimumDetectedmz();
    public double getMaximumDetectedmz();
    
    public boolean isProcessed();
    public boolean isContinuous();
            
    public double[][] generateTICImage();
}
