package com.example.app.service;

import org.springframework.stereotype.Service;

import com.example.app.domain.MonthlyBudget;
import com.example.app.domain.User;
import com.example.app.mapper.BudgetMapper;
import com.example.app.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BudgetService {

	private final BudgetMapper budgetMapper;
	private final UserMapper userMapper;

	public MonthlyBudget getMonthlyBudget(String targetMonth, String loginId) {
		User user = userMapper.findByLoginId(loginId);

		return budgetMapper.findByMonth(targetMonth, user.getUserId());
	}

}
