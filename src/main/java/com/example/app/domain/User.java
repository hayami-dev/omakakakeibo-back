package com.example.app.domain;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
	private Long userId; // SERIAL -> Long
	private String LoginId;
	private String passwordHash;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
