package com.pattasu.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.pattasu.dto.AddToCartRequest;
import com.pattasu.dto.CartDTO;
import com.pattasu.entity.User;

public interface CartService {
    void addToCart(AddToCartRequest request, User user);
    ResponseEntity<List<CartDTO>> getCartItems(User user);
    void removeFromCart(Long productId, User user);
}
