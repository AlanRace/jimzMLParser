package com.alanmrace.jimzmlparser.mzml;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;

public class Product extends MzMLContentWithChildren implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private IsolationWindow isolationWindow;

    public Product() {
    }

    public Product(Product product, ReferenceableParamGroupList rpgList) {
        if (product.isolationWindow != null) {
            isolationWindow = new IsolationWindow(product.isolationWindow, rpgList);
        }
    }

    public void setIsolationWindow(IsolationWindow isolationWindow) {
        isolationWindow.setParent(this);

        this.isolationWindow = isolationWindow;
    }

    public IsolationWindow getIsolationWindow() {
        return isolationWindow;
    }

    @Override
    protected void outputXMLContent(BufferedWriter output, int indent) throws IOException {
        if (isolationWindow != null) {
            isolationWindow.outputXML(output, indent + 1);
        }
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
