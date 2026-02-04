package com.pattasu.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pattasu.dto.AddToCartRequest;
import com.pattasu.dto.CartDTO;
import com.pattasu.entity.User;
import com.pattasu.service.CartService;

@RestController
@RequestMapping("/api/cart")
public class CartController {
	
	private static final Logger log = LoggerFactory.getLogger(CartController.class);

    private final CartService cartService;
	
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }
    
    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestBody AddToCartRequest request,
                                            @AuthenticationPrincipal User user) {
    	try {
    		cartService.addToCart(request, user);
    		return ResponseEntity.ok("Product added to cart");
    	}catch(Exception e) {
    		log.info("error at addToCart {}", e.getMessage());
    		return ResponseEntity
    				.status(HttpStatus.BAD_REQUEST)
    				.body(e.getLocalizedMessage());
    	}
    }

    @GetMapping
    public ResponseEntity<List<CartDTO>> getUserCart(@AuthenticationPrincipal User user) {
        return cartService.getCartItems(user);
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<String> removeFromCart(@PathVariable Long productId,
                                                 @AuthenticationPrincipal User user) {
        try {
        	cartService.removeFromCart(productId, user);
            return ResponseEntity.ok("Item removed from cart");
    	}catch(Exception e) {
    		log.info("error at addToCart {}", e.getMessage());
    		return ResponseEntity
    				.status(HttpStatus.BAD_REQUEST)
    				.body(e.getLocalizedMessage());
    	}
    }
}
