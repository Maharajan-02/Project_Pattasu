package com.pattasu.exception;

public class EmptyCartException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
    public EmptyCartException(String message) {
        super(message);
    }
}
