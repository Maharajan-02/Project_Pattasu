package com.pattasu.dto;

import com.pattasu.entity.Product;

public class ProductDTO {
	
	private Long productId;
    private String name;
    private String description;
    private double price;
    private String imageUrl;
    private boolean active;
    private int stockQuantity;
    private double discount;
    
	public double getDiscount() {
		return discount;
	}
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int getStockQuantity() {
		return stockQuantity;
	}

	public void setStockQuantity(int stockQuantity) {
		this.stockQuantity = stockQuantity;
	}
	
	public ProductDTO() {}

	public ProductDTO(Product product) {
		this.productId = product.getId();
		this.name = product.getName();
		this.description = product.getDescription();
		this.price = product.getPrice();
		this.imageUrl = product.getImageUrl();
		this.active = product.isActive();
		this.stockQuantity = product.getStockQuantity();
		this.discount = product.getDiscount();
	}
}
