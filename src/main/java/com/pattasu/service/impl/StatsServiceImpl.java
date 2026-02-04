package com.pattasu.service.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.pattasu.dto.StatsDTO;
import com.pattasu.enums.OrderStatus;
import com.pattasu.repository.OrderRepository;
import com.pattasu.repository.ProductRepository;
import com.pattasu.service.StatsService;

@Service
public class StatsServiceImpl implements StatsService {

	private final ProductRepository productRepository;
	private final OrderRepository orderRepository;
	
	private final List<OrderStatus> pendingList = Arrays.asList(
	        OrderStatus.PLACED,
	        OrderStatus.CONFIRMED,
	        OrderStatus.PROCESSING
	);
	
	public StatsServiceImpl(ProductRepository productRepository, OrderRepository orderRepository) {
		this.productRepository = productRepository;
		this.orderRepository = orderRepository;
	}
	
	@Override
	public ResponseEntity<StatsDTO> getQuickStats() {
		try {
			StatsDTO statsDto = new StatsDTO();
			Long totalCount = orderRepository.count();
			Long pendingCount = orderRepository.countByOrderStatusIn(pendingList);
			statsDto.setTotalOrders(totalCount);
			statsDto.setTotalProducts(productRepository.count());
			
			statsDto.setPendingOrders(pendingCount);
			statsDto.setCompletedOrders(totalCount - pendingCount);
			
			return ResponseEntity.status(HttpStatus.OK).body(statsDto);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StatsDTO());
		}
	}
	
}
