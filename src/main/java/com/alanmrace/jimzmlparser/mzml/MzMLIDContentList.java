/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.listener.ReferenceListener;
import java.io.IOException;
import java.util.ArrayList;
import com.alanmrace.jimzmlparser.writer.MzMLWritable;

/**
 *
 * <p>TODO: Consider including the map like in SpectrumList to improve speed of access.
 * 
 * @author Alan Race
 * @param <T>
 */
public abstract class MzMLIDContentList<T extends ReferenceableTag & MzMLTag> extends MzMLContentList<T> 
    implements ReferenceListener<T> {

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
    
    @Override
    protected void outputXMLContent(MzMLWritable output, int indent) throws IOException {
        ArrayList<MzMLTag> children = new ArrayList<MzMLTag>();
        
        addChildrenToCollection(children);
        
        for(MzMLTag child : children) {
            if(child instanceof MzMLDataContainer) {
                output.flush();
                
                ((MzMLDataContainer) child).setmzMLLocation(output.getDataPointer());
            }
        }
        
        super.outputXMLContent(output, indent);
    }
    
    @Override
    public T referenceModified(T processing) {
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
