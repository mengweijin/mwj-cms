package com.mwj.cms.common.exception;

import com.mwj.cms.framework.util.MessageSourceUtils;

/**
 * @author Meng Wei Jin
 * @description 应用
 **/
public class MwjCmsException extends RuntimeException {

    public MwjCmsException() {
        super(MessageSourceUtils.message("exception.server"));
    }
    public MwjCmsException(String message) {
        super(message);
    }

    public MwjCmsException(Throwable cause) {
        super(cause);
    }

    public MwjCmsException(String message, Throwable cause) {
        super(message, cause);
    }
}
