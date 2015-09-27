package com.alanmrace.jimzmlparser.mzML;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.tree.TreeNode;

public class ProductList extends MzMLContent  implements Serializable, Iterable<Product> {

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
		
		for(Product product : productList) 
			this.productList.add(new Product(product, rpgList));
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
	
	public void outputXML(BufferedWriter output, int indent) throws IOException {
		MzMLContent.indent(output, indent);
		output.write("<productList");
		output.write(" count=\"" + productList.size() + "\"");
		output.write(">\n");
		
		for(Product product : productList)
			product.outputXML(output, indent+1);
		
		MzMLContent.indent(output, indent);
		output.write("</productList>\n");
	}
	
	public String toString() {
		return "productList";
	}
	
//	@Override
//	public int getChildCount() {
//		// 
//		return size();
//	}
//	
//	@Override
//	public int getIndex(TreeNode childNode) {
//		return productList.indexOf(childNode);
//	}
//	
//	@Override
//	public TreeNode getChildAt(int index) {
//		return productList.get(index);
//	}
//	
//	@Override
//	public Enumeration<TreeNode> children() {
//		Vector<TreeNode> children = new Vector<TreeNode>();
//		children.addAll(productList);
//		
//		return children.elements();
//	}

	@Override
	public Iterator<Product> iterator() {
		return productList.iterator();
	}
}
