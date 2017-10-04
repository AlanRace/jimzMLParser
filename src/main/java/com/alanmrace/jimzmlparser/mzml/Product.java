package com.alanmrace.jimzmlparser.mzml;

import java.util.Collection;

/**
 * Class describing the method of product ion selection and activation.
 * 
 * @author Alan Race
 */
public class Product extends MzMLContentWithChildren {

    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The isolation (or selection) window configured to isolate one or more ions.
     */
    private IsolationWindow isolationWindow;

    /**
     * Create empty Product, does nothing.
     */
    public Product() {
    }

    /**
     * Copy constructor.
     *
     * @param product Old Product to copy
     * @param rpgList New ReferenceableParamGroupList to match references to
     */
    public Product(Product product, ReferenceableParamGroupList rpgList) {
        if (product.isolationWindow != null) {
            isolationWindow = new IsolationWindow(product.isolationWindow, rpgList);
        }
    }

    /**
     * Set the isolation (or selection) window configuration used to isolate 
     * one or more ions.
     * 
     * @param isolationWindow IsolationWindow
     */
    public void setIsolationWindow(IsolationWindow isolationWindow) {
        isolationWindow.setParent(this);

        this.isolationWindow = isolationWindow;
    }

    /**
     * Returns the isolation (or selection) window configuration used to isolate 
     * one or more ions.
     * 
     * @return IsolationWindow
     */
    public IsolationWindow getIsolationWindow() {
        return isolationWindow;
    }

    @Override
    public String getTagName() {
        return "product";
    }

    @Override
    public void addChildrenToCollection(Collection<MzMLTag> children) {
        children.add(isolationWindow);
    }
}
