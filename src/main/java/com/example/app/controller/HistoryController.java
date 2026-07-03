package com.example.app.controller;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

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
import com.example.app.mapper.CategoryMapper;
import com.example.app.mapper.HistoryMapper;
import com.example.app.service.HistoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/histories")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class HistoryController {

	private final HistoryService historyService;
	private final HistoryMapper historyMapper;
	private final CategoryMapper categoryMapper;

	//histories全件を取得
	// http://localhost:8080/histories/
	@GetMapping
	public ResponseEntity<List<History>> findAllHistories(
			HttpServletRequest request) {

		String loginId = (String) request.getAttribute("loginId");

		// メアドから履歴を取得するメソッドを呼ぶ

		List<History> histories = historyService.getHistoriesByLoginId(loginId);
		return ResponseEntity.ok(histories);
	}

	// 新規追加
	// http://localhost:8080/histories/add
	@PostMapping("/add")
	public ResponseEntity<String> addHistory(
			@RequestBody History history,
			HttpServletRequest request) {
		String loginId = (String) request.getAttribute("loginId");
		historyService.addHistory(history, loginId);
		return ResponseEntity.ok("Success");
	}

	// idを元に編集
	// http://localhost:8080/histories/edit/1
	@PutMapping("edit/{historyId}")
	public ResponseEntity<String> editHistory(
			@PathVariable("historyId") Long historyId,
			@RequestBody History history) {

		historyService.editHistory(history, historyId);

		return ResponseEntity.ok("Success");
	}

	// 削除
	// http://localhost:8080/histories/delete/1
	@DeleteMapping("delete/{historyId}")
	public ResponseEntity<String> deleteHistory(
			@PathVariable("historyId") Long historyId,
			HttpServletRequest request) {
		String loginId = (String) request.getAttribute("loginId");
		historyService.deleteHistory(historyId, loginId);
		return ResponseEntity.ok("Success");
	}

}
