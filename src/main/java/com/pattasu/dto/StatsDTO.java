package com.pattasu.dto;

public class StatsDTO {

	private Long totalProducts;
	
	private Long totalOrders;
	
	private Long pendingOrders;
	
	private Long completedOrders;

	public Long getTotalProducts() {
		return totalProducts;
	}

	public void setTotalProducts(Long totalProducts) {
		this.totalProducts = totalProducts;
	}

	public Long getTotalOrders() {
		return totalOrders;
	}

	public void setTotalOrders(Long totalOrders) {
		this.totalOrders = totalOrders;
	}

	public Long getPendingOrders() {
		return pendingOrders;
	}

	public void setPendingOrders(Long pendingOrders) {
		this.pendingOrders = pendingOrders;
	}

	public Long getCompletedOrders() {
		return completedOrders;
	}

	public void setCompletedOrders(Long completedOrders) {
		this.completedOrders = completedOrders;
	}
	
}
