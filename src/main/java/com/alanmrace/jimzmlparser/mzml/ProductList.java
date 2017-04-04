package com.alanmrace.jimzmlparser.mzml;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class ProductList extends MzMLContentList<Product> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private ArrayList<Product> productList;

    public ProductList(int count) {
        productList = new ArrayList<Product>(count);
    }

    public ProductList(ProductList productList, ReferenceableParamGroupList rpgList) {
        this(productList.size());

        for (Product product : productList) {
            this.productList.add(new Product(product, rpgList));
        }
    }

    public int size() {
        return productList.size();
    }

    public void addProduct(Product product) {
        product.setParent(this);

        productList.add(product);
    }

    public Product getProduct(int index) {
        return productList.get(index);
    }

    @Override
    public void outputXML(BufferedWriter output, int indent) throws IOException {
        MzMLContent.indent(output, indent);
        output.write("<productList");
        output.write(" count=\"" + productList.size() + "\"");
        output.write(">\n");

        for (Product product : productList) {
            product.outputXML(output, indent + 1);
        }

        MzMLContent.indent(output, indent);
        output.write("</productList>\n");
    }

    @Override
    public String toString() {
        return "productList";
    }


    @Override
    public Iterator<Product> iterator() {
        return productList.iterator();
    }

    @Override
    public String getTagName() {
        return "productList";
    }
    
    @Override
    public void addChildrenToCollection(Collection<MzMLTag> children) {
        if(productList != null)
            children.addAll(productList);
        
        super.addChildrenToCollection(children);
    }
}
