package com.example.app.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.example.app.domain.MonthlyBudget;

@Mapper
public interface BudgetMapper {

	// 年月を指定して目標金額を取得
	@Select("SELECT * FROM monthly_budgets "
			+ "WHERE target_month = #{targetMonth} "
			+ "AND user_id = #{userId} "
			+ "ORDER BY updated_at DESC LIMIT 1")
	MonthlyBudget findByMonth(
			@Param("targetMonth") String targetMonth,
			@Param("userId") Long userId);

	// 今月の目標金額を追加
	@Insert("INSERT INTO monthly_budgets(user_id, target_month, target_amount, updated_at) "
			+ "VALUES(#{userId}, #{targetMonth}, #{targetAmount},NOW())")
	void addMonthlyBudget(
			@Param("targetMonth") String targetMonth,
			@Param("userId") Long userId,
			@Param("targetAmount") Integer targetAmount);

	// 目標金額データを物理削除
	@Delete("DELETE FROM monthly_budgets WHERE target_month < #{targetMonth}")
	int deleteOldBudgets(@Param("targetMonth") String targetMonth);

	// 目標金額削除時、最終月の目標金額を最新のものに更新する
	@Insert("INSERT INTO monthly_budgets (target_month, target_amount, user_id) "
			+ "VALUES (#{targetMonth}, #{targetAmount}, #{userId})")
	void insertLastestBudget(
			@Param("targetMonth") String targetMonth,
			@Param("targetAmount") Integer targetAmount,
			@Param("userId") Long userId);

}
