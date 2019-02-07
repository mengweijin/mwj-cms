package com.mwj.cms.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mwj.cms.common.enums.JobLogStatus;
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
@ApiModel(description = "调度任务执行日志")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_job_log")
public class JobLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "调度任务日志ID", example = "1")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "任务ID", example = "1")
    private Integer jobId;

    @ApiModelProperty(value = "状态枚举类JobLogStatus（0执行中 1成功 2失败）")
    private JobLogStatus status;

    @ApiModelProperty(value = "Job执行主机IP地址")
    private String ip;

    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = DateFormatUtil.YYYY_MM_DD_HH_MM_SS)
    @JsonFormat(pattern = DateFormatUtil.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "结束时间")
    @DateTimeFormat(pattern = DateFormatUtil.YYYY_MM_DD_HH_MM_SS)
    @JsonFormat(pattern = DateFormatUtil.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime endTime;

    @ApiModelProperty(value = "错误消息")
    private String errorMsg;

    /**
     * JavaScript 无法处理 Java 的长整型 Long 导致精度丢失，具体表现为主键最后两位永远为 0，
     * 解决思路： Long 转为 String 返回
     * @return
     */
    @JsonSerialize(using = ToStringSerializer.class)
    public Long getId() {
        return id;
    }
}
