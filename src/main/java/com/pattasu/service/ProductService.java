package com.pattasu.service;

import com.pattasu.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {
	ResponseEntity<Product> addProduct(Product product, MultipartFile file);
	ResponseEntity<Product> updateProduct(Long id, Product product);
    void deleteProduct(Long id);
    Page<Product> getAllProducts(Pageable pageable, String search);
    Product getProductById(Long id);
}
