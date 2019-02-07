package com.mwj.cms.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mwj.cms.common.enums.Status;
import com.mwj.cms.system.entity.Notice;
import com.mwj.cms.system.entity.User;
import com.mwj.cms.system.mapper.NoticeMapper;
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
public class NoticeService extends ServiceImpl<NoticeMapper, Notice> {

    @Autowired
    private NoticeMapper noticeMapper;
    /**
     * 获取最近20条通知公告
     * @return
     */
    public List<Notice> getLatestNotice(){
        return this.lambdaQuery().orderByDesc(Notice::getCreateTime).last("limit 20").list();
    }

    /**
     * 自定义分页查询
     * @param page
     * @param notice
     * @return
     */
    public IPage<Map<String, Object>> selectPageVO(IPage<Map<String, Object>> page, Notice notice) {
        page = noticeMapper.selectPageVO(page, notice);
        if(page.getTotal() > 0){
            List<Map<String, Object>> recordList = page.getRecords();
            recordList.stream()
                    .map(map -> {
                        map.put("status", Status.getDesc(String.valueOf(map.get("status"))));
                        return map;
                    })
                    .collect(Collectors.toList());
        }

        return page;
    }

    /**
     * 通知公告启用/停用转换
     * @param id
     * @param status
     * @param loginUser
     * @return
     */
    public Boolean switchStatus(Integer id, String status, User loginUser) {
        return this.updateById(new Notice().setId(id).setStatus(Status.get(status)).setUpdateBy(loginUser.getId()));
    }

}
