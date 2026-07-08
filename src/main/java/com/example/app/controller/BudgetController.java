package com.example.app.controller;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.app.domain.MonthlyBudget;
import com.example.app.mapper.BudgetMapper;
import com.example.app.service.BudgetService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/budget")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class BudgetController {

	private final BudgetMapper budgetMapper;
	private final BudgetService budgetService;

	// 年月を指定して目標金額を取得
	// http://localhost:8080/api/budget/2026-03
	@GetMapping("/{targetMonth}")
	public ResponseEntity<MonthlyBudget> getMonthlyBudget(
			HttpServletRequest request,
			@PathVariable("targetMonth") String targetMonth) {
		String loginId = (String) request.getAttribute("loginId");

		MonthlyBudget mb = budgetService.getMonthlyBudget(targetMonth, loginId);
		return ResponseEntity.ok(mb);
	}

	// 今月の目標金額を追加
	// http://localhost:8080/api/budget/add
	@PostMapping("/add")
	public ResponseEntity<?> addMonthBudget(
			@RequestBody MonthlyBudget monthlyBudget,
			HttpServletRequest request) {
		String loginId = (String) request.getAttribute("loginId");

		budgetService.addMonthlyBudget(loginId, monthlyBudget);

		return ResponseEntity.ok("目標金額追加に成功");
	}

}
