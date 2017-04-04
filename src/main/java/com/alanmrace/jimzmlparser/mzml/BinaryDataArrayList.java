package com.alanmrace.jimzmlparser.mzml;

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

    private BinaryDataArray mzArray;
    private BinaryDataArray intensityArray;

    /**
     * Instantiates a new binaryDataArrayList tag.
     *
     * @param count the size of the list
     */
    public BinaryDataArrayList(int count) {
        super(count);
    }

    public BinaryDataArrayList(BinaryDataArrayList bdaList, ReferenceableParamGroupList rpgList, DataProcessingList dpList) {
        this(bdaList.size());

        for (BinaryDataArray bda : bdaList) {
            this.add(new BinaryDataArray(bda, rpgList, dpList));
        }
    }

    public BinaryDataArray getmzArray() {
        if (mzArray == null) {
            for (BinaryDataArray binaryDataArray : list) {
                if (binaryDataArray.ismzArray()) {
                    mzArray = binaryDataArray;
                    break;
                }
            }
        }

        return mzArray;
    }

    public BinaryDataArray getIntensityArray() {
        if (intensityArray == null) {
            for (BinaryDataArray binaryDataArray : list) {
                if (binaryDataArray.isIntensityArray()) {
                    intensityArray = binaryDataArray;
                    break;
                }
            }
        }

        return intensityArray;
    }

    public void updatemzAndIntensityArray() {
        for (BinaryDataArray binaryDataArray : list) {
            if (binaryDataArray.ismzArray()) {
                mzArray = binaryDataArray;
            } else if (binaryDataArray.isIntensityArray()) {
                intensityArray = binaryDataArray;
            }
        }
    }

    public void addBinaryDataArray(BinaryDataArray bda) {
        add(bda);
    }
    
    public BinaryDataArray getBinaryDataArray(int index) {
        return get(index);
    }
    
    @Override
    public String getTagName() {
        return "binaryDataArrayList";
    }
}
