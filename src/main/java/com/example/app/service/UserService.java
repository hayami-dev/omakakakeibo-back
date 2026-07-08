package com.example.app.service;

import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.app.domain.CategoryMaster;
import com.example.app.domain.DtoRegisterRequest;
import com.example.app.domain.User;
import com.example.app.mapper.CategoryMapper;
import com.example.app.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final CategoryMapper categoryMapper;
	private final UserMapper userMapper;

	// 新規登録
	public ResponseEntity<?> registerUser(DtoRegisterRequest request) {
		// DTO から User ドメインへ詰め替え
		User user = new User();
		user.setLoginId(request.getLoginId());

		// パスワードをハッシュ化してセット
		String rawPassword = request.getPassword();
		String hashedPassword = BCrypt.hashpw(rawPassword, BCrypt.gensalt());
		user.setPasswordHash(hashedPassword);

		// DBへ保存
		userMapper.insertUser(user);
		// この時点でuserIdが発行
		Long newUserId = user.getUserId();
		System.out.println("発行されたID: " + newUserId);

		// uesrIdを使ってcategoriesMaster,activeCategoriesに初期値を投入する
		categoryMapper.addInitialCategoriesMaster(newUserId);

		// 初期カテゴリマスタを取得
		List<CategoryMaster> newCategoriesMaster = categoryMapper.findAllCategoriesMaster(newUserId);

		for (CategoryMaster category : newCategoriesMaster) {
			// アクティブカテゴリを投入(colorIndex0-5、slotNumber1-6のため+1)
			categoryMapper.addInitialActiveCategories(newUserId, category.getCategoryId(), category.getColorIndex() + 1);
		}

		return ResponseEntity.ok().body(newUserId);
	}

}
