package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.exceptions.InvalidXPathException;
import com.alanmrace.jimzmlparser.exceptions.UnfollowableXPathException;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Alan Race
 * @param <T>
 */
public abstract class MzMLContentList<T extends MzMLTag> 
        extends MzMLContentWithChildren implements MzMLTagList<T> {

    /**
     *
     */
    protected final List<T> list;

    protected MzMLContentList() {
        list = new ArrayList<T>();
    }
    
    public MzMLContentList(int count) {
        list = new ArrayList<T>(count);
    }

    public MzMLContentList(MzMLContentList<T> contentList) {
        this(contentList.size());

        for (T item : contentList) {
            this.list.add(item);
        }
    }

    @Override
    public void add(T item) {
        item.setParent(this);

        list.add(item);
    }

    @Override
    public T get(int index) {
        return list.get(index);
    }

    @Override
    public T remove(int index) {
        return list.remove(index);
    }

    @Override
    public int indexOf(T item) {
        return list.indexOf(item);
    }

    @Override
    public int size() {
        return list.size();
    }
    
    @Override
    public void addChildrenToCollection(Collection<MzMLTag> children) {
        if(list != null)
            children.addAll(list);
    }
    
    @Override
    protected void addTagSpecificElementsAtXPathToCollection(Collection<MzMLTag> elements, String fullXPath, String currentXPath) throws InvalidXPathException {
        if (currentXPath.startsWith("/" + getTagName())) {
            if (list == null) {
                throw new UnfollowableXPathException("No " + getTagName() + " exists, so cannot go to " + fullXPath, fullXPath, currentXPath);
            }

            for (T item : list) {
                item.addElementsAtXPathToCollection(elements, fullXPath, currentXPath);
            }
        }
    }

    protected void outputOpenTag(BufferedWriter output) throws IOException {
        output.write("<" + getTagName());
        output.write(" count=\"" + list.size() + "\"");
        output.write(">\n");
    }
    
    @Override
    public void outputXML(BufferedWriter output, int indent) throws IOException {
        MzMLContent.indent(output, indent);
        outputOpenTag(output);

        for (T item : this) {
            item.outputXML(output, indent + 1);
        }

        MzMLContent.indent(output, indent);
        output.write("</" + getTagName() + ">\n");
    }

    @Override
    public String toString() {
        return getTagName();
    }

    @Override
    public Iterator<T> iterator() {
        return list.iterator();
    }
}
