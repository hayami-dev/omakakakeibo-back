package com.example.app.domain;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthToken {
	private Long tokenId; // SERIAL -> Long
	private Long userId; // INTEGER -> Long
	private String tokenType;
	private String tokenHash;
	private String newLoginId;
	private LocalDateTime expiresAt;
	private LocalDateTime usedAt;
}
