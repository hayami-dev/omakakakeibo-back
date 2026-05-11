package com.example.app.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryColors {
	private Long colorIndex; // SERIAL -> Long
	private String label;
	private String code;
	private String bgCode;
	private String disabledCode;

}
