package com.pattasu.service.impl;

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

import com.pattasu.dto.ProductResponseDto;
import com.pattasu.dto.ProductUploadRequest;
import com.pattasu.entity.Product;
import com.pattasu.repository.ProductRepository;
import com.pattasu.service.ProductService;
import com.pattasu.util.ImageUploadService;

@Service
public class ProductServiceImpl implements ProductService {
	
	private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ProductRepository productRepository;
    private final ImageUploadService imageUploadService;

    public ProductServiceImpl(ProductRepository productRepository, ImageUploadService imageUploadService) {
        this.productRepository = productRepository;
        this.imageUploadService = imageUploadService;
    }

    @Override
    @CacheEvict(value = { "products", "product" }, allEntries = true)
    public ResponseEntity<Product> addProduct(ProductUploadRequest productDto) {
    	try {
    		String imageUrl = null;
    		MultipartFile file = productDto.getImage();
    		
    		if(file != null && !file.isEmpty()) {
       		 	imageUrl = imageUploadService.upload(file);
    		}
    		
    		Product product = new Product(productDto);
    		if(imageUrl != null) {
    			product.setImageUrl( imageUrl);
    		}
    		
    		if(productDto.getDiscount() != null){
    		    product.setDiscount(productDto.getDiscount());
    		}
    		
    		Product savedProduct = productRepository.save(product);
    		return ResponseEntity.ok(savedProduct);
    	}catch(Exception e) {
    		log.info("Error while adding Product {}", e.getMessage());
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}
    }

    @Override
    @CacheEvict(value = { "products", "product" }, allEntries = true)
    public ResponseEntity<Product> updateProduct(Long id, ProductUploadRequest updatedProduct) {
        try {
        	Product product = productRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Product not found"));
        	String filename = null;
        	MultipartFile file = updatedProduct.getImage();
    		
    		if(file != null && !file.isEmpty()) {
    			filename=imageUploadService.upload(file);
    			if(filename != null) {
    				product.setImageUrl(filename);
    			}
    		}

            product.setName(updatedProduct.getName());
            product.setDescription(updatedProduct.getDescription());
            product.setPrice(updatedProduct.getPrice());
            
            product.setStockQuantity(updatedProduct.getStockQuantity());
            product.setActiveProduct(updatedProduct.getActive());
            if(updatedProduct.getDiscount() != null){
                product.setDiscount(updatedProduct.getDiscount());
            }
            
            Product products = productRepository.save(product);
            return ResponseEntity.ok(products);
        }catch(Exception e) {
        	log.info("Error while updating product {}", e.getMessage());
        	return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
//    @Cacheable(value = "products", condition = "#search == null || #search.trim().isEmpty()", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<ProductResponseDto> getAllProducts(Pageable pageable, String search) {
    	Page<Product> products;
    	if(search == null || search.trim().isEmpty())
    		products = productRepository.findAll(pageable);
    	else {
    		products = productRepository.findByNameContainingIgnoreCase(search.trim(), pageable);
    	}
    	return products.map(ProductResponseDto::new);
    }


    @Override
    @Cacheable(value = "product", key = "#id")
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found"));
    }

    @Override
    public Page<ProductResponseDto> getActiveProducts(Pageable pageable, String search) {
        Page<Product> products;
        if (search == null || search.trim().isEmpty()) {
            products = productRepository.findByActiveProductTrue(pageable);
        } else {
            products = productRepository.findByActiveProductTrueAndNameContainingIgnoreCase(search.trim(), pageable);
        }
        return products.map(ProductResponseDto::new);
    }
}
