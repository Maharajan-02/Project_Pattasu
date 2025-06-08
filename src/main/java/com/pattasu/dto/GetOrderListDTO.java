package com.pattasu.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.pattasu.enums.OrderStatus;

public class GetOrderListDTO {

	private Long orderId;
	private List<OrderItemDTO> orderItemDto;
	private String address;
	private LocalDateTime orderDate;
	private OrderStatus status;
	private int numberOfItems;
	
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public List<OrderItemDTO> getOrderItemDto() {
		return orderItemDto;
	}
	public void setOrderItemDto(List<OrderItemDTO> orderItemDto) {
		this.orderItemDto = orderItemDto;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public LocalDateTime getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(LocalDateTime orderDate) {
		this.orderDate = orderDate;
	}
	public OrderStatus getStatus() {
		return status;
	}
	public void setStatus(OrderStatus status) {
		this.status = status;
	}
	public int getNumberOfItems() {
		return numberOfItems;
	}
	public void setNumberOfItems(int numberOfItems) {
		this.numberOfItems = numberOfItems;
	}
	
}
