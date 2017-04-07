/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.writer.MzMLWriteable;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

/**
 *
 * <p>TODO: Consider including the map like in SpectrumList to improve speed of access.
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
    
    @Override
    protected void outputXMLContent(MzMLWriteable output, int indent) throws IOException {
        ArrayList<MzMLTag> children = new ArrayList<MzMLTag>();
        
        addChildrenToCollection(children);
        
        for(MzMLTag child : children) {
            if(child instanceof MzMLDataContainer) {
                output.flush();
                
                ((MzMLDataContainer) child).setmzMLLocation(output.getDataPointer());
            }
            
            child.outputXML(output, indent);
        }
    }
}
