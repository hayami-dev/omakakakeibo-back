package com.example.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.example.app.domain.ActiveCategory;

@Mapper
public interface ActiveCategoryMapper {

	// ユーザーIDに紐づくカテゴリのスロット6つを取得
	@Select("SELECT * FROM active_categories "
			+ "WHERE user_id = #{userId} ORDER BY active_cat_id")
	List<ActiveCategory> findByUserId(Long userId);

	// 渡されたIDのカテゴリを返す
	@Select("SELECT * FROM active_categories "
			+ "WHERE active_cat_id = #{activeCatId}")
	ActiveCategory findById(Long activeCatId);

	// カテゴリ名、色のインデックスを変更、更新する

}
