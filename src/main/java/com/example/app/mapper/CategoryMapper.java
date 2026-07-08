package com.example.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.app.domain.CategoryMaster;
import com.example.app.domain.DtoCategoryResponse;

@Mapper
public interface CategoryMapper {

	// ユーザーIDに紐づくカテゴリのスロット6つを取得
	List<DtoCategoryResponse> findByUserId(Long userId);

	// 新しいカテゴリをマスタテーブルへ登録する
	int insertMaster(CategoryMaster master);

	// マスタを元にアクティブカテゴリを上書きする
	int updateActive(
			@Param("activeCatId") Long activeCatId,
			@Param("userId") Long userId,
			@Param("categoryId") Long categoryId);

	// 指定したカテゴリの有効・無効を切替
	int updateActiveStatus(
			@Param("categoryId") Long categoryId,
			@Param("isActive") Boolean isActive);

	// ユーザーIDに紐づくマスタテーブルを全件取得
	List<CategoryMaster> findAllCategoriesMaster(Long userId);

	// マスタテーブルの中から渡されたIDのカテゴリを返す
	CategoryMaster findById(Long userId, Long categoryId);

	// マスタテーブルからカテゴリを削除
	void deleteCategoryById(Long userId, Long categoryId);

	// 新規登録用：初期値を登録する
	// カテゴリマスタ
	void addInitialCategoriesMaster(Long userId);

	// アクティブカテゴリ
	void addInitialActiveCategories(Long userId, Long categoryId, Integer slotNumber);

}
