package com.example.app.domain;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyBudgets {
	private Long budgetId; // SERIAL -> Long
	private Long userId; // INTEGER -> Long
	private String targetMonth; // CHAR(7) -> String
	private Integer targetAmount;
	private LocalDateTime updatedAt;
}
