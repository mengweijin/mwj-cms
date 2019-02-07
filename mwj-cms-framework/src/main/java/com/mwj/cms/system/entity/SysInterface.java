package com.mwj.cms.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.mwj.cms.common.enums.InterfaceType;
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
@ApiModel(description = "接口管理配置对象")
@Data
@Accessors(chain = true)
@TableName("sys_interface")
public class SysInterface implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID", example = "1")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "接口名称")
    private String name;

    @ApiModelProperty(value = "接口编码，标识键")
    private String keyCode;

    @ApiModelProperty(value = "接口类型枚举类InterfaceType")
    private InterfaceType type;

    @ApiModelProperty(value = "请求url,如果是Web Service(cxf)接口类型，则为wsdl地址")
    private String url;

    @ApiModelProperty(value = "接口命名空间，可通过访问wsdl地址后的第一个标签中的targetNamespace属性的值获取到")
    private String nameSpace;

    @ApiModelProperty(value = "要调用的服务端的方法名称，通过wsdl文档获取")
    private String methodName;

    @ApiModelProperty(value = "HTTP接口时的请求类型（POST, GET）")
    private String requestType;

    @ApiModelProperty(value = "更新者", example = "1")
    private Integer updateBy;

    @ApiModelProperty(value = "更新时间")
    @DateTimeFormat(pattern = DateFormatUtil.YYYY_MM_DD_HH_MM_SS)
    @JsonFormat(pattern = DateFormatUtil.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "备注")
    private String remark;
}
