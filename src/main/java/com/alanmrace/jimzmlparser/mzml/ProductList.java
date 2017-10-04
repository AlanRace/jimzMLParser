package com.alanmrace.jimzmlparser.mzml;

/**
 * Class describing {@literal <productList>} tag, a list of product ions.
 * 
 * @author Alan Race
 */
public class ProductList extends MzMLContentList<Product> {

    /**
     * Serialisation version ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Create an empty list with specified capacity.
     * 
     * @param count Capacity
     */
    public ProductList(int count) {
        super(count);
    }

    /**
     * Copy constructor.
     *
     * @param productList Old ProductList to copy
     * @param rpgList New ReferenceableParamGroupList to match references to
     */
    public ProductList(ProductList productList, ReferenceableParamGroupList rpgList) {
        this(productList.size());

        for (Product product : productList) {
            this.add(new Product(product, rpgList));
        }
    }

    /**
     * Add Product. Helper method to retain API, calls 
     * {@link ProductList#add(com.alanmrace.jimzmlparser.mzml.MzMLTag)}.
     * 
     * @param product Product to add to list
     */
    public void addProduct(Product product) {
        add(product);
    }

    /**
     * Returns Product at specified index in list. Helper method to retain 
     * API, calls {@link ProductList#get(int)}.
     * 
     * @param index Index in the list
     * @return Product ion at index, or null if none exists
     */
    public Product getProduct(int index) {
        return get(index);
    }

    @Override
    public String getTagName() {
        return "productList";
    }
}
