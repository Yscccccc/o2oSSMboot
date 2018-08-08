package com.ysc.exceptions;

public class WechatAuthOperationException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6192392121069452025L;
	
	public WechatAuthOperationException(String msg) {
		super(msg);
	}
}
