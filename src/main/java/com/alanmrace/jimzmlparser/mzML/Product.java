package com.alanmrace.jimzmlparser.mzml;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;

public class Product extends MzMLContent implements Serializable {

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
    public void outputXML(BufferedWriter output, int indent) throws IOException {
        MzMLContent.indent(output, indent);
        output.write("<product>\n");

        if (isolationWindow != null) {
            isolationWindow.outputXML(output, indent + 1);
        }

        MzMLContent.indent(output, indent);
        output.write("</product>\n");
    }

    @Override
    public String toString() {
        return "product";
    }

    @Override
    public String getTagName() {
        return "product";
    }
}
