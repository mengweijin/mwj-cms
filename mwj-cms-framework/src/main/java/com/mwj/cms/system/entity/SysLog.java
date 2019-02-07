package com.mwj.cms.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mwj.cms.common.enums.ResultStatus;
import com.mwj.cms.common.util.date.DateFormatUtil;
import com.mwj.cms.common.enums.LogType;
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
@ApiModel(description = "系统操作日志")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_log")
public class SysLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID", example = "1")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "操作模块")
    @TableField(condition = SqlCondition.LIKE)
    private String title;

    @ApiModelProperty(value = "请求url")
    private String url;

    @ApiModelProperty(value = "请求参数 JSON格式")
    private String reqParam;

    @ApiModelProperty(value = "请求方法")
    private String method;

    @ApiModelProperty(value = "日志类型枚举")
    private LogType type;

    @ApiModelProperty(value = "操作人员ID", example = "1")
    private Integer operator;

    @ApiModelProperty(value = "操作时间")
    @DateTimeFormat(pattern = DateFormatUtil.YYYY_MM_DD_HH_MM_SS)
    @JsonFormat(pattern = DateFormatUtil.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime operTime;

    @ApiModelProperty(value = "操作IP地址")
    private String operIp;

    @ApiModelProperty(value = "操作地点")
    private String operLocation;

    @ApiModelProperty(value = "日志状态枚举类，0成功 1异常")
    private ResultStatus status;

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
