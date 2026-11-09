package com.example.app.service;

import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.domain.CategoryMaster;
import com.example.app.domain.DtoRegisterConfirmRequest;
import com.example.app.domain.User;
import com.example.app.domain.UserAuthToken;
import com.example.app.exception.BusinessException;
import com.example.app.exception.ErrorCode;
import com.example.app.mapper.CategoryMapper;
import com.example.app.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final CategoryMapper categoryMapper;
	private final UserMapper userMapper;

	// 新規登録
	@Transactional(rollbackFor = Exception.class)
	public Long registerUser(DtoRegisterConfirmRequest request) {
		// 未使用かつ期限内のトークンか確認
		UserAuthToken tokenData = userMapper.findValidToken(request.getToken());
		if (tokenData == null) {
			throw new BusinessException(ErrorCode.TOKEN_INVALID);
		}

		// トークンに紐づいたメールアドレスを取得
		String loginId = tokenData.getNewLoginId();

		// 既に登録されたloginIdの場合、新規登録を止める
		if (userMapper.findByLoginId(loginId) != null) {
			throw new BusinessException(ErrorCode.LOGIN_ID_DUPLICATED);
		}

		// トークンを使用済みにする
		if (userMapper.markTokenUsed(request.getToken()) == 0) {
			throw new BusinessException(ErrorCode.TOKEN_INVALID);
		}

		// DTOからUserドメインへ詰め替える
		User user = new User();
		user.setLoginId(loginId);

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
			categoryMapper.addInitialActiveCategories(newUserId, category.getCategoryId(),
					category.getColorIndex() + 1);
		}

		return newUserId;
	}

}
