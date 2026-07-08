package com.example.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.example.app.domain.UserAuthToken;

@Mapper
public interface UserAuthTokenMapper {

	// 有効期限が切れたトークン全件を取得
	@Select("SELECT * FROM user_auth_tokens WHERE expires_at < NOW()")
	List<UserAuthToken> findAllExpired();

	// 有効期限が切れたトークン全件を削除
	@Delete("DELETE FROM user_auth_tokens WHERE expires_at < NOW()")
	void deleteAllExpired();
}
