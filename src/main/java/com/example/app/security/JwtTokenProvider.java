package com.example.app.security;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

	@Value("${jwt.secret}")
	private String secretKey;

	private final long VALIDITY_IN_MILLISECONDS = 3600000; // 1時間

	public String createToken(String loginId) {
		// 誰のものか（loginId）を記録する
		Claims claims = Jwts.claims().setSubject(loginId);
		// 「いつ発行したか」と「いつまで有効か」を書く
		Date now = new Date();
		Date validity = new Date(now.getTime() + VALIDITY_IN_MILLISECONDS);
		// 最後に「偽造できないように秘密のハンコ（署名）」を押して完成！
		return Jwts.builder()
				.setClaims(claims)
				.setIssuedAt(now)
				.setExpiration(validity)
				.signWith(getKey(), SignatureAlgorithm.HS256)
				.compact();
	}

	// tokenを検証
	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder()
					.setSigningKey(getKey())
					.build().parseClaimsJws(token);

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// トークンからIDを取り出す
	public String getLoginId(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(getKey())
				.build()
				.parseClaimsJws(token)
				.getBody()
				.getSubject();// createToken で setSubject(loginId) したやつを取り出す

	}

	// secretKeyを取得するメソッド
	private Key getKey() {
		return Keys.hmacShaKeyFor(secretKey.getBytes());
	}
}
