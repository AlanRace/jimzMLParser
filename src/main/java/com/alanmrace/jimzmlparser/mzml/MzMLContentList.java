package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.exceptions.InvalidXPathException;
import com.alanmrace.jimzmlparser.exceptions.UnfollowableXPathException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Abstract class implementing the basic functionality for a list tag in MzML.
 *
 * @author Alan Race
 * @param <T> The type of MzMLTag within this list.
 *
 * @see MzMLIDContentList
 * @see BinaryDataArrayList
 * @see CVList
 * @see DataProcessing
 * @see PrecursorList
 * @see ProductList
 * @see ReferenceableParamGroupList
 * @see SampleList
 * @see SelectedIonList
 * @see SourceFileRefList
 * @see TargetList
 */
public abstract class MzMLContentList<T extends MzMLTag>
        extends MzMLContent implements MzMLTagList<T>, HasChildren {

    /**
     * The list of MzMLTags.
     */
    private List<T> list = Collections.emptyList();

    /**
     * Create an empty list, used by subclasses.
     */
    protected MzMLContentList() {
    }

    /**
     * Create an empty list with the specified initial capacity.
     *
     * @param count Initial capacity
     */
    public MzMLContentList(int count) {
        // TODO: Rethink how to do this for the initial capacity
    }

    /**
     * Copy constructor for list, shallow copy.
     *
     * @param contentList List to copy
     */
    public MzMLContentList(MzMLContentList<T> contentList) {
        this(contentList.size());

        for (T item : contentList) {
            add(item);
        }
    }

    protected List<T> getList() {
        return list;
    }
    
    @Override
    public void add(T item) {
        if (item instanceof MzMLContent) {
            item.setParent(this);
        }
        
        if (list.size() > 1) {
            list.add(item);
        } else if (list.size() == 1) {
            list = new ArrayList<T>(list);
            list.add(item);
        } else {
            list = Collections.singletonList(item);
        }
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
    public boolean remove(T item) {
        return list.remove(item);
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
        children.addAll(list);
    }

    @Override
    protected void addTagSpecificElementsAtXPathToCollection(Collection<MzMLTag> elements, String fullXPath, String currentXPath) throws InvalidXPathException {
        if (!fullXPath.equals(currentXPath) && list.isEmpty()) {
            throw new UnfollowableXPathException("No " + getTagName() + " exists, so cannot go to " + fullXPath, fullXPath, currentXPath);
        }
        
        if (size() > 0) {
            // Get the first element from the list so that we can use it to get 
            // the name of the tag.
            T firstElement = list.get(0);

            if (currentXPath.startsWith("/" + firstElement.getTagName())) {
                for (T item : list) {
                    item.addElementsAtXPathToCollection(elements, fullXPath, currentXPath);
                }
            }
        }
    }

    @Override
    public String getXMLAttributeText() {
        String attributeText = super.getXMLAttributeText();
        
        if(!attributeText.isEmpty())
            attributeText += " ";
        
        return attributeText + "count=\"" + size() + "\"";
    }


    @Override
    public String toString() {
        return getTagName();
    }

    @Override
    public Iterator<T> iterator() {
        return list.iterator();
    }

    @Override
    public boolean contains(T item) {        
        return list.contains(item);
    }
    
    @Override
    public void clear() {
        list.clear();
    }
}
