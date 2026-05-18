package com.example.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.app.domain.History;

@Mapper
public interface HistoryMapper {

	// histories全件取得
	List<History> findByUserId(Long userId);

	// 1件をidから取得
	History findByHistoryId(Long userId, Long historyId);
}
