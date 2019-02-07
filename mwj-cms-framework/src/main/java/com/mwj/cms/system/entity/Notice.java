package com.mwj.cms.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.mwj.cms.common.enums.Status;
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
@ApiModel(description = "通知公告")
@Data
@Accessors(chain = true)
@TableName("sys_notice")
public class Notice implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID", example = "1")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "状态")
    private Status status;

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

}
