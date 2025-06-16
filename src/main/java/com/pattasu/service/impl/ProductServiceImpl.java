package com.pattasu.service.impl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.pattasu.entity.Product;
import com.pattasu.repository.ProductRepository;
import com.pattasu.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
	
	private static final String UPLOAD = "uploads/";

	private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @CacheEvict(value = { "products", "product" }, allEntries = true)
    public ResponseEntity<Product> addProduct(Product product, MultipartFile file) {
    	try {
    		String fileName = System.currentTimeMillis() + "_" +file.getOriginalFilename();
    		Path path = Paths.get(UPLOAD + fileName);
    		Files.createDirectories(path.getParent());
    		Files.write(path, file.getBytes());
    		log.info("Path to store image is - {}",path.getParent());
    		product.setImageUrl(path.getParent().toString());
    		Product savedProduct = productRepository.save(product);
    		return ResponseEntity.ok(savedProduct);
    	}catch(Exception e) {
    		log.info("Error while adding Product {}", e.getMessage());
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}
    }

    @Override
    @CacheEvict(value = { "products", "product" }, allEntries = true)
    public ResponseEntity<Product> updateProduct(Long id, Product updatedProduct) {
        try {
        	Product product = productRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Product not found"));

            product.setName(updatedProduct.getName());
            product.setDescription(updatedProduct.getDescription());
            product.setPrice(updatedProduct.getPrice());
            product.setImageUrl(updatedProduct.getImageUrl());
            product.setStockQuantity(updatedProduct.getStockQuantity());
            
            Product products = productRepository.save(product);
            return ResponseEntity.ok(products);
        }catch(Exception e) {
        	log.info("Error while updating product {}", e.getMessage());
        	return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        
    }

    @Override
    @CacheEvict(value = { "products", "product" }, allEntries = true)
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    @Cacheable(value = "products", condition = "#search == null || #search.trim().isEmpty()", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<Product> getAllProducts(Pageable pageable, String search) {
    	if(search == null || search.trim().isEmpty())
    		return productRepository.findAll(pageable);
    	else {
    		return productRepository.findByNameContainingIgnoreCase(search.trim(), pageable);
    	}
    }


    @Override
    @Cacheable(value = "product", key = "#id")
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found"));
    }
}
