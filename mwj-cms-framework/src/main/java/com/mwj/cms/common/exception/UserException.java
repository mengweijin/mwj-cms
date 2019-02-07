package com.mwj.cms.common.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author Meng Wei Jin
 * @description 用户认证异常
 **/
public class UserException extends AuthenticationException {

    public UserException(String msg) {
        super(msg);
    }

    public UserException(String msg, Throwable e) {
        super(msg, e);
    }

}
