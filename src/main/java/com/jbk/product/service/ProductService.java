package com.jbk.product.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.jbk.product.entity.Product;

@Service
public interface ProductService {
	
	public boolean saveProduct(Product product);
	public List<Product> getAllProduct();
	public Product getProductById(String productId);
	public boolean deleteProduct(String productId);
	public boolean updateProduct(Product product);
	
	public List<Product> sortProduct(String sortBy);
	
	public double getTotalCount();
	public Product getMaxPriceProduct();
	public double countSumProductPrice();
	
	public int uploadSheet(CommonsMultipartFile file, HttpSession httpSession);  // upload Excel sheet
	
	public String exportToExcel();
	
}
