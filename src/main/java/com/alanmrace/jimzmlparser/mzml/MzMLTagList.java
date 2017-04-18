package com.alanmrace.jimzmlparser.mzml;

/**
 * 
 * 
 * @author Alan Race
 * @param <T>
 */
public interface MzMLTagList<T extends MzMLTag> extends Iterable<T> {
    
    /**
     * Add an item to the list. If an item is added which could be used as a reference
     * (i.e. something which extends MzMLIDContent) then the relevant 
     * 
     * @param item
     */
    public void add(T item);
    
    public T get(int index);
    
    public T remove(int index);
    
    public boolean remove(T item);
    
    public int indexOf(T item);
    
    public int size();
}
