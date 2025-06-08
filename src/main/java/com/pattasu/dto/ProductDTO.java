package com.pattasu.dto;

import com.pattasu.entity.Product;

public class ProductDTO {
	
	public ProductDTO() {}

	public ProductDTO(Product product) {
		this.productId = product.getId();
		this.name = product.getName();
		this.description = product.getDescription();
		this.price = product.getPrice();
		this.imageUrl = product.getImageUrl();
	}
	private Long productId;
    private String name;
    private String description;
    private double price;
    private String imageUrl;
    
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
}
