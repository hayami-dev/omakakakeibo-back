package com.example.app.security;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@Order(1) // 最初に動かすフィルター
@RequiredArgsConstructor
public class JwtFilter implements Filter {

	private final JwtTokenProvider jwtTokenProvider;

	@Override
	public void doFilter(
			ServletRequest request,
			ServletResponse response,
			FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		String requestURI = httpRequest.getRequestURI();

		// ログイン画面はスルー
		// ただしログイン状態の確認用API(/auth/me)は除外
		if (requestURI.equals("/api/auth/login") ||
				requestURI.equals("/api/auth/register") ||
				requestURI.equals("/api/auth/register-request") ||
				requestURI.equals("/api/auth/verify-token")) {
			chain.doFilter(request, response);
			return;
		}

		// トークンがあるかチェック
		String token = resolveToken(httpRequest);

		// 本物かチェック
		if (token != null && jwtTokenProvider.validateToken(token)) {
			String loginId = jwtTokenProvider.getLoginId(token);
			httpRequest.setAttribute("loginId", loginId); // 👈ここloginIdにそろえたほうがいい？
			chain.doFilter(request, response);
		} else {
			// 偽物ならエラーを返す
			httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

		}
	}

	// Cookieからトークンを探すメソッド
	private String resolveToken(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();

		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("SESSION_TOKEN".equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}

		return null;
	}

}
