package com.alanmrace.jimzmlparser.imzml;

import com.alanmrace.jimzmlparser.mzml.Spectrum;

/**
 * Interface describing general mass spectrometry imaging functions.
 * 
 * @author Alan Race
 */
public interface MassSpectrometryImagingData {

    /**
     * Get the full m/z list that is the union of all individual m/z lists
     * from each spectrum within the dataset.
     * 
     * @return List of all m/z values within the dataset
     */
    public double[] getFullmzList();

    /**
     * Get the spatial dimensionality. 1 if MS experiment, 2 if MSI experiment 
     * or 3 if 3D MSI experiment.
     * 
     * @return Spatial dimensionality
     */
    public int getSpatialDimensionality();

    /**
     * Get the dimensionality of dataset. At least 1 greater than 
     * {@link MassSpectrometryImagingData#getSpatialDimensionality()} to account 
     * for m/z dimension. If MS/MS or ion mobility then dimensionality is increased
     * by a further 1.
     * 
     * @return Dimensionality
     */
    public int getDimensionality();

    /**
     * Get the number of spectra per pixel with different scan properties, such 
     * as ion mobility or MS/MS. For example, for travelling wave ion
     * mobility data on a Synapt G2S-i this will be 200. 
     * 
     * @return Number of spectra per pixel
     */
    public int getNumberOfSpectraPerPixel();

    /**
     * Get the spectrum at (x, y).
     * 
     * <p>TODO: Rethink how to get spectrum at (x, y) but an additional dimension, 
     * for example with ion mobility etc.
     * 
     * @param x x-coordinate
     * @param y y-coordinate
     * @return First spectrum at (x, y), or null if none
     */
    public Spectrum getSpectrum(int x, int y);

    /**
     * Get the spectrum at (x, y, z).
     * 
     * @param x x-coordinate
     * @param y y-coordinate
     * @param z z-coordinate
     * @return First spectrum at (x, y, z), or null if none
     */
    public Spectrum getSpectrum(int x, int y, int z);

    /**
     * Get width of image in pixels.
     * 
     * @return Width of image in pixels
     */
    public int getWidth();
    
    /**
     * Get height of image in pixels.
     * 
     * @return Height of image in pixels
     */
    public int getHeight();
    
    /**
     * Get depth of image in pixels.
     * 
     * @return Depth of image in pixels
     */
    public int getDepth();

    /**
     * Get the minimum m/z that was detected within any spectrum in the dataset.
     * 
     * @return Minimum m/z value
     */
    public double getMinimumDetectedmz();
    
    /**
     * Get the maximum m/z that was detected within any spectrum in the dataset.
     * 
     * @return Maximum m/z value
     */
    public double getMaximumDetectedmz();
    
    /**
     * Get whether the MSI data is stored in processed format (discrete).
     * 
     * @return true if data is discrete, false otherwise
     */
    public boolean isProcessed();
    
    /**
     * Get whether the MSI data is stored in continuous format.
     * 
     * @return true if data is continuous, false otherwise
     */
    public boolean isContinuous();
            
    /**
     * Get the total ion count (TIC) image for the dataset by summing up all intensities 
     * for each spatial coordinate within the dataset.
     * 
     * @return TIC image
     */
    public double[][] generateTICImage();
}
