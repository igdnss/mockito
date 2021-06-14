package com.research.controller;

import java.util.List;

import com.research.dto.User;
import com.research.service.UserService;

/**
 *随便写的一个Controller,方便后面介绍UT而存在的
 */
public class FindingController {
	
	UserService userService;
	
	//这里为了方便直接使用构造方法，真实项目中基本都是使用依赖注入的方式
	public FindingController(UserService userService) {
		this.userService = userService;
	}


	public String getUser(int id) {
		User user = null;
		try {
			user = userService.findById(id);
		} catch (UnsupportedOperationException e) {
			return "Error";
		}
		if(user == null) {
			return "No one";
		}else {
			return "Hello";
		}
		
	}
	
	public void clear() {
		userService.clear();
	}
	
	public List<User> getGroup() {
		return userService.getGroup();
	}
	
	public int getAge() {
		return userService.getAge();
	}

}
