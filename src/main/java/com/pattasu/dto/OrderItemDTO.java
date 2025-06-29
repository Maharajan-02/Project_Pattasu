package com.pattasu.dto;

import com.pattasu.entity.OrderItem;

public class OrderItemDTO {

	    public OrderItemDTO(OrderItem orderItem) {
			this.product =  new ProductDTO(orderItem.getProduct());
			this.quantity = orderItem.getQuantity();
		}
	    
	    public OrderItemDTO() {}
	    
		private ProductDTO product;

	    private int quantity;
	    
	    private Long price;

	    public ProductDTO getProduct() { return product; }
	    public void setProduct(ProductDTO product) { this.product = product; }

	    public int getQuantity() { return quantity; }
	    public void setQuantity(int quantity) { this.quantity = quantity; }

		public Long getPrice() {
			return price;
		}

		public void setPrice(Long price) {
			this.price = price;
		}
}
