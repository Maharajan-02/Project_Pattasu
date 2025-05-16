package com.pattasu.service;

import com.pattasu.entity.Order;
import com.pattasu.entity.User;

import java.util.List;

public interface OrderService {
    Order placeOrder(User user);
    List<Order> getUserOrders(User user);
}
