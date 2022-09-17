package com.rohan.springweb.controllers;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.rohan.springweb.entities.Product;
import com.rohan.springweb.repos.ProductRepository;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Product Rest Endpoint")
//@Hidden
public class ProductRestController {

	@Autowired
	ProductRepository repo;

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductRestController.class);

	@RequestMapping(value = "/products", method = RequestMethod.GET)
	@Hidden
	public List<Product> getProducts() {
		return repo.findAll();
	}

	@RequestMapping(value = "/products/{id}", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	@Cacheable("product-cache")
	@Operation(summary = "Returns a product", description = "Takes ID and returns a single Product")
	public @ApiResponse(description = "Product Object") Product getProduct(@Parameter(description = "Id of the Product") @PathVariable("id") int id) {
		LOGGER.info("Finding product by ID: " + id);
		return repo.findById(id).get();
	}

	@RequestMapping(value = "/products", method = RequestMethod.POST)
	public Product createProduct(@Valid @RequestBody Product product) {
		return repo.save(product);
	}

	@RequestMapping(value = "/products", method = RequestMethod.PUT)
	public Product updateProduct(@RequestBody Product product) {
		return repo.save(product);
	}

	@RequestMapping(value = "/products/{id}", method = RequestMethod.DELETE)
	@CacheEvict("product-cache")
	public void deleteProduct(@PathVariable("id") int id) {
		repo.deleteById(id);
	}

}