package com.example.app.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.app.domain.History;
import com.example.app.mapper.HistoryMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/histories")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class HistoryController {

	private final HistoryMapper historyMapper;

	//histories全件を取得
	// http://localhost:8080/histories/1
	@GetMapping("/{userId}")
	public ResponseEntity<List<History>> findAllHistories(
			@PathVariable("userId") Long userId) {
		List<History> histories = historyMapper.findByUserId(userId);
		return ResponseEntity.ok(histories);
	}

	// historyIdから1件を取得

	// 新規追加
	// http://localhost:8080/histories/add/1
	@PostMapping("/add/{userId}")
	public ResponseEntity<String> addHistory(
			@PathVariable("userId") Long userId,
			@RequestBody History history) {
		historyMapper.addHistory(userId, history);

		return ResponseEntity.ok("Success");
	}

	// idを元に編集
	// http://localhost:8080/histories/edit/1/1
	@PutMapping("edit/{userId}/{historyId}")
	public ResponseEntity<String> editHistory(
			@PathVariable("userId") Long userId,
			@PathVariable("historyId") Long historyId,
			@RequestBody History history) {
		historyMapper.editHistory(userId, historyId, history);
		return ResponseEntity.ok("Success");
	}

	// 削除
	// http://localhost:8080/histories/delete/1/1
	@DeleteMapping("delete/{userId}/{historyId}")
	public ResponseEntity<String> deleteHistory(
			@PathVariable("userId") Long userId,
			@PathVariable("historyId") Long historyId) {
		historyMapper.deleteHistory(userId, historyId);
		return ResponseEntity.ok("Success");
	}

}
