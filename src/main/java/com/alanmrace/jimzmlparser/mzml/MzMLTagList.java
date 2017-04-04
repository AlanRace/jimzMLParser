/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alanmrace.jimzmlparser.mzml;

/**
 *
 * @author Alan Race
 * @param <T>
 */
public interface MzMLTagList<T extends MzMLTag> extends Iterable<T>{
    
    public void add(T item);
    
    public T get(int index);
    
    public T remove(int index);
    
    public int indexOf(T item);
    
    public int size();
}
