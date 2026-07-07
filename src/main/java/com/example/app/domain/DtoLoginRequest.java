package com.example.app.domain;

import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DtoLoginRequest {
	@NotBlank(message = "ユーザーIDは必須入力です。")
	private String loginId;

	@NotBlank(message = "パスワードは必須入力です。")
	private String password;

}
