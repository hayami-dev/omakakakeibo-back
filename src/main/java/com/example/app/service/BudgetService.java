package com.example.app.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.app.domain.MonthlyBudget;
import com.example.app.domain.User;
import com.example.app.exception.BusinessException;
import com.example.app.exception.ErrorCode;
import com.example.app.mapper.BudgetMapper;
import com.example.app.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BudgetService {

	private final BudgetMapper budgetMapper;
	private final UserMapper userMapper;

	// 年月とユーザーIDから目標金額を取得
	public MonthlyBudget getMonthlyBudget(String targetMonth, String loginId) {
		User user = userMapper.findByLoginId(loginId);

		return budgetMapper.findByMonth(targetMonth, user.getUserId());
	}

	// 目標金額の追加
	public ResponseEntity<?> addMonthlyBudget(String loginId, MonthlyBudget monthlyBudget) {
		User user = userMapper.findByLoginId(loginId);
		try {
			budgetMapper.addMonthlyBudget(
					monthlyBudget.getTargetMonth(),
					user.getUserId(),
					monthlyBudget.getTargetAmount());
			return ResponseEntity.ok("Success");

		} catch (DataIntegrityViolationException e) {
			throw new BusinessException(ErrorCode.BUDGET_DUPLICATE);
		} catch (Exception e) {
			throw new BusinessException(ErrorCode.INTERNAL_SERVER);
		}
	}

}
