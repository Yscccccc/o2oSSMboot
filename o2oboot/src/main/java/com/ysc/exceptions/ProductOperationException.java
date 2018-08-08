package com.ysc.exceptions;

public class ProductOperationException extends RuntimeException{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2462665468791108546L;

	public ProductOperationException (String msg) {
		super(msg);
	}
}
