package com.example.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.app.domain.CategoryMaster;
import com.example.app.domain.DtoCategoryResponse;
import com.example.app.exception.BusinessException;
import com.example.app.mapper.CategoryMapper;

@RestController
@RequestMapping("api/categories")
@CrossOrigin(origins = "http://localhost:5173")
public class CategoryController {

	@Autowired
	private CategoryMapper categoryMapper;

	// アクティブを全件返す：http://localhost:8080/api/categories/active/1
	@GetMapping("/active/{userId}")
	public ResponseEntity<List<DtoCategoryResponse>> getActiveCategories(
			@PathVariable("userId") Long userId) {
		List<DtoCategoryResponse> categories = categoryMapper.findByUserId(userId);
		return ResponseEntity.ok(categories);
	}

	// 新しいカテゴリを登録するフロー
	@PutMapping("/update")
	@Transactional(rollbackFor = Exception.class) // 例外が起きたらロールバック
	public ResponseEntity<?> updateCategories(
			@RequestBody List<DtoCategoryResponse> response) {

		// 前回の変更月が同じ場合、エラーを返す
		for (DtoCategoryResponse res : response) {
			CategoryMaster currentMaster = categoryMapper.findById(res.getUserId(), res.getCategoryId());

			if (currentMaster != null && currentMaster.getUpdatedAt() != null) {
				java.time.LocalDateTime updatedAt = currentMaster.getUpdatedAt();
				java.time.LocalDateTime now = java.time.LocalDateTime.now();

				if (updatedAt.getYear() == now.getYear() && updatedAt.getMonth() == now.getMonth()) {
					throw new BusinessException("ERR_MONTHLY_LIMIT", "カテゴリの変更は月に1回までです。");
				}
			}
		}

		// 名前のあるカテゴリが2つ以下だったらエラーを返す
		int validCategoryCount = 0;

		for (DtoCategoryResponse res : response) {
			// 空欄ではないカテゴリをカウント
			String name = res.getCategoryName();
			if (name != null && !name.trim().isBlank()) {
				validCategoryCount++;
			}
		}
		// カウントが2つ以下であればエラーを返す
		if (validCategoryCount < 2) {
			throw new BusinessException("ERR_MIN_CATEGORIES", "カテゴリは最低2つセットする必要があります。");
		}

		// カテゴリ名が10文字以上だったらエラーを返す
		for (DtoCategoryResponse res : response) {
			String name = res.getCategoryName();
			if (name != null && name.length() > 10) {
				throw new BusinessException("ERR_CATEGORY_LENGTH", "カテゴリー名は10文字以内で入力してください。");
			}
		}

		// 各エラーが無かった場合
		for (DtoCategoryResponse res : response) {
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
			newMaster.setCategoryName(newName);
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
		return ResponseEntity.ok("Success");
	}

	// 指定されたuser_idのmaster_categoriesテーブルを全件返す
	// http://localhost:8080/api/categories/master/1
	@GetMapping("/master/{userId}")
	public ResponseEntity<List<CategoryMaster>> getCategoriesMaster(
			@PathVariable("userId") Long userId) {
		List<CategoryMaster> categoryMasterList = categoryMapper.findAllCategoriesMaster(userId);
		return ResponseEntity.ok(categoryMasterList);
	}

	// 指定されたuser_idとcategory_idから1件を返す
	// http://localhost:8080/api/categories/master/1/1
	@GetMapping("/master/{userId}/{categoryId}")
	public ResponseEntity<CategoryMaster> getMasterCategoryById(
			@PathVariable("userId") Long userId,
			@PathVariable("categoryId") Long categoryId) {
		CategoryMaster categoryMaster = categoryMapper.findById(userId, categoryId);
		return ResponseEntity.ok(categoryMaster);
	}

}
