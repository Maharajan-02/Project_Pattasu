package com.pattasu.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.pattasu.dto.AddToCartRequest;
import com.pattasu.dto.CartDTO;
import com.pattasu.dto.ProductDTO;
import com.pattasu.entity.Cart;
import com.pattasu.entity.Product;
import com.pattasu.entity.User;
import com.pattasu.repository.CartRepository;
import com.pattasu.repository.ProductRepository;
import com.pattasu.service.CartService;

import jakarta.transaction.Transactional;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private static final Logger log = LoggerFactory.getLogger(CartServiceImpl.class);

    public CartServiceImpl(CartRepository cartRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void addToCart(AddToCartRequest request, User user) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Cart cart = cartRepository.findByUserAndProductId(user, request.getProductId())
                .orElse(new Cart());

        if(request.getQuantity() == 0) {
        	cartRepository.delete(cart);
        }else {
	        cart.setUser(user);
	        cart.setProduct(product);
	        cart.setQuantity(request.getQuantity());
	
	        cartRepository.save(cart);
        }
    }

    
    @Override
    public ResponseEntity<List<CartDTO>> getCartItems(User user) {
    	try {
    		List<Cart> cartList = cartRepository.findByUserId(user.getId());
            List<CartDTO> cartDtoList = new ArrayList<>();
            for(Cart cart : cartList) {
            	ProductDTO productDto = new ProductDTO();
            	CartDTO cartDto = new CartDTO();
            	cartDto.setId(cart.getId());
            	productDto.setProductId(cart.getProduct().getId());
            	productDto.setName(cart.getProduct().getName());
            	productDto.setDescription(cart.getProduct().getDescription());
            	productDto.setPrice(cart.getProduct().getPrice());
            	productDto.setImageUrl(cart.getProduct().getImageUrl());
            	cartDto.setProduct(productDto);
            	cartDto.setQuantity(cart.getQuantity());
            	
            	cartDtoList.add(cartDto);
            }
            
            return ResponseEntity.status(HttpStatus.OK).body(cartDtoList);
    	}catch(Exception e) {
    		log.info("error during fetching cart {}", e.getMessage());
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ArrayList<>());
    	}
    }

    @Override
    @Transactional
    public void removeFromCart(Long productId, User user) {
        cartRepository.deleteByUserAndProductId(user, productId);
    }

}
