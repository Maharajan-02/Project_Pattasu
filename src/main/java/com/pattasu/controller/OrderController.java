package com.pattasu.controller;

import com.pattasu.entity.Order;
import com.pattasu.entity.User;
import com.pattasu.service.OrderService;

import jakarta.transaction.Transactional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/place")
    @Transactional
    public ResponseEntity<Order> placeOrder(@AuthenticationPrincipal User user) {
        Order order = orderService.placeOrder(user);
        return ResponseEntity.ok(order);
    }

    @GetMapping
    public ResponseEntity<List<Order>> getUserOrders(@AuthenticationPrincipal User user) {
        List<Order> orders = orderService.getUserOrders(user);
        return ResponseEntity.ok(orders);
    }
}
