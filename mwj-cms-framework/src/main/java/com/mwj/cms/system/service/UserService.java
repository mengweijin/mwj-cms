package com.mwj.cms.system.service;

import cn.hutool.core.util.ReflectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mwj.cms.common.constant.Const;
import com.mwj.cms.common.enums.ConfigEnum;
import com.mwj.cms.common.enums.Sex;
import com.mwj.cms.common.enums.Status;
import com.mwj.cms.common.exception.MwjCmsException;
import com.mwj.cms.common.exception.UserException;
import com.mwj.cms.framework.security.SecurityUtils;
import com.mwj.cms.framework.util.ServletUtils;
import com.mwj.cms.framework.web.entity.Result;
import com.mwj.cms.system.entity.User;
import com.mwj.cms.system.entity.UserRoleRlt;
import com.mwj.cms.system.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Meng Wei Jin
 * @description
 **/
@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    private final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
 	private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ConfigService configService;

    @Autowired
    private UserRoleRLTService userRoleRLTService;

    @Autowired
    private UserOnlineService userOnlineService;

    /**
     * 获取性别类型枚举类集合
     * @return
     */
    public Sex[] getSexes(){
        return Sex.values();
    }


    public User getByLoginName(String loginName) {
        return getOne(new QueryWrapper<>(new User().setLoginName(loginName)));
    }

    /**
     * 保存用户
     * @param user
     * @return
     */
 	@Override
    public boolean save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userMapper.insert(user) > 0;
    }

    /**
     * 重置密码
     * @param id
     * @param loginUser
     * @return
     */
    public Boolean resetPassword(Integer id, User loginUser) {
 	    String defaultPassword = configService.getValueByEnum(ConfigEnum.DEFAULT_PASSWORD);
        User user = new User()
                .setId(id)
                .setPassword(passwordEncoder.encode(defaultPassword))
                .setUpdateBy(loginUser.getId());
        return this.updateById(user);
    }

    /**
     * 修改密码
     * @param loginUser
     * @param oldPassword
     * @param newPassword
     * @return
     */
    public Result updatePassword(User loginUser, String oldPassword, String newPassword) {
        // 参数顺序不能乱，第一个参数为前台传来的明文密码，不需要加密
        boolean bool = passwordEncoder.matches(oldPassword, loginUser.getPassword());
        if(bool){
            User user = new User()
                    .setId(loginUser.getId())
                    .setPassword(passwordEncoder.encode(newPassword))
                    .setUpdateBy(loginUser.getId());
            bool = this.updateById(user);
            if(bool){
                // 强制下线用户
                userOnlineService.invalidateSession(loginUser.getLoginName());
                return Result.success("修改成功！请重新登录！");
            } else {
                return Result.error("Update user password failed!");
            }
        } else {
            return Result.error("The old password is incorrect!");
        }
    }

    /**
     * 自定义分页查询
     * @param page
     * @param user
     * @return
     */
    public IPage<Map<String, Object>> selectPageVO(IPage<Map<String, Object>> page, User user){
        page = userMapper.selectPageVO(page, user);
        if(page.getTotal() > 0){
            List<Map<String, Object>> recordList = page.getRecords();
            recordList.stream()
                    .map(map -> {
                        map.put("sex", Sex.getDesc(String.valueOf(map.get("sex"))));
                        return map;
                    })
                    .collect(Collectors.toList());
        }

        return page;
    }

    /**
     * 用户启用/停用转换
     * @param id
     * @param status
     * @param loginUser
     * @return
     */
    public Boolean switchStatus(Integer id, String status, User loginUser) {
        return this.updateById(new User().setId(id).setStatus(Status.get(status)).setUpdateBy(loginUser.getId()));
    }

    /**
     * 查询树结构数据
     * @param user
     * @return
     * @throws MwjCmsException
     */
    public List<JSONObject> querySelect(User user, Integer roleId) throws MwjCmsException {
        List<UserRoleRlt> userRoleRltList = userRoleRLTService.list(new QueryWrapper<>(new UserRoleRlt().setRoleId(roleId)));
        Set<Integer> selectedIdList = userRoleRltList.stream().map(UserRoleRlt::getUserId).collect(Collectors.toSet());
        return buildUserFormSelect(this.list(new QueryWrapper<>(user)), selectedIdList);
    }

    /**
     * 构造部门树结构数据
     * @param list
     * @param selectedIdList 默认需要选中的用户的id数组
     * @return
     * @throws MwjCmsException
     */
    public List<JSONObject> buildUserFormSelect(List<User> list, Set<Integer> selectedIdList) throws MwjCmsException {
        List<JSONObject> jsonTreeList = new ArrayList<>();

        try {
            for (User user: list){
                JSONObject newNode = new JSONObject();

                Field[] fields = user.getClass().getDeclaredFields();
                for (Field field: fields) {
                    newNode.fluentPut(field.getName(), ReflectUtil.getFieldValue(user, field));
                }

                if(selectedIdList.contains(user.getId().intValue())){
                    newNode.fluentPut("selected", "selected");
                }
                if(Status.DISABLE == user.getStatus()){
                    newNode.fluentPut("disabled", "disabled");
                }

                jsonTreeList.add(newNode);
            }
        } catch (RuntimeException e){
            logger.error(e.getMessage(), e);
            throw new MwjCmsException(e);
        }

        return jsonTreeList;
    }

    /**
     * 增加用户和角色关系表
     * @param roleIds
     * @param user
     */
    public void addUserRoleRltByRoleIds(String roleIds, User user) {
        if(StringUtils.isNotBlank(roleIds)) {
            String[] roleIdArray = roleIds.split(Const.COMMA);
            if (roleIdArray.length > 0) {
                List<UserRoleRlt> userRoleRltList = new ArrayList<>(roleIdArray.length);
                for (String roleId : roleIdArray) {
                    userRoleRltList.add(new UserRoleRlt().setRoleId(Integer.valueOf(roleId)).setUserId(user.getId()));
                }
                userRoleRLTService.saveBatch(userRoleRltList);
            }
        }
    }
}
