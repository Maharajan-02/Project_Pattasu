package com.pattasu.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.pattasu.dto.GetOrderListDTO;
import com.pattasu.dto.OrderItemDTO;
import com.pattasu.entity.Cart;
import com.pattasu.entity.Order;
import com.pattasu.entity.OrderItem;
import com.pattasu.entity.Product;
import com.pattasu.entity.User;
import com.pattasu.enums.OrderStatus;
import com.pattasu.exception.EmptyCartException;
import com.pattasu.exception.InventoryException;
import com.pattasu.repository.CartRepository;
import com.pattasu.repository.OrderRepository;
import com.pattasu.repository.ProductRepository;
import com.pattasu.service.OrderService;

import jakarta.transaction.Transactional;

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
    public Order placeOrder(String address,  User user) {
        List<Cart> cartItems = cartRepository.findByUser(user);

        if (cartItems.isEmpty()) {
            throw new EmptyCartException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setAddress(address);

        List<OrderItem> orderItems = new ArrayList<>();
        double total = 0.0;

        for (Cart cart : cartItems) {
            Product product = cart.getProduct();
            int quantity = cart.getQuantity();
            double price = product.getPrice();

            // Check stock
            if (product.getStockQuantity() < quantity) {
                throw new InventoryException("Insufficient stock for: " + product.getName());
            }

            // Deduct stock
            product.setStockQuantity(product.getStockQuantity() - quantity);
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
        order.setOrderStatus(OrderStatus.PLACED);

        // Save the order and cascade items
        Order savedOrder = orderRepository.save(order);

        // Clear the cart
        cartRepository.deleteAll(cartItems);

        return savedOrder;
    }

    @Override
    public List<GetOrderListDTO> getUserOrders(User user) {
        List<Order> orderList = orderRepository.findByUser(user);
        
        List<GetOrderListDTO> orderListDto = new ArrayList<>();
        for(Order order : orderList) {
        	GetOrderListDTO orderDto = new GetOrderListDTO();
        	orderDto.setAddress(order.getAddress());
        	orderDto.setOrderDate(order.getOrderDate());
        	orderDto.setStatus(order.getOrderStatus());
        	List<OrderItemDTO> orderItemList = order.getItems().stream()
        		    .map(OrderItemDTO::new)  // uses the conversion constructor
        		    .collect(Collectors.toList());
        	orderDto.setOrderItemDto(orderItemList);
        	orderDto.setNumberOfItems(orderItemList.size());
        	
        	orderListDto.add(orderDto);
        }
        
        return orderListDto;
    }
}
