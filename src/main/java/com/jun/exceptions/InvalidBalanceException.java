package com.jun.exceptions;

public class InvalidBalanceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -759100143540568554L;

	public InvalidBalanceException() {
		// TODO Auto-generated constructor stub
	}

	public InvalidBalanceException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public InvalidBalanceException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public InvalidBalanceException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public InvalidBalanceException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
