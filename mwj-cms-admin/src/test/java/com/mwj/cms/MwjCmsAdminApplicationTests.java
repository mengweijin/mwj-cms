package com.mwj.cms;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mwj.cms.system.entity.SysFile;
import com.mwj.cms.system.entity.User;
import com.mwj.cms.system.mapper.UserMapper;
import com.mwj.cms.system.service.FileService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MwjCmsAdminApplicationTests {

    @Resource
    private UserMapper userMapper;

    @Resource
    private FileService fileService;

    @Test
    public void testUserSelect() {
        System.out.println(("----- selectAll method test ------"));
        List<User> userList = userMapper.selectList(null);
        userList.forEach(System.out::println);
    }


    @Test
    public void testFileSelect() {
        System.out.println(("----- selectAll method test ------"));
        List<SysFile> list = fileService.list(new QueryWrapper<SysFile>().lambda().eq(SysFile::getGroupId, "1"));
        list.forEach(System.out::println);
    }

}
