package com.example.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.app.domain.CategoryMaster;
import com.example.app.dto.CategoryResponse;
import com.example.app.mapper.CategoryMapper;

@RestController
@RequestMapping("api/categories")
@CrossOrigin(origins = "http://localhost:5173")
public class CategoryController {

	@Autowired
	private CategoryMapper categoryMapper;

	// アクティブを全件返す：http://localhost:8080/api/categories/active/1
	@GetMapping("/active/{userId}")
	public List<CategoryResponse> getActiveCategories(
			@PathVariable("userId") Long userId) {
		return categoryMapper.findByUserId(userId);
	}

	// 新しいカテゴリの登録するフロー
	@PutMapping("/update")
	@Transactional
	public void updateCategories(
			@RequestBody List<CategoryResponse> response) {
		for (CategoryResponse res : response) {
			// 現在のDBの状態を取得
			CategoryMaster currentMaster = categoryMapper.findById(res.getUserId(), res.getCategoryId());

			// 入力値のトリミング（念のためスペースのみの場合も空とみなす）
			String newName = (res.getCategoryName() == null) ? "" : res.getCategoryName().trim();
			String currentName = (currentMaster == null) ? "" : currentMaster.getCategoryName();

			// 名前が変わっていない場合何もしない
			if (newName.equals(currentName)) {
				continue;
			}

			// ここから下は名前に変更あり

			// もともと値があった場合、古いマスターをアーカイブ(false)にする
			// (値あり→値なし、値あり→別の値、どちらのケースでも共通して必要)
			if (currentMaster != null) {
				categoryMapper.updateActiveStatus(currentMaster.getCategoryId(), false);
			}

			// 新しい名前でマスターを登録（""であっても登録する）
			CategoryMaster newMaster = new CategoryMaster();
			newMaster.setUserId(res.getUserId());
			newMaster.setCategoryName(newName); // ここに "" が入る
			newMaster.setColorIndex(res.getColorIndex());
			newMaster.setIsActive(true);

			// 新しいIDを生成して登録
			categoryMapper.insertMaster(newMaster);

			// active_categories（スロット）の指し先を新しいIDに書き換える
			categoryMapper.updateActive(
					res.getActiveCatId(),
					res.getUserId(),
					newMaster.getCategoryId());
		}
	}

	// 指定されたuser_idのmaster_categoriesテーブルを全件返す
	// http://localhost:8080/api/categories/master/1
	@GetMapping("/master/{userId}")
	public List<CategoryMaster> getCategoriesMaster(
			@PathVariable("userId") Long userId) {
		return categoryMapper.findAllCategoriesMaster(userId);
	}

	// 指定されたuser_idとcategory_idから1件を返す
	// http://localhost:8080/api/categories/master/1/1
	@GetMapping("/master/{userId}/{categoryId}")
	public CategoryMaster getMasterCategoryById(
			@PathVariable("userId") Long userId,
			@PathVariable("categoryId") Long categoryId) {
		return categoryMapper.findById(userId, categoryId);
	}

	// TODO：nameが空欄かつfalseのカテゴリをMasterから削除

}
