package com.mwj.cms.sys;

import com.mwj.cms.system.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTests {

	@Resource
	private UserService userService;

	@Test
	public void test() {


	}

}
