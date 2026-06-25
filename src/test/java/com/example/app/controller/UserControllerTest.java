package com.example.app.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.app.domain.DtoRegisterRequest;
import com.example.app.domain.User;
import com.example.app.exception.GlobalExceptionHandler;
import com.example.app.mapper.UserMapper;

import tools.jackson.databind.ObjectMapper;

@WebMvcTest({ UserController.class, GlobalExceptionHandler.class }) // UserControllerのみを標的にして高速にテストする設定
class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper; // JavaオブジェクトをJSON文字列に変換する道具

	@MockitoBean
	private UserMapper userMapper; // DB通信部分はモック（偽物）にして差し替える

	@Test
	@DisplayName("正常系：正しい情報であれば登録に成功し、200 OKとSuccessが返ること")
	void registerUser_Success() throws Exception {
		// 1. テスト用のリクエストデータ（DTO）を用意
		DtoRegisterRequest request = new DtoRegisterRequest();
		request.setLoginId("test@example.com");
		request.setPassword("password123");

		// 2. MockMvcを使って、実際にフロントからリクエストが飛んできた状態をシミュレート
		mockMvc.perform(post("/api/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))) // DTOをJSONに変換してボディに入れる
				.andExpect(status().isOk()) // ステータスが 200 OK であること
				.andExpect(content().string("Success")); // レスポンスの文字が "Success" であること

		// 3. 検証：userMapper.insertUser が「確実に1回」呼び出されたかをチェック
		// 引数には何かしらのUserオブジェクト(any)が渡されているはず
		verify(userMapper, times(1)).insertUser(any(User.class));
	}

	@Test
	@DisplayName("異常系：メールアドレスの形式が不正な場合、400 Bad Requestで弾かれること")
	void registerUser_BadRequest_InvalidEmail() throws Exception {
		// 1. メアドの形式が明らかに不正なDTOを用意
		DtoRegisterRequest request = new DtoRegisterRequest();
		request.setLoginId("invalid-email-format"); // @がない
		request.setPassword("password123");

		// 2. リクエストを送信してテスト
		mockMvc.perform(post("/api/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest()); // 🌟 400 Bad Request が返ることを期待！

		// 3. 検証：バリデーションで弾かれているため、DB保存(Mapper)は一度も実行されていないこと
		verify(userMapper, times(0)).insertUser(any(User.class));
	}

	@Test
	@DisplayName("異常系：メールアドレスが空っぽの場合、400 Bad Requestで弾かれること")
	void registerUser_BadRequest_BlankEmail() throws Exception {
		// 1. メアドが空っぽのDTOを用意
		DtoRegisterRequest request = new DtoRegisterRequest();
		request.setLoginId(""); // 空文字
		request.setPassword("password123");

		// 2. リクエストを送信してテスト
		mockMvc.perform(post("/api/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest()); // 🌟 400 Bad Request が返ることを期待！

		// 3. 検証：こちらもDB保存は実行されていないこと
		verify(userMapper, times(0)).insertUser(any(User.class));
	}
}