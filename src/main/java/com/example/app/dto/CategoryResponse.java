package com.example.app.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {
	private Long userId;
	private Long activeCatId;
	private Long categoryId;
	private LocalDateTime updatedAt;
	private String categoryName; // JOINで取ってきた名前
	private Integer colorIndex; // JOINで取ってきた色
}
