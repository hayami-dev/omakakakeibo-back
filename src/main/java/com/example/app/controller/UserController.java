package com.example.app.controller;

import jakarta.validation.Valid;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.app.domain.DtoRegisterRequest;
import com.example.app.domain.User;
import com.example.app.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

	private final UserMapper userMapper;

	// ユーザー新規登録
	@PostMapping("/register")
	public ResponseEntity<String> registerUser(
			@Valid @RequestBody DtoRegisterRequest request) {

		// DTO から User ドメインへ詰め替え
		User user = new User();
		user.setEmail(request.getEmail());
		// パスワードをハッシュ化してセット

		String rawPassword = request.getPassword();
		String hashedPassword = BCrypt.hashpw(rawPassword, BCrypt.gensalt());
		user.setPasswordHash(hashedPassword);

		// DBへ保存
		userMapper.insertUser(user);
		// この時点でuserIdが発行
		System.out.println("発行されたID: " + user.getUserId());

		return ResponseEntity.ok().body("Success");
	}
}
