package com.pattasu.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.pattasu.dto.GetOrderListDTO;
import com.pattasu.dto.OrderResponseDTO;
import com.pattasu.dto.UpdateOrderDTO;
import com.pattasu.entity.Order;
import com.pattasu.entity.User;

public interface OrderService {
	Order placeOrder(String address,  User user);
    List<GetOrderListDTO> getUserOrders(User user);
    byte[] generateInvoicePdf(Long orderId);
    ResponseEntity<String> updateOrder(UpdateOrderDTO updateOrder);
    ResponseEntity<List<OrderResponseDTO>> getAllOrderWithUserInfo();
}
