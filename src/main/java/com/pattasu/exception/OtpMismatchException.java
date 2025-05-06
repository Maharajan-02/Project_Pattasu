package com.pattasu.exception;

public class OtpMismatchException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
    public OtpMismatchException(String message) {
        super(message);
    }
}
