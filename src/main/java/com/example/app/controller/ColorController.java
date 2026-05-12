package com.example.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.app.domain.CategoryColor;
import com.example.app.mapper.CategoryColorMapper;

@RestController
@RequestMapping("/api/colors")
public class ColorController {

	@Autowired
	private CategoryColorMapper colorMapper;

	// 全色取得：http://localhost:8080/api/colors
	@GetMapping
	public List<CategoryColor> getAllColors() {
		return colorMapper.findAll();
	}

	// 特定の色を取得：http://localhost:8080/api/colors/0
	@GetMapping("/{index}")
	public CategoryColor getColorByIndex(
			@PathVariable("index") Long index) {
		return colorMapper.findByIndex(index);
	}

}
