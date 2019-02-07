package com.mwj.cms.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.mwj.cms.common.enums.Sex;
import com.mwj.cms.common.enums.Status;
import com.mwj.cms.common.util.date.DateFormatUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Accessors(chain = true) 生成的setter方法返回this
 * @author Meng Wei Jin
 */
@ApiModel(description = "用户")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID", example = "1")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "登录账号")
    @Pattern(regexp="^[a-zA-Z0-9]+$", message="{login.name.format}")
    @Size(min = 1, max = 30, message = "{size.error}")
    private String loginName;

    @ApiModelProperty(value = "用户昵称")
    @Size(min = 1, max = 30, message = "{size.error}")
    @TableField(condition = SqlCondition.LIKE)
    private String nick;

    @ApiModelProperty(value = "用户邮箱")
    @Email(message = "{email.format}")
    private String email;

    @ApiModelProperty(value = "手机号码")
    @Pattern(regexp = "^1(3|4|5|7|8)\\d{9}$", message  = "{phone.number.format}")
    private String phoneNumber;

    @ApiModelProperty(value = "用户性别枚举（0:男 male， 1:女 female）")
    private Sex sex;

    @ApiModelProperty(value = "密码。可以包含数字、字母、下划线，并且要同时含有数字和字母，且长度要在8-16位之间")
    @Pattern(regexp="^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z_]{8,16}$", message="{password.format}")
    @Size(min = 8, max = 16, message = "{size.error}")
    private String password;

    @ApiModelProperty(value = "部门ID", example = "1")
    private Integer deptId;

    @ApiModelProperty(value = "帐号状态枚举类（0正常 1停用）")
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
