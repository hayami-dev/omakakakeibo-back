package com.example.app.exception;

public enum ErrorCode {
	// ここにエラーコードとメッセージを一括管理
	MONTHLY_LIMIT("ERR_MONTHLY_LIMIT", "カテゴリの変更は月に1回までです。"), MIN_CATEGORIES("ERR_MIN_CATEGORIES",
			"カテゴリは最低2つセットする必要があります。"), CATEGORY_LENGTH("ERR_CATEGORY_LENGTH", "カテゴリー名は10文字以内で入力してください。"), AMOUNT_TOO_LARGE(
					"ERR_AMOUNT_TOO_LARGE", "金額が大きすぎます。9,999万9,999円以内で入力してください。"), AMOUNT_NEGATIVE("ERR_AMOUNT_NEGATIVE",
							"1円より小さな値は入力できません。"), DATE_FUTURE("ERR_DATE_FUTURE", "未来の日付は登録できません。"), DATE_TOO_PAST("ERR_DATE_TOO_PAST",
									"6ヶ月以上前の日付は登録できません。"), CATEGORY_INVALID("ERR_CATEGORY_INVALID",
											"指定されたカテゴリは利用できません。"), BUDGET_DUPLICATE("ERR_BUDGET_DUPLICATE",
													"この月の目標金額は既に登録されています"), INTERNAL_SERVER("ERR_INTERNAL_SERVER", "サーバー内部で予期せぬエラーが発生しました。");

	private final String code;
	private final String message;

	// コンストラクタ
	ErrorCode(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}