package com.example.app.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.app.domain.MonthlyBudget;
import com.example.app.domain.User;
import com.example.app.mapper.BudgetMapper;
import com.example.app.mapper.UserMapper;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class DataBaseCleanTask {

	private final BudgetMapper budgetMapper;
	private final UserMapper userMapper;

	@Scheduled(cron = "0 0 1 1 * ?")
	public void cleanUpOldData() {
		// yyyy-MMの形式に変えるフォーマッター
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

		System.out.println("====== [定期バッチ] 古いデータの削除を開始します ======");

		// 今日から今月含む6カ月前の日付を取得
		LocalDate sixMontyAgo = LocalDate.now().minusMonths(5);

		// Mapperに渡すtargetMonth（"yyyy-MM"文字列に変換）
		String targetMonth = sixMontyAgo.format(formatter);

		/* 6カ月前の月に目標金額の登録が無かった場合、
			前月をさかのぼって最終の登録があった月の目標金額を登録する */

		// 全ユーザーIDを取得
		List<User> users = userMapper.findAllUser();

		// 各ユーザーの過去の目標金額を取得
		for (User user : users) {
			Long userId = user.getUserId();

			// すでに登録があるかをチェック
			MonthlyBudget existingBudget = budgetMapper.findByMonth(targetMonth, userId);
			if (existingBudget != null) {
				System.out.println("ユーザーID: " + userId + " は既に " + targetMonth + " 月のデータを入力済みのためスキップします。");
				continue;
			}

			MonthlyBudget latestPastBudget = null;

			for (int i = 0; latestPastBudget == null; i++) {
				// 6ヶ月さかのぼって無かったら停止
				if (i > 6) {
					break;
				}

				// 6ヶ月前の前月を取得（i=0の場合、5カ月前の月）
				LocalDate prevMonth = sixMontyAgo.minusMonths(i);

				// 前月を変換 
				String strPrevMonth = prevMonth.format(formatter);

				latestPastBudget = budgetMapper.findByMonth(strPrevMonth, userId);
			}

			// 最終的な目標金額
			Integer targetAmount;
			if (latestPastBudget == null) {
				targetAmount = 50000;
			} else {
				targetAmount = latestPastBudget.getTargetAmount();
			}

			// 取得した最も古い目標金額を、最終月に代入
			budgetMapper.insertLastestBudget(targetMonth, targetAmount, userId);
			System.out.println("ユーザーID: " + userId + " の目標金額（" + targetAmount + "円）を " + targetMonth + " 月に引き継ぎました。");
		}

		/* 削除を実行 */

		System.out.println(targetMonth + " より古いデータを削除します。");

		// 削除クエリを実行
		int deletedBudgetsCount = budgetMapper.deleteOldBudgets(targetMonth);

		System.out.println("削除された目標金額データ件数: " + deletedBudgetsCount + " 件");
		System.out.println("====== [定期バッチ] クリーンアップ完了 ======");
	}

}
