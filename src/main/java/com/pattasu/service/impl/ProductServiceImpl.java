package com.pattasu.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.pattasu.dto.ProductResponseDto;
import com.pattasu.dto.ProductUploadRequest;
import com.pattasu.entity.Product;
import com.pattasu.repository.CartRepository;
import com.pattasu.repository.OrderItemRepository;
import com.pattasu.repository.ProductRepository;
import com.pattasu.service.ProductService;

import jakarta.transaction.Transactional;

@Service
public class ProductServiceImpl implements ProductService {

    private static final String UPLOAD = "uploads/";
    private static final long MAX_FILE_SIZE = 2L * 1024 * 1024; // 2MB

    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final OrderItemRepository orderItemRepository;

    public ProductServiceImpl(ProductRepository productRepository, CartRepository cartRepository,
                              OrderItemRepository orderItemRepository) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    @CacheEvict(value = { "products", "product" }, allEntries = true)
    public ResponseEntity<Product> addProduct(ProductUploadRequest productDto) {
        String filename = "";
        Path filepath = null;
        try {
            MultipartFile file = productDto.getImage();

            if (file != null && !file.isEmpty()) {
                validateFile(file);

                File directory = new File(UPLOAD);
                if (!directory.exists()) directory.mkdirs();

                filename = System.currentTimeMillis() + "_" + UUID.randomUUID() + "_" + file.getOriginalFilename();
                filepath = Paths.get(UPLOAD, filename);
                Files.copy(file.getInputStream(), filepath, StandardCopyOption.REPLACE_EXISTING);
            }

            Product product = new Product(productDto);
            product.setImageUrl("/images/" + filename);
            Product savedProduct = productRepository.save(product);
            return ResponseEntity.ok(savedProduct);

        } catch (Exception e) {
            log.error("❌ Error while adding product: {}", e.getMessage());
            // Cleanup if image was saved
            try {
                if (filepath != null) Files.deleteIfExists(filepath);
            } catch (IOException ex) {
                log.error("⚠️ Failed to cleanup image file after error: {}", ex.getMessage());
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @CacheEvict(value = { "products", "product" }, allEntries = true)
    public ResponseEntity<Product> updateProduct(Long id, ProductUploadRequest updatedProduct) {
        String filename = "";
        Path filepath = null;
        try {
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Product not found"));

            MultipartFile file = updatedProduct.getImage();

            if (file != null && !file.isEmpty()) {
                validateFile(file);

                File directory = new File(UPLOAD);
                if (!directory.exists()) directory.mkdirs();

                filename = System.currentTimeMillis() + "_" + UUID.randomUUID() + "_" + file.getOriginalFilename();
                filepath = Paths.get(UPLOAD, filename);
                Files.copy(file.getInputStream(), filepath, StandardCopyOption.REPLACE_EXISTING);

                product.setImageUrl("/images/" + filename); // Only update if image was uploaded
            }

            product.setName(updatedProduct.getName());
            product.setDescription(updatedProduct.getDescription());
            product.setPrice(updatedProduct.getPrice());
            product.setStockQuantity(updatedProduct.getStockQuantity());

            Product products = productRepository.save(product);
            return ResponseEntity.ok(products);

        } catch (Exception e) {
            log.error("❌ Error while updating product: {}", e.getMessage());
            try {
                if (filepath != null) Files.deleteIfExists(filepath);
            } catch (IOException ex) {
                log.error("⚠️ Failed to cleanup image file after error: {}", ex.getMessage());
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = { "products", "product" }, allEntries = true)
    public void deleteProduct(Long id) {
        cartRepository.deleteByProductId(id);
        orderItemRepository.deleteByProductId(id);
        productRepository.deleteById(id);
    }

    @Override
    @Cacheable(value = "products", condition = "#search == null || #search.trim().isEmpty()", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<ProductResponseDto> getAllProducts(Pageable pageable, String search) {
        Page<Product> products;
        if (search == null || search.trim().isEmpty())
            products = productRepository.findAll(pageable);
        else
            products = productRepository.findByNameContainingIgnoreCase(search.trim(), pageable);
        return products.map(ProductResponseDto::new);
    }

    @Override
    @Cacheable(value = "product", key = "#id")
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found"));
    }

    private void validateFile(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Only image files are allowed");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("Image too large. Max 2MB allowed");
        }
    }
}
