package com.example.app.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException; // 🌟追加
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
	 * @Validによるバリデーションエラー（400番）
	 * 入力チェックエラーが400番でフロントに返る
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();

		// どのフィールド（emailなど）が、どんなメッセージでエラーになったかを収集
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});

		// ステータスコード 400 (Bad Request) でエラー内容を返却
		return ResponseEntity.badRequest().body(errors);
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