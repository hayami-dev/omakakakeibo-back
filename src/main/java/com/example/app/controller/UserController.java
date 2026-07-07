package com.example.app.controller;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.app.domain.DtoLoginRequest;
import com.example.app.domain.DtoRegisterRequest;
import com.example.app.domain.DtoUserAuthToken;
import com.example.app.domain.User;
import com.example.app.domain.UserAuthToken;
import com.example.app.mapper.UserMapper;
import com.example.app.security.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class UserController {

	private final UserMapper userMapper;
	private final JwtTokenProvider jwtTokenProvider;

	// ユーザーの新規登録用のトークン発行
	@PostMapping("/register-request")
	public ResponseEntity<String> requestRegister(
			@Valid @RequestBody DtoRegisterRequest request) {
		// メアドを取得
		String loginId = request.getLoginId();

		// トークン発行
		String token = java.util.UUID.randomUUID().toString();

		// DBにトークンを保存
		userMapper.insertToken(loginId, token);

		// 開発用：コンソールに認証用のフロントURLを出力

		System.out.println("==================================================");
		System.out.println("【開発用】認証メールを送信しました。以下のURLにアクセスしてください：");
		System.out.println("http://localhost:5173/auth/register/verify?token=" + token);
		System.out.println("==================================================");

		return ResponseEntity.ok().body("Success");

	}

	// トークンを検証
	@PostMapping("/verify-token")
	public ResponseEntity<?> verifyToken(
			@Valid @RequestBody DtoUserAuthToken request) {

		// フロントから届いたトークンを取り出す
		String token = request.getToken();

		// Mapperを使って検証
		UserAuthToken validTokenData = userMapper.findValidToken(token);

		// 見つからなかった場合
		if (validTokenData == null) {
			return ResponseEntity.badRequest().body("無効なトークン、または有効期限切れです。");
		}

		// 見つかった場合Mapに詰める
		Map<String, String> response = new HashMap<>();
		response.put("loginId", validTokenData.getNewLoginId());

		return ResponseEntity.ok(response);

	}

	// ユーザー新規登録
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(
			@Valid @RequestBody DtoRegisterRequest request) {

		// DTO から User ドメインへ詰め替え
		User user = new User();
		user.setLoginId(request.getLoginId());
		// パスワードをハッシュ化してセット

		String rawPassword = request.getPassword();
		String hashedPassword = BCrypt.hashpw(rawPassword, BCrypt.gensalt());
		user.setPasswordHash(hashedPassword);

		// DBへ保存
		userMapper.insertUser(user);
		// この時点でuserIdが発行
		Long newUserId = user.getUserId();
		System.out.println("発行されたID: " + newUserId);

		return ResponseEntity.ok().body(newUserId);
	}

	// ログイン
	@PostMapping("/login")
	public ResponseEntity<?> loginUser(
			@Valid @RequestBody DtoLoginRequest request,
			HttpServletResponse response) {

		String loginId = request.getLoginId();
		String password = request.getPassword();

		User user = userMapper.findByLoginId(loginId);

		if (user == null) {
			return ResponseEntity.badRequest().body("ログインできませんでした。ユーザーID、パスワードを再度ご確認ください。");
		}

		boolean isPasswordMatch = BCrypt.checkpw(password, user.getPasswordHash());

		if (!isPasswordMatch) {
			return ResponseEntity.badRequest().body("ログインできませんでした。ユーザーID、パスワードを再度ご確認ください。");
		}

		// トークンを発行
		String token = jwtTokenProvider.createToken(user.getLoginId());

		// cookieへ登録
		ResponseCookie cookie = ResponseCookie.from("SESSION_TOKEN", token)
				.httpOnly(true)
				.secure(true)
				.sameSite("Lax")
				.path("/")
				.maxAge(3600)
				.build();

		response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

		return ResponseEntity.ok("ログイン成功");
	}

	// 現在ログイン中（トークンが有効か）を返す
	@GetMapping("/me")
	public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
		String loginId = (String) request.getAttribute("loginId");
		if (loginId == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		return ResponseEntity.ok(loginId);

	}

	// ログアウト
	@PostMapping("/logout")
	public ResponseEntity<?> logoutUser(
			HttpServletResponse response) {
		ResponseCookie cookie = ResponseCookie.from("SESSION_TOKEN", "")
				.path("/")
				.maxAge(0)
				.build();

		return ResponseEntity.ok()
				.header(HttpHeaders.SET_COOKIE, cookie.toString())
				.body("ログアウト成功");
	}

}
