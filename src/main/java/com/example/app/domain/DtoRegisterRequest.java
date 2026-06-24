package com.example.app.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtoRegisterRequest {
	@NotBlank(message = "メールアドレスは必須入力です。")
	@Size(max = 255, message = "メールアドレスは255文字以内で入力してください。")
	@Email(message = "正しいメールアドレスの形式で入力してください。")
	private String email;

	private String password;
}
