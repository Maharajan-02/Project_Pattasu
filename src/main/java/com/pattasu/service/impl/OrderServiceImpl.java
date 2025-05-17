package com.pattasu.service.impl;

import com.pattasu.entity.*;
import com.pattasu.exception.EmptyCartException;
import com.pattasu.exception.InventoryException;
import com.pattasu.repository.CartRepository;
import com.pattasu.repository.OrderRepository;
import com.pattasu.repository.ProductRepository;
import com.pattasu.service.OrderService;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public OrderServiceImpl(OrderRepository orderRepository, CartRepository cartRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public Order placeOrder(User user) {
        List<Cart> cartItems = cartRepository.findByUser(user);

        if (cartItems.isEmpty()) {
            throw new EmptyCartException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());

        List<OrderItem> orderItems = new ArrayList<>();
        double total = 0.0;

        for (Cart cart : cartItems) {
            Product product = cart.getProduct();
            int quantity = cart.getQuantity();
            double price = product.getPrice();

            // Check stock
            if (product.getQuantity() < quantity) {
                throw new InventoryException("Insufficient stock for: " + product.getName());
            }

            // Deduct stock
            product.setQuantity(product.getQuantity() - quantity);
            productRepository.save(product);

            // Create order item
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(quantity);
            item.setPrice(price);

            orderItems.add(item);
            total += price * quantity;
        }

        order.setItems(orderItems);
        order.setTotalPrice(total);

        // Save the order and cascade items
        Order savedOrder = orderRepository.save(order);

        // Clear the cart
        cartRepository.deleteAll(cartItems);

        return savedOrder;
    }

    @Override
    public List<Order> getUserOrders(User user) {
        return orderRepository.findByUser(user);
    }
}
