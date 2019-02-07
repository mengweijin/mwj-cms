package com.mwj.cms.util;

import com.alibaba.fastjson.JSONObject;
import com.mwj.cms.common.util.JSONUtils;
import com.mwj.cms.common.util.TreeUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Meng Wei Jin
 * @description
 **/
public class TreeUtilsTest {

    @Test
    public void testFilterBean() {
        try {
            List<TestUser> userList = new ArrayList<>();
            userList.add(new TestUser(1, 0, "一"));
            userList.add(new TestUser(2, 0, "二"));
            userList.add(new TestUser(3, 1, "三"));
            userList.add(new TestUser(4, 1, "四"));
            userList.add(new TestUser(5, 3, "五"));

            List<TestUser> testUserList = TreeUtils.filterBeanByPid(userList, "1");

            System.out.println(testUserList);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void test() {
        try {
            List<TestUser> userList = new ArrayList<>();
            userList.add(new TestUser(1, 0, "一"));
            userList.add(new TestUser(2, 0, "二"));
            userList.add(new TestUser(3, 1, "三"));
            userList.add(new TestUser(4, 1, "四"));
            userList.add(new TestUser(5, 3, "五"));

            List<JSONObject> jsonTreeList = TreeUtils.buildJsonBeanTreeByParentId(userList, String.valueOf(1));

            // 格式化输出JSON
            String jsonString = JSONUtils.format(jsonTreeList);

            System.out.println(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Data
    @AllArgsConstructor
    class TestUser {

        private Integer id;
        private Integer pid;
        private String name;
    }
}
