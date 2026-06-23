package com.example.app.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.app.domain.MonthlyBudget;
import com.example.app.exception.BusinessException;
import com.example.app.exception.ErrorCode;
import com.example.app.mapper.BudgetMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/budget")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class BudgetController {

	private final BudgetMapper budgetMapper;

	// 年月を指定して目標金額を取得
	// http://localhost:8080/api/budget/1/2026-03
	@GetMapping("/{userId}/{targetMonth}")
	public ResponseEntity<MonthlyBudget> getMonthlyBudget(
			@PathVariable("userId") Long userId,
			@PathVariable("targetMonth") String targetMonth) {
		MonthlyBudget mb = budgetMapper.findByMonth(targetMonth, userId);
		return ResponseEntity.ok(mb);
	}

	// 今月の目標金額を追加
	// http://localhost:8080/api/budget/add/1
	@PostMapping("/add")
	public ResponseEntity<?> addMonthBudget(
			@RequestBody MonthlyBudget monthlyBudget) {
		try {
			budgetMapper.addMonthlyBudget(
					monthlyBudget.getTargetMonth(),
					monthlyBudget.getUserId(),
					monthlyBudget.getTargetAmount());
			return ResponseEntity.ok("Success");

		} catch (DataIntegrityViolationException e) {
			throw new BusinessException(ErrorCode.BUDGET_DUPLICATE);
		} catch (Exception e) {
			throw new BusinessException(ErrorCode.INTERNAL_SERVER);
		}
	}

}
