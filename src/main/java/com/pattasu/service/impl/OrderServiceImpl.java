package com.pattasu.service.impl;

import com.pattasu.entity.*;
import com.pattasu.repository.CartRepository;
import com.pattasu.repository.OrderRepository;
import com.pattasu.service.OrderService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;

    public OrderServiceImpl(OrderRepository orderRepository, CartRepository cartRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
    }

    @Override
    public Order placeOrder(User user) {
        List<Cart> cartItems = cartRepository.findByUser(user);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
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
