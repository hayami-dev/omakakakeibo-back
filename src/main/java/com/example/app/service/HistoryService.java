package com.example.app.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.app.domain.CategoryMaster;
import com.example.app.domain.History;
import com.example.app.domain.User;
import com.example.app.exception.BusinessException;
import com.example.app.exception.ErrorCode;
import com.example.app.mapper.CategoryMapper;
import com.example.app.mapper.HistoryMapper;
import com.example.app.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HistoryService {

	private final HistoryMapper historyMapper;
	private final UserMapper userMapper;
	private final CategoryMapper categoryMapper;

	// loginIdからhistoriesを取得
	public List<History> getHistoriesByLoginId(String loginId) {
		User user = userMapper.findByLoginId(loginId);

		return historyMapper.findByUserId(user.getUserId());
	}

	// historiesに支出登録
	public void addHistory(History history, String loginId) {
		User user = userMapper.findByLoginId(loginId);
		history.setUserId(user.getUserId());

		System.out.println(history);
		validateHistory(history);

		historyMapper.addHistory(history);
	}

	// historyの編集
	public void editHistory(History history, Long historyId) {
		History newHistory = historyMapper.findByHistoryId(historyId);
		newHistory.setAmount(history.getAmount());
		newHistory.setCategoryId(history.getCategoryId());
		newHistory.setHistoryDate(history.getHistoryDate());

		validateHistory(newHistory);

		historyMapper.editHistory(newHistory.getUserId(), newHistory.getHistoryId(), newHistory);
	}

	// historyの削除
	public void deleteHistory(Long historyId, String loginId) {
		User user = userMapper.findByLoginId(loginId);

		historyMapper.deleteHistory(user.getUserId(), historyId);
	}

	// ----------
	// 共通のバリデーションメソッド
	// ----------
	private ResponseEntity<?> validateHistory(History request) {

		// amountのチェック
		// 大きすぎる値
		if (request.getAmount() != null && request.getAmount() > 99999999) {
			throw new BusinessException(ErrorCode.AMOUNT_TOO_LARGE);
		}
		// 最低値～負の値
		if (request.getAmount() != null && request.getAmount() < 1) {
			throw new BusinessException(ErrorCode.AMOUNT_NEGATIVE);
		}

		// historyDateのチェック
		LocalDate inputDate = request.getHistoryDate();

		if (inputDate != null) {
			LocalDate today = LocalDate.now(); // 今日の年月日を取得

			// 今日より未来の日付はエラー
			if (inputDate.isAfter(today)) {
				throw new BusinessException(ErrorCode.DATE_FUTURE);
			}

			// 今日より6ヶ月間の基準日を計算する（例：今日が6/4→1/1になる）
			LocalDate sixMonthsAgo = today.minusMonths(5).withDayOfMonth(1);

			// 入力された日付が、6ヶ月よりも過去ならエラー
			if (inputDate.isBefore(sixMonthsAgo)) {
				throw new BusinessException(ErrorCode.DATE_TOO_PAST);
			}
		}

		// categoryIdのチェック
		Long userId = request.getUserId();
		Long categoryId = request.getCategoryId();

		CategoryMaster cateogory = categoryMapper.findById(userId, categoryId);
		boolean isOwnCategory = (cateogory != null);

		if (!isOwnCategory) {
			throw new BusinessException(ErrorCode.CATEGORY_INVALID);
		}

		return null;

	}

}
