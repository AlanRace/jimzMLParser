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
public abstract class MzMLIDContentList<T extends ReferenceableTag & MzMLTag> extends MzMLContentList<T> {

    public MzMLIDContentList(int count) {
        super(count);
    }
    
    public MzMLIDContentList(MzMLIDContentList<T> contentList) {
        super(contentList);
    }
    
    public T get(String id) {
        for (T item : list) {
            if (item.getID().equals(id)) {
                return item;
            }
        }

        return null;
    }
}
