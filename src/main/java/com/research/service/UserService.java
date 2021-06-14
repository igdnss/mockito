package com.research.service;

import java.util.List;

import com.research.dao.UserMapper;
import com.research.dto.Base;
import com.research.dto.User;

public class UserService{
	
	UserMapper userMapper;
	
	public User findById(int userId) {
		User user = userMapper.findById(userId);
		return user;
	}
	
	public void clear() {
		throw new RuntimeException();
	}
	
	public List<User> getGroup() {
		throw new RuntimeException();
	}
	
	public int getAge() {
		return 18;
	}
	public int getAgeByIdAndName(int id,String name) {
		return 18;
	}
	
	public String getName(Base base) {
		throw new RuntimeException();
	}

}
