package com.pattasu.service.impl;

import com.pattasu.entity.Product;
import com.pattasu.repository.ProductRepository;
import com.pattasu.service.ProductService;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @CacheEvict(value = { "products", "product" }, allEntries = true)
    public Product addProduct(Product product) {
        return productRepository.save(product);
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
        product.setQuantity(updatedProduct.getQuantity());

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
