package com.example.app.domain;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transactions {
	private Long transactionId; // SERIAL -> Long
	private Long userId; // INTEGER -> Long
	private Long activeCatId; // INTEGER -> Long
	private Long archivedCatId; // INTEGER -> Long
	private Integer amount;
	private String memo;
	private Boolean isSubscription;
	private LocalDateTime createdAt;
}
