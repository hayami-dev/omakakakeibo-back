package com.example.app.security;

import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

	private final String SECRET_KEY = "super-secret-and-very-long-key-for-jwt-authentication-example"; // 本当はランダムな32文字の文字列
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
				.signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), SignatureAlgorithm.HS256)
				.compact();
	}

	// tokenを検証
	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder()
					.setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
					.build().parseClaimsJws(token);

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// トークンからIDを取り出す
	public String getLoginId(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
				.build()
				.parseClaimsJws(token)
				.getBody()
				.getSubject();// createToken で setSubject(loginId) したやつを取り出す

	}
}
