package com.example.app.exception;

public class BusinessException extends RuntimeException {
	private final String code;

	public BusinessException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.code = errorCode.getCode();
	}

	public String getCode() {
		return code;
	}

}
