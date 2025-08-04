package com.pattasu.dto;

import com.pattasu.entity.Product;

public class CartDTO {

    private Long id;
    private ProductDTO product;
    private int quantity;
    private Double discount;
    private Double finalPrice;

    public CartDTO(Product product, Integer quantity) {
        this.id = product.getId();
        this.product = new ProductDTO(product); // populate from entity
        this.quantity = quantity;
        this.discount = product.getDiscount();

        Double price = product.getPrice();
        if (discount != null && discount > 0) {
            double discountedPrice = price - (price * (discount / 100.0));
            this.finalPrice = Math.round(discountedPrice * 100.0) / 100.0;
        } else {
            this.finalPrice = price;
        }
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public ProductDTO getProduct() { return product; }
    public void setProduct(ProductDTO product) { this.product = product; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public Double getDiscount() { return discount; }
    public void setDiscount(Double discount) { this.discount = discount; }

    public Double getFinalPrice() { return finalPrice; }
    public void setFinalPrice(Double finalPrice) { this.finalPrice = finalPrice; }
}
