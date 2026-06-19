package com.example.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.example.app.domain.User;

@Mapper
public interface UserMapper {

	// 全ユーザーを取得
	@Select("SELECT * FROM users")
	List<User> findAllUser();
}
