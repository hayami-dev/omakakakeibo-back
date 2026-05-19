package com.example.app.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class History {
	private Long historyId; // SERIAL -> Long
	private Long userId; // INTEGER -> Long
	private Long categoryId; // INTEGER -> Long
	private Integer amount; // amount INTEGER
	private LocalDate historyDate; // history_date DATE (時分秒なし)
	private String note;
	private Boolean isSubscription;
	private LocalDateTime createdAt;
}
