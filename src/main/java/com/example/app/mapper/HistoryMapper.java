package com.example.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.app.domain.History;

@Mapper
public interface HistoryMapper {

	// histories全件取得
	List<History> findByUserId(@Param("userId") Long userId);

	// 1件をidから取得
	History findByHistoryId(
			@Param("userId") Long userId,
			@Param("history") Long historyId);

	// 新規追加
	void addHistory(
			@Param("userId") Long userId,
			@Param("history") History history);

	// idを元に編集
	void editHistory(
			@Param("userId") Long userId,
			@Param("historyId") Long historyId,
			@Param("history") History history);

	// 削除
	void deleteHistory(
			@Param("userId") Long userId,
			@Param("historyId") Long historyId);
}
