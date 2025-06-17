package com.pattasu.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.pattasu.dto.ProductUploadRequest;
import com.pattasu.entity.Product;

public interface ProductService {
	ResponseEntity<Product> addProduct(ProductUploadRequest product);
	ResponseEntity<Product> updateProduct(Long id, Product product);
    void deleteProduct(Long id);
    Page<Product> getAllProducts(Pageable pageable, String search);
    Product getProductById(Long id);
}
