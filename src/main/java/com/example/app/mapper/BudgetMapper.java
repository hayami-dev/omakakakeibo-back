package com.example.app.mapper;

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

	// TODO:各月1つのみ追加可能なようカウント

	// 今月の目標金額を追加
	@Insert("INSERT INTO monthly_budgets(user_id, target_month, target_amount, updated_at) "
			+ "VALUES(#{userId}, #{targetMonth}, #{targetAmount},NOW())")
	void addMonthlyBudget(
			@Param("targetMonth") String targetMonth,
			@Param("userId") Long userId,
			@Param("targetAmount") Integer targetAmount);
}
