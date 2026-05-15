package com.example.app.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActiveCategory {
	private Long activeCatId; // SERIAL -> Long
	private Long userId; // INTEGER -> Long
	private Long categoryId;
}
