package com.alanmrace.jimzmlparser.mzml;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    Map<String, T> dictionary = new HashMap<String, T>();

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
        return dictionary.get(id);
    }
    
    private static final Pattern LAST_INTEGER_PATTERN = Pattern.compile("[^0-9]+([0-9]+)$");

    @Override
    public void add(T item) {
        if(containsID(item.getID())) {
            
            Matcher matcher = LAST_INTEGER_PATTERN.matcher(item.getID());
            if (matcher.find()) {
                String someNumberStr = matcher.group(1);
                int lastNumberInt = Integer.parseInt(someNumberStr);
                
                item.setID(item.getID().replace(matcher.group(1), "" + (lastNumberInt + 1)));
            } else {
                item.setID(item.getID() + "0");
            }
            
            //TODO: change ID and then warn the validator
        }

        // TODO: Unsure why this is necessary, but dictionary == null when converting
        if(dictionary == null)
            dictionary = new HashMap<String, T>();

        dictionary.put(item.getID(), item);
        super.add(item);
    }

    @Override
    public T remove(int index) {
        T removed = super.remove(index);

        if(removed != null)
            dictionary.remove(removed);

        return removed;
    }

    @Override
    public boolean remove(T item) {
        dictionary.remove(item);

        return super.remove(item);
    }
    
    public boolean containsID(String id) {
//        System.out.println("CHecking for " + id);

        if(dictionary == null)
            return false;

        return dictionary.containsKey(id);
    }
    
    @Override
    public T getValidReference(T processing) {        
        for(T curProcessing : this) {
            if(processing.equals(curProcessing) || processing.getID().equals(curProcessing.getID())) {
                return curProcessing;
            }
        }
        
        // Didn't find the reference, so add it to the list
        this.add(processing);
        
        return processing;
    }
}
