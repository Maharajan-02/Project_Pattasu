package com.pattasu.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.pattasu.dto.ProductResponseDto;
import com.pattasu.dto.ProductUploadRequest;
import com.pattasu.entity.Product;

public interface ProductService {
	ResponseEntity<Product> addProduct(ProductUploadRequest product);
	ResponseEntity<Product> updateProduct(Long id, Product product);
    void deleteProduct(Long id);
    Page<ProductResponseDto> getAllProducts(Pageable pageable, String search);
    Product getProductById(Long id);
}
