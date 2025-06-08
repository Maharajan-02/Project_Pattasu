package com.pattasu.service;

import java.util.List;

import com.pattasu.dto.GetOrderListDTO;
import com.pattasu.entity.Order;
import com.pattasu.entity.User;

public interface OrderService {
	Order placeOrder(String address,  User user);
    List<GetOrderListDTO> getUserOrders(User user);
}
