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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.pattasu.entity.Product;
import com.pattasu.repository.ProductRepository;
import com.pattasu.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @CacheEvict(value = { "products", "product" }, allEntries = true)
    public Product addProduct(Product product, MultipartFile file) {
    	try {
    		String uploadDir = "uploads/";
    		String fileName = System.currentTimeMillis() + "_" +file.getOriginalFilename();
    		Path path = Paths.get(uploadDir + fileName);
    		Files.createDirectories(path.getParent());
    		Files.write(path, file.getBytes());
    		log.info("Path to store image is - {}",path.getParent());
    		product.setImageUrl(path.getParent().toString());
    		return productRepository.save(product);
    	}catch(Exception e) {
    		log.info("Error while adding Product {}", e.getMessage());
    		return new Product();
    	}
    }

    @Override
    @CacheEvict(value = { "products", "product" }, allEntries = true)
    public Product updateProduct(Long id, Product updatedProduct) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found"));

        product.setName(updatedProduct.getName());
        product.setDescription(updatedProduct.getDescription());
        product.setPrice(updatedProduct.getPrice());
        product.setImageUrl(updatedProduct.getImageUrl());
        product.setStockQuantity(updatedProduct.getStockQuantity());

        return productRepository.save(product);
    }

    @Override
    @CacheEvict(value = { "products", "product" }, allEntries = true)
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    @Cacheable(value = "products", key = "#page+ '-' + #size")
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    @Cacheable(value = "product", key = "#id")
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found"));
    }
}
