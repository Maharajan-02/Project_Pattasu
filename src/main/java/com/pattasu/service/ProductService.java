package com.pattasu.service;

import com.pattasu.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {
    Product addProduct(Product product, MultipartFile file);
    Product updateProduct(Long id, Product product);
    void deleteProduct(Long id);
    Page<Product> getAllProducts(Pageable pageable);
    Product getProductById(Long id);
}
