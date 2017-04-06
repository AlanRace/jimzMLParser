package com.alanmrace.jimzmlparser.mzml;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author Alan Race
 * @param <T>
 */
public abstract class MzMLReference<T extends ReferenceableTag> extends MzMLContent {

    protected T reference;
    
    public MzMLReference(T reference) {
        this.reference = reference;
    }
    
    @Deprecated
    public T getRef() {
        return reference;
    }
    
    public T getReference() {
        return reference;
    }
    
    @Override
    public void outputXML(RandomAccessFile raf, BufferedWriter output, int indent) throws IOException {
        MzMLContent.indent(output, indent);
        
        output.write("<" + getTagName());
        output.write(" ref=\"" + reference.getID() + "\"");
        output.write("/>\n");
    }
    
    @Override
    public String toString() {
        return getTagName() + ": " + reference.getID();
    }
}
