package com.nexsys.controller;

import com.nexsys.model.Category;
import com.nexsys.model.Product;
import com.nexsys.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/nexsys/v1")
public class ProductController {

	@Autowired
	private ProductService productService;

	@GetMapping("/products")
	public ResponseEntity<List<Product>> getAllProducts() {
		return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
	}

	@GetMapping("/categories")
	public ResponseEntity<List<Category>> getAllCategories() {
		return new ResponseEntity<>(productService.getAllCategories(), HttpStatus.OK);
	}

	@PostMapping("/products")
	public ResponseEntity<Map<String, Integer>> createProduct(@RequestBody Product product) {
		Product createdProduct = productService.createProduct(product);
		return new ResponseEntity<>(Map.of("pid", createdProduct.getPid()), HttpStatus.CREATED);
	}
}
