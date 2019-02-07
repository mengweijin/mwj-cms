package com.mwj.cms.sys;

import com.mwj.cms.common.util.http.JsoupUtils;
import com.mwj.cms.framework.web.service.PublicIPService;
import com.mwj.cms.system.service.InterfaceService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InterfaceTests {

	@Resource
	private InterfaceService interfaceService;

	@Resource
	private PublicIPService publicIPService;

	@Test
	public void test() {
		String keyCode = "ipInfoByTaoBao";
		String paramsJson = "{\"ip\":\"60.205.177.13\"}";
		try {
			String str = interfaceService.execute(keyCode, paramsJson);
			System.out.println(str);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testPublicIp() {
		publicIPService.getPublicIp();
	}
}
