package com.mwj.cms.system.service;

import cn.hutool.core.util.ReflectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mwj.cms.common.enums.Status;
import com.mwj.cms.common.exception.MwjCmsException;
import com.mwj.cms.common.util.TreeUtils;
import com.mwj.cms.system.entity.Dept;
import com.mwj.cms.system.entity.User;
import com.mwj.cms.system.mapper.DeptMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 部门表 服务实现类
 * </p>
 *
 * @author Meng Wei Jin
 */
@Service
public class DeptService extends ServiceImpl<DeptMapper, Dept> {

    private final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private DeptMapper deptMapper;

    @Autowired
    private UserService userService;

    /**
     * 查询树表格数据
     * @param dept
     * @return
     */
    public List<Map<String, Object>> selectVO(Dept dept){
        return deptMapper.selectVO(dept);
    }

    /**
     * 删除
     * @param id
     * @return
     */
    public boolean deleteById(Integer id) {
        // 查询是否拥有子部门
        List<Dept> deptList = this.list(new QueryWrapper<>(new Dept().setParentId(id)));
        if(CollectionUtils.isNotEmpty(deptList)){
            // 有子部门，当前部门不能被删除
            return false;
        }

        List<User> userList = userService.list(new QueryWrapper<>(new User().setDeptId(id)));
        if(CollectionUtils.isNotEmpty(userList)){
            // 有用户属于当前部门，当前部门不能被删除
            return false;
        }

        // 执行删除操作
        return this.removeById(id);
    }

    /**
     * 启用/停用部门，递归启用/停用子孙部门
     * @param id
     * @param status
     * @param loginUser
     */
    public boolean switchStatus(Integer id, String status, User loginUser) throws MwjCmsException {
        // 查询所有部门
        List<Dept> deptList = this.list();
        // 根据rootId筛选其下所有子孙部门
        deptList = TreeUtils.filterBeanByParentId(new ArrayList<>(deptList), String.valueOf(id));

        List<Dept> updateList = new ArrayList<>(deptList.size() + 1);
        // 更新子孙部门状态
        deptList.forEach(dept ->
            updateList.add(new Dept().setId(dept.getId()).setStatus(Status.get(status)).setUpdateBy(loginUser.getId()))
        );
        // 更新当前部门的状态
        updateList.add(new Dept().setId(id).setStatus(Status.get(status)).setUpdateBy(loginUser.getId()));
        return this.updateBatchById(updateList);
    }

    /**
     * 查询树结构数据
     * @param dept
     * @return
     * @throws MwjCmsException
     */
    public List<JSONObject> querySelectTree(Dept dept, List<String> selectedIdList) throws MwjCmsException {
        return buildDeptTree(this.list(new QueryWrapper<>(dept)), "1", selectedIdList);
    }

    /**
     * 构造部门树结构数据
     * @param list
     * @param rootId
     * @param selectedIdList 默认需要选中的部门的id数组
     * @return
     * @throws MwjCmsException
     */
    public List<JSONObject> buildDeptTree(List<Dept> list, String rootId, List<String> selectedIdList) throws MwjCmsException {
        List<JSONObject> jsonTreeList = new ArrayList<>();

        try {
            for (Dept dept: list){
                String pid = String.valueOf(dept.getParentId());
                if (rootId.equals(pid)) {

                    JSONObject newNode = new JSONObject();

                    Field[] fields = dept.getClass().getDeclaredFields();
                    for (Field field: fields) {
                        newNode.fluentPut(field.getName(), ReflectUtil.getFieldValue(dept, field));
                    }

                    if(selectedIdList.contains(String.valueOf(dept.getId()))){
                        newNode.fluentPut("selected", "selected");
                    }
                    if(Status.DISABLE == dept.getStatus()){
                        newNode.fluentPut("disabled", "disabled");
                    }

                    newNode.fluentPut("children", buildDeptTree(list, String.valueOf(dept.getId()), selectedIdList));

                    jsonTreeList.add(newNode);
                }
            }
        } catch (RuntimeException e){
            logger.error(e.getMessage(), e);
            throw new MwjCmsException(e);
        }

        return jsonTreeList;
    }
}
