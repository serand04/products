package com.nexsys.service;

import com.nexsys.model.Category;
import com.nexsys.model.JSCategory;
import com.nexsys.model.JSProduct;
import com.nexsys.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductService {

	@Autowired
	private RestTemplate restTemplate;

	public Product createProduct(Product product) {
		String url = "https://api.escuelajs.co/api/v1/products";

		if (product.getName() == null || product.getName().isEmpty()) {
			throw new IllegalArgumentException("El nombre del producto es obligatorio.");
		}
		if (product.getPriceFinal() <= 0) {
			throw new IllegalArgumentException("El precio del producto debe ser mayor que cero.");
		}

		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("title", product.getName());
		requestBody.put("price", product.getPriceFinal());
		requestBody.put("description", product.getDescription());
		requestBody.put("categoryId", product.getCategoryId());
		requestBody.put("images", List.of(product.getImageUrl()));

		try {
			ResponseEntity<JSProduct> response = restTemplate.postForEntity(url, requestBody, JSProduct.class);
			JSProduct createdProduct = response.getBody();
			Product result = new Product();
			result.setPid(createdProduct.getId());
			result.setName(createdProduct.getTitle());
			result.setPriceFinal(createdProduct.getPrice());
			result.setDescription(createdProduct.getDescription());
			result.setCategoryId(createdProduct.getId());
			result.setImageUrl(createdProduct.getImages().get(0));

			return result;

		} catch (HttpClientErrorException ex) {
			if (ex.getStatusCode() == HttpStatus.BAD_REQUEST) {
				throw new IllegalArgumentException("Error al crear el producto: " + ex.getResponseBodyAsString());
			} else {
				throw new RuntimeException("Error al comunicarse con la API de EscuelaJS", ex);
			}
		}
	}

	public List<Product> getAllProducts() {
		String url = "https://api.escuelajs.co/api/v1/products";
		JSProduct[] products = restTemplate.getForObject(url, JSProduct[].class);

		return Arrays.stream(products).map(prod -> {
			Product product = new Product();
			product.setPid(prod.getId());
			product.setName(prod.getTitle());
			product.setPriceFinal(prod.getPrice());
			product.setDescription(prod.getDescription());
			return product;
		}).toList();
	}

	public List<Category> getAllCategories() {
		String url = "https://api.escuelajs.co/api/v1/categories";
		JSCategory[] categories = restTemplate.getForObject(url, JSCategory[].class);

		return Arrays.stream(categories).map(cat -> {
			Category category = new Category();
			category.setCid(cat.getId());
			category.setTitle(cat.getName());
			return category;
		}).toList();
	}
}