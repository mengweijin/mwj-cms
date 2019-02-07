package com.mwj.cms.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.mwj.cms.common.util.date.DateFormatUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * @Accessors(chain = true) 生成的setter方法返回this
 * </p>
 *
 * @author Meng Wei Jin
 */
@ApiModel(description = "系统配置")
@Data
@Accessors(chain = true)
@TableName("sys_config")
public class Config implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID", example = "1")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "配置分类")
    private String type;

    @ApiModelProperty(value = "配置名称")
    private String name;

    @ApiModelProperty(value = "配置键值属性名称")
    private String keyName;

    @ApiModelProperty(value = "当前配置的值")
    private String value;

    @ApiModelProperty(value = "创建者", example = "1")
    private Integer createBy;

    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = DateFormatUtil.YYYY_MM_DD_HH_MM_SS)
    @JsonFormat(pattern = DateFormatUtil.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新者", example = "1")
    private Integer updateBy;

    @ApiModelProperty(value = "更新时间")
    @DateTimeFormat(pattern = DateFormatUtil.YYYY_MM_DD_HH_MM_SS)
    @JsonFormat(pattern = DateFormatUtil.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "备注")
    private String remark;
}
