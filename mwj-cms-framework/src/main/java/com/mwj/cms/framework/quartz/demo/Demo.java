package com.mwj.cms.framework.quartz.demo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Meng Wei Jin
 * @description
 **/
@ApiModel(description = "示例任务业务实体类")
@Data
@Accessors(chain = true)
public class Demo {

    /**
     * id
     */
    private Integer id;

    /**
     * 名称
     */
    private String name;
}
