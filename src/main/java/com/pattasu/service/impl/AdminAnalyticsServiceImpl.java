package com.pattasu.service.impl;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.pattasu.dto.AnalyticsSummaryResponse;
import com.pattasu.dto.SalesDataPoint;
import com.pattasu.entity.Order;
import com.pattasu.repository.OrderRepository;
import com.pattasu.repository.ProductRepository;
import com.pattasu.repository.UserRepository;
import com.pattasu.service.AdminAnalyticsService;

@Service
public class AdminAnalyticsServiceImpl implements AdminAnalyticsService {

    private OrderRepository orderRepository;

    private ProductRepository productRepository;

    private UserRepository userRepository;
    
    public AdminAnalyticsServiceImpl(OrderRepository orderRepository, ProductRepository productRepository, UserRepository userRepositor) {
		this.orderRepository = orderRepository;
		this.productRepository = productRepository;
		this.userRepository = userRepositor;
	}

    @Override
    public AnalyticsSummaryResponse getSummary() {
        AnalyticsSummaryResponse res = new AnalyticsSummaryResponse();
        res.setTotalOrders(orderRepository.count());
        res.setTotalRevenue(orderRepository.sumAllOrderTotals());
        res.setTotalUsers(userRepository.count());
        res.setTotalProducts(productRepository.count());
        return res;
    }

    @Override
    public List<SalesDataPoint> getSalesData() {
        List<Order> orders = orderRepository.findAll();

        Map<String, Double> dateToRevenueMap = new TreeMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd");

        for (Order order : orders) {
            String dateKey = order.getOrderDate().toLocalDate().format(formatter);
            dateToRevenueMap.put(dateKey,
                dateToRevenueMap.getOrDefault(dateKey, 0.0) + order.getTotalPrice());
        }

        return dateToRevenueMap.entrySet().stream()
                .map(e -> new SalesDataPoint(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }
}
