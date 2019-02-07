package com.mwj.cms.system.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mwj.cms.system.entity.SysFile;
import com.mwj.cms.system.mapper.FileMapper;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Meng Wei Jin
 */
@Service
public class FileService extends ServiceImpl<FileMapper, SysFile> {

    @Autowired
    private FileMapper fileMapper;

    /**
     * 自定义分页查询
     * @param page
     * @param sysFile
     * @return
     */
    public IPage<Map<String, Object>> selectPageVO(IPage<Map<String, Object>> page, SysFile sysFile) {
        return fileMapper.selectPageVO(page, sysFile);
    }

    /**
     * 删除文件
     * @param id
     * @return
     * @throws IOException
     */
    public Boolean deleteFileById(Integer id) throws IOException {
        SysFile sysFile = fileMapper.selectById(id);
        if(sysFile != null){
            // 从磁盘移除文件
            File file = FileUtils.getFile(sysFile.getFilePath());
            if(file.exists()){
                FileUtils.forceDelete(file);
            }
        }
        // 从数据库移除当前文件记录
        return this.removeById(id);
    }
}
