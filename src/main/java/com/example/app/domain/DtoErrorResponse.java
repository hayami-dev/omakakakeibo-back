package com.example.app.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DtoErrorResponse {

	private String code;
	private String message;
}
