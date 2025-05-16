package com.pattasu.controller;

import com.pattasu.dto.AddToCartRequest;
import com.pattasu.entity.Cart;
import com.pattasu.entity.User;
import com.pattasu.service.CartService;
import com.pattasu.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final UserService userService;

    public CartController(CartService cartService, UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestBody AddToCartRequest request,
                                            @AuthenticationPrincipal User user) {
        cartService.addToCart(request, user);
        return ResponseEntity.ok("Product added to cart");
    }

    @GetMapping
    public ResponseEntity<List<Cart>> getUserCart(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(cartService.getCartItems(user));
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<String> removeFromCart(@PathVariable Long productId,
                                                 @AuthenticationPrincipal User user) {
        cartService.removeFromCart(productId, user);
        return ResponseEntity.ok("Item removed from cart");
    }
}
