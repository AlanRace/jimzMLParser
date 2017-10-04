package com.alanmrace.jimzmlparser.mzml;

/**
 * Class describing a list of MzML tags which have an ID, for example Spectrum.
 * 
 * <p>TODO: Consider throwing an error if we try and add an item with the same ID twice?
 * 
 * @author Alan Race
 * @param <T> MzML tag which are children of the list
 * 
 * @see ChromatogramList
 * @see CVList
 * @see DataProcessingList
 * @see InstrumentConfigurationList
 * @see SampleList
 * @see ScanSettingsList
 * @see SoftwareList
 * @see SourceFileList
 * @see SpectrumList
 * @see ChromatogramList
 */
public abstract class MzMLIDContentList<T extends ReferenceableTag & MzMLTag> extends MzMLContentList<T> 
    implements ReferenceList<T> {

    /**
     * Create an empty list with specified capacity.
     * 
     * @param count Capacity
     */
    public MzMLIDContentList(int count) {
        super(count);
    }
    
    /**
     * Copy constructor.
     * 
     * @param contentList Old list
     */
    public MzMLIDContentList(MzMLIDContentList<T> contentList) {
        super(contentList);
    }
    
    /**
     * Returns an element of the list with the specified unique ID. 
     * 
     * @param id Unique ID.
     * @return Element of the list with the unique ID, null if none found
     */
    public T get(String id) {
        for (T item : list) {
            if (item.getID().equals(id)) {
                return item;
            }
        }

        return null;
    }
    
//    @Override
//    protected void outputXMLContent(MzMLWritable output, int indent) throws IOException {
//        ArrayList<MzMLTag> children = new ArrayList<MzMLTag>();
//        
//        addChildrenToCollection(children);
//        
//        for(MzMLTag child : children) {
//            if(child instanceof MzMLDataContainer) {
//                output.flush();
//                
//                ((MzMLDataContainer) child).setmzMLLocation(output.getDataPointer());
//            }
//        }
//        
//        super.outputXMLContent(output, indent);
//    }
    
    @Override
    public T getValidReference(T processing) {
        boolean found = false;
        
        for(T curProcessing : this) {
            if(processing.equals(curProcessing) || processing.getID().equals(curProcessing.getID())) {
                return curProcessing;
            }
        }
        
        if(!found)
            this.add(processing);
        
        return processing;
    }
}
