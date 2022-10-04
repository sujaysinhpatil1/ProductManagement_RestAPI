package com.jbk.product.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jbk.product.entity.Product;

@Repository
public interface ProductDao {
	
	public boolean saveProduct(Product product);
	public List<Product> getAllProduct();
	public Product getProductById(String productId);
	public boolean deleteProduct(String productId);
	public boolean updateProduct(Product product);
	
	public List<Product> sortProduct(String sortBy);
	
	public double getTotalCount();
	public Product getMaxPriceProduct();
	public double countSumProductPrice();
	
	public int uploadProductList(List<Product> list);

}
