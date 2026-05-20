package com.example.app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.app.domain.MonthlyBudget;
import com.example.app.mapper.BudgetMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/budget")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class BudgetController {

	private final BudgetMapper budgetMapper;

	// 年月を指定して目標金額を取得
	// http://localhost:8080/budget/1/2026-03
	@GetMapping("/{userId}/{targetMonth}")
	public ResponseEntity<MonthlyBudget> getMonthlyBudget(
			@PathVariable("userId") Long userId,
			@PathVariable("targetMonth") String targetMonth) {
		MonthlyBudget mb = budgetMapper.findByMonth(targetMonth, userId);
		return ResponseEntity.ok(mb);
	}

}
