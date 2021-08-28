package com.taiping.exception;

import lombok.Data;

/**
 * 所有的业务异常
 *
 * @author liyj
 * @date 2019/10/16
 */
@Data
public class BizException extends RuntimeException {
    /**
     * code
     */
    private Integer code;
    /**
     * 信息
     */
    private String msg;

    /**
     * Constructs a new runtime exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public BizException(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
