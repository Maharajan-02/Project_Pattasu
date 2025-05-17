package com.pattasu.exception;

public class InventoryException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
    public InventoryException(String message) {
        super(message);
    }
}
