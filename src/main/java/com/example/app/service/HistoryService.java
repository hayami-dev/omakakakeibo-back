package com.example.app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.app.domain.History;
import com.example.app.domain.User;
import com.example.app.mapper.HistoryMapper;
import com.example.app.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HistoryService {

	private final HistoryMapper historyMapper;
	private final UserMapper userMapper;

	public List<History> getHistoriesByLoginId(String loginId) {
		User user = userMapper.findByLoginId(loginId);

		return historyMapper.findByUserId(user.getUserId());

	}

}
