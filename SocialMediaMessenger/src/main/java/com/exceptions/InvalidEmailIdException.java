package com.exceptions;

public class InvalidEmailIdException extends RuntimeException{
	
	
	private static final long serialVersionUID = 1L;

	public InvalidEmailIdException(String msg) {
		super(msg);
	}

}
