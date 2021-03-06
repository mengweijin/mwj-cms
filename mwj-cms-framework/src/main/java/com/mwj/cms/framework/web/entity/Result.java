package com.mwj.cms.framework.web.entity;

import com.mwj.cms.common.constant.Const;
import com.mwj.cms.framework.util.MessageSourceUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

/**
 * @author Meng Wei Jin
 * @description 实体类属性名称对应layui表格请求和响应所需要的默认参数名称
 **/
@ApiModel(description = "返回类")
@Data
@Accessors(chain = true)
@NoArgsConstructor
@RequiredArgsConstructor
public class Result<T> {

    private static final long serialVersionUID = 1967183638333866256L;

    @ApiModelProperty(value = "成功的状态码，默认 0 为成功", example = "0")
    private int code = 0;

    @ApiModelProperty(value = "附加描述信息")
    private String msg = Const.EMPTY;

    @NonNull
    @ApiModelProperty(value = "返回数据")
    private T data;

    /**
     * 返回成功消息
     *
     * @param msg 内容
     * @return 成功消息
     */
    public static Result success(String msg) {
        Result result = new Result();
        result.setMsg(msg);
        return result;
    }

    public static Result success() {
        return success(MessageSourceUtils.message("operation.successful"));
    }

    /**
     * 返回失败消息
     *
     * @param code 错误码
     * @param msg  内容
     * @return
     */
    public static Result error(int code, String msg) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    /**
     * 默认错误类型为500
     *
     * @param msg
     * @return
     */
    public static Result error(String msg) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR.value(), msg);
    }

    public static Result error() {
        return error(MessageSourceUtils.message("operation.failed"));
    }

    /**
     * @param rows 受影响的行数
     * @return
     */
    public static Result resultByRows(int rows) {
        return rows > 0 ? success() : error();
    }

    /**
     * @param flag
     * @return
     */
    public static Result resultByBoolean(Boolean flag) {
        return flag ? success() : error();
    }

    public static Result resultByBoolean(Boolean flag, String msg) {
        if(StringUtils.isBlank(msg)){
            return  flag ? success() : error();
        }
        return flag ? success(msg) : error(msg);
    }
}
