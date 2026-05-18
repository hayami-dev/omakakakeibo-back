package com.example.app.controller;

import java.util.List;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	@GetMapping("/{userId}")
	public List<History> findAllHistories(
			@PathVariable("userId") Long userId,
			Model model) {
		return historyMapper.findByUserId(userId);
	}

	// historyIdから1件を取得

}
