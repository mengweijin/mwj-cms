package com.mwj.cms.common.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author Meng Wei Jin
 * @description 验证码异常
 **/
public class KaptchaException extends AuthenticationException {

    public KaptchaException(String msg) {
        super(msg);
    }

    public KaptchaException(String msg, Throwable e) {
        super(msg, e);
    }

}
