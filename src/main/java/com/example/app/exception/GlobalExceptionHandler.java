package com.example.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.app.domain.DtoErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * 自作のビジネス例外（400番）
	 */
	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<DtoErrorResponse> handleBusinessException(BusinessException ex) {
		DtoErrorResponse errorResponse = new DtoErrorResponse(ex.getCode(), ex.getMessage());
		return ResponseEntity.badRequest().body(errorResponse);
	}

	/**
	 * それ以外のJavaのシステムエラーや予期せぬ例外（500番）
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<DtoErrorResponse> handleGeneralException(Exception ex) {
		ex.printStackTrace(); // コンソールにエラーログを出す

		// フロントには一律でシステムエラーの形にして返す
		DtoErrorResponse errorResponse = new DtoErrorResponse("ERR_INTERNAL_SERVER", "サーバー内部で予期せぬエラーが発生しました。");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	}
}
