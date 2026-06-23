package com.example.app.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import com.example.app.domain.MonthlyBudget;
import com.example.app.exception.BusinessException;
import com.example.app.exception.ErrorCode;
import com.example.app.mapper.BudgetMapper;

@ExtendWith(MockitoExtension.class)
public class BudgetControllerTest {

	@Mock
	private BudgetMapper budgetMapper;

	@InjectMocks
	private BudgetController budgetController;

	@Test
	void 正常なデータであれば目標金額の追加に成功すること() {
		MonthlyBudget budget = new MonthlyBudget();
		budget.setTargetMonth("2026-06");
		budget.setUserId(1L);
		budget.setTargetAmount(50000);

		assertDoesNotThrow(() -> {
			budgetController.addMonthBudget(budget);
		});

		verify(budgetMapper, times(1)).addMonthlyBudget("2026-06", 1L, 50000);
	}

	@Test
	void すでに同じ年月のデータが存在する場合はBUDGET_DUPLICATEエラーになること() {
		MonthlyBudget budget = new MonthlyBudget();
		budget.setTargetMonth("2026-06");
		budget.setUserId(1L);
		budget.setTargetAmount(50000);

		doThrow(new DataIntegrityViolationException("Duplicate entry"))
				.when(budgetMapper).addMonthlyBudget("2026-06", 1L, 50000);

		BusinessException exception = assertThrows(BusinessException.class, () -> {
			budgetController.addMonthBudget(budget);
		});

		assertTrue(ErrorCode.BUDGET_DUPLICATE.getCode().equals(exception.getCode()));
	}
}