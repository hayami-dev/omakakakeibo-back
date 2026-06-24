package com.example.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.example.app.domain.User;
import com.example.app.domain.UserAuthToken;

@Mapper
public interface UserMapper {

	// クリーンアップ用：全ユーザーを取得
	@Select("SELECT * FROM users")
	List<User> findAllUser();

	// userIdからユーザー情報を1件取得
	@Select("SELECT * FROM users "
			+ "WHERE user_id = #{userId}")
	User findById(
			@Param("userId") Long userId);

	// ログイン用：メールアドレスからユーザー情報を1件取得
	@Select("SELECT * FROM users "
			+ "WHERE email = #{email}")
	User findByEmail(
			@Param("email") String email);

	// 新規登録用：ユーザーを追加
	@Insert("INSERT INTO users (email, password_hash) "
			+ "VALUES(#{email}, #{passwordHash})")
	@Options(useGeneratedKeys = true, keyProperty = "userId")
	void insertUser(
			User user);

	// 新規登録用トークンを発行、保存する
	// token_typeは 'email_verify'、new_emailカラムに
	// 入力されたメアドを一時保存
	@Insert("INSERT INTO user_auth_tokens "
			+ "(token_type, token_hash, new_email, expires_at) "
			+ "VALUES('email_verify', #{token}, #{email}, "
			+ "DATE_ADD(NOW(), INTERVAL 30 MINUTE))")
	void insertToken(@Param("email") String email, @Param("token") String token);

	// トークンをDBから探す
	@Select("SELECT * FROM user_auth_tokens "
			+ "WHERE token_hash = #{token} "
			+ "AND token_type = 'email_verify' "
			+ "AND expires_at > NOW()")
	UserAuthToken findValidToken(@Param("token") String token);
}
