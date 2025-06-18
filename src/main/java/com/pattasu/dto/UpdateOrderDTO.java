package com.pattasu.dto;

import com.pattasu.enums.OrderStatus;

public class UpdateOrderDTO {
	
	private Long id;
	private OrderStatus orderStatus;
	private String trackingId;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public OrderStatus getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getTrackingId() {
		return trackingId;
	}
	public void setTrackingId(String trackingId) {
		this.trackingId = trackingId;
	}
	
	

}
