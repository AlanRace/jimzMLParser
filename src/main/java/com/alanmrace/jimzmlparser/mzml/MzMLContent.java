package com.alanmrace.jimzmlparser.mzml;

import com.alanmrace.jimzmlparser.event.MzMLContentListener;
import com.alanmrace.jimzmlparser.event.MzMLEvent;
import com.alanmrace.jimzmlparser.exceptions.InvalidXPathException;
import com.alanmrace.jimzmlparser.util.XMLHelper;
import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Base class for all mzML tags. This includes default handling for inclusion of
 * lists of {@link ReferenceableParamGroupRef}, {@link CVParam} and
 * {@link UserParam}.
 *
 * @author Alan Race
 */
public abstract class MzMLContent implements Serializable, MzMLTag {

    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Parent of the current MzMLTag.
     */
    protected MzMLTag parent;
    
    /**
     * Content change listener list.
     */
    protected List<MzMLContentListener> listeners;
    
    /**
     * Add content change listener.
     * 
     * @param listener Listener to add
     */
    public void addListener(MzMLContentListener listener) {
        if(listeners == null)
            listeners = new LinkedList<MzMLContentListener>();
        
        listeners.add(listener);
    }
    
    /**
     * Remove the specified content change listener.
     * 
     * @param listener Listener to remove
     */
    public void removeListener(MzMLContentListener listener) {
        if(listeners != null)
            listeners.remove(listener);
    }
    
    /**
     * Remove all listeners currently added to the content.
     */
    public void removeAllListeners() {
        if(listeners != null)
            listeners.clear();
    }
    
    /**
     * Get all content change listeners added to this content.
     * 
     * @return Content listener list
     */
    public List<MzMLContentListener> getListeners() {
        return listeners;
    }
    
    /**
     * Notify all listeners of the specified event. If the event should be reported
     * to parents then also the listeners of the parent are also notified.
     * 
     * @param event Event
     */
    protected void notifyListeners(MzMLEvent event) {
        if(listeners != null) {
            for(MzMLContentListener listener : listeners) {
                listener.eventOccured(event);
            }
        }
        
        if(event.notifyParents() && parent != null && 
                parent instanceof MzMLContent && ((MzMLContent) parent).hasListeners())
            ((MzMLContent)parent).notifyListeners(event);
    }
    
    /**
     * Return whether any listeners have been added to this content or to any
     * parent or grandparent etc.
     * 
     * @return true if one or more listeners are found
     */
    public boolean hasListeners() {
        boolean hasListeners = listeners != null && !listeners.isEmpty();
        
        if(parent != null && parent instanceof MzMLContent)
            hasListeners |= ((MzMLContent)parent).hasListeners();
        
        return hasListeners;
    }
    
    @Override
    public void setParent(MzMLTag parent) {
        this.parent = parent;
    }
    
    @Override
    public MzMLTag getParent() {
        return parent;
    }
    
    @Override
    public String getXPath() {
        String xPath = "";
        
        if(parent != null)
            xPath = parent.getXPath();
        
        return xPath + "/" + getTagName();
    }
    
    /**
     * Add all child MzMLContent (mzML tags) that match the specified XPath,
     * which are specific to this tag (i.e. child tags which are not
     * {@literal <referenceableParamGroupRef>}, {@literal <cvParam>} and
     * {@literal <userParam>}), to the specified collection.
     *
     * <p>This method should be overridden in subclasses (mzML tags) which have
     * specific children (i.e. child tags in addition to {@literal <referenceableParamGroupRef>},
     * {@literal <cvParam>} and {@literal <userParam>}.
     *
     * @param elements Collection of MzMLContent to add children to
     * @param fullXPath Full XPath to query
     * @param currentXPath Sub XPath that should start with the tag name for the
     * current MzMLContent
     * @throws InvalidXPathException thrown if the XPath can not be followed
     */
    protected void addTagSpecificElementsAtXPathToCollection(Collection<MzMLTag> elements, String fullXPath, String currentXPath) throws InvalidXPathException {
        // Default is no tag specific elements, so don't do anything.
    }

    @Override
    public void addElementsAtXPathToCollection(Collection<MzMLTag> elements, String xPath) throws InvalidXPathException {
        addElementsAtXPathToCollection(elements, xPath, xPath);
    }

    @Override
    public final void addElementsAtXPathToCollection(Collection<MzMLTag> elements, String fullXPath, String currentXPath) throws InvalidXPathException {
        if (currentXPath.startsWith("/" + getTagName())) {
            String subXPath = currentXPath.replaceFirst("/" + getTagName(), "");

            if (subXPath.isEmpty()) {
                elements.add(this);

                return;
            }

            addTagSpecificElementsAtXPathToCollection(elements, fullXPath, subXPath);

            if (elements.isEmpty()) {
                throw new InvalidXPathException("Invalid sub-XPath (" + subXPath + ") in XPath " + fullXPath, fullXPath);
            }
        } else {
            throw new InvalidXPathException("XPath does not start with /" + getTagName() + " in sub-XPath [" + currentXPath + "] of [" + fullXPath + "]", fullXPath);
        }
    }
    
    /**
     * Returns XML formatted text describing XML attributes for the MzMLTag.
     * 
     * @return XML formatted attribute text
     */
    @Override
    public String getXMLAttributeText() {
        if(this instanceof ReferenceableTag)
            return "id=\"" + XMLHelper.ensureSafeXML(((ReferenceableTag)this).getID()) + "\"";
        
        return "";
    }
        
    @Override
    public String toString() {
        return getTagName();
    }
    
    /**
     * For any child class which has attribute references (e.g. dataProcessingRef in Spectrum)
     * this should be overwritten to ensure that the ReferenceList is always 
     * kept up to date. This method will be called whenever a change is made to 
     * either the ReferenceList or a reference.
     */
    protected void ensureValidReferences() {
        // For any child class which has references (e.g. dataProcessingRef in Spectrum)
        // this should be called whenever a change is made to the ReferenceList 
    }
}
