package com.mwj.cms.system.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mwj.cms.common.enums.MenuType;
import com.mwj.cms.common.enums.Status;
import com.mwj.cms.common.util.TreeUtils;
import com.mwj.cms.system.entity.Menu;
import com.mwj.cms.system.entity.User;
import com.mwj.cms.system.mapper.MenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Meng Wei Jin
 */
@Service
public class MenuService extends ServiceImpl<MenuMapper, Menu> {

    @Autowired
    private RoleMenuRLTService roleMenuRLTService;

    /**
     * 获取菜单类型枚举类集合
     * @return
     */
    public MenuType[] getMenuTypes(){
        return MenuType.values();
    }

    public MenuType getMenuType(String value){
        return MenuType.get(value);
    }

    /**
     * 启用/停用菜单，递归启用/停用子孙菜单
     * @param id
     * @param status
     * @param loginUser
     */
    public boolean switchStatus(Integer id, String status, User loginUser) {
        // 更新当前菜单的状态
        return this.updateById(new Menu().setId(id).setStatus(Status.get(status)).setUpdateBy(loginUser.getId()));
    }

    public List<Map<String, Object>> selectAuthorizationTreeGrid(Integer roleId) {
        List<Integer> menuIdList = roleMenuRLTService.selectMenuByRoleId(roleId);

        List<Map<String, Object>> menuList = this.listMaps(new QueryWrapper<>(new Menu().setStatus(Status.NORMAL)));
        menuList = menuList.stream().map(menuMap -> {
            if(menuIdList.contains(Integer.valueOf(String.valueOf(menuMap.get("id"))).intValue())){
                // treeGrid的checkbox默认选中
                menuMap.put("LAY_CHECKED", true);
            }
            return menuMap;
        }).collect(Collectors.toList());

        return menuList;
    }

    /**
     * 水平导航栏菜单
     * @return
     */
    public List<JSONObject> getHeaderMenu(){
        List<Menu> menuList = this.lambdaQuery().eq(Menu::getParentId, -2).list();

        // 水平导航栏菜单
        return TreeUtils.buildJsonBeanTreeByParentId(menuList, "-2");
    }

    /**
     * 侧边栏菜单
     * @return
     */
    public List<JSONObject> getSideMenu(){
        List<Menu> menuList = this.lambdaQuery().ne(Menu::getMenuType, MenuType.BUTTON).ne(Menu::getParentId, -2).list();

        // 侧边栏菜单
        List<Menu> menuJsonList = TreeUtils.filterBeanByParentId(menuList, "-3");

        return TreeUtils.buildJsonBeanTreeByParentId(menuJsonList, "-3");
    }
}
