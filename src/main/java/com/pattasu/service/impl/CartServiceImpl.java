package com.pattasu.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pattasu.dto.AddToCartRequest;
import com.pattasu.entity.Cart;
import com.pattasu.entity.Product;
import com.pattasu.entity.User;
import com.pattasu.repository.CartRepository;
import com.pattasu.repository.ProductRepository;
import com.pattasu.service.CartService;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

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

        cart.setUser(user);
        cart.setProduct(product);

        if (cart.getId() == null) {
            // new item
            cart.setQuantity(Math.max(1, request.getQuantity()));
        } else {
            int updatedQuantity = cart.getQuantity() + request.getQuantity();
            if (updatedQuantity <= 0) {
                cartRepository.delete(cart); // remove item if quantity drops to 0
                return;
            }
            cart.setQuantity(updatedQuantity);
        }

        cartRepository.save(cart);
    }

    
    @Override
    public List<Cart> getCartItems(User user) {
        return cartRepository.findByUser(user);
    }

    @Override
    public void removeFromCart(Long productId, User user) {
        cartRepository.deleteByUserAndProductId(user, productId);
    }

}
