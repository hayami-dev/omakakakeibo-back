package com.example.app.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.app.domain.History;
import com.example.app.domain.MonthlyBudget;
import com.example.app.domain.User;
import com.example.app.mapper.BudgetMapper;
import com.example.app.mapper.HistoryMapper;
import com.example.app.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataBaseCleanTask {

	private final BudgetMapper budgetMapper;
	private final HistoryMapper historyMapper;
	private final UserMapper userMapper;

	// @Scheduled(cron = "0 0 1 1 * ?") // 本番用
	@Scheduled(cron = "*/10 * * * * ?") // テスト用（10秒おき）
	public void allCleanUp() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
		LocalDate sixMonthsAgo = LocalDate.now().minusMonths(5);
		List<User> users = userMapper.findAllUser();

		System.out.println("==================================================");
		System.out.println("====== [定期バッチ] 古いデータの削除を開始します ======");
		System.out.println("==================================================");

		// 1. 支出履歴(histories)のクレンジング
		cleanUpOldHistory(users, sixMonthsAgo);

		System.out.println("--------------------------------------------------");

		// 2. 目標金額(Monthly_budgets)のクレンジング
		cleanUpOldBudget(users, sixMonthsAgo, formatter);

		System.out.println("==================================================");
		System.out.println("====== [定期バッチ] クリーンアップ完了 ======");
		System.out.println("==================================================\n");
	}

	/**
	 * History
	 * @param users
	 * @param sixMonthsAgo
	 */
	public void cleanUpOldHistory(List<User> users, LocalDate sixMonthsAgo) {

		System.out.println("------ 1. 支出履歴 クレンジング ------");

		LocalDate latestDate = sixMonthsAgo.withDayOfMonth(1);
		int totalDeletedCount = 0;

		for (User user : users) {
			Long userId = user.getUserId();
			List<History> histories = historyMapper.findByUserId(userId);
			List<Long> historyIds = new ArrayList<>();

			for (History history : histories) {
				LocalDate targetDate = history.getHistoryDate();
				if (targetDate.isBefore(latestDate)) {
					historyIds.add(history.getHistoryId());
				}
			}

			if (!historyIds.isEmpty()) {
				System.out.println("  [検出] ユーザーID: " + userId + " の古い履歴を " + historyIds.size() + " 件検出しました。削除します...");
				for (Long historyId : historyIds) {
					historyMapper.deleteHistory(userId, historyId);
				}
				totalDeletedCount += historyIds.size();
			} else {
				System.out.println("  [スキップ] ユーザーID: " + userId + " に削除対象の古い履歴はありません。");
			}
		}

		System.out.println("------ 支出履歴 完了（合計削除件数: " + totalDeletedCount + " 件） ------");
	}

	/**
	 * Budget
	 * @param users
	 * @param sixMonthsAgo
	 * @param formatter
	 */
	public void cleanUpOldBudget(List<User> users, LocalDate sixMonthsAgo, DateTimeFormatter formatter) {

		System.out.println("------ 2. 目標金額 クレンジング ------");

		String targetMonth = sixMonthsAgo.format(formatter);

		for (User user : users) {
			Long userId = user.getUserId();

			MonthlyBudget existingBudget = budgetMapper.findByMonth(targetMonth, userId);
			if (existingBudget != null) {
				System.out.println("  [スキップ] ユーザーID: " + userId + " は既に " + targetMonth + " 月のデータを入力済みです。");
				continue;
			}

			MonthlyBudget latestPastBudget = null;
			for (int i = 0; latestPastBudget == null; i++) {
				if (i > 6) {
					break;
				}
				LocalDate prevMonth = sixMonthsAgo.minusMonths(i);
				String strPrevMonth = prevMonth.format(formatter);
				latestPastBudget = budgetMapper.findByMonth(strPrevMonth, userId);
			}

			Integer targetAmount;
			if (latestPastBudget == null) {
				targetAmount = 50000;
			} else {
				targetAmount = latestPastBudget.getTargetAmount();
			}

			budgetMapper.insertLastestBudget(targetMonth, targetAmount, userId);
			System.out.println("  [引き継ぎ] ユーザーID: " + userId + " の目標金額（" + targetAmount + "円）を " + targetMonth + " 月に挿入しました。");
		}

		System.out.println("  [削除] " + targetMonth + " より古いデータを削除します。");
		int deletedBudgetsCount = budgetMapper.deleteOldBudgets(targetMonth);

		// 🌟 なかみの完了区切り
		System.out.println("------ 目標金額 完了（合計削除件数: " + deletedBudgetsCount + " 件） ------");
	}
}