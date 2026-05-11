package com.example.app.domain;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActiveCategories {
	private Long activeCatId; // SERIAL -> Long
	private Long userId; // INTEGER -> Long
	private String categoryName;
	private Long colorIndex; // INTEGER -> Long
	private LocalDateTime updatedAt;
}
