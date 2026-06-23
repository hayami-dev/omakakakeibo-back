package com.example.app.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.app.domain.CategoryMaster;
import com.example.app.domain.DtoCategoryResponse;
import com.example.app.exception.BusinessException;
import com.example.app.exception.ErrorCode;
import com.example.app.mapper.CategoryMapper;

@ExtendWith(MockitoExtension.class)
public class CategoryControllerTest {

	@Mock
	private CategoryMapper categoryMapper;

	@InjectMocks
	private CategoryController categoryController;

	@Test
	void 同一月内にすでにカテゴリを変更している場合はMONTHLY_LIMITエラーになること() {
		List<DtoCategoryResponse> list = new ArrayList<>();
		DtoCategoryResponse res = new DtoCategoryResponse();
		res.setUserId(1L);
		res.setCategoryId(10L);
		res.setCategoryName("新しいカテゴリ");
		list.add(res);

		CategoryMaster currentMaster = new CategoryMaster();
		currentMaster.setUpdatedAt(LocalDateTime.now());

		when(categoryMapper.findById(1L, 10L)).thenReturn(currentMaster);

		BusinessException exception = assertThrows(BusinessException.class, () -> {
			categoryController.updateCategories(list);
		});

		assertTrue(ErrorCode.MONTHLY_LIMIT.getCode().equals(exception.getCode()));
	}

	@Test
	void 有効なカテゴリ名が2つ未満の場合はMIN_CATEGORIESエラーになること() {
		List<DtoCategoryResponse> list = new ArrayList<>();

		DtoCategoryResponse cat1 = new DtoCategoryResponse();
		cat1.setCategoryName("食費");
		list.add(cat1);

		DtoCategoryResponse cat2 = new DtoCategoryResponse();
		cat2.setCategoryName("");
		list.add(cat2);

		BusinessException exception = assertThrows(BusinessException.class, () -> {
			categoryController.updateCategories(list);
		});

		assertTrue(ErrorCode.MIN_CATEGORIES.getCode().equals(exception.getCode()));
	}

	@Test
	void カテゴリ名が10文字を超える場合はCATEGORY_LENGTHエラーになること() {
		List<DtoCategoryResponse> list = new ArrayList<>();

		DtoCategoryResponse cat1 = new DtoCategoryResponse();
		cat1.setCategoryName("あいうえおかきくけこさ");
		list.add(cat1);

		DtoCategoryResponse cat2 = new DtoCategoryResponse();
		cat2.setCategoryName("有効な名前");
		list.add(cat2);

		BusinessException exception = assertThrows(BusinessException.class, () -> {
			categoryController.updateCategories(list);
		});

		assertTrue(ErrorCode.CATEGORY_LENGTH.getCode().equals(exception.getCode()));
	}
}