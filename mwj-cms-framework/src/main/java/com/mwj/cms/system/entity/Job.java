package com.mwj.cms.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.mwj.cms.common.enums.JobSchedulerStatus;
import com.mwj.cms.common.enums.MisfirePolicy;
import com.mwj.cms.common.util.date.DateFormatUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Meng Wei Jin
 */
@ApiModel(description = "调度器job")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_job")
public class Job<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID", example = "1")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "任务名称")
    private String name;

    @ApiModelProperty(value = "cron执行表达式")
    private String cron;

    @ApiModelProperty(value = "调度JOB类型ID", example = "1")
    private Integer jobTypeId;

    @ApiModelProperty(value = "业务关联表Id")
    private Integer jobTableId;

    @ApiModelProperty(value = "计划执行错误策略（0立刻执行（默认） 1放弃执行 2全部执行）")
    private MisfirePolicy misfirePolicy;

    @ApiModelProperty(value = "状态枚举类（0草稿 1正常 2暂停 3结束）")
    private JobSchedulerStatus status;

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

    @ApiModelProperty(value = "Job实现类的具体业务实体类，非表字段")
    @TableField(exist = false)
    private T object;
}
