package com.example.app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.app.domain.CategoryMaster;
import com.example.app.domain.DtoCategoryResponse;
import com.example.app.domain.User;
import com.example.app.mapper.CategoryMapper;
import com.example.app.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

	private final CategoryMapper categoryMapper;
	private final UserMapper userMapper;

	public List<DtoCategoryResponse> getActiveCategoryByLoginId(String loginId) {
		User user = userMapper.findByLoginId(loginId);

		return categoryMapper.findByUserId(user.getUserId());
	}

	public List<CategoryMaster> getCategoryMasterByLoginId(String loginId) {
		User user = userMapper.findByLoginId(loginId);

		return categoryMapper.findAllCategoriesMaster(user.getUserId());
	}

}
