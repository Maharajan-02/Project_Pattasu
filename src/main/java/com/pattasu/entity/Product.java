package com.pattasu.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pattasu.dto.ProductUploadRequest;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "product")
@JsonSerialize
public class Product {
	
    public Product() {
		super();
	}
    
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private double price;
    private String imageUrl;
    private int stockQuantity;
    
    public Product(ProductUploadRequest productDto) {
		this.name = productDto.getName();
		this.description = productDto.getDescription();
		this.price = productDto.getPrice();
		this.stockQuantity = productDto.getStockQuantity();
	}

    public Long getId() {
        return id;
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

	public int getStockQuantity() {
		return stockQuantity;
	}

	public void setStockQuantity(int stockQuantity) {
		this.stockQuantity = stockQuantity;
	}
    
	@Transient
	public String getFullImageUrl() {
	    if (this.imageUrl == null || this.imageUrl.isEmpty()) {
	        return "/images/logo.png";
	    }
	    return this.imageUrl;
	}
}
