package com.example.app.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.app.domain.CategoryMaster;
import com.example.app.domain.DtoErrorResponse;
import com.example.app.domain.History;
import com.example.app.mapper.CategoryMapper;
import com.example.app.mapper.HistoryMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/histories")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class HistoryController {

	private final HistoryMapper historyMapper;
	private final CategoryMapper categoryMapper;

	//histories全件を取得
	// http://localhost:8080/histories/1
	@GetMapping("/{userId}")
	public ResponseEntity<List<History>> findAllHistories(
			@PathVariable("userId") Long userId) {
		List<History> histories = historyMapper.findByUserId(userId);
		return ResponseEntity.ok(histories);
	}

	// 新規追加
	// http://localhost:8080/histories/add
	@PostMapping("/add")
	public ResponseEntity<String> addHistory(
			@RequestBody History history) {
		ResponseEntity<?> errorResponse = validateHistory(history);

		if (errorResponse != null) {
			return (ResponseEntity<String>) errorResponse;
		}

		historyMapper.addHistory(history);
		return ResponseEntity.ok("Success");
	}

	// idを元に編集
	// http://localhost:8080/histories/edit/1/1
	@PutMapping("edit/{userId}/{historyId}")
	public ResponseEntity<String> editHistory(
			@PathVariable("userId") Long userId,
			@PathVariable("historyId") Long historyId,
			@RequestBody History history) {
		ResponseEntity<?> errorResponse = validateHistory(history);

		if (errorResponse != null) {
			return (ResponseEntity<String>) errorResponse;
		}

		historyMapper.editHistory(userId, historyId, history);
		return ResponseEntity.ok("Success");
	}

	// 削除
	// http://localhost:8080/histories/delete/1/1
	@DeleteMapping("delete/{userId}/{historyId}")
	public ResponseEntity<String> deleteHistory(
			@PathVariable("userId") Long userId,
			@PathVariable("historyId") Long historyId) {
		historyMapper.deleteHistory(userId, historyId);
		return ResponseEntity.ok("Success");
	}

	// ----------
	// 共通のバリデーションメソッド
	// ----------
	private ResponseEntity<?> validateHistory(History request) {
		// amountのチェック
		// 大きすぎる値
		if (request.getAmount() != null && request.getAmount() > 99999999) {
			return ResponseEntity.badRequest()
					.body(new DtoErrorResponse("ERR_AMOUNT_TOO_LARGE", "金額が大きすぎます。9,999万9,999円以内で入力してください。"));
		}
		// 最低値～負の値
		if (request.getAmount() != null && request.getAmount() < 1) {
			return ResponseEntity.badRequest()
					.body(new DtoErrorResponse("ERR_AMOUNT_NEGATIVE", "1円より小さな値は入力できません。"));
		}

		// historyDateのチェック
		LocalDate inputDate = request.getHistoryDate();

		if (inputDate != null) {
			LocalDate today = LocalDate.now(); // 今日の年月日を取得

			// 今日より未来の日付はエラー
			if (inputDate.isAfter(today)) {
				return ResponseEntity.badRequest()
						.body(new DtoErrorResponse("ERR_DATE_FUTURE", "未来の日付は登録できません。"));
			}

			// 今日より6ヶ月間の基準日を計算する（例：今日が6/4→1/1になる）
			LocalDate sixMonthsAgo = today.minusMonths(5).withDayOfMonth(1);

			// 入力された日付が、6ヶ月よりも過去ならエラー
			if (inputDate.isBefore(sixMonthsAgo)) {
				return ResponseEntity.badRequest()
						.body(new DtoErrorResponse("ERR_DATE_TOO_PAST", "6ヶ月以上前の日付は登録できません。"));
			}
		}

		// categoryIdのチェック
		Long userId = request.getUserId();
		Long categoryId = request.getCategoryId();

		CategoryMaster cateogory = categoryMapper.findById(userId, categoryId);
		boolean isOwnCategory = (cateogory != null);

		if (!isOwnCategory) {
			return ResponseEntity.badRequest()
					.body(new DtoErrorResponse("ERR_CATEGORY_INVALID", "指定されたカテゴリは利用できません。"));
		}

		return null;

	}

}
