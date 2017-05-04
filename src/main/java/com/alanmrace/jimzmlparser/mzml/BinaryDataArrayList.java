package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.data.DataTypeTransform;

/**
 * BinaryDataArrayList tag.
 *
 * @author Alan Race
 */
public class BinaryDataArrayList extends MzMLContentList<BinaryDataArray> {

    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new binaryDataArrayList tag.
     *
     * @param count the size of the list
     */
    public BinaryDataArrayList(int count) {
        super(count);
    }

    /**
     * Copy constructor.
     *
     * @param bdaList Old BinaryDataArrayList to copy
     * @param rpgList New ReferenceableParamGroupList to match references to
     * @param dpList New DataProcessingList to match references to
     */
    public BinaryDataArrayList(BinaryDataArrayList bdaList, ReferenceableParamGroupList rpgList, DataProcessingList dpList) {
        this(bdaList.size());

        for (BinaryDataArray bda : bdaList) {
            this.add(new BinaryDataArray(bda, rpgList, dpList));
        }
    }

    /**
     * Returns the BinaryDataArray which contains the CVParam 
     * {@link BinaryDataArray#mzArrayID}, or null if one is not present within the 
     * list.
     * 
     * @return m/z BinaryDataArray, or null if not found
     */
    public BinaryDataArray getmzArray() {
        BinaryDataArray mzArray = null;

        for (BinaryDataArray binaryDataArray : list) {
            if (binaryDataArray.ismzArray()) {
                mzArray = binaryDataArray;
                break;
            }
        }

        return mzArray;
    }

    /**
     * Returns the BinaryDataArray which contains the CVParam 
     * {@link BinaryDataArray#intensityArrayID}, or null if one is not present within the 
     * list.
     * 
     * @return Intensity BinaryDataArray, or null if not found
     */
    public BinaryDataArray getIntensityArray() {
        BinaryDataArray intensityArray = null;

        for (BinaryDataArray binaryDataArray : list) {
            if (binaryDataArray.isIntensityArray()) {
                intensityArray = binaryDataArray;
                break;
            }
        }

        return intensityArray;
    }

    /**
     * Add BinaryDataArray. Helper method to retain API, calls 
     * {@link BinaryDataArrayList#add(com.alanmrace.jimzmlparser.mzml.MzMLTag)}.
     * 
     * @param bda BinaryDataArray to add to list
     */
    public void addBinaryDataArray(BinaryDataArray bda) {
        add(bda);
    }

    /**
     * Returns BinaryDataArray at specified index in list. Helper method to retain 
     * API, calls {@link BinaryDataArrayList#get(int)}.
     * 
     * @param index Index in the list
     * @return BinaryDataArray at index, or null if none exists
     */
    public BinaryDataArray getBinaryDataArray(int index) {
        return get(index);
    }
    
    /**
     * Sets the compression (or none) to all BinaryDataArray within the spectrum.
     * This only takes effect when the data is written out.
     * 
     * @param compression Compression
     */
    public void setCompression(BinaryDataArray.CompressionType compression) {
        for(BinaryDataArray bda : this)
            bda.setCompression(compression);
    }

    /**
     * Sets the data type to be used when storing data. This only takes effect
     * when the data is written out.
     * 
     * @param dataType New data type
     */
    public void setDataType(DataTypeTransform.DataType dataType) {
        for(BinaryDataArray bda : this)
            bda.setDataType(dataType);
    }
    
    @Override
    public String getTagName() {
        return "binaryDataArrayList";
    }
}
