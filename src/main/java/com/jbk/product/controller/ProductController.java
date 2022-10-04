package com.jbk.product.controller;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.jbk.product.entity.Product;
import com.jbk.product.exception.ProductAreadyExistException;
import com.jbk.product.exception.ProductNotFoundException;
import com.jbk.product.service.ProductService;

@RestController
public class ProductController {

	@Autowired
	private ProductService service;

	@PostMapping(value = "/saveProduct")
	public ResponseEntity<Boolean> saveProduct(@Valid @RequestBody Product product) { // type must be wrapper class in ResponseEntity
																				
		boolean isAdded = service.saveProduct(product);

		if (isAdded) {
			return new ResponseEntity<Boolean>(isAdded, HttpStatus.CREATED);
		} else {
			throw new ProductAreadyExistException("Product already exist with id : "+ product.getProductId());
		}
	}

	@GetMapping(value = "/getAllProduct")
	public ResponseEntity<List<Product>> getAllProduct() {

		List<Product> allProduct = service.getAllProduct();

		if (allProduct.isEmpty()) {
			return new ResponseEntity<List<Product>>(HttpStatus.NO_CONTENT);
		} else {
			throw new ProductNotFoundException("Products are not found");
		}
	}

	@GetMapping(value = "/getProdctById")
	public ResponseEntity<Product> getProdctById(@RequestParam String productId) {

		Product product = service.getProductById(productId);
		if (product != null) {
			return new ResponseEntity<Product>(product, HttpStatus.OK);
		} else {
			throw new ProductNotFoundException("Product not found for Id: " +productId);
		}
	}

	@GetMapping(value = "/deleteProduct")
	public ResponseEntity<Boolean> deleteProduct(@RequestParam String productId) {

		boolean isDeleted = service.deleteProduct(productId);
		if (isDeleted) {
			return new ResponseEntity<Boolean>(isDeleted, HttpStatus.OK);
		} else {
			throw new ProductNotFoundException("Product not found for Id: " +productId);
		}
	}

	@PutMapping(value = "/updateProduct")
	public ResponseEntity<Boolean> updateProduct(@Valid @RequestBody Product product) {

		boolean isUpdated = service.updateProduct(product);
		if (isUpdated) {
			return new ResponseEntity<Boolean>(isUpdated, HttpStatus.OK);
		}else {
			throw new ProductNotFoundException("Product not found for update for Id: " +product.getProductId());
		}
	}
	
	@GetMapping(value = "/sortProducts")
	public ResponseEntity<List<Product>> sortProducts(@RequestParam String sortBy){
		
		List<Product> list = service.sortProduct(sortBy);
		if(!list.isEmpty()) {
			return new ResponseEntity<List<Product>>(list, HttpStatus.OK);
		}else {
			throw new ProductNotFoundException("Products are not found");
		}
	}
	
	@GetMapping(value = "/getTotalCount")
	public ResponseEntity<Double> getTotalCount(){
		
		double count = service.getTotalCount();
		if(count > 0) {
			return new ResponseEntity<Double>(count, HttpStatus.OK);
		}else {
			throw new ProductNotFoundException("Products are not found");
		}
	}
	
	@GetMapping(value ="/getMaxPriceProduct")
	public ResponseEntity<Product> getMaxPriceProduct() {
		
		Product product = service.getMaxPriceProduct();
		if(product != null) {
			return new ResponseEntity<Product>(product, HttpStatus.OK);
		}else {
			throw new ProductNotFoundException("Products are not found");
		}
	}
	
	@GetMapping(value ="/countSumProductPrice")
	public ResponseEntity<Double> countSumProductPrice() {
		
		double sum = service.countSumProductPrice();
		if(sum > 0) {
			return new ResponseEntity<Double>(sum, HttpStatus.OK);
		}else {
			throw new ProductNotFoundException("Products are not found");
		}
	}
	
	@PostMapping(value ="/uploadSheet")
	public ResponseEntity<Integer> uploadSheet(@RequestParam CommonsMultipartFile file, HttpSession session){
		
		int count = service.uploadSheet(file, session);
		return new ResponseEntity<Integer>(count, HttpStatus.OK);
	}
	
	@GetMapping(value = "/exportToExcel")
	public ResponseEntity<String> exportToExcel() {
		String msg = service.exportToExcel();
		return new ResponseEntity<String>(msg, HttpStatus.OK);

	}
	

}
