package com.example.app.domain;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryMaster {
	private Long categoryId; // SERIAL -> Long
	private Long userId; // INTEGER -> Long
	private String categoryName;
	private Integer colorIndex; // INTEGER -> Long
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private Boolean isActive;
}
