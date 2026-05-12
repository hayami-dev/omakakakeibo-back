package com.example.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.app.domain.ActiveCategory;
import com.example.app.mapper.ActiveCategoryMapper;

@RestController
@RequestMapping("api/categories")
@CrossOrigin(origins = "http://localhost:5173")
public class CategoryController {

	@Autowired
	private ActiveCategoryMapper activeCategoryMapper;

	// アクティブを全件返す：http://localhost:8080/api/categories/active/1
	@GetMapping("/active/{userId}")
	public List<ActiveCategory> getActiveCategories(
			@PathVariable("userId") Long userId) {
		return activeCategoryMapper.findByUserId(userId);
	}

}
