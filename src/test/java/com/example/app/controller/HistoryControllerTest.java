package com.example.app.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.app.domain.CategoryMaster;
import com.example.app.domain.History;
import com.example.app.exception.BusinessException;
import com.example.app.exception.ErrorCode;
import com.example.app.mapper.CategoryMapper;
import com.example.app.mapper.HistoryMapper;

@ExtendWith(MockitoExtension.class)
public class HistoryControllerTest {

	@Mock
	private CategoryMapper categoryMapper;

	@Mock
	private HistoryMapper historyMapper;

	@InjectMocks
	private HistoryController historyController;

	private History validHistory;

	@BeforeEach
	void setUp() {
		validHistory = new History();
		validHistory.setUserId(1L);
		validHistory.setCategoryId(10L);
		validHistory.setAmount(1000);
		validHistory.setHistoryDate(LocalDate.now());
	}

	@Test
	void 正常なデータであれば例外が発生せず追加に成功すること() {
		when(categoryMapper.findById(1L, 10L)).thenReturn(new CategoryMaster());

		assertDoesNotThrow(() -> {
			historyController.addHistory(validHistory);
		});

		verify(historyMapper, times(1)).addHistory(validHistory);
	}

	@Test
	void 金額が大きすぎる場合はAMOUNT_TOO_LARGEエラーになること() {
		validHistory.setAmount(100000000);

		BusinessException exception = assertThrows(BusinessException.class, () -> {
			historyController.addHistory(validHistory);
		});

		assertTrue(ErrorCode.AMOUNT_TOO_LARGE.getCode().equals(exception.getCode()));
	}

	@Test
	void 金額が0円以下の場合はAMOUNT_NEGATIVEエラーになること() {
		validHistory.setAmount(0);

		BusinessException exception = assertThrows(BusinessException.class, () -> {
			historyController.addHistory(validHistory);
		});

		assertTrue(ErrorCode.AMOUNT_NEGATIVE.getCode().equals(exception.getCode()));
	}

	@Test
	void 未来の日付の場合はDATE_FUTUREエラーになること() {
		validHistory.setHistoryDate(LocalDate.now().plusDays(1));

		BusinessException exception = assertThrows(BusinessException.class, () -> {
			historyController.addHistory(validHistory);
		});

		assertTrue(ErrorCode.DATE_FUTURE.getCode().equals(exception.getCode()));
	}

	@Test
	void 制限より過去すぎる日付の場合はDATE_TOO_PASTエラーになること() {
		validHistory.setHistoryDate(LocalDate.now().minusMonths(7));

		BusinessException exception = assertThrows(BusinessException.class, () -> {
			historyController.addHistory(validHistory);
		});

		assertTrue(ErrorCode.DATE_TOO_PAST.getCode().equals(exception.getCode()));
	}

	@Test
	void 他人のカテゴリを指定した場合はCATEGORY_INVALIDエラーになること() {
		when(categoryMapper.findById(1L, 10L)).thenReturn(null);

		BusinessException exception = assertThrows(BusinessException.class, () -> {
			historyController.addHistory(validHistory);
		});

		assertTrue(ErrorCode.CATEGORY_INVALID.getCode().equals(exception.getCode()));
	}
}