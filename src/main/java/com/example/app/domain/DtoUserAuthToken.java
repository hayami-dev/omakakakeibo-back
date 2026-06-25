package com.example.app.domain;

import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DtoUserAuthToken {
	@NotBlank(message = "トークンがありません。")
	private String token;
}
