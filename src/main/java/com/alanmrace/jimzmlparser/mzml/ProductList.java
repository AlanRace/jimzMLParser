package com.alanmrace.jimzmlparser.mzml;

public class ProductList extends MzMLContentList<Product> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public ProductList(int count) {
        super(count);
    }

    public ProductList(ProductList productList, ReferenceableParamGroupList rpgList) {
        this(productList.size());

        for (Product product : productList) {
            this.add(new Product(product, rpgList));
        }
    }

    public void addProduct(Product product) {
        add(product);
    }

    public Product getProduct(int index) {
        return get(index);
    }

    @Override
    public String getTagName() {
        return "productList";
    }
}
