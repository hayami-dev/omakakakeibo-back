package com.example.app.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtoRegisterConfirmRequest {
	@NotBlank(message = "トークンが指定されていません。")
	private String token;

	@NotBlank(message = "パスワードは必須入力です。")
	@Size(min = 8, max = 72, message = "パスワードは8文字以上72文字以内で入力してください。")
	private String password;
}