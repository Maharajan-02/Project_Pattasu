package com.pattasu.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pattasu.dto.GetOrderListDTO;
import com.pattasu.entity.Order;
import com.pattasu.entity.User;
import com.pattasu.service.OrderService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/place")
    @Transactional
    public ResponseEntity<Order> placeOrder(@AuthenticationPrincipal User user, @RequestBody String address) {
        Order order = orderService.placeOrder(address, user);
        return ResponseEntity.ok(order);
    }

    @GetMapping
    public ResponseEntity<List<GetOrderListDTO>> getUserOrders(@AuthenticationPrincipal User user) {
        List<GetOrderListDTO> orders = orderService.getUserOrders(user);
        return ResponseEntity.ok(orders);
    }
}
