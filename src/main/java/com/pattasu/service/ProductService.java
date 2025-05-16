package com.pattasu.service;

import com.pattasu.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    Product addProduct(Product product);
    Product updateProduct(Long id, Product product);
    void deleteProduct(Long id);
    Page<Product> getAllProducts(Pageable pageable);
    Product getProductById(Long id);
}
