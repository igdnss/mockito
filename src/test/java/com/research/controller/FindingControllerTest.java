package com.research.controller;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.either;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.research.dto.Base;
import com.research.dto.Client;
import com.research.dto.User;
import com.research.service.UserService;

@RunWith(MockitoJUnitRunner.class)
public class FindingControllerTest {

	@Mock(answer = Answers.RETURNS_DEEP_STUBS) // 深度mock
	// 通过此标签可以直接mock出对象，也可以通过@Before 在某个方法中mock
	private UserService userService;

	@Mock // 这里可以通过深度mock
	User user;

	@Mock
	List<User> list;

	// 这里的对象在@Before中mock
	private FindingController findingController;

	private String successResult;

	@Before
	public void setUp() {
		// 一般mock对象直接通过@Mock的方法，Before标签中通常做一些其他初始化的操作。
		findingController = mock(FindingController.class);// 这里使用的是mock方法
//		MockitoAnnotations.initMocks(this); //可以让mock的对象属性不为空
		successResult = "Hello";
	}

	/**
	 * 成功获取id为1的用户
	 */
	@Test
	public void testGetUserSuccessWith1() {
		when(findingController.getUser(1)).thenReturn(successResult);
		String result = findingController.getUser(1);
		assertEquals(successResult, result);
	}

	/**
	 * 使用doReturn成功获取id为1的用户
	 */
	@Test
	public void testGetUserSuccessWith1DoReturn() {
		doReturn(successResult).when(findingController).getUser(1);
		String result = findingController.getUser(1);
		assertEquals(successResult, result);
	}

	/**
	 * 成功获取任意id的用户
	 */
	@Test
	public void testGetUserSuccessWithAnyInt() {
		when(findingController.getUser(anyInt())).thenReturn(successResult);
		String result = findingController.getUser(1);
		assertEquals(successResult, result);
	}

	/**
	 * 方法一：抛出异常
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testGetUserException1WithAnyInt() {
		// 这种方法已经被deprecated了，可以用方法二
		when(findingController.getUser(anyInt())).thenThrow(UnsupportedOperationException.class);
		assertThrows(UnsupportedOperationException.class, () -> {
			findingController.getUser(1);
		});

	}

	/**
	 * 方法二：抛出异常
	 */
	@Test
	public void testGetUserExceptionWithAnyInt() {
		doThrow(UnsupportedOperationException.class).when(findingController).getUser(anyInt());
		assertThrows(UnsupportedOperationException.class, () -> {
			findingController.getUser(1);
		});

	}

	/**
	 * userService和user对象都是mock出来的，这里可以通过deepMock省略一个对象
	 */
	@Test
	public void testGetUserSuccessWithoutDeep() {
		when(userService.findById(1)).thenReturn(user);
		User user = userService.findById(1);
		when(user.getName()).thenReturn(successResult);
		String result = user.getName();
		assertEquals(successResult, result);
	}

	/**
	 * deepMock
	 */
	@Test
	public void testGetUserSuccessWithDeep() {
		when(userService.findById(1).getName()).thenReturn(successResult);
		String userName = userService.findById(1).getName();
		assertEquals(successResult, userName);
	}

	/**
	 * 测试返回值为空的方法
	 */
	@Test
	public void testClear() {
		doNothing().when(findingController).clear();
		findingController.clear();
		// 验证次方法是否被执行过一次
		verify(findingController, times(1)).clear();
	}

	/**
	 * 测试迭代调用的方法，语法一
	 */
	@Test
	public void testGetGroup1() {
		when(findingController.getGroup()).thenReturn(list);
		when(list.size()).thenReturn(1, 2, 3, 4);
		assertEquals(1, list.size());
		assertEquals(2, list.size());
		assertEquals(3, list.size());
		assertEquals(4, list.size());
	}

	/**
	 * 测试迭代调用的方法，语法二
	 */
	@Test
	public void testGetGroup2() {
		when(findingController.getGroup()).thenReturn(list);
		when(list.size()).thenReturn(1).thenReturn(2).thenReturn(3).thenReturn(4);
		assertEquals(1, list.size());
		assertEquals(2, list.size());
		assertEquals(3, list.size());
		assertEquals(4, list.size());
	}

	/**
	 * 使用doAnswer测试一定的逻辑. 返回下标值的5倍
	 */
	@Test
	public void testDoAnswer() {
		when(findingController.getGroup()).thenReturn(list);
		when(list.get(anyInt())).thenAnswer(answer -> {
			Integer index = answer.getArgumentAt(0, Integer.class);
			return String.valueOf(index * 5);
		});
		assertEquals("0", list.get(0));
		assertEquals("50", list.get(10));

	}

	/**
	 * 测试调用真正的方法
	 */
	@Test
	public void testGetAge() {
		when(userService.getAge()).thenCallRealMethod();
		assertEquals(18, userService.getAge());
	}

	/**
	 * 测试部分mock
	 */
	public void testSpy() {
		List<String> userNameList = new ArrayList<String>();
		List<String> spyList = spy(userNameList);
		spyList.add("A");
		spyList.add("B");
		spyList.add("C");
		assertEquals("A", spyList.get(0));
		assertEquals("B", spyList.get(1));
		assertEquals("C", spyList.get(2));
		assertEquals(false, spyList.isEmpty());
		when(spyList.isEmpty()).thenReturn(true);
		when(spyList.size()).thenReturn(100);
		assertEquals(true, spyList.isEmpty());
		assertEquals(100, spyList.size());
	}

	/**
	 * 类型判断测试isA
	 */
	@Test
	public void testGetNameWithIsA() {
		when(userService.getName(isA(Base.class))).thenReturn(successResult);
		String name = userService.getName(new User());
		assertEquals(successResult, name);
	}

	/**
	 * 类型判断测试any
	 */
	@Test
	public void testGetNameWithAny() {
		when(userService.getName(any(User.class))).thenReturn(successResult);
		String name = userService.getName(new Client());
		assertEquals(successResult, name);
	}

	/**
	 * 通配符测试
	 */
	@Test
	public void testGetNameByIdAndName() {
		when(userService.getAgeByIdAndName(anyInt(), anyString())).thenReturn(100);
		int result1 = userService.getAgeByIdAndName(1, "A");
		assertEquals(100, result1);
		// 如果想传入指定的值，下面这种方法不支持. Mockito 中如果多个参数，一处使用通配符处处使用通配符。
		/*
		 * when(userService.getAgeByIdAndName(anyInt(), "B")).thenReturn(200); int
		 * result2 = userService.getAgeByIdAndName(1, "B"); assertEquals(200, result2);
		 */
		// 传入指定的值必须使用eq()

		when(userService.getAgeByIdAndName(anyInt(), eq("B"))).thenReturn(200);
		int result2 = userService.getAgeByIdAndName(1, "B");
		assertEquals(200, result2);

	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testHamcrest() {
		int i = 20;
		double price = 13.2;
		assertThat(i, equalTo(20));
		assertThat(i, not(equalTo(210)));
		assertThat(i, is(20));
		assertThat(i, is(not(100)));
		assertThat(price,either(equalTo(13.2)).or(equalTo(10)));
		//匹配任何一个
		assertThat(price,anyOf(is(5),is(13.2),not(18)));
		
	}
}
